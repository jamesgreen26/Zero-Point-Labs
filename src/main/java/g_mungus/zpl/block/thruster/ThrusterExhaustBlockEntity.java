package g_mungus.zpl.block.thruster;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
        int current = getBlockState().getValue(ThrusterExhaustBlock.POWER);
        int target = getInputSignal();
        if (current < target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ThrusterExhaustBlock.POWER, current + 1), 3);
            current++;
        } else if (current > target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ThrusterExhaustBlock.POWER, current - 1), 3);
            current--;
        }

        double force_strength = current / 15.0;

        if (thrust == null) {
            ThrusterExhaustBlock.addApplier(getBlockState(), level, getBlockPos());
        }
        thrust.strength = force_strength * 800_000;
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
}