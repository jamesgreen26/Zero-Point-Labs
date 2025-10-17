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

    public static final RegistryObject<Item> MASS_SUSPENSION_MATRIX_ITEM = ITEMS.register("mass_suspension_matrix",
            () -> new BlockItem(ModBlocks.MASS_SUSPENSION_MATRIX_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> ENERGY_ORB_LAUNCHER_ITEM = ITEMS.register("photon_pulse_blaster",
            () -> new BlockItem(ModBlocks.ENERGY_ORB_LAUNCHER.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_ITEM = ITEMS.register("space_plating",
            () -> new BlockItem(ModBlocks.SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_SLAB_ITEM = ITEMS.register("space_plating_slab",
            () -> new BlockItem(ModBlocks.SPACE_PLATING_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_STAIRS_ITEM = ITEMS.register("space_plating_stairs",
            () -> new BlockItem(ModBlocks.SPACE_PLATING_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Item> CAUTION_BLOCK_ITEM = ITEMS.register("caution_block",
            () -> new BlockItem(ModBlocks.CAUTION_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_PLATFORM_ITEM = ITEMS.register("launch_platform",
            () -> new BlockItem(ModBlocks.LAUNCH_PLATFORM.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_BUTTON_ITEM = ITEMS.register("launch_button",
            () -> new BlockItem(ModBlocks.LAUNCH_BUTTON.get(), new Item.Properties()));
} 