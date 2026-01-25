package g_mungus.zpl.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

public class GrabHandleBlock extends Block {
    public static final double PULL_FORCE = 0.1;
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    protected final VoxelShape northAabb;
    protected final VoxelShape southAabb;
    protected final VoxelShape eastAabb;
    protected final VoxelShape westAabb;
    protected final VoxelShape upAabb;
    protected final VoxelShape downAabb;

    public GrabHandleBlock(Properties arg) {
        super(arg);
        int i = 7;
        int j = 2;
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(WATERLOGGED, false)).setValue(FACING, Direction.UP));
        this.upAabb = Block.box((double)j, (double)0.0F, (double)j, (double)(16 - j), (double)i, (double)(16 - j));
        this.downAabb = Block.box((double)j, (double)(16 - i), (double)j, (double)(16 - j), (double)16.0F, (double)(16 - j));
        this.northAabb = Block.box((double)j, (double)j, (double)(16 - i), (double)(16 - j), (double)(16 - j), (double)16.0F);
        this.southAabb = Block.box((double)j, (double)j, (double)0.0F, (double)(16 - j), (double)(16 - j), (double)i);
        this.eastAabb = Block.box((double)0.0F, (double)j, (double)j, (double)i, (double)(16 - j), (double)(16 - j));
        this.westAabb = Block.box((double)(16 - i), (double)j, (double)j, (double)16.0F, (double)(16 - j), (double)(16 - j));
    }

    public VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        Direction direction = (Direction)arg.getValue(FACING);
        switch (direction) {
            case NORTH:
                return this.northAabb;
            case SOUTH:
                return this.southAabb;
            case EAST:
                return this.eastAabb;
            case WEST:
                return this.westAabb;
            case DOWN:
                return this.downAabb;
            case UP:
            default:
                return this.upAabb;
        }
    }

    public boolean canSurvive(BlockState arg, LevelReader arg2, BlockPos arg3) {
        Direction direction = (Direction)arg.getValue(FACING);
        BlockPos blockPos = arg3.relative(direction.getOpposite());
        return arg2.getBlockState(blockPos).isFaceSturdy(arg2, blockPos, direction);
    }

    public BlockState updateShape(BlockState arg, Direction arg2, BlockState arg3, LevelAccessor arg4, BlockPos arg5, BlockPos arg6) {
        if ((Boolean)arg.getValue(WATERLOGGED)) {
            arg4.scheduleTick(arg5, Fluids.WATER, Fluids.WATER.getTickDelay(arg4));
        }

        return arg2 == ((Direction)arg.getValue(FACING)).getOpposite() && !arg.canSurvive(arg4, arg5) ? Blocks.AIR.defaultBlockState() : super.updateShape(arg, arg2, arg3, arg4, arg5, arg6);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        LevelAccessor levelAccessor = arg.getLevel();
        BlockPos blockPos = arg.getClickedPos();
        return (BlockState)((BlockState)this.defaultBlockState().setValue(WATERLOGGED, levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER)).setValue(FACING, arg.getClickedFace());
    }

    public BlockState rotate(BlockState arg, Rotation arg2) {
        return (BlockState)arg.setValue(FACING, arg2.rotate((Direction)arg.getValue(FACING)));
    }

    public BlockState mirror(BlockState arg, Mirror arg2) {
        return arg.rotate(arg2.getRotation((Direction)arg.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState arg) {
        return (Boolean)arg.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(arg);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(new Property[]{WATERLOGGED, FACING});
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        FACING = BlockStateProperties.FACING;
    }

    public InteractionResult use(BlockState block_state, Level level, BlockPos block_pos, Player player, InteractionHand interaction_hand, BlockHitResult block_hit_result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            var look_angle = player.getLookAngle();
            var x = look_angle.x;
            var y = look_angle.y;
            var z = look_angle.z;
            Vec3 pull = new Vec3(x * PULL_FORCE, y * PULL_FORCE, z * PULL_FORCE);

            player.addDeltaMovement(pull);
            player.hurtMarked = true;
//            player.setDeltaMovement(player.getLookAngle());
            return InteractionResult.CONSUME;
        }
    }
}
