package g_mungus.zpl.client.thruster;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ThrusterRenderData {
    public final Vec3 pos;
    public final Direction facing;
    public final float power;
    public final Matrix4f matrix;

    public ThrusterRenderData(Vec3 pos, Direction facing, float power, PoseStack poseStack) {
        this.pos = pos;
        this.facing = facing;
        this.power = power;
        this.matrix = new Matrix4f(poseStack.last().pose());
    }
}

