package g_mungus.zpl.block.gyro;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.*;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class GyroForceApplier implements IForceApplier {

    public ThrusterData thrust;

    public GyroForceApplier(ThrusterData thrust) {
        this.thrust = thrust;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        final ShipTransform transform = ship.getTransform();
        Vector3dc scaling = transform.getShipToWorldScaling();

        double massScaleFactor = scaling.x() * scaling.y() * scaling.z();
        double torqueScaleFactor = massScaleFactor * scaling.x() * scaling.z() * 1.5;

        Vector3d invOmega = ship.getAngularVelocity().mul(-8000, new Vector3d());

        Matrix4dc worldToShip = transform.getWorldToShip();
        Matrix3d rotPart = new Matrix3d();
        worldToShip.get3x3(rotPart);

        double scale = java.lang.Math.sqrt(rotPart.m00() * rotPart.m00() + rotPart.m10() * rotPart.m10() + rotPart.m20() * rotPart.m20());

        rotPart.scale(16.0 / scale);

        Vector3d shipSpaceOmega = rotPart.transform(invOmega, new Vector3d());
        Vector3d thrustAxis = thrust.direction.normalize(new Vector3d());

        double omegaDotThrust = shipSpaceOmega.dot(thrustAxis);

        if (thrust.strength > 0.01) {
            ship.applyRotDependentTorque(thrust.direction.normalize(thrust.strength).mul(torqueScaleFactor));
            Vector3d dampingTorque = thrustAxis.mul(omegaDotThrust * torqueScaleFactor, new Vector3d());
            ship.applyRotDependentTorque(dampingTorque);
        } else {
            double thrustDotOmega = thrustAxis.dot(shipSpaceOmega);
            if (thrustDotOmega < 0) {
                Vector3d strongDampingTorque = thrustAxis.mul(omegaDotThrust * torqueScaleFactor * 16.0, new Vector3d());
                ship.applyRotDependentTorque(strongDampingTorque);
            }
        }
    }
}