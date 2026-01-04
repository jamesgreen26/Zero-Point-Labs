package g_mungus.zpl.client.flywheel;

import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.api.layout.FloatRepr;
import dev.engine_room.flywheel.api.layout.LayoutBuilder;
import dev.engine_room.flywheel.lib.instance.SimpleInstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static g_mungus.zpl.ZeroPointLabsMod.asResource;

@OnlyIn(Dist.CLIENT)
public class InstanceTypes {
    public static final InstanceType<ThrusterInstance> THRUSTER
            = SimpleInstanceType.builder(ThrusterInstance::new)
            .cullShader(asResource("instance/cull/thruster.glsl"))
            .vertexShader(asResource("instance/thruster.vert"))
            .layout(LayoutBuilder.create()
                    .matrix("pose", FloatRepr.FLOAT, 4)
                    .build()
            )
            .writer((ptr, instance) -> {
                ExtraMemoryOps.putMatrix4f(ptr, instance.pose);
            })
            .build();
}
