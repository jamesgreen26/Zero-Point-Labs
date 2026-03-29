package g_mungus.zpl.block.advanced_gryo;

import g_mungus.zpl.menu.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class AdvancedGyroscopeControllerMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 1;
    private static final int SLOT_X = 80;
    private static final int SLOT_Y = 35;
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    private final @Nullable BlockEntity blockEntity;
    private final Level level;
    private final ContainerLevelAccess access;
    private final IItemHandler itemHandler;
    private int energyStored;
    private int maxEnergyStored;
    private int energyUsage;

    public AdvancedGyroscopeControllerMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, getBlockEntity(playerInventory, buffer));
    }

    public AdvancedGyroscopeControllerMenu(int id, Inventory playerInventory, BlockEntity blockEntity) {
        super(ModMenuTypes.ADVANCED_GYROSCOPE_CONTROLLER.get(), id);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
        this.access = blockEntity == null
                ? ContainerLevelAccess.NULL
                : ContainerLevelAccess.create(level, blockEntity.getBlockPos());
        this.itemHandler = resolveItemHandler(blockEntity);
        this.maxEnergyStored = resolveMaxEnergy(blockEntity);

        addSlot(new SlotItemHandler(itemHandler, 0, SLOT_X, SLOT_Y));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlot(createEnergySlot(blockEntity));
        addDataSlot(createEnergyUsageSlot(blockEntity));
    }

    private static BlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        return playerInventory.player.level().getBlockEntity(pos);
    }

    private static IItemHandler resolveItemHandler(@Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity controller) {
            return controller.getItemHandler();
        }
        return new ItemStackHandler(SLOT_COUNT);
    }

    private static int resolveMaxEnergy(@Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity controller) {
            return controller.getMaxEnergyStored();
        }
        return AdvancedGyroscopeControllerBlockEntity.MAX_ENERGY;
    }

    private DataSlot createEnergySlot(@Nullable BlockEntity blockEntity) {
        return new DataSlot() {
            @Override
            public int get() {
                if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity controller) {
                    return controller.getEnergyStored();
                }
                return energyStored;
            }

            @Override
            public void set(int value) {
                energyStored = value;
            }
        };
    }

    private DataSlot createEnergyUsageSlot(@Nullable BlockEntity blockEntity) {
        return new DataSlot() {
            @Override
            public int get() {
                if (blockEntity instanceof AdvancedGyroscopeControllerBlockEntity controller) {
                    return controller.getEnergyUsage();
                }
                return energyUsage;
            }

            @Override
            public void set(int value) {
                energyUsage = value;
            }
        };
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) {
            return false;
        }
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < SLOT_COUNT) {
                if (!moveItemStackTo(slotStack, SLOT_COUNT, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotStack, 0, SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    public int getEnergyStored() {
        return energyStored;
    }

    public int getMaxEnergyStored() {
        return maxEnergyStored;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, PLAYER_INV_Y + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, HOTBAR_Y));
        }
    }
}
