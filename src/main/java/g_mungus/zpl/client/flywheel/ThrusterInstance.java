package g_mungus.zpl.client.flywheel;

import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import org.joml.Matrix4fc;

public class ThrusterInstance extends AbstractInstance {
    public Matrix4fc pose;

    protected ThrusterInstance(InstanceType<?> type, InstanceHandle handle) {
        super(type, handle);
    }

    public void setTransform(Matrix4fc transform) {
        this.pose = transform;
        this.setChanged();
    }

}
