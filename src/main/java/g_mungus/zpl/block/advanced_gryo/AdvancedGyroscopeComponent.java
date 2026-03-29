package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AdvancedGyroscopeComponent extends Block {

    public AdvancedGyroscopeComponent(Properties arg) {
        super(arg);
    }

    public void updateControllers(ServerLevel level, BlockPos self) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos pos = self.offset(dx, dy, dz);
                    Block block = level.getBlockState(pos).getBlock();
                    if (block instanceof AdvancedGyroscopeController controller) {
                        controller.update(level, pos);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState arg4, boolean bl) {
        super.onPlace(state, level, pos, arg4, bl);

        if (level instanceof ServerLevel serverLevel) {
            updateControllers(serverLevel, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState arg4, boolean bl) {
        super.onRemove(state, level, pos, arg4, bl);

        if (level instanceof ServerLevel serverLevel) {
            updateControllers(serverLevel, pos);
        }
    }
}
