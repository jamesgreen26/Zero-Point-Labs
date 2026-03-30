package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AdvancedGyroscopeController extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");

    public AdvancedGyroscopeController(Properties arg) {
        super(arg);
        registerDefaultState(stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH).setValue(ASSEMBLED, false));
    }

    public void update(ServerLevel level, BlockPos self) {
        BlockState state = level.getBlockState(self);
        if (!(state.getBlock() instanceof AdvancedGyroscopeController)) return;

        BlockPos center = self.offset(state.getValue(FACING).getOpposite().getNormal());

        Direction facing = state.getValue(FACING);
        BlockPos rightPos = self.relative(facing.getClockWise());
        BlockPos leftPos = self.relative(facing.getCounterClockWise());

        boolean valid = level.getBlockState(rightPos).getBlock() instanceof AdvancedGyroscopeInputModule
                && level.getBlockState(leftPos).getBlock() instanceof AdvancedGyroscopeInputModule;

        for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
            if (!valid) break;
            BlockPos pos = expected.resolve(center);
            if (pos.equals(rightPos) || pos.equals(leftPos)) continue;
            BlockState blockState = level.getBlockState(pos);
            if (!(blockState.getBlock() instanceof AdvancedGyroscopeFrame)) {
                valid = false;
                break;
            }
            GyroFrameState current = blockState.getValue(AdvancedGyroscopeFrame.FRAME_STATE);
            if (current != GyroFrameState.DISASSEMBLED && current != expected) {
                valid = false;
            }
        }

        if (valid) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos otherPos = center.relative(dir);
                if (otherPos.equals(self)) continue;
                BlockState otherState = level.getBlockState(otherPos);
                if (otherState.getBlock() instanceof AdvancedGyroscopeController
                        && otherState.getValue(ASSEMBLED)
                        && otherState.getValue(FACING) == dir) {
                    valid = false;
                    break;
                }
            }
        }

        if (valid) {
            for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
                BlockPos pos = expected.resolve(center);
                if (pos.equals(rightPos) || pos.equals(leftPos)) continue;
                level.setBlock(pos, level.getBlockState(pos).setValue(AdvancedGyroscopeFrame.FRAME_STATE, expected), 3);
            }
            level.setBlock(self, state.setValue(ASSEMBLED, true), 3);
        } else {
            // Release any frames previously claimed by this controller
            for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
                BlockPos pos = expected.resolve(center);
                if (pos.equals(rightPos) || pos.equals(leftPos)) continue;
                BlockState blockState = level.getBlockState(pos);
                if (blockState.getBlock() instanceof AdvancedGyroscopeFrame &&
                        blockState.getValue(AdvancedGyroscopeFrame.FRAME_STATE) == expected) {
                    level.setBlock(pos, blockState.setValue(AdvancedGyroscopeFrame.FRAME_STATE, GyroFrameState.DISASSEMBLED), 3);
                }
            }
            level.setBlock(self, state.setValue(ASSEMBLED, false), 3);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING, ASSEMBLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvancedGyroscopeControllerBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, (AdvancedGyroscopeControllerBlockEntity) blockEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    //todo: properly handle other controller present
    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity controller) {
                SimpleContainer container = new SimpleContainer(1);
                container.setItem(0, controller.getItemHandler().getStackInSlot(0));
                Containers.dropContents(level, pos, container);
            }

            if (state.getValue(ASSEMBLED) && level instanceof ServerLevel serverLevel) {
                Direction removedFacing = state.getValue(FACING);
                BlockPos center = pos.offset(removedFacing.getOpposite().getNormal());
                BlockPos rightPos = pos.relative(removedFacing.getClockWise());
                BlockPos leftPos = pos.relative(removedFacing.getCounterClockWise());
                for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
                    BlockPos framePos = expected.resolve(center);
                    if (framePos.equals(rightPos) || framePos.equals(leftPos)) continue;
                    BlockState frameState = level.getBlockState(framePos);
                    if (frameState.getBlock() instanceof AdvancedGyroscopeFrame &&
                            frameState.getValue(AdvancedGyroscopeFrame.FRAME_STATE) == expected) {
                        serverLevel.setBlock(framePos, frameState.setValue(AdvancedGyroscopeFrame.FRAME_STATE, GyroFrameState.DISASSEMBLED), 3);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState arg, Level level, BlockPos pos, BlockState arg4, boolean bl) {
        super.onPlace(arg, level, pos, arg4, bl);
        if (level instanceof ServerLevel serverLevel) {
            update(serverLevel, pos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
