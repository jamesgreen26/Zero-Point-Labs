package g_mungus.zpl;

import g_mungus.zpl.item.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntityRenderer;
import g_mungus.zpl.block.thruster.IonModulatorBlockEntity;
import g_mungus.zpl.item.ModItems;

@Mod(ZeroPointLabsMod.MOD_ID)
public final class ZeroPointLabsMod {
    public static final String MOD_ID = "zpl";

    public ZeroPointLabsMod(IEventBus eventBus) {
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModCreativeTabs.register(eventBus);

        eventBus.addListener(this::registerCapabilities);

        eventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), ThrusterExhaustBlockEntityRenderer::new);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        IonModulatorBlockEntity.registerCapabilities(event);
    }
}
