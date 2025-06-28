package g_mungus.zpl.block.thruster;

import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IonModulatorBlockEntity extends BlockEntity {
    private static final int MAX_ENERGY = 4_000;
    private static final int MAX_TRANSFER = 2_000;

    private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, MAX_TRANSFER);
    private final LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    public IonModulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ION_MODULATOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyHandler.invalidate();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", energyStorage.serializeNBT());
    }
}
