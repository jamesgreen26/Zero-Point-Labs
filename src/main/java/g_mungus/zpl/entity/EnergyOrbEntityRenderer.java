package g_mungus.zpl.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import g_mungus.zpl.ZeroPointLabsMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EnergyOrbEntityRenderer extends EntityRenderer<EnergyOrbEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ZeroPointLabsMod.MOD_ID, "textures/particle/energy_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentEmissive(TEXTURE);

    public EnergyOrbEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EnergyOrbEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Make it face the camera (billboard effect)
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // Size of the quad
        float size = 0.3F;

        // Add pulsing effect
        float pulse = 1.0F + 0.1F * Mth.sin(entity.tickCount * 0.5F);
        size *= pulse;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RENDER_TYPE);

        // Draw a quad (two triangles)
        // Vertex 1 (top-left)
        vertexConsumer.vertex(matrix4f, -size, size, 0)
                .color(77, 204, 255, 204) // Cyan color with alpha
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(240) // Full brightness
                .normal(matrix3f, 0, 1, 0)
                .endVertex();

        // Vertex 2 (top-right)
        vertexConsumer.vertex(matrix4f, size, size, 0)
                .color(77, 204, 255, 204)
                .uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(240)
                .normal(matrix3f, 0, 1, 0)
                .endVertex();

        // Vertex 3 (bottom-right)
        vertexConsumer.vertex(matrix4f, size, -size, 0)
                .color(77, 204, 255, 204)
                .uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(240)
                .normal(matrix3f, 0, 1, 0)
                .endVertex();

        // Vertex 4 (bottom-left)
        vertexConsumer.vertex(matrix4f, -size, -size, 0)
                .color(77, 204, 255, 204)
                .uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(240)
                .normal(matrix3f, 0, 1, 0)
                .endVertex();

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EnergyOrbEntity entity) {
        return TEXTURE;
    }
}
