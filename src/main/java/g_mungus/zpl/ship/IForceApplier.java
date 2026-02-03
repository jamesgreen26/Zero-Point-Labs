package g_mungus.zpl.ship;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import g_mungus.zpl.block.gyro.GyroForceApplier;
import g_mungus.zpl.block.hover.HoverForceApplier;
import g_mungus.zpl.block.thruster.ThrusterForceApplier;
import net.minecraft.core.BlockPos;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GyroForceApplier.class, name = "gyro"),
        @JsonSubTypes.Type(value = ThrusterForceApplier.class, name = "thruster"),
        @JsonSubTypes.Type(value = HoverForceApplier.class, name = "hover"),
})
public interface IForceApplier {
    void applyForces(BlockPos pos, PhysShipImpl ship, PhysLevel physLevel);
}