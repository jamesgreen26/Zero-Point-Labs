package g_mungus.zpl;

import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntityRenderer;
import g_mungus.zpl.item.ModCreativeTabs;
import g_mungus.zpl.item.ModItems;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ZeroPointLabsMod.MOD_ID)
public final class ZeroPointLabsMod {
    public static final String MOD_ID = "zpl";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ZeroPointLabsMod(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();

        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModCreativeTabs.register(eventBus);

        eventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), ThrusterExhaustBlockEntityRenderer::new);
    }
}
