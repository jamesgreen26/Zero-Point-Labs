package g_mungus.zpl.client;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.BlockEntityVisualizer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.api.visualization.VisualizerRegistry;
import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntity;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.client.flywheel.DummyBERenderer;
import g_mungus.zpl.client.flywheel.ThrusterVisual;
import g_mungus.zpl.entity.EnergyOrbEntityRenderer;
import g_mungus.zpl.entity.ModEntities;
import g_mungus.zpl.client.particle.EnergyOrbParticle;
import g_mungus.zpl.client.particle.ModParticles;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ZeroPointLabsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZeroPointLabsModClient {


    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), DummyBERenderer::new);
        event.registerEntityRenderer(ModEntities.ENERGY_ORB.get(), EnergyOrbEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ENERGY_ORB.get(), EnergyOrbParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            VisualizerRegistry.setVisualizer(
                    ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(),
                    new BlockEntityVisualizer<>() {
                        @Override
                        public BlockEntityVisual<? super ThrusterExhaustBlockEntity> createVisual(VisualizationContext ctx, ThrusterExhaustBlockEntity blockEntity, float partialTick) {
                            return new ThrusterVisual(ctx, blockEntity, partialTick);
                        }

                        @Override
                        public boolean skipVanillaRender(ThrusterExhaustBlockEntity blockEntity) {
                            return true;
                        }
                    }
            );

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPACE_SCAFFOLD.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPACE_TRUSS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPACE_WALKWAY.get(), RenderType.cutout());
        });
    }
}
