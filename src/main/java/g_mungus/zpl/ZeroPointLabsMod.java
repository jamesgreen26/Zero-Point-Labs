package g_mungus.zpl;

import g_mungus.vlib.dimension.DimensionSettingsManager;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntityRenderer;
import g_mungus.zpl.entity.EnergyOrbEntityRenderer;
import g_mungus.zpl.entity.ModEntities;
import g_mungus.zpl.item.ModCreativeTabs;
import g_mungus.zpl.item.ModItems;
import g_mungus.zpl.particle.EnergyOrbParticle;
import g_mungus.zpl.particle.ModParticles;
import g_mungus.zpl.sound.ModSounds;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
        ModParticles.PARTICLE_TYPES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
    }

    public static double getDimensionScale(Level level) {
        return DimensionSettingsManager.INSTANCE.getSettingsForLevel("minecraft:dimension:" + level.dimension().location()).getShipScale();
    }
}
