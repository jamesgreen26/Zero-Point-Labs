package g_mungus.zpl.block.droidcore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DroidCoreBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty FRONT_POWER = IntegerProperty.create("front", 0, 15);
    public static final IntegerProperty BACK_POWER = IntegerProperty.create("back", 0, 15);
    public static final IntegerProperty LEFT_POWER = IntegerProperty.create("left", 0, 15);
    public static final IntegerProperty RIGHT_POWER = IntegerProperty.create("right", 0, 15);
    public static final IntegerProperty UP_POWER = IntegerProperty.create("up", 0, 15);
    public static final IntegerProperty DOWN_POWER = IntegerProperty.create("down", 0, 15);


    public DroidCoreBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BACK_POWER, 0)
                .setValue(FRONT_POWER, 0)
                .setValue(LEFT_POWER, 0)
                .setValue(RIGHT_POWER, 0)
                .setValue(UP_POWER, 0)
                .setValue(DOWN_POWER, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BACK_POWER, FRONT_POWER, LEFT_POWER, RIGHT_POWER, UP_POWER, DOWN_POWER);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction;
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            direction = context.getNearestLookingDirection();
        } else {
            direction = context.getNearestLookingDirection().getOpposite();
        }
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DroidCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof DroidCoreBlockEntity droidCore) {
                droidCore.tick();
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getDirectSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        // Provide strong power from the back face (opposite of facing direction)
        Direction facing = state.getValue(FACING);
        if (direction == facing) {
            return state.getValue(BACK_POWER);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getDirectSignal(state, level, pos, direction);
    }
}
