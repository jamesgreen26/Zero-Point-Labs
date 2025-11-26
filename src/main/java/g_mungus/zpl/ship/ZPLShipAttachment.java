package g_mungus.zpl.ship;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.*;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.mod.common.jackson.BlockPosKeyDeserializer;
import org.valkyrienskies.mod.common.jackson.BlockPosKeySerializer;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static g_mungus.zpl.ZeroPointLabsMod.getShipAt;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public final class ZPLShipAttachment implements ShipPhysicsListener {
    @JsonProperty
    @JsonSerialize(keyUsing = BlockPosKeySerializer.class)
    @JsonDeserialize(keyUsing = BlockPosKeyDeserializer.class)
    public Map<BlockPos, IForceApplier> appliersMapping = new ConcurrentHashMap<>();
    public ZPLShipAttachment() {}

    @Override
    public void physTick (@NotNull PhysShip physicShip, @NotNull PhysLevel physLevel){
        PhysShipImpl ship = (PhysShipImpl)physicShip;
        appliersMapping.forEach((pos, applier) -> {
            applier.applyForces(pos, ship);
        });
    }

    public void addApplier(BlockPos pos, IForceApplier applier){
        appliersMapping.put(pos, applier);
    }

    public void removeApplier(ServerLevel level, BlockPos pos){
        appliersMapping.remove(pos);

        if (appliersMapping.isEmpty()) {
            getShipAt(level, pos).ifPresent(ship -> {
                ship.removeAttachment(ZPLShipAttachment.class);
            });
        }
    }

    public static Optional<ZPLShipAttachment> getOrCreate(ServerLevel level, BlockPos pos) {
            return getShipAt(level, pos).map(it -> it.getOrPutAttachment(ZPLShipAttachment.class, ZPLShipAttachment::new));
    }
}