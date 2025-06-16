package g_mungus.zpl;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ThrusterExhaustBlockEntityRenderer;
import g_mungus.zpl.item.ModItems;

@Mod(ZeroPointLabsMod.MOD_ID)
public final class ZeroPointLabsMod {
    public static final String MOD_ID = "zpl";

    public ZeroPointLabsMod(IEventBus eventBus) {
        // Register blocks and block entities
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);

        // Register client-side renderer
        eventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), ThrusterExhaustBlockEntityRenderer::new);
    }
}
