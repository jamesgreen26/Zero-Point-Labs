package g_mungus.zpl.block.thruster;

import g_mungus.zpl.ship.ZPLShipAttachment;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class ThrusterExhaustBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public ThrusterExhaustBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWER, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER);
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
        return new ThrusterExhaustBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ThrusterExhaustBlockEntity exampleBlockEntity) {
                exampleBlockEntity.tick();
            }
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);

        VoxelShape upShape = Shapes.or(
                Block.box(2, 0, 2, 14, 10, 14),
                Block.box(1, 10, 1, 15, 16, 15)
        );

        VoxelShape downShape = Shapes.or(
                Block.box(2, 6, 2, 14, 16, 14),
                Block.box(1, 0, 1, 15, 6, 15)
        );

        VoxelShape northShape = Shapes.or(
                Block.box(2, 2, 6, 14, 14, 16),
                Block.box(1, 1, 0, 15, 15, 6)
        );

        VoxelShape southShape = Shapes.or(
                Block.box(2, 2, 0, 14, 14, 10),
                Block.box(1, 1, 10, 15, 15, 16)
        );

        VoxelShape westShape = Shapes.or(
                Block.box(6, 2, 2, 16, 14, 14),
                Block.box(0, 1, 1, 6, 15, 15)
        );

        VoxelShape eastShape = Shapes.or(
                Block.box(0, 2, 2, 10, 14, 14),
                Block.box(10, 1, 1, 16, 15, 15)
        );

        return switch (dir) {
            case UP -> upShape;
            case DOWN -> downShape;
            case NORTH -> northShape;
            case SOUTH -> southShape;
            case WEST -> westShape;
            case EAST -> eastShape;
        };
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            addApplier(state, level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    public static void addApplier(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        ZPLShipAttachment attachment = ZPLShipAttachment.get(level, pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (attachment != null && blockEntity instanceof ThrusterExhaustBlockEntity thrusterBlockEntity) {
            thrusterBlockEntity.thrust = new ThrusterData(VectorConversionsMCKt.toJOMLD(state.getValue(FACING).getOpposite().getNormal()), 0.0);

            ThrusterForceApplier applier = new ThrusterForceApplier(thrusterBlockEntity.thrust);
            attachment.addApplier(pos, applier);
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide()) {
            ZPLShipAttachment ship = ZPLShipAttachment.get(level, pos);
            if (ship != null) {
                ship.removeApplier((ServerLevel) level, pos);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

}