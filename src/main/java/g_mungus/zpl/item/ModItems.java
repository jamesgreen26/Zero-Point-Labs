package g_mungus.zpl.item;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.item.tooltip.FERedstoneBlockItem;
import g_mungus.zpl.item.tooltip.RedstoneBlockItem;
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
            () -> new FERedstoneBlockItem(ModBlocks.ION_MODULATOR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> GYROSCOPE_ITEM = ITEMS.register("gyroscope",
            () -> new RedstoneBlockItem(ModBlocks.GYROSCOPE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> MASS_SUSPENSION_MATRIX_ITEM = ITEMS.register("mass_suspension_matrix",
            () -> new RedstoneBlockItem(ModBlocks.MASS_SUSPENSION_MATRIX_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> ENERGY_ORB_LAUNCHER_ITEM = ITEMS.register("photon_pulse_blaster",
            () -> new FERedstoneBlockItem(ModBlocks.ENERGY_ORB_LAUNCHER.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_PLATFORM_ITEM = ITEMS.register("launch_platform",
            () -> new BlockItem(ModBlocks.LAUNCH_PLATFORM.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_BUTTON_ITEM = ITEMS.register("launch_button",
            () -> new BlockItem(ModBlocks.LAUNCH_BUTTON.get(), new Item.Properties()));

    public static final RegistryObject<Item> DROID_CORE_ITEM = ITEMS.register("droid_core",
            () -> new BlockItem(ModBlocks.DROID_CORE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_GYROSCOPE_CONTROLLER_ITEM = ITEMS.register("advanced_gyroscope_controller",
            () -> new BlockItem(ModBlocks.ADVANCED_GYROSCOPE_CONTROLLER.get(), new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_GYROSCOPE_FRAME_ITEM = ITEMS.register("advanced_gyroscope_frame",
            () -> new BlockItem(ModBlocks.ADVANCED_GYROSCOPE_FRAME.get(), new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_GYROSCOPE_INPUT_MODULE_ITEM = ITEMS.register("advanced_gyroscope_input_module",
            () -> new BlockItem(ModBlocks.ADVANCED_GYROSCOPE_INPUT_MODULE.get(), new Item.Properties()));
} 
