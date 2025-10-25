package g_mungus.zpl.block.launcher;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.entity.EnergyOrbEntity;
import g_mungus.zpl.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyOrbLauncherBlockEntity extends BlockEntity {
    private static final int MAX_ENERGY = 1_000;
    private static final int MAX_TRANSFER = 500;
    private static final int ENERGY_PER_SHOT = 64;

    private int tickCounter = 0;

    private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, 0) {
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            // Allow internal extraction by directly modifying energy
            if (!simulate) {
                int energyExtracted = Math.min(energy, maxExtract);
                energy -= energyExtracted;
                return energyExtracted;
            }
            return Math.min(energy, maxExtract);
        }
    };
    private final LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return energyStorage.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            // Prevent external extraction
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return energyStorage.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return energyStorage.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return energyStorage.canReceive();
        }
    });

    public EnergyOrbLauncherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ENERGY_ORB_LAUNCHER.get(), pos, blockState);
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyHandler.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", energyStorage.serializeNBT());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnergyOrbLauncherBlockEntity blockEntity) {
        if (level.isClientSide) return;

        boolean isPowered = level.hasNeighborSignal(pos);

        if (isPowered) {
            if (blockEntity.tickCounter == 0) {
                // Check if there's enough energy to fire
                if (blockEntity.energyStorage.getEnergyStored() >= ENERGY_PER_SHOT) {
                    // Extract the energy and fire
                    blockEntity.energyStorage.extractEnergy(ENERGY_PER_SHOT, false);
                    blockEntity.fireEnergyOrb(level, pos, state);
                }
            }
        }
        if (blockEntity.tickCounter != 0 || isPowered) {
            int TICKS_PER_SHOT = 5;
            blockEntity.tickCounter = (blockEntity.tickCounter + 1) % TICKS_PER_SHOT;
        }
    }

    private void fireEnergyOrb(Level level, BlockPos pos, BlockState state) {
        Vec3 direction = EnergyOrbLauncherBlock.getAimVector(state);

        double scale = ZeroPointLabsMod.getDimensionScale(level);

        Vec3 blockCenter = Vec3.atCenterOf(pos);
        Vec3 spawnPos = blockCenter.add(direction.scale(0.6));

        Vec3 velocity = direction.scale(7.0);

        EnergyOrbEntity energyOrb;

        Ship ship = VSGameUtilsKt.getShipManagingPos(level, pos);
        if (ship != null) {
            Matrix4dc transform = ship.getTransform().getShipToWorld();
            Vector3d newVelocity = transform.transformDirection(VectorConversionsMCKt.toJOML(velocity));
            Vector3d newPosition = transform.transformPosition(VectorConversionsMCKt.toJOML(blockCenter));
            newPosition = newPosition.add(newVelocity.normalize(0.6 * scale, new Vector3d()));

            Vector3dc shipVelocity = ship.getVelocity();
            Vector3d velocityDirection = newVelocity.normalize(new Vector3d());
            double parallelComponent = shipVelocity.dot(velocityDirection);
            Vector3d parallelVelocity = velocityDirection.mul(parallelComponent, new Vector3d());

            Vector3d finalVelocity;
            if (scale < 0.5) {
                finalVelocity = newVelocity.add(parallelVelocity.mul(scale), new Vector3d());
            } else {
                finalVelocity = newVelocity;
            }

            energyOrb = new EnergyOrbEntity(level, newPosition.x, newPosition.y, newPosition.z, VectorConversionsMCKt.toMinecraft(finalVelocity));

            energyOrb.setExcludedShipId(ship.getId());
        } else {
            energyOrb = new EnergyOrbEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, velocity.scale(scale));
        }

        level.addFreshEntity(energyOrb);

        level.playSound(
            null,
            pos,
            ModSounds.ENERGY_ORB_SHOOT.get(),
            SoundSource.BLOCKS,
            1.0F,
            1.0F
        );
    }
}
