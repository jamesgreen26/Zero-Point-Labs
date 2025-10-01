package g_mungus.zpl.block.hover;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.ZPLShipAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class MassSuspensionMatrixBlock extends Block implements EntityBlock {
    public MassSuspensionMatrixBlock(Properties arg) {
        super(arg);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new MassSuspensionMatrixBlockEntity(arg, arg2);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState arg2, BlockEntityType<T> arg3) {
        return level.isClientSide() ? null : (level1, pos, state, blockEntity) -> {
            if (blockEntity instanceof MassSuspensionMatrixBlockEntity matrixBlockEntity) {
                matrixBlockEntity.tick();
            }
        };
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            addApplier(level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    public static void addApplier(@NotNull Level level, @NotNull BlockPos pos) {
        ZPLShipAttachment attachment = ZPLShipAttachment.get(level, pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (attachment != null && blockEntity instanceof MassSuspensionMatrixBlockEntity matrixBlockEntity) {
            matrixBlockEntity.thrust = new ThrusterData(new Vector3d(), 0.0);

            String dimension = "minecraft:dimension:" + level.dimension().location();

            HoverForceApplier applier = new HoverForceApplier(dimension, matrixBlockEntity.thrust);
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
