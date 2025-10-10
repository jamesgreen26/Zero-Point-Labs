package g_mungus.zpl.entity;

import g_mungus.zpl.ZeroPointLabsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<EntityType<EnergyOrbEntity>> ENERGY_ORB =
            ENTITY_TYPES.register("energy_orb", () -> EntityType.Builder.<EnergyOrbEntity>of(
                    (entityType, level) -> new EnergyOrbEntity(entityType, level),
                    MobCategory.MISC
            )
            .sized(0.25F, 0.25F) // Small hitbox
            .clientTrackingRange(4) // Sync range in chunks
            .updateInterval(10) // Update every 10 ticks
            .build("energy_orb"));
}
