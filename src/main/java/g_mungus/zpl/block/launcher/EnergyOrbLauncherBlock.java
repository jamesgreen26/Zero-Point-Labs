package g_mungus.zpl.block.launcher;

import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

public class EnergyOrbLauncherBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 8);

    private static final Vec3[] ROTATION_OFFSETS = {
        new Vec3(0, 0, 0),      // 0: No rotation
        new Vec3(5, 0, 0),      // 1: X5
        new Vec3(10, 0, 0),     // 2: X10
        new Vec3(-5, 0, 0),     // 3: XM5
        new Vec3(-10, 0, 0),    // 4: XM10
        new Vec3(0, 5, 0),      // 5: Y5
        new Vec3(0, 10, 0),     // 6: Y10
        new Vec3(0, -5, 0),     // 7: YM5
        new Vec3(0, -10, 0)     // 8: YM10
    };

    public EnergyOrbLauncherBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ROTATION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(ROTATION, (state.getValue(ROTATION) + 1) % 9), Block.UPDATE_CLIENTS);
            player.swing(hand);
            return InteractionResult.CONSUME;
        }
        return super.use(state, level, pos, player, hand, result);
    }

    public static Vec3 getAimVector(BlockState state) {
        Direction facing = state.getValue(FACING);
        int rotation = state.getValue(ROTATION);

        Vec3 localDirection = new Vec3(0, 0, -1);
        Vec3 rotationOffset = ROTATION_OFFSETS[rotation];

        if (rotationOffset.x != 0) {
            double angleRadians = -Math.toRadians(rotationOffset.x);
            localDirection = localDirection.xRot((float) angleRadians);
        }
        if (rotationOffset.y != 0) {
            double angleRadians = Math.toRadians(rotationOffset.y);
            localDirection = localDirection.yRot((float) angleRadians);
        }
        if (rotationOffset.z != 0) {
            double angleRadians = Math.toRadians(rotationOffset.z);
            localDirection = localDirection.zRot((float) angleRadians);
        }

        Vec3 worldDirection = transformLocalToWorld(localDirection, facing);
        return worldDirection.normalize();
    }

    private static Vec3 transformLocalToWorld(Vec3 local, Direction facing) {
        return switch (facing) {
            case NORTH -> new Vec3(local.x, local.y, local.z);      // No rotation
            case SOUTH -> new Vec3(-local.x, local.y, -local.z);    // Rotate 180° around Y
            case EAST -> new Vec3(-local.z, local.y, local.x);      // Rotate 90° around Y
            case WEST -> new Vec3(local.z, local.y, -local.x);      // Rotate -90° around Y
            case UP -> new Vec3(local.x, -local.z, local.y);        // Rotate 90° around X
            case DOWN -> new Vec3(local.x, local.z, -local.y);      // Rotate -90° around X
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyOrbLauncherBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.ENERGY_ORB_LAUNCHER.get(), EnergyOrbLauncherBlockEntity::serverTick);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }
}
