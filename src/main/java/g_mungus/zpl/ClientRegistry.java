package g_mungus.zpl;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.client.thruster.ThrusterExhaustBlockEntityRenderer;
import g_mungus.zpl.entity.EnergyOrbEntityRenderer;
import g_mungus.zpl.entity.ModEntities;
import g_mungus.zpl.particle.EnergyOrbParticle;
import g_mungus.zpl.particle.ModParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import static team.lodestar.lodestone.registry.client.LodestoneShaderRegistry.registerShader;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ZeroPointLabsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {

    public static final ShaderHolder THRUST_SHADER = new ShaderHolder(ResourceLocation.fromNamespaceAndPath(ZeroPointLabsMod.MOD_ID, "thrust"), DefaultVertexFormat.POSITION_TEX);

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        registerShader(event, THRUST_SHADER);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.THRUSTER_EXHAUST_BLOCK_ENTITY.get(), ThrusterExhaustBlockEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.ENERGY_ORB.get(), EnergyOrbEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ENERGY_ORB.get(), EnergyOrbParticle.Provider::new);
    }
}
