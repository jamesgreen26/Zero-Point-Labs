package g_mungus.zpl.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import g_mungus.zpl.ZeroPointLabsMod;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ZeroPointLabsMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExampleBlockEntity>> EXAMPLE_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("example_block_entity", 
            () -> BlockEntityType.Builder.of(ExampleBlockEntity::new, ModBlocks.EXAMPLE_BLOCK.get()).build(null));
} 