package g_mungus.zpl.client.thruster;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import g_mungus.zpl.mixin.RenderStateShardAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;

import java.util.List;

import static g_mungus.zpl.ClientRegistry.THRUST_SHADER;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ThrusterRendererHandler {

    private static LodestoneRenderType THRUST;
    private static long initialTime = -1;

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

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();


        LodestoneRenderType thrustType = LodestoneRenderTypeRegistry.copyWithUniformChanges(getThrustRenderType(), shader -> {
                    if (initialTime == -1) {
                        initialTime = System.currentTimeMillis();
                    }
                    float time = (System.currentTimeMillis() - initialTime) / 50f;
                    shader.safeGetUniform("ThrusterTime").set((time % 24000f) / 24000f);
                }
        );
        VertexConsumer consumer = buffer.getBuffer(thrustType);

        for (ThrusterRenderData data : ThrusterRenderQueue.getQueue()) {
            // Use the captured transformation matrix
            renderCube(data.matrix, consumer);
        }

        buffer.endBatch(thrustType);
        ThrusterRenderQueue.clear();
    }

    public static void renderForPonderLevel() {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        LodestoneRenderType thrustType = LodestoneRenderTypeRegistry.copyWithUniformChanges(getThrustRenderType(), shader -> {
                    if (initialTime == -1) {
                        initialTime = System.currentTimeMillis();
                    }
                    float time = (System.currentTimeMillis() - initialTime) / 50f;
                    shader.safeGetUniform("ThrusterTime").set((time % 24000f) / 24000f);
                }
        );
        VertexConsumer consumer = buffer.getBuffer(thrustType);

        for (ThrusterRenderData data : ThrusterRenderQueuePonder.getQueue()) {
            // Use the captured transformation matrix
            renderCube(data.matrix, consumer);
        }

        buffer.endBatch(thrustType);
        ThrusterRenderQueuePonder.clear();
    }

    private static void renderCube(Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        renderHex(matrix4f, vertexConsumer, 0);
        renderHex(matrix4f, vertexConsumer, 7);
    }

    private static void renderHex( Matrix4f matrix4f, VertexConsumer vertexConsumer, int offset) {
        renderFace(matrix4f, vertexConsumer, shape.get(0 + offset), shape.get(1 + offset), shape.get(8 + offset), shape.get(7 + offset));
        renderFace(matrix4f, vertexConsumer, shape.get(1 + offset), shape.get(2 + offset), shape.get(9 + offset), shape.get(8 + offset));
        renderFace(matrix4f, vertexConsumer, shape.get(2 + offset), shape.get(3 + offset), shape.get(10 + offset), shape.get(9 + offset));
        renderFace(matrix4f, vertexConsumer, shape.get(3 + offset), shape.get(4 + offset), shape.get(11 + offset), shape.get(10 + offset));
        renderFace(matrix4f, vertexConsumer, shape.get(4 + offset), shape.get(5 + offset), shape.get(12 + offset), shape.get(11 + offset));
        renderFace(matrix4f, vertexConsumer, shape.get(5 + offset), shape.get(6 + offset), shape.get(13 + offset), shape.get(12 + offset));
    }

    private static void renderFace(Matrix4f matrix4f, VertexConsumer vertexConsumer, Vector4f a, Vector4f b, Vector4f c, Vector4f d) {

        vertexConsumer.vertex(matrix4f, d.x, d.y, d.z).uv(d.y, d.w).endVertex();
        vertexConsumer.vertex(matrix4f, c.x, c.y, c.z).uv(c.y, c.w).endVertex();
        vertexConsumer.vertex(matrix4f, b.x, b.y, b.z).uv(b.y, b.w).endVertex();
        vertexConsumer.vertex(matrix4f, a.x, a.y, a.z).uv(a.y, a.w).endVertex();
    }

    private static final List<Vector4f> shape = List.of(
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

