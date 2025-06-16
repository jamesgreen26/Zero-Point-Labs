package g_mungus.zpl.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import org.jetbrains.annotations.Nullable;

public class ThrusterExhaustBlockEntity extends BlockEntity {
    public ThrusterExhaustBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), pos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tick() {
        if (level == null) return;
        int current = getBlockState().getValue(ThrusterExhaustBlock.POWER);
        int target = level.getBestNeighborSignal(getBlockPos());
        if (current < target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ThrusterExhaustBlock.POWER, current + 1), 3);
        } else if (current > target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ThrusterExhaustBlock.POWER, current - 1), 3);
        }
    }
}