package g_mungus.zpl.block.launcher;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.entity.EnergyOrbEntity;
import g_mungus.zpl.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class EnergyOrbLauncherBlockEntity extends BlockEntity {
    private boolean wasPowered = false;

    public EnergyOrbLauncherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ENERGY_ORB_LAUNCHER.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnergyOrbLauncherBlockEntity blockEntity) {
        if (level.isClientSide) return;

        // Check if block is receiving redstone power
        boolean isPowered = level.hasNeighborSignal(pos);

        // Fire on rising edge (when power turns on)
        if (isPowered && !blockEntity.wasPowered) {
            blockEntity.fireEnergyOrb(level, pos, state);
        }

        blockEntity.wasPowered = isPowered;
    }

    private void fireEnergyOrb(Level level, BlockPos pos, BlockState state) {
        // Get the facing direction
        Direction facing = state.getValue(EnergyOrbLauncherBlock.FACING);
        Vec3 direction = Vec3.atLowerCornerOf(facing.getNormal());

        // Get dimension scale
        double scale = ZeroPointLabsMod.getDimensionScale(level);

        // Calculate spawn position (center of block + offset in facing direction)
        Vec3 blockCenter = Vec3.atCenterOf(pos);
        Vec3 spawnPos = blockCenter.add(direction.scale(0.6));

        // Calculate velocity (scaled by dimension)
        Vec3 velocity = direction.scale(5.0);

        // Create and spawn the energy orb
        EnergyOrbEntity energyOrb;

        // Check if this block is on a ship, and if so, exclude that ship from collisions
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, pos);
        if (ship != null) {
            Matrix4dc transform = ship.getTransform().getShipToWorld();
            Vector3dc newVelocity = transform.transformDirection(VectorConversionsMCKt.toJOML(velocity));
            Vector3d newPosition = transform.transformPosition(VectorConversionsMCKt.toJOML(blockCenter));
            newPosition = newPosition.add(newVelocity.normalize(0.6, new Vector3d()));

            energyOrb = new EnergyOrbEntity(level, newPosition.x, newPosition.y, newPosition.z, VectorConversionsMCKt.toMinecraft(newVelocity));

            energyOrb.setExcludedShipId(ship.getId());
        } else {
            energyOrb = new EnergyOrbEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, velocity.scale(scale));
        }

        level.addFreshEntity(energyOrb);

        // Play sound effect
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
