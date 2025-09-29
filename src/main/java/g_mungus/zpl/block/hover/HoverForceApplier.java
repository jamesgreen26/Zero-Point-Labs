package g_mungus.zpl.block.hover;

import g_mungus.vlib.data.DimensionSettings;
import g_mungus.vlib.dimension.DimensionSettingsManager;
import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class HoverForceApplier implements IForceApplier {

    public final String dimension;
    public final ThrusterData thrusterData;

    public HoverForceApplier(String dimension, ThrusterData thrusterData) {
        this.dimension = dimension;
        this.thrusterData = thrusterData;
    }

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        final DimensionSettings dimensionSettings = DimensionSettingsManager.INSTANCE.getSettingsForLevel(dimension);

        if (dimensionSettings.getGravity() != 0.0d && thrusterData.strength > 0) {
            double gravity = thrusterData.strength * dimensionSettings.getGravity() * 10 * ship.get_inertia().getShipMass();

            ship.applyInvariantForce(new Vector3d(0.0, gravity, 0.0));
        }
    }
}
