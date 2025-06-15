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

    public static final DeferredHolder<Item, BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
        () -> new BlockItem(ModBlocks.EXAMPLE_BLOCK.get(), new Item.Properties()));
} 