package g_mungus.zpl.block.advanced_gryo;

import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class AdvancedGyroscopeControllerBlockEntity extends BlockEntity implements MenuProvider {
    private static final int SLOT_COUNT = 1;
    public static final int MAX_ENERGY = 24_000;
    public static final int MAX_TRANSFER = 2_000;

    private final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);
    private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, MAX_TRANSFER);
    private final LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    public AdvancedGyroscopeControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_GYROSCOPE_CONTROLLER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.zpl.advanced_gyroscope_controller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AdvancedGyroscopeControllerMenu(id, playerInventory, this);
    }

    public IItemHandler getItemHandler() {
        return items;
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        itemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Items")) {
            items.deserializeNBT(tag.getCompound("Items"));
        }
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.put("Energy", energyStorage.serializeNBT());
    }
}
