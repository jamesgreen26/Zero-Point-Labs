package g_mungus.zpl.block.thruster;

import org.joml.Vector3d;

public class ThrusterData {
    public Vector3d direction;
    public double strength;

    public ThrusterData(Vector3d direction, double strength) {
        this.direction = direction;
        this.strength = strength;
    }
}
