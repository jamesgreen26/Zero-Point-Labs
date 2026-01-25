package g_mungus.zpl.item;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.datagen.BlockType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ZeroPointLabsMod.MOD_ID);


    private static final Set<String> alreadyAdded = new HashSet<>();
    public static final RegistryObject<CreativeModeTab> ZPS_TAB = CREATIVE_MODE_TABS.register("zps_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Zero Point Ship Components"))
                    .icon(() -> new ItemStack(ModItems.THRUSTER_EXHAUST_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GYROSCOPE_ITEM.get());
                        output.accept(ModItems.MASS_SUSPENSION_MATRIX_ITEM.get());
                        output.accept(ModItems.ION_MODULATOR_ITEM.get());
                        output.accept(ModItems.THRUSTER_EXHAUST_ITEM.get());
                        output.accept(ModItems.ENERGY_ORB_LAUNCHER_ITEM.get());
                        output.accept(ModItems.LAUNCH_BUTTON_ITEM.get());
                        output.accept(ModItems.LAUNCH_PLATFORM_ITEM.get());
                        output.accept(ModItems.DROID_CORE_ITEM.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> ZPD_TAB = CREATIVE_MODE_TABS.register("zpd",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Zero Point Decoration"))
                    .icon(() -> new ItemStack(ModItems.THRUSTER_EXHAUST_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        addItemGroup(output, ModItems.RED_SPACE_PLATING);
                        addItemGroup(output, ModItems.ORANGE_SPACE_PLATING);
                        addItemGroup(output, ModItems.YELLOW_SPACE_PLATING);
                        addItemGroup(output, ModItems.GREEN_SPACE_PLATING);
                        addItemGroup(output, ModItems.LIME_SPACE_PLATING);
                        addItemGroup(output, ModItems.CYAN_SPACE_PLATING);
                        addItemGroup(output, ModItems.BLUE_SPACE_PLATING);
                        addItemGroup(output, ModItems.LIGHT_BLUE_SPACE_PLATING);
                        addItemGroup(output, ModItems.PURPLE_SPACE_PLATING);
                        addItemGroup(output, ModItems.MAGENTA_SPACE_PLATING);
                        addItemGroup(output, ModItems.PINK_SPACE_PLATING);
                        addItemGroup(output, ModItems.BLACK_SPACE_PLATING);
                        addItemGroup(output, ModItems.BROWN_SPACE_PLATING);
                        addItemGroup(output, ModItems.LIGHT_GRAY_SPACE_PLATING);
                        addItemGroup(output, ModItems.GRAY_SPACE_PLATING);
                        addItemGroup(output, ModItems.CHROME_SPACE_PLATING);
                        addItemGroup(output, ModItems.AGATE_SPACE_PLATING);
                        output.accept(ModItems.SPACE_PLATING_ITEM.get());
                        output.accept(ModItems.SPACE_PLATING_STAIRS_ITEM.get());
                        output.accept(ModItems.SPACE_PLATING_SLAB_ITEM.get());
                        output.accept(ModItems.RIVETED_SPACE_PLATING_ITEM.get());
                        output.accept(ModItems.RIVETED_SPACE_PLATING_STAIRS_ITEM.get());
                        output.accept(ModItems.RIVETED_SPACE_PLATING_SLAB_ITEM.get());
                        output.accept(ModItems.CAUTION_BLOCK_ITEM.get());
                        output.accept(ModItems.VOID_CAUTION_BLOCK.get());
                        output.accept(ModItems.RADIATION_CAUTION_BLOCK_ITEM.get());
                        output.accept(ModItems.SPACE_SCAFFOLD.get());
                        output.accept(ModItems.SPACE_TRUSS.get());
                        output.accept(ModItems.CATWALK.get());
                        output.accept(ModItems.CATWALK_STAIRS.get());
                        for (var item : ModItems.DYNAMIC_ITEMS.values()) {
                            if (alreadyAdded.contains(item.getId().getPath())) {
                                continue;
                            }
                            output.accept(item.get());
                        }
                    }).build());


    private static void addItemGroup(CreativeModeTab.Output output, RegistryObject<Item> item) {
        output.accept(item.get());

        String name = item.getId().getPath();

        for (String suffix : BlockType.suffixes) {
            RegistryObject<Item> it = ModItems.DYNAMIC_ITEMS.get(name + suffix);
            if (it != null) {
                alreadyAdded.add(it.getId().getPath());
                output.accept(it.get());
            }
        }
    }


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}