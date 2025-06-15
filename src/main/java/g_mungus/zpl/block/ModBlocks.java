package g_mungus.zpl.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import g_mungus.zpl.ZeroPointLabsMod;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, ZeroPointLabsMod.MOD_ID);

    public static final DeferredHolder<Block, ExampleBlock> EXAMPLE_BLOCK = BLOCKS.register("example_block",
        () -> new ExampleBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()));
} 