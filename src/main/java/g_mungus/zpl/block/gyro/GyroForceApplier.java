package g_mungus.zpl.block.gyro;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.*;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

import java.lang.Math;

public class GyroForceApplier implements IForceApplier {

    public ThrusterData thrust;

    public GyroForceApplier() {
    }

    public GyroForceApplier(ThrusterData thrust) {
        this.thrust = thrust;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        final ShipTransform transform = ship.getTransform();
        double torqueScaleFactor = 1.5;

        try {
            AABBic aabb = ValkyrienSkiesMod.getApi().getServerShipWorld(ValkyrienSkiesMod.getCurrentServer()).getAllShips().getById(ship.getId()).getShipAABB();
            if (aabb != null) {
                Vector3d extent = aabb.extent(new Vector3d());

                torqueScaleFactor = 0.75 + Math.pow(extent.x * extent.y * extent.z, 1 / 3d) / (transform.getShipToWorldScaling().x() * 12);
            }
        } catch (Throwable ignored) {}

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