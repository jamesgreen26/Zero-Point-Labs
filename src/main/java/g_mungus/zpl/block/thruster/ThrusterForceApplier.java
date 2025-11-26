package g_mungus.zpl.block.thruster;

import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class ThrusterForceApplier implements IForceApplier {

    public ThrusterData thrust;

    private final Vector3d worldForceDirection = new Vector3d();

    public ThrusterForceApplier(ThrusterData thrust) {
        this.thrust = thrust;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        final ShipTransform transform = ship.getTransform();
        Vector3dc scaling = transform.getShipToWorldScaling();

        double massScaleFactor = scaling.x() * scaling.y() * scaling.z() * 1.5;

        if (thrust.strength > 0.01) {

            transform.getShipToWorld().transformDirection(thrust.direction, worldForceDirection);
            ship.applyInvariantForce(worldForceDirection.normalize(thrust.strength * 800_000).mul(massScaleFactor).mul(scaling));

            Vector3dc worldVelocity = ship.getVelocity();

            Vector3d shipSpaceVelocity = transform.getWorldToShip().transformDirection(worldVelocity, new Vector3d());

            double velocityDotThrust = shipSpaceVelocity.dot(thrust.direction);

            Vector3d offAxisVelocityShip;
            if (velocityDotThrust > 0) {
                Vector3d onAxisVelocity = thrust.direction.mul(velocityDotThrust, new Vector3d());
                offAxisVelocityShip = shipSpaceVelocity.sub(onAxisVelocity, new Vector3d());
            } else {
                offAxisVelocityShip = new Vector3d(shipSpaceVelocity);
            }

            Vector3d offAxisVelocityWorld = transform.getShipToWorld().transformDirection(offAxisVelocityShip, new Vector3d());

            Vector3d dampingForce = offAxisVelocityWorld.mul(massScaleFactor * -8000 * thrust.strength, new Vector3d());
            ship.applyInvariantForce(dampingForce);
        }

        ship.applyInvariantForce(ship.getVelocity().mul(-2400, new Vector3d()).mul(massScaleFactor));
    }
}
