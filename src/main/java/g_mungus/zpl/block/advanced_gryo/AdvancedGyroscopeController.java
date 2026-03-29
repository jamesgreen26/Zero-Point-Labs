package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AdvancedGyroscopeController extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AdvancedGyroscopeController(Properties arg) {
        super(arg);
        registerDefaultState(stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    public void update(ServerLevel level, BlockPos self) {
        BlockState state = level.getBlockState(self);
        if (!(state.getBlock() instanceof AdvancedGyroscopeController)) return;

        BlockPos center = self.offset(state.getValue(FACING).getOpposite().getNormal());

        boolean valid = true;
        for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
            BlockPos pos = expected.resolve(center);
            BlockState blockState = level.getBlockState(pos);
            if (!(blockState.getBlock() instanceof AdvancedGyroscopeFrame)) {
                valid = false;
                break;
            }
            GyroFrameState current = blockState.getValue(AdvancedGyroscopeFrame.FRAME_STATE);
            if (current != GyroFrameState.DISASSEMBLED && current != expected) {
                valid = false;
                break;
            }
        }

        if (valid) {
            for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
                BlockPos pos = expected.resolve(center);
                level.setBlock(pos, level.getBlockState(pos).setValue(AdvancedGyroscopeFrame.FRAME_STATE, expected), 3);
            }
        } else {
            // Release any frames previously claimed by this controller
            for (GyroFrameState expected : GyroFrameState.OUTLINE_POSITIONS) {
                BlockPos pos = expected.resolve(center);
                BlockState blockState = level.getBlockState(pos);
                if (blockState.getBlock() instanceof AdvancedGyroscopeFrame &&
                        blockState.getValue(AdvancedGyroscopeFrame.FRAME_STATE) == expected) {
                    level.setBlock(pos, blockState.setValue(AdvancedGyroscopeFrame.FRAME_STATE, GyroFrameState.DISASSEMBLED), 3);
                }
            }
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
        builder.add(FACING);
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
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
