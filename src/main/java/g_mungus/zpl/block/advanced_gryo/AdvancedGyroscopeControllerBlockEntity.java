package g_mungus.zpl.block.advanced_gryo;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zps.block.cableNetwork.core.Channels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import g_mungus.zpl.config.ZPLConfig;
import org.joml.Matrix3d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.mod.api.BlockEntityPhysicsListener;
import org.valkyrienskies.mod.common.BlockStateInfo;
import org.jetbrains.annotations.Nullable;
import kotlin.Pair;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancedGyroscopeControllerBlockEntity extends BlockEntity implements MenuProvider, BlockEntityPhysicsListener {
    private static final int SLOT_COUNT = 1;
    public static final int MAX_ENERGY = 24_000;
    public static final int MAX_TRANSFER = 2_000;

    private final AtomicInteger energyUsageCached = new AtomicInteger();

    private int[] inputFunctionMapping = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
    private int[] mappedFunctionValues = new int[8];

    private final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);
    private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, MAX_TRANSFER);
    private final LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    public AdvancedGyroscopeControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_GYROSCOPE_CONTROLLER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.zpl.advanced_gyroscope_controller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AdvancedGyroscopeControllerMenu(id, playerInventory, this);
    }

    public IItemHandler getItemHandler() {
        return items;
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    public int[] getInputFunctionMapping() {
        return inputFunctionMapping.clone();
    }

    public void setInputFunctionMapping(int[] mapping) {
        this.inputFunctionMapping = mapping.clone();
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int[] getMappedFunctionValues() {
        return mappedFunctionValues.clone();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AdvancedGyroscopeControllerBlockEntity be) {
        if (level.isClientSide) return;

        Direction facing = state.getValue(AdvancedGyroscopeController.FACING);
        BlockPos leftPos  = pos.relative(facing.getCounterClockWise());
        BlockPos rightPos = pos.relative(facing.getClockWise());

        int[] leftSignals  = new int[4];
        int[] rightSignals = new int[4];

        if (level.getBlockEntity(leftPos) instanceof AdvancedGyroInputModuleBlockEntity leftModule) {
            for (int i = 0; i < 4; i++) leftSignals[i] = leftModule.getSignal(Channels.QUAD_1 + i);
        }
        if (level.getBlockEntity(rightPos) instanceof AdvancedGyroInputModuleBlockEntity rightModule) {
            for (int i = 0; i < 4; i++) rightSignals[i] = rightModule.getSignal(Channels.QUAD_1 + i);
        }

        int[] functionValues = new int[8];
        int[] mapping = be.inputFunctionMapping;
        for (int inputIdx = 0; inputIdx < 8; inputIdx++) {
            int funcIdx = mapping[inputIdx];
            if (funcIdx < 0) continue;
            int signal = inputIdx < 4 ? leftSignals[inputIdx] : rightSignals[inputIdx - 4];
            functionValues[funcIdx] = Math.max(functionValues[funcIdx], signal);
        }
        be.mappedFunctionValues = functionValues;

        GyroFunctions gyroFunctions = GyroFunctions.fromArray(functionValues);
        int maxActive = Math.max(Math.max(Math.max(gyroFunctions.rotXPos(), gyroFunctions.rotXNeg()),
                Math.max(gyroFunctions.rotYPos(), gyroFunctions.rotYNeg())),
                Math.max(Math.max(gyroFunctions.rotZPos(), gyroFunctions.rotZNeg()), gyroFunctions.stabilize()));
        double activityFactor = maxActive / 15.0;
        double bypassFactor = 1.0 - (gyroFunctions.bypass() / 15.0);
        int rawUsage = be.getEnergyUsage();
        int toConsume = (int) Math.round(rawUsage * activityFactor * bypassFactor);
        int consumed = be.energyStorage.extractEnergy(toConsume, false);

        if (be.energyStorage.getEnergyStored() > consumed) {
            be.energyUsageCached.set(rawUsage);
        } else {
            be.energyUsageCached.set(consumed);
        }
    }

    public int getEnergyUsage() {
        ItemStack stack = items.getStackInSlot(0);
        if (stack.isEmpty()) {
            return 0;
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            Pair<Double, ?> data = BlockStateInfo.INSTANCE.get(state);
            if (data != null) {
                double mass = data.getFirst();
                return (int) Math.max(1, Math.round(mass / 100.0));
            }
        }
        return 0;
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        itemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Items")) {
            items.deserializeNBT(tag.getCompound("Items"));
        }
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
        if (tag.contains("InputFunctionMapping")) {
            int[] loaded = tag.getIntArray("InputFunctionMapping");
            if (loaded.length == 8) {
                inputFunctionMapping = loaded.clone();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.put("Energy", energyStorage.serializeNBT());
        tag.putIntArray("InputFunctionMapping", inputFunctionMapping);
    }

    String vsDimension = "";

    @Override
    public @NotNull String getDimension() {
        return vsDimension;
    }

    @Override
    public void setDimension(@NotNull String s) {
        vsDimension = s;
    }

    @Override
    public void physTick(@Nullable PhysShip physShip, @NotNull PhysLevel physLevel) {
        if (physShip == null) return;

        GyroFunctions gyroFunctions = getBlockState().getValue(AdvancedGyroscopeController.ASSEMBLED) ?
                GyroFunctions.fromArray(getMappedFunctionValues()) : GyroFunctions.ZERO;

        Vector3dc scaling = physShip.getTransform().getShipToWorldScaling();
        double massScale = scaling.x() * scaling.y() * scaling.z();
        double rawMass = physShip.getMass() / massScale;
        Vector3dc angularVelocity = physShip.getAngularVelocity();

        double maxTorque = energyUsageCached.get() * ZPLConfig.getAdvGyroMaxTorqueFactor() * 1000;

        // world→ship rotation matrix (same pattern as DroidAttachment / GyroForceApplier)
        Matrix4dc worldToShip = physShip.getTransform().getWorldToShip();
        Matrix3d rotToShip = new Matrix3d();
        worldToShip.get3x3(rotToShip);
        double matScale = Math.sqrt(
                rotToShip.m00() * rotToShip.m00() +
                rotToShip.m10() * rotToShip.m10() +
                rotToShip.m20() * rotToShip.m20());
        rotToShip.scale(1.0 / matScale);

        // angular velocity in ship space
        Vector3d shipOmega = rotToShip.transform(new Vector3d(angularVelocity));

        // target omega from spin signals (ship-space X/Y/Z axes)
        Vector3d targetOmega = getTargetOmega(gyroFunctions);

        targetOmega.add(stabilizeContribution(physShip, rotToShip, gyroFunctions.stabilize(), shipOmega));

        // proportional control on omega error; clamped to maxTorque
        final double KP = rawMass * ZPLConfig.getAdvGyroProportionalGain();
        Vector3d controlTorque = targetOmega.sub(shipOmega, new Vector3d()).mul(KP);

        // bypass scales down gyro authority
        controlTorque.mul(1.0 - (gyroFunctions.bypass() / 15.0));

        // clamp each axis independently; allow 4x if torque opposes omega on that axis
        double clampX = (controlTorque.x * shipOmega.x < 0) ? maxTorque * 4.0 : maxTorque;
        double clampY = (controlTorque.y * shipOmega.y < 0) ? maxTorque * 4.0 : maxTorque;
        double clampZ = (controlTorque.z * shipOmega.z < 0) ? maxTorque * 4.0 : maxTorque;
        controlTorque.set(
            Math.max(-clampX, Math.min(clampX, controlTorque.x)),
            Math.max(-clampY, Math.min(clampY, controlTorque.y)),
            Math.max(-clampZ, Math.min(clampZ, controlTorque.z))
        );

        physShip.applyBodyTorque(controlTorque);
    }

    private static @NotNull Vector3d getTargetOmega(GyroFunctions gyroFunctions) {
        double maxSpin = ZPLConfig.getAdvGyroMaxSpin();
        double tX = (gyroFunctions.rotXPos() - gyroFunctions.rotXNeg()) / 15.0 * maxSpin;
        double tY = (gyroFunctions.rotYPos() - gyroFunctions.rotYNeg()) / 15.0 * maxSpin;
        double tZ = (gyroFunctions.rotZPos() - gyroFunctions.rotZNeg()) / 15.0 * maxSpin;
        Vector3d targetOmega = new Vector3d(tX, tY, tZ);

        if (targetOmega.lengthSquared() > maxSpin * maxSpin) {
            targetOmega.normalize(maxSpin);
        }
        return targetOmega;
    }

    /**
     * Returns the target omega contribution (ship space) needed to align the ship's
     * local Y-up with world Y-up, scaled by the stabilize signal strength.
     */
    private static Vector3d stabilizeContribution(PhysShip physShip, Matrix3d rotToShip, int stabilizeSignal, Vector3d shipOmega) {
        double stabilizeFactor = stabilizeSignal / 15.0;
        if (stabilizeFactor <= 0.001) return new Vector3d();

        Matrix3d rotToWorld = new Matrix3d();
        physShip.getTransform().getShipToWorld().get3x3(rotToWorld);
        double sScale = Math.sqrt(
                rotToWorld.m00() * rotToWorld.m00() +
                rotToWorld.m10() * rotToWorld.m10() +
                rotToWorld.m20() * rotToWorld.m20());
        rotToWorld.scale(1.0 / sScale);

        Vector3d localUpWorld = rotToWorld.transform(new Vector3d(0, 1, 0));
        // cross(localUp, worldUp): axis to rotate around to correct tilt, magnitude = sin(angle)
        Vector3d correctionShip = rotToShip.transform(
                localUpWorld.cross(new Vector3d(0, 1, 0), new Vector3d()), new Vector3d());

        double corrLen = correctionShip.length();

        double speed = stabilizeFactor * ZPLConfig.getAdvGyroStabilizeSpeed();

        // P term: target omega proportional to tilt angle (sin via cross product magnitude)
        Vector3d correction = corrLen > 1e-6
                ? new Vector3d(correctionShip).normalize().mul(speed * corrLen)
                : new Vector3d();

        // D term: brake angular velocity in the tilt plane (X and Z in ship space).
        // Projecting onto corrDir alone (previous approach) left perpendicular tilt-plane
        // velocity uncontrolled, causing the error axis to precess in a circle.
        // Y is intentionally excluded so yaw spin commands still work.
        Vector3d damping = new Vector3d(shipOmega.x(), 0.0, shipOmega.z()).mul(stabilizeFactor);

        return correction.sub(damping);
    }
}
