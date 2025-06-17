package g_mungus.zpl.block.gyro;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class GyroForceApplier implements IForceApplier {

    public ThrusterData thrust;

    public GyroForceApplier(ThrusterData thrust) {
        this.thrust = thrust;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        if (thrust.strength > 0.01) {
            ship.applyRotDependentTorque(thrust.direction.normalize(thrust.strength));
        }
        ship.applyInvariantTorque(ship.getOmega().mul(-8000, new Vector3d()));
        ship.applyInvariantForce(ship.getVelocity().mul(-3200, new Vector3d()));
    }
}