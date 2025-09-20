package g_mungus.zpl.block;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.gyro.GyroscopeBlock;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(Registries.BLOCK, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<Block> THRUSTER_EXHAUST_BLOCK = BLOCKS.register("ion_thruster_exhaust",
        () -> new ThrusterExhaustBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ION_MODULATOR_BLOCK = BLOCKS.register("ion_thrust_modulator",
            () -> new IonModulatorBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> GYROSCOPE_BLOCK = BLOCKS.register("gyroscope",
            () -> new GyroscopeBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CAUTION_BLOCK = BLOCKS.register("caution_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SPACE_PLATING = BLOCKS.register("space_plating",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SPACE_PLATING_SLAB = BLOCKS.register("space_plating_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SPACE_PLATING_STAIRS = BLOCKS.register("space_plating_stairs",
            () -> new StairBlock(() -> SPACE_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));
} 