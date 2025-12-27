package g_mungus.zpl.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import g_mungus.zpl.client.thruster.ThrusterRendererHandler;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PonderLevel.class)
public class PonderLevelMixin {

    @Inject(method = "renderParticles", at = @At("HEAD"), remap = false, require = 0)
    private void beforeRenderParticles(PoseStack ms, MultiBufferSource buffer, Camera ari, float pt, CallbackInfo ci) {
        ThrusterRendererHandler.renderForPonderLevel();
    }
}
