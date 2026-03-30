package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AdvancedGyroscopeInputModule extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape SLAB_BOTTOM = Block.box(0, 0, 0, 16, 8, 16);
    private static final VoxelShape SLAB_TOP = Block.box(0, 8, 0, 16, 16, 16);
    private static final VoxelShape SLAB_NORTH = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape SLAB_SOUTH = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape SLAB_WEST = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape SLAB_EAST = Block.box(8, 0, 0, 16, 16, 16);

    public AdvancedGyroscopeInputModule(Properties arg) {
        super(arg);
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> SLAB_BOTTOM;
            case UP -> SLAB_TOP;
            case NORTH -> SLAB_NORTH;
            case SOUTH -> SLAB_SOUTH;
            case WEST -> SLAB_WEST;
            case EAST -> SLAB_EAST;
        };
    }
}
