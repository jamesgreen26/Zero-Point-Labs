package g_mungus.zpl.block.advanced_gryo;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zps.block.cableNetwork.core.Channels;
import g_mungus.zps.blockentity.NetworkTerminalImpl;
import g_mungus.zps.blockentity.RedstoneReceivingTerminal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AdvancedGyroInputModuleBlockEntity extends NetworkTerminalImpl implements RedstoneReceivingTerminal {

    private final int[] signals = new int[]{0, 0, 0, 0};

    public AdvancedGyroInputModuleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_GYRO_INPUT_MODULE.get(), pos, state);
    }

    @Override
    public void receiveSignal(int strength, int channel) {
        signals[channel - Channels.QUAD_1] = strength;
    }

    public int getSignal(int channel) {
        return signals[channel - Channels.QUAD_1];
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putIntArray("Signals", signals);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Signals")) {
            int[] saved = tag.getIntArray("Signals");
            System.arraycopy(saved, 0, signals, 0, Math.min(saved.length, signals.length));
        }
    }
}
