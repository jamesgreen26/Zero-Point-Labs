package g_mungus.zpl.block.assembly;

import g_mungus.vlib.api.VLibGameUtils;
import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LaunchButtonBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    // Shape matches the model: base (0,0,0 to 16,10,16) + button top (2,10,2 to 14,13,14)
    private static final VoxelShape SHAPE_UNPRESSED = Shapes.or(
        Block.box(0, 0, 0, 16, 10, 16),
        Block.box(2, 10, 2, 14, 13, 14)
    );
    // Pressed shape: base + lower button top (2,8,2 to 14,11,14)
    private static final VoxelShape SHAPE_PRESSED = Shapes.or(
        Block.box(0, 0, 0, 16, 10, 16),
        Block.box(2, 8, 2, 14, 11, 14)
    );

    private final int ticksToStayPressed;

    public LaunchButtonBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
        this.ticksToStayPressed = 20; // 1 second
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(POWERED) ? SHAPE_PRESSED : SHAPE_UNPRESSED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (state.getValue(POWERED)) {
            return InteractionResult.CONSUME;
        } else {
            this.press(state, level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && !state.getValue(POWERED)) {
            this.checkPressed(state, level, pos);
        }
    }

    protected void checkPressed(BlockState state, Level level, BlockPos pos) {
        if (!state.getValue(POWERED)) {
            this.press(state, level, pos, null);
        }
    }

    public void press(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        BlockState blockState = state.setValue(POWERED, true);
        level.setBlock(pos, blockState, 3);
        this.updateNeighbours(blockState, level, pos);
        this.playSound(player, level, pos, true);
        level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        level.scheduleTick(pos, this, this.ticksToStayPressed);

        tryAssembly(level, pos);
    }

    private void tryAssembly(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {

            Block launchButton = ModBlocks.LAUNCH_BUTTON.get();
            Block launchPlatform = ModBlocks.LAUNCH_PLATFORM.get();

            final Set<BlockPos> checked = new HashSet<>();
            final java.util.Queue<BlockPos> toCheck = new java.util.LinkedList<>();

            toCheck.add(pos.below());

            while (!toCheck.isEmpty()) {
                BlockPos current = toCheck.poll();

                if (checked.contains(current)) {
                    continue;
                }

                if (!level.getBlockState(current).is(launchPlatform)) {
                    continue;
                }

                checked.add(current);

                BlockState aboveState = level.getBlockState(current.above());

                if (!aboveState.is(launchButton) && !aboveState.isAir()) {
                    VLibGameUtils.INSTANCE.assembleByConnectivity(serverLevel, current.above(), List.of(launchPlatform, launchButton));
                    return;
                }

                toCheck.add(current.north());
                toCheck.add(current.south());
                toCheck.add(current.east());
                toCheck.add(current.west());
            }
        }
    }

    protected void playSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        // Play stone button sound with lower pitch (0.5 = one octave lower)
        level.playSound(
            pressed ? player : null,
            pos,
            this.getSound(pressed),
            SoundSource.BLOCKS,
            1.2F,
            pressed ? 0.05F : 0.4F
        );
    }

    protected SoundEvent getSound(boolean pressed) {
        return pressed ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(POWERED)) {
            this.checkPressed(state, level, pos);
        }
    }

    @Override
    public void onRemove(BlockState arg, Level arg2, BlockPos arg3, BlockState arg4, boolean bl) {
        if (bl || arg.is(arg4.getBlock())) {
            return;
        }
        if (arg.getValue(POWERED).booleanValue()) {
            this.updateNeighbours(arg, arg2, arg3);
        }
        super.onRemove(arg, arg2, arg3, arg4, bl);
    }

    protected void checkPressed(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getValue(POWERED)) {
            BlockState blockState = state.setValue(POWERED, false);
            level.setBlock(pos, blockState, 3);
            this.updateNeighbours(blockState, level, pos);
            this.playSound(null, level, pos, false);
            level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        }
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.below(), this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getDirectSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return state.getValue(POWERED) && direction == Direction.DOWN ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }
}
