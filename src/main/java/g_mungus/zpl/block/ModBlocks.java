package g_mungus.zpl.block;

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
} 