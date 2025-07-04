package g_mungus.zpl.block;

import g_mungus.zpl.block.gyro.GyroscopeBlock;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import g_mungus.zpl.ZeroPointLabsMod;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, ZeroPointLabsMod.MOD_ID);

    public static final DeferredHolder<Block, ThrusterExhaustBlock> THRUSTER_EXHAUST_BLOCK = BLOCKS.register("ion_thruster_exhaust",
        () -> new ThrusterExhaustBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, IonModulatorBlock> ION_MODULATOR_BLOCK = BLOCKS.register("ion_thrust_modulator",
            () -> new IonModulatorBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, GyroscopeBlock> GYROSCOPE_BLOCK = BLOCKS.register("gyroscope",
            () -> new GyroscopeBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));
} 