package g_mungus.zpl.block.thruster;

import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
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
        if (thrust.strength > 0.01) {
            final ShipTransform transform = ship.getTransform();

            transform.getShipToWorld().transformDirection(thrust.direction, worldForceDirection);
            ship.applyInvariantForce(worldForceDirection.normalize(thrust.strength));
        }
    }
}
