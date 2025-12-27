package g_mungus.zpl;

import g_mungus.vlib.dimension.DimensionSettingsManager;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.droidcore.DroidAttachment;
import g_mungus.zpl.client.ponder.ZPLPonderPlugin;
import g_mungus.zpl.config.ZPLConfig;
import g_mungus.zpl.entity.ModEntities;
import g_mungus.zpl.item.ModCreativeTabs;
import g_mungus.zpl.item.ModItems;
import g_mungus.zpl.particle.ModParticles;
import g_mungus.zpl.ship.ZPLShipAttachment;
import g_mungus.zpl.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyrienskies.core.api.ships.LoadedServerShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.api.ValkyrienSkies;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.Optional;

@Mod(ZeroPointLabsMod.MOD_ID)
public final class ZeroPointLabsMod {
    public static final String MOD_ID = "zpl";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ZeroPointLabsMod(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.SERVER, ZPLConfig.CONFIG_SPEC);

        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModCreativeTabs.register(eventBus);
        ModParticles.PARTICLE_TYPES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);

        ValkyrienSkies.api().registerAttachment(ZPLShipAttachment.class);

        ValkyrienSkies.api().registerAttachment(ValkyrienSkies.api()
                .newAttachmentRegistrationBuilder(DroidAttachment.class)
                .useTransientSerializer()
                .build()
        );

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ZPLPonderPlugin::registerPlugin);
    }

    public static double getDimensionScale(Level level) {
        return DimensionSettingsManager.INSTANCE.getSettingsForLevel("minecraft:dimension:" + level.dimension().location()).getShipScale();
    }



    public static Optional<LoadedServerShip> getShipAt(ServerLevel serverLevel, BlockPos pos) {

        ServerShip ship = VSGameUtilsKt.getShipObjectManagingPos(serverLevel, pos);
        if (ship == null){
            ship = VSGameUtilsKt.getShipManagingPos(serverLevel, pos);
        }
        if (ship instanceof LoadedServerShip loadedServerShip) {
            return Optional.of(loadedServerShip);
        }
        return Optional.empty();
    }
}
