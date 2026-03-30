package g_mungus.zpl.block.advanced_gryo;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zps.block.cableNetwork.core.BuiltinCableStandards;
import g_mungus.zps.block.cableNetwork.core.CableComponentBlock;
import g_mungus.zps.block.cableNetwork.core.Channels;
import g_mungus.zps.block.cableNetwork.core.NetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedGyroscopeInputModule extends CableComponentBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

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
                .setValue(CONNECTED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED);
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
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState arg4, boolean bl) {
        super.onPlace(state, level, pos, arg4, bl);

        if (level instanceof ServerLevel serverLevel) {
            updateControllers(serverLevel, pos);
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState arg4, boolean bl) {
        super.onRemove(state, level, pos, arg4, bl);

        if (level instanceof ServerLevel serverLevel) {
            updateControllers(serverLevel, pos);
        }
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.ADVANCED_GYRO_INPUT_MODULE.get().create(pos, state);
    }

    @Override
    public String getCableStandard() {
        return BuiltinCableStandards.DEFAULT;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public int getTotalChannelCount() {
        return 4;
    }

    @Override
    public int getChannelCountForConnection(BlockPos self, BlockPos from, Level level) {
        BlockState state = level.getBlockState(self);
        Direction facing = state.getValue(FACING);
        if (from.equals(self.offset(facing.getNormal()))) {
            return 4;
        }
        return 0;
    }

    @Override
    public List<BlockPos> getConnectingNeighbors(NetworkNode self, Level level) {
        BlockState state = level.getBlockState(self.pos());
        Direction facing = state.getValue(FACING);
        return List.of(self.pos().offset(facing.getNormal()));
    }

    @Override
    public int getNewChannel(BlockPos self, NetworkNode input, Level level) {
        return Channels.toQuad(input.channel());
    }

    @Override
    public void updateConnections(BlockState state, Level level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean connected = canConnect(pos, pos.offset(facing.getNormal()), level);
        BlockState newState = state.setValue(CONNECTED, connected);
        if (!state.equals(newState)) {
            level.setBlock(pos, newState, 3);
            updateNetwork(pos, level);
        }
    }
}
