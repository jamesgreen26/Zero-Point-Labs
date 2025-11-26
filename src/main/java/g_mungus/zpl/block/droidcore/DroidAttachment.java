package g_mungus.zpl.block.droidcore;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ShipPhysicsListener;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.api.world.PhysLevel;

import java.lang.Math;

import static g_mungus.zpl.ZeroPointLabsMod.getShipAt;

public final class DroidAttachment implements ShipPhysicsListener {

    public DroidData droidData;

    public DroidAttachment(DroidData droidData) {
        this.droidData = droidData;
    }

    @Override
    public void physTick (@NotNull PhysShip ship, @NotNull PhysLevel physLevel){
        if (droidData.facingDirection == null) return;

        final ShipTransform transform = ship.getTransform();
        Vector3dc scaling = transform.getShipToWorldScaling();

        double massScaleFactor = scaling.x() * scaling.y() * scaling.z();
        double torqueScaleFactor = massScaleFactor * scaling.x() * scaling.z() * 1.5;

        // Get angular velocity in world space, then transform to ship space
        Vector3d invOmega = ship.getAngularVelocity().mul(-8000, new Vector3d());

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

        if (droidData.hasTarget() && droidData.ownShip != null) {
            // Get ship center in world space from AABB
            Vector3dc shipPos = droidData.ownShip.getWorldAABB().center(new Vector3d());
            Vector3dc targetPos = droidData.targetPos;

            // Calculate direction to target in world space
            Vector3d toTargetWorld = targetPos.sub(shipPos, new Vector3d()).normalize();

            // Get facing direction in world space
            Vector3d facingWorld = transform.getShipToWorld().transformDirection(facingAxis, new Vector3d()).normalize();

            if (droidData.targetDist > 32) {
                // Dampen rotation in all axes to prevent oscillation
                Vector3d dampingTorqueAll = shipSpaceOmega.mul(torqueScaleFactor, new Vector3d());
                ship.applyRotDependentTorque(dampingTorqueAll.mul(1.0));

                // Apply torque to face towards target
                // Cross product gives us the axis to rotate around
                Vector3d torqueAxis = facingWorld.cross(toTargetWorld, new Vector3d());
                double torqueMagnitude = torqueAxis.length() * torqueScaleFactor * 40000.0;

                if (torqueMagnitude > 0.00001) {
                    // Transform torque axis to ship space (reuse worldToShip from earlier)
                    Vector3d torqueShipSpace = rotPart.transform(torqueAxis.normalize(), new Vector3d());

                    ship.applyRotDependentTorque(torqueShipSpace.mul(torqueMagnitude));
                }

                // Cancel perpendicular velocity
                Vector3dc velocity = ship.getVelocity();

                // Project velocity onto target direction
                double velocityParallel = velocity.dot(toTargetWorld);
                Vector3d parallelComponent = toTargetWorld.mul(velocityParallel, new Vector3d());

                // Perpendicular component is what we want to cancel
                Vector3d perpendicularVelocity = velocity.sub(parallelComponent, new Vector3d());

                // Apply force to cancel perpendicular velocity
                double dampingStrength = (ship.getMass() / scaling.x()) / 16.0;
                Vector3d dampingForce = perpendicularVelocity.mul(-dampingStrength, new Vector3d());

                ship.applyInvariantForce(dampingForce);

                // Apply equal magnitude force towards target
                double forwardForceMagnitude = dampingForce.length();
                Vector3d forwardForce = toTargetWorld.mul(forwardForceMagnitude, new Vector3d());

                ship.applyInvariantForce(forwardForce);
            }
        }
    }

    public static void removeApplier(ServerLevel level, BlockPos pos){
        getShipAt(level, pos).ifPresent(ship -> {
            ship.removeAttachment(DroidAttachment.class);
        });
    }

    public static void addNew(ServerLevel level, BlockPos pos, DroidData droidData) {
        getShipAt(level, pos).ifPresent(ship -> {
            ship.removeAttachment(DroidAttachment.class);
            ship.setAttachment(new DroidAttachment(droidData));
        });
    }
}
