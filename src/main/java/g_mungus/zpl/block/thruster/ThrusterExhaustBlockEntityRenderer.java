package g_mungus.zpl.block.thruster;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import g_mungus.zpl.mixin.RenderStateShardAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;

import java.util.List;

import static g_mungus.zpl.ClientRegistry.THRUST_SHADER;

public class ThrusterExhaustBlockEntityRenderer implements BlockEntityRenderer<ThrusterExhaustBlockEntity> {
    private static LodestoneRenderType THRUST;
    
    public ThrusterExhaustBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    private static LodestoneRenderType getThrustRenderType() {
        if (THRUST == null) {
            THRUST = LodestoneRenderTypeRegistry.createGenericRenderType("thruster_render_type", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, LodestoneRenderTypeRegistry.builder()
                    .setShaderState(THRUST_SHADER)
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setDepthTestState(new RenderStateShard.DepthTestStateShard("<=", 515))
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setOutputState(RenderStateShardAccessor.getTRANSLUCENT_TARGET())
                    .setLayeringState(RenderStateShardAccessor.getVIEW_OFFSET_Z_LAYERING())
            );
        }
        return THRUST;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRenderOffScreen(ThrusterExhaustBlockEntity arg) {
        return true;
    }

    @Override
    public boolean shouldRender(ThrusterExhaustBlockEntity arg, Vec3 arg2) {
        return true;
    }

    @Override
    public void render(ThrusterExhaustBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState blockState = blockEntity.getBlockState();
        float power = blockState.getValue(ThrusterExhaustBlock.POWER) / 15f;
        if (power < 0.0001f) return;

        float hScale = (1 + power) / 2f;

        poseStack.pushPose();

        try {
            poseStack.translate(0.5, 0.5, 0.5);

            Direction direction = blockState.getValue(ThrusterExhaustBlock.FACING);
            switch (direction) {
                case UP -> {}
                case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(180f));
                case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
                case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90f));
                case WEST -> {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90f));
                }
                case EAST -> {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(-90f));
                }
            }

            poseStack.translate(-0.5, -0.5, -0.5);

            poseStack.translate(-0.5f * power, 1, -0.5f * power);
            poseStack.scale(2 * hScale, 6 * power, 2 * hScale);

            Matrix4f matrix4f = poseStack.last().pose();

            VertexConsumer consumer = bufferSource.getBuffer(getThrustRenderType());
            this.renderCube(blockEntity, matrix4f, consumer);
        } finally {
            poseStack.popPose();
        }
    }


    private void renderCube(ThrusterExhaustBlockEntity blockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        renderHex(blockEntity, matrix4f, vertexConsumer, 0);
        renderHex(blockEntity, matrix4f, vertexConsumer, 7);
    }

    private void renderHex(ThrusterExhaustBlockEntity blockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer, int offset) {
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(0 + offset), shape.get(1 + offset), shape.get(8 + offset), shape.get(7 + offset));
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(1 + offset), shape.get(2 + offset), shape.get(9 + offset), shape.get(8 + offset));
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(2 + offset), shape.get(3 + offset), shape.get(10 + offset), shape.get(9 + offset));
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(3 + offset), shape.get(4 + offset), shape.get(11 + offset), shape.get(10 + offset));
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(4 + offset), shape.get(5 + offset), shape.get(12 + offset), shape.get(11 + offset));
        this.renderFace(blockEntity, matrix4f, vertexConsumer, shape.get(5 + offset), shape.get(6 + offset), shape.get(13 + offset), shape.get(12 + offset));
    }

    private void renderFace(ThrusterExhaustBlockEntity arg, Matrix4f matrix4f, VertexConsumer vertexConsumer, Vector4f a, Vector4f b, Vector4f c, Vector4f d) {

        vertexConsumer.vertex(matrix4f, d.x, d.y, d.z).uv(d.y, d.w).endVertex();
        vertexConsumer.vertex(matrix4f, c.x, c.y, c.z).uv(c.y, c.w).endVertex();
        vertexConsumer.vertex(matrix4f, b.x, b.y, b.z).uv(b.y, b.w).endVertex();
        vertexConsumer.vertex(matrix4f, a.x, a.y, a.z).uv(a.y, a.w).endVertex();
    }

    private final List<Vector4f> shape = List.of(
            new Vector4f(0.6667f, 0, 0.5f, 0f),
            new Vector4f(0.5833f, 0, 0.6443f, 0.167f),
            new Vector4f(0.4167f, 0, 0.6443f, 0.333f),
            new Vector4f(0.3333f, 0, 0.5f, 0.5f),
            new Vector4f(0.4167f, 0, 0.3557f, 0.667f),
            new Vector4f(0.5833f, 0, 0.3557f, 0.833f),
            new Vector4f(0.6667f, 0, 0.5f, 1f),

            new Vector4f(1.0f, 0.1f, 0.5f, 0f),
            new Vector4f(0.75f, 0.1f, 0.9333f, 0.167f),
            new Vector4f(0.25f, 0.1f, 0.9333f, 0.333f),
            new Vector4f(0.0f, 0.1f, 0.5f, 0.5f),
            new Vector4f(0.25f, 0.1f, 0.0667f, 0.667f),
            new Vector4f(0.75f, 0.1f, 0.0667f, 0.833f),
            new Vector4f(1.0f, 0.1f, 0.5f, 1f),

            new Vector4f(0.6667f, 1, 0.5f, 0f),
            new Vector4f(0.5833f, 1, 0.6443f, 0.167f),
            new Vector4f(0.4167f, 1, 0.6443f, 0.333f),
            new Vector4f(0.3333f, 1, 0.5f, 0.5f),
            new Vector4f(0.4167f, 1, 0.3557f, 0.667f),
            new Vector4f(0.5833f, 1, 0.3557f, 0.833f),
            new Vector4f(0.6667f, 1, 0.5f, 1f)
    );
}