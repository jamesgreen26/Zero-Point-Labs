package g_mungus.zpl.block.droidcore;

import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.*;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

import java.lang.Math;

public class DroidForceApplier implements IForceApplier {

    public DroidData droidData;

    public DroidForceApplier(DroidData droidData) {
        this.droidData = droidData;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        if (droidData.facingDirection == null) return;

        final ShipTransform transform = ship.getTransform();
        Vector3dc scaling = transform.getShipToWorldScaling();

        double massScaleFactor = scaling.x() * scaling.y() * scaling.z();
        double torqueScaleFactor = massScaleFactor * scaling.x() * scaling.z() * 1.5;

        // Get angular velocity in world space, then transform to ship space
        Vector3d invOmega = ship.getPoseVel().getOmega().mul(-8000, new Vector3d());

        Matrix4dc worldToShip = transform.getWorldToShip();
        Matrix3d rotPart = new Matrix3d();
        worldToShip.get3x3(rotPart);

        double scale = Math.sqrt(rotPart.m00() * rotPart.m00() + rotPart.m10() * rotPart.m10() + rotPart.m20() * rotPart.m20());
        rotPart.scale(16.0 / scale);

        Vector3d shipSpaceOmega = rotPart.transform(invOmega, new Vector3d());

        // Get the facing axis in ship space (already stored in ship space)
        Vector3d facingAxis = droidData.facingDirection.normalize(new Vector3d());

        // Calculate rotation around the facing axis
        double omegaDotFacing = shipSpaceOmega.dot(facingAxis);

        // Apply damping torque to cancel rotation around facing axis
        Vector3d dampingTorque = facingAxis.mul(omegaDotFacing * torqueScaleFactor, new Vector3d());
        ship.applyRotDependentTorque(dampingTorque);

        if (droidData.hasTarget()) {
            // Force application will be implemented later
        }
    }
}
