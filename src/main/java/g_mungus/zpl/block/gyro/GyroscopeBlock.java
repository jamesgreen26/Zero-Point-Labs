package g_mungus.zpl.block.gyro;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntity;
import g_mungus.zpl.block.thruster.ThrusterForceApplier;
import g_mungus.zpl.ship.ZPLShipAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class GyroscopeBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public GyroscopeBlock(Properties arg) {
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
    public @Nullable BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new GyroscopeBlockEntity(arg, arg2);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState arg2, BlockEntityType<T> arg3) {
        return level.isClientSide() ? null : (level1, pos, state, blockEntity) -> {
            if (blockEntity instanceof GyroscopeBlockEntity gyro) {
                gyro.tick();
            }
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
        if (attachment != null && blockEntity instanceof GyroscopeBlockEntity gyro) {
            gyro.thrust = new ThrusterData(VectorConversionsMCKt.toJOMLD(state.getValue(FACING).getOpposite().getNormal()), 0.0);

            GyroForceApplier applier = new GyroForceApplier(gyro.thrust);
            attachment.addApplier(pos, applier);
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        removeApplier(level, pos);

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private static void removeApplier(@NotNull Level level, @NotNull BlockPos pos) {
        if (!level.isClientSide()) {
            ZPLShipAttachment ship = ZPLShipAttachment.get(level, pos);
            if (ship != null) {
                ship.removeApplier((ServerLevel) level, pos);
            }
        }
    }
}
