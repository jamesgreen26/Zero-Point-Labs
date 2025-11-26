package g_mungus.zpl.block.hover;

import g_mungus.vlib.data.DimensionSettings;
import g_mungus.vlib.dimension.DimensionSettingsManager;
import g_mungus.zpl.block.thruster.ThrusterData;
import g_mungus.zpl.ship.IForceApplier;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public record HoverForceApplier(String dimension, ThrusterData thrusterData) implements IForceApplier {

    @Override
    public void applyForces(BlockPos pos, PhysShipImpl ship) {
        final DimensionSettings dimensionSettings = DimensionSettingsManager.INSTANCE.getSettingsForLevel(dimension);

        if (dimensionSettings.getGravity() != 0.0d && thrusterData.strength > 0) {
            double gravity = thrusterData.strength * dimensionSettings.getGravity() * 10 * ship.getMass();

            ship.applyInvariantForce(new Vector3d(0.0, gravity, 0.0));
        }
    }
}
