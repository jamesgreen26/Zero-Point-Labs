package g_mungus.zpl.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnergyOrbParticle extends TextureSheetParticle {

    protected EnergyOrbParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        this.quadSize = 0.2F;
        this.lifetime = 60;

        this.rCol = 0.3F;
        this.gCol = 0.8F;
        this.bCol = 1.0F;

        this.hasPhysics = false;
        this.setAlpha(0.8F);
    }

    @Override
    public void tick() {
        super.tick();

        // Fade out as the particle ages
        float ageRatio = (float) this.age / (float) this.lifetime;
        this.alpha = 0.8F * (1.0F - ageRatio);

        // Optional: slight pulsing effect
        this.quadSize = 0.2F + 0.05F * (float) Math.sin(this.age * 0.5);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ENERGY_ORB_RENDER_TYPE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 255;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                      double x, double y, double z,
                                      double xSpeed, double ySpeed, double zSpeed) {
            EnergyOrbParticle particle = new EnergyOrbParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }


    public static final ParticleRenderType ENERGY_ORB_RENDER_TYPE = new ParticleRenderType(){

        @Override
        public void begin(BufferBuilder arg, TextureManager arg2) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            arg.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator arg) {
            arg.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT";
        }
    };
}
