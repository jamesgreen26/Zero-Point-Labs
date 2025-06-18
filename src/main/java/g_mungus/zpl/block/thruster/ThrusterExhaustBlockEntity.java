package g_mungus.zpl.block.thruster;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class ThrusterExhaustBlockEntity extends BlockEntity {
    public ThrusterExhaustBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), pos, state);
    }

    public ThrusterData thrust;

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tick() {
        if (level == null) return;

        int prev = getBlockState().getValue(ThrusterExhaustBlock.POWER);
        int current = getThrusterStrength(prev);

        double force_strength = current / 15.0;

        if (current != prev) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ThrusterExhaustBlock.POWER, current), 3);
        }
        if (thrust == null) {
            ThrusterExhaustBlock.addApplier(getBlockState(), level, getBlockPos());
        }
        if (thrust != null) {
            thrust.strength = force_strength * 800_000;
        }
    }

    private int getThrusterStrength(int prev) {
        int output = prev;

        int target = getInputSignal();
        if (output < target) {
            output++;
        } else if (output > target) {
            output--;
        }

        double requestedStrength = output / 15.0;
        double requestedEnergy = requestedStrength * 250.0;

        IEnergyStorage energySource = getNeighborEnergyStorage();

        int extracted = -1;
        if (energySource != null && requestedEnergy > 0) {
            extracted = energySource.extractEnergy((int) requestedEnergy, false);
        }
        if (extracted + 1 <= requestedEnergy) {
            if (prev > 0) {
                output = prev - 1;
            } else {
                output = 0;
            }
        }
        return output;
    }

    private int getInputSignal() {
        if (level == null) return 0;

        BlockPos thisPos = getBlockPos();
        BlockState thisState = level.getBlockState(thisPos);

        BlockPos neighbor = thisPos.offset(thisState.getValue(ThrusterExhaustBlock.FACING).getOpposite().getNormal());
        BlockState neighborState = level.getBlockState(neighbor);

        if (neighborState.is(ModBlocks.ION_MODULATOR_BLOCK) && neighborState.getValue(IonModulatorBlock.FACING).equals(thisState.getValue(ThrusterExhaustBlock.FACING))) {
            return level.getBestNeighborSignal(neighbor);
        }
        return 0;
    }

    @Nullable
    private IEnergyStorage getNeighborEnergyStorage() {
        if (level == null) return null;

        BlockState state = getBlockState();
        Direction facing = state.getValue(ThrusterExhaustBlock.FACING);
        BlockPos neighborPos = worldPosition.relative(facing.getOpposite());
        BlockEntity neighborEntity = level.getBlockEntity(neighborPos);

        if (neighborEntity != null) {
            return level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, facing);
        }

        return null;
    }
}