package g_mungus.zpl.block.hover;

import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.core.internal.world.VsiPhysLevel;

public class HoverForceApplier implements IForceApplier {

    public String dimension;
    public ThrusterData thrusterData;

    public HoverForceApplier() {}

    public HoverForceApplier(String dimension, ThrusterData thrusterData) {
        this.dimension = dimension;
        this.thrusterData = thrusterData;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship, PhysLevel physLevel) {
        if (physLevel instanceof VsiPhysLevel level) {
            Vector3dc gravity = level.getGravity();

            if (gravity.y() != 0.0d && thrusterData.strength > 0) {

                ship.applyInvariantForce(gravity.mul(-1 * thrusterData.strength * ship.getMass(), new Vector3d()));
            }
        }
    }
}
