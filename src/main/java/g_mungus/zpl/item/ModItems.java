package g_mungus.zpl.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(Registries.ITEM, ZeroPointLabsMod.MOD_ID);

    public static final DeferredHolder<Item, BlockItem> THRUSTER_EXHAUST_ITEM = ITEMS.register("ion_thruster_exhaust",
        () -> new BlockItem(ModBlocks.THRUSTER_EXHAUST_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> ION_MODULATOR_ITEM = ITEMS.register("ion_thrust_modulator",
            () -> new BlockItem(ModBlocks.ION_MODULATOR_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> GYROSCOPE_ITEM = ITEMS.register("gyroscope",
            () -> new BlockItem(ModBlocks.GYROSCOPE_BLOCK.get(), new Item.Properties()));
} 