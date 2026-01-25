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

import java.util.HashMap;
import java.util.Map;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(Registries.ITEM, ZeroPointLabsMod.MOD_ID);

    public static Map<String, RegistryObject<Item>> DYNAMIC_ITEMS = new HashMap<>();

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

    public static final RegistryObject<Item> CATWALK = ITEMS.register("catwalk",
            () -> new BlockItem(ModBlocks.CATWALK.get(), new Item.Properties()));
    public static final RegistryObject<Item> CATWALK_STAIRS = ITEMS.register("catwalk_stairs",
            () -> new BlockItem(ModBlocks.CATWALK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> GRAB_HANDLE = ITEMS.register("grab_handle",
            () -> new BlockItem(ModBlocks.GRAB_HANDLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_PLATFORM_ITEM = ITEMS.register("launch_platform",
            () -> new BlockItem(ModBlocks.LAUNCH_PLATFORM.get(), new Item.Properties()));

    public static final RegistryObject<Item> LAUNCH_BUTTON_ITEM = ITEMS.register("launch_button",
            () -> new BlockItem(ModBlocks.LAUNCH_BUTTON.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_TRUSS = ITEMS.register("space_truss",
            () -> new BlockItem(ModBlocks.SPACE_TRUSS.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPACE_SCAFFOLD = ITEMS.register("space_scaffold",
            () -> new BlockItem(ModBlocks.SPACE_SCAFFOLD.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_ITEM = ITEMS.register("space_plating",
            () -> new BlockItem(ModBlocks.SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_SLAB_ITEM = ITEMS.register("space_plating_slab",
            () -> new BlockItem(ModBlocks.SPACE_PLATING_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_PLATING_STAIRS_ITEM = ITEMS.register("space_plating_stairs",
            () -> new BlockItem(ModBlocks.SPACE_PLATING_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Item> RIVETED_SPACE_PLATING_ITEM = ITEMS.register("riveted_space_plating",
            () -> new BlockItem(ModBlocks.RIVETED_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> RIVETED_SPACE_PLATING_SLAB_ITEM = ITEMS.register("riveted_space_plating_slab",
            () -> new BlockItem(ModBlocks.RIVETED_SPACE_PLATING_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Item> RIVETED_SPACE_PLATING_STAIRS_ITEM = ITEMS.register("riveted_space_plating_stairs",
            () -> new BlockItem(ModBlocks.RIVETED_SPACE_PLATING_STAIRS.get(), new Item.Properties()));



    public static final RegistryObject<Item> RED_SPACE_PLATING = ITEMS.register("red_space_plating",
            () -> new BlockItem(ModBlocks.RED_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> ORANGE_SPACE_PLATING = ITEMS.register("orange_space_plating",
            () -> new BlockItem(ModBlocks.ORANGE_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> YELLOW_SPACE_PLATING = ITEMS.register("yellow_space_plating",
            () -> new BlockItem(ModBlocks.YELLOW_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> GREEN_SPACE_PLATING = ITEMS.register("green_space_plating",
            () -> new BlockItem(ModBlocks.GREEN_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> LIME_SPACE_PLATING = ITEMS.register("lime_space_plating",
            () -> new BlockItem(ModBlocks.LIME_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> CYAN_SPACE_PLATING = ITEMS.register("cyan_space_plating",
            () -> new BlockItem(ModBlocks.CYAN_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLUE_SPACE_PLATING = ITEMS.register("blue_space_plating",
            () -> new BlockItem(ModBlocks.BLUE_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> LIGHT_BLUE_SPACE_PLATING = ITEMS.register("light_blue_space_plating",
            () -> new BlockItem(ModBlocks.LIGHT_BLUE_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> PURPLE_SPACE_PLATING = ITEMS.register("purple_space_plating",
            () -> new BlockItem(ModBlocks.PURPLE_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> MAGENTA_SPACE_PLATING = ITEMS.register("magenta_space_plating",
            () -> new BlockItem(ModBlocks.MAGENTA_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> PINK_SPACE_PLATING = ITEMS.register("pink_space_plating",
            () -> new BlockItem(ModBlocks.PINK_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> BROWN_SPACE_PLATING = ITEMS.register("brown_space_plating",
            () -> new BlockItem(ModBlocks.BROWN_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLACK_SPACE_PLATING = ITEMS.register("black_space_plating",
            () -> new BlockItem(ModBlocks.BLACK_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> LIGHT_GRAY_SPACE_PLATING = ITEMS.register("light_gray_space_plating",
            () -> new BlockItem(ModBlocks.LIGHT_GRAY_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> GRAY_SPACE_PLATING = ITEMS.register("gray_space_plating",
            () -> new BlockItem(ModBlocks.GRAY_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> CHROME_SPACE_PLATING = ITEMS.register("chrome_space_plating",
            () -> new BlockItem(ModBlocks.CHROME_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> AGATE_SPACE_PLATING = ITEMS.register("agate_space_plating",
            () -> new BlockItem(ModBlocks.AGATE_SPACE_PLATING.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_GRATING_BLOCK = ITEMS.register("space_grating_block",
            () -> new BlockItem(ModBlocks.SPACE_GRATING_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPACE_MESH_BLOCK = ITEMS.register("space_mesh_block",
            () -> new BlockItem(ModBlocks.SPACE_MESH_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> CAUTION_BLOCK_ITEM = ITEMS.register("caution_block",
            () -> new BlockItem(ModBlocks.CAUTION_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> RADIATION_CAUTION_BLOCK_ITEM = ITEMS.register("radiation_caution_block",
            () -> new BlockItem(ModBlocks.RADIATION_CAUTION_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> VOID_CAUTION_BLOCK = ITEMS.register("void_caution_block",
            () -> new BlockItem(ModBlocks.VOID_CAUTION_BLOCK.get(), new Item.Properties()));



    public static final RegistryObject<Item> DROID_CORE_ITEM = ITEMS.register("droid_core",
            () -> new BlockItem(ModBlocks.DROID_CORE_BLOCK.get(), new Item.Properties()));

    // behold, random items for crafting recipes
    public static final RegistryObject<Item> SPACE_METAL_INGOT = ITEMS.register("space_metal_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_PLATE = ITEMS.register("space_metal_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_MESH = ITEMS.register("space_metal_mesh",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_ROD = ITEMS.register("space_metal_rod",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_PIPE = ITEMS.register("space_metal_pipe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_SCREW = ITEMS.register("space_metal_screw",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_BOLT = ITEMS.register("space_metal_bolt",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPACE_METAL_SPRING = ITEMS.register("space_metal_spring",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CAPACITOR = ITEMS.register("capacitor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TRANSISTOR = ITEMS.register("transistor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MODULATOR = ITEMS.register("modulator",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_MAGNETRON = ITEMS.register("copper_magnetron",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_MAGNETRON = ITEMS.register("gold_magnetron",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_WIRE = ITEMS.register("gold_wire",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VERDITE_WIRE = ITEMS.register("verdite_wire",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EMPTY_SPOOL = ITEMS.register("empty_spool",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SPOOL = ITEMS.register("copper_spool",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_SPOOL = ITEMS.register("gold_spool",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VERDITE_SPOOL = ITEMS.register("verdite_spool",
            () -> new Item(new Item.Properties()));
} 