package g_mungus.zpl.block.gyro;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class GyroForceApplier implements IForceApplier {

    public ThrusterData thrust;

    private static final double DAMPING_STRENGTH = 2048.0;
    private static final double MAX_ANGULAR_VELOCITY = 10.0;

    public GyroForceApplier() {
    }

    public GyroForceApplier(ThrusterData thrust) {
        this.thrust = thrust;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        Vector3d thrustDirection = thrust.direction.normalize(new Vector3d());
        Vector3d angularVelocity = new Vector3d(ship.getAngularVelocity());
        ship.getTransform().getShipToWorldRotation().invert(new Quaterniond()).transform(angularVelocity);
        Vector3dc scaling = ship.getTransform().getShipToWorldScaling();

        // Clamp angular velocity magnitude to prevent excessive damping after collisions
        double angularSpeed = angularVelocity.length();
        if (angularSpeed > MAX_ANGULAR_VELOCITY) {
            angularVelocity.normalize().mul(MAX_ANGULAR_VELOCITY);
        }

        double torqueScale = Math.sqrt(ship.getMass() / (scaling.x() * scaling.y() * scaling.z())) / 16;
        double dampingScale = 12.0;

        // Apply thrust torque
        if (thrust.strength > 0) {
            ship.applyBodyTorque(thrustDirection.mul(thrust.strength * torqueScale, new Vector3d()));
            dampingScale = 1.0;
        }

        // Apply damping torque to oppose rotation
        Vector3d dampingTorque = angularVelocity.mul(-DAMPING_STRENGTH * dampingScale * torqueScale, new Vector3d());
        ship.applyBodyTorque(dampingTorque);
    }
}