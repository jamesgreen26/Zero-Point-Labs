package g_mungus.zpl.block.gyro;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3dc;
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
        double torqueScaleFactor = massScaleFactor * scaling.x() * scaling.z();

        if (thrust.strength > 0.01) {
            ship.applyRotDependentTorque(thrust.direction.normalize(thrust.strength).mul(torqueScaleFactor));
        }

        ship.applyInvariantTorque(ship.getPoseVel().getOmega().mul(-8000, new Vector3d()).mul(torqueScaleFactor));
        ship.applyInvariantForce(ship.getPoseVel().getVel().mul(-3200, new Vector3d()).mul(massScaleFactor));
    }
}