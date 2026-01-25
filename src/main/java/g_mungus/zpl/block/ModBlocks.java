package g_mungus.zpl.block;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.datagen.BlockDataGenerator;
import g_mungus.zpl.block.droidcore.DroidCoreBlock;
import g_mungus.zpl.block.gyro.GyroscopeBlock;
import g_mungus.zpl.block.hover.MassSuspensionMatrixBlock;
import g_mungus.zpl.block.launcher.EnergyOrbLauncherBlock;
import g_mungus.zpl.block.assembly.LaunchButtonBlock;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
import g_mungus.zpl.item.ModItems;
import g_mungus.zpl.mixin.BlockBehaviourAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(Registries.BLOCK, ZeroPointLabsMod.MOD_ID);


    static Optional<RegistryObject<Block>> blockLookup(String name) {
        for (var block: BLOCKS.getEntries()) {
            ResourceLocation id = block.getId();
            if (id != null && id.getPath().equals(name)) {
                return Optional.of(block);
            }
        }
        return Optional.empty();
    }

    public static final RegistryObject<Block> THRUSTER_EXHAUST_BLOCK = BLOCKS.register("ion_thruster_exhaust",
        () -> new ThrusterExhaustBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> ION_MODULATOR_BLOCK = BLOCKS.register("ion_thrust_modulator",
        () -> new IonModulatorBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> GYROSCOPE_BLOCK = BLOCKS.register("gyroscope",
        () -> new GyroscopeBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> CAUTION_BLOCK = BLOCKS.register("caution_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
        )
    );

    public static final RegistryObject<Block> RADIATION_CAUTION_BLOCK = BLOCKS.register("radiation_caution_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
        )
    );

    public static final RegistryObject<Block> VOID_CAUTION_BLOCK = BLOCKS.register("void_caution_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
        )
    );

    public static final RegistryObject<Block> SPACE_PLATING = BLOCKS.register("space_plating",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> SPACE_PLATING_SLAB = BLOCKS.register("space_plating_slab",
        () -> new SlabBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> SPACE_PLATING_STAIRS = BLOCKS.register("space_plating_stairs",
        () -> new StairBlock(() -> SPACE_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> RED_SPACE_PLATING = BLOCKS.register("red_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> ORANGE_SPACE_PLATING = BLOCKS.register("orange_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> YELLOW_SPACE_PLATING = BLOCKS.register("yellow_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> GREEN_SPACE_PLATING = BLOCKS.register("green_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> LIME_SPACE_PLATING = BLOCKS.register("lime_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> CYAN_SPACE_PLATING = BLOCKS.register("cyan_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> BLUE_SPACE_PLATING = BLOCKS.register("blue_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> LIGHT_BLUE_SPACE_PLATING = BLOCKS.register("light_blue_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> PURPLE_SPACE_PLATING = BLOCKS.register("purple_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> MAGENTA_SPACE_PLATING = BLOCKS.register("magenta_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> PINK_SPACE_PLATING = BLOCKS.register("pink_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> BROWN_SPACE_PLATING = BLOCKS.register("brown_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> BLACK_SPACE_PLATING = BLOCKS.register("black_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> GRAY_SPACE_PLATING = BLOCKS.register("gray_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> LIGHT_GRAY_SPACE_PLATING = BLOCKS.register("light_gray_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> CHROME_SPACE_PLATING = BLOCKS.register("chrome_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> AGATE_SPACE_PLATING = BLOCKS.register("agate_space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            )
    );

    public static final RegistryObject<Block> RIVETED_SPACE_PLATING = BLOCKS.register("riveted_space_plating",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> RIVETED_SPACE_PLATING_SLAB = BLOCKS.register("riveted_space_plating_slab",
        () -> new SlabBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> RIVETED_SPACE_PLATING_STAIRS = BLOCKS.register("riveted_space_plating_stairs",
        () -> new StairBlock(() -> SPACE_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
        )
    );

    public static final RegistryObject<Block> SPACE_TRUSS = BLOCKS.register("space_truss",
        () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .noOcclusion()
        )
    );

    public static final RegistryObject<Block> SPACE_SCAFFOLD = BLOCKS.register("space_scaffold",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .noOcclusion()
        )
    );

    public static final RegistryObject<Block> SPACE_MESH_BLOCK = BLOCKS.register("space_mesh_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
            )
    );

    public static final RegistryObject<Block> SPACE_GRATING_BLOCK = BLOCKS.register("space_grating_block",
            () -> new GlassBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
            )
    );

    public static final RegistryObject<Block> CATWALK = BLOCKS.register("catwalk",
        () -> new CatwalkBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .noOcclusion()
        )
    );

    public static final RegistryObject<Block> CATWALK_STAIRS = BLOCKS.register("catwalk_stairs",
            () -> new CatwalkStairsBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
            )
    );

    public static final RegistryObject<Block> GRAB_HANDLE = BLOCKS.register("grab_handle",
            () -> new GrabHandleBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
            )
    );



    public static final RegistryObject<Block> MASS_SUSPENSION_MATRIX_BLOCK = BLOCKS.register("mass_suspension_matrix",
            () -> new MassSuspensionMatrixBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ENERGY_ORB_LAUNCHER = BLOCKS.register("photon_pulse_blaster",
            () -> new EnergyOrbLauncherBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LAUNCH_PLATFORM = BLOCKS.register("launch_platform",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LAUNCH_BUTTON = BLOCKS.register("launch_button",
            () -> new LaunchButtonBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DROID_CORE_BLOCK = BLOCKS.register("droid_core",
            () -> new DroidCoreBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));
    static {
        for (var entry : BlockDataGenerator.blocksToDatagen.entrySet()) {
            for (var type : entry.getValue()) {
                switch (type) {
                    case simple -> { /* registered manually */}
                    case slab -> {
                        String id = entry.getKey() + "_slab";
                        RegistryObject<Block> slab = BLOCKS.register(id,
                                () -> new SlabBlock(((BlockBehaviourAccessor) blockLookup(entry.getKey()).get().get()).getProperties()));

                        ModItems.DYNAMIC_ITEMS.put(id, ModItems.ITEMS.register(id,
                                () -> new BlockItem(slab.get(), new Item.Properties())));
                    }
                    case stairs -> {
                        String id = entry.getKey() + "_stairs";
                        RegistryObject<Block> stairs = BLOCKS.register(id,
                                () -> {
                                    Block block = blockLookup(entry.getKey()).get().get();
                                    return new StairBlock(block::defaultBlockState, ((BlockBehaviourAccessor) block).getProperties());
                                });

                        ModItems.DYNAMIC_ITEMS.put(id, ModItems.ITEMS.register(id,
                                () -> new BlockItem(stairs.get(), new Item.Properties())));
                    }

                    case wall -> {
                        String id = entry.getKey() + "_wall";
                        RegistryObject<Block> wall = BLOCKS.register(id,
                                () -> {
                                    Block block = blockLookup(entry.getKey()).get().get();
                                    return new WallBlock(BlockBehaviour.Properties.copy(block).forceSolidOn());
                                });

                        ModItems.DYNAMIC_ITEMS.put(id, ModItems.ITEMS.register(id,
                                () -> new BlockItem(wall.get(), new Item.Properties())));
                    }
                }
            }
        }
    }

}