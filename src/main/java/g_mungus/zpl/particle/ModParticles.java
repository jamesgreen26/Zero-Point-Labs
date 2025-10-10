package g_mungus.zpl.particle;

import g_mungus.zpl.ZeroPointLabsMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> ENERGY_ORB =
            PARTICLE_TYPES.register("energy_orb", () -> new SimpleParticleType(true));
}