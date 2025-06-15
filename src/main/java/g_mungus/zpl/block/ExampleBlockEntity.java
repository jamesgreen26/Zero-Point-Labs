package g_mungus.zpl.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ExampleBlockEntity extends BlockEntity {
    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXAMPLE_BLOCK_ENTITY.get(), pos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tick() {
        if (level == null) return;
        int current = getBlockState().getValue(ExampleBlock.POWER);
        int target = level.getBestNeighborSignal(getBlockPos());
        if (current < target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ExampleBlock.POWER, current + 1), 3);
        } else if (current > target) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ExampleBlock.POWER, current - 1), 3);
        }
    }
}