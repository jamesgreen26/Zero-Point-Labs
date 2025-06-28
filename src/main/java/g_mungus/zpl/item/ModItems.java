package g_mungus.zpl.item;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(Registries.ITEM, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<Item> THRUSTER_EXHAUST_ITEM = ITEMS.register("ion_thruster_exhaust",
        () -> new BlockItem(ModBlocks.THRUSTER_EXHAUST_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> ION_MODULATOR_ITEM = ITEMS.register("ion_thrust_modulator",
            () -> new BlockItem(ModBlocks.ION_MODULATOR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> GYROSCOPE_ITEM = ITEMS.register("gyroscope",
            () -> new BlockItem(ModBlocks.GYROSCOPE_BLOCK.get(), new Item.Properties()));
} 