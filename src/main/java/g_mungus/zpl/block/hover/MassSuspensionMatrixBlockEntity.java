package g_mungus.zpl.block.hover;


import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.gyro.GyroscopeBlock;
import g_mungus.zpl.block.thruster.ThrusterData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MassSuspensionMatrixBlockEntity extends BlockEntity {
    public MassSuspensionMatrixBlockEntity(BlockPos arg2, BlockState arg3) {
        super(ModBlockEntities.MASS_SUSPENSION_MATRIX_BLOCK_ENTITY.get(), arg2, arg3);
    }

    public ThrusterData thrust;

    public void tick() {
        if (level == null) return;
        if (thrust == null) {
            MassSuspensionMatrixBlock.addApplier(level, getBlockPos());
        }
        if (thrust != null) {
            thrust.strength = level.getBestNeighborSignal(getBlockPos()) / 15.0;
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
