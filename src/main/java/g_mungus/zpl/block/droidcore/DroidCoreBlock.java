package g_mungus.zpl.block.droidcore;

import g_mungus.zpl.ship.ZPLShipAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DroidCoreBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public DroidCoreBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DroidCoreBlockEntity droidCore) {
            return droidCore.getPowerForDirection(direction.getOpposite());
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getDirectSignal(state, level, pos, direction);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (level instanceof ServerLevel serverLevel) {
            addApplier(state, serverLevel, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    public static void addApplier(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos) {
        ZPLShipAttachment.get(level, pos).ifPresent(attachment -> {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DroidCoreBlockEntity droidCore) {
                droidCore.droidData = new DroidData();

                DroidForceApplier applier = new DroidForceApplier(droidCore.droidData);
                attachment.addApplier(pos, applier);
            }
        });
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        removeApplier(level, pos);

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private static void removeApplier(@NotNull Level level, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            ZPLShipAttachment.get(serverLevel, pos).ifPresent(attachment ->
                    attachment.removeApplier(serverLevel, pos)
            );
        }
    }
}
