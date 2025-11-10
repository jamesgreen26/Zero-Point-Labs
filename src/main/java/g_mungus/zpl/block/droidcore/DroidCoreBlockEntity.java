package g_mungus.zpl.block.droidcore;

import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBd;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.Optional;

public class DroidCoreBlockEntity extends BlockEntity {

    public DroidCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DROID_CORE_BLOCK_ENTITY.get(), pos, state);
    }

    private static final int searchDistance = 1024;


    public void tick() {
        if (level == null) return;

        Ship ship = VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        if (ship == null) return;

        Vector3dc shipCenter = ship.getWorldAABB().center(new Vector3d());

        updateTarget(shipCenter, ship).ifPresent(target -> {
            Vec3i facingShip = getBlockState().getValue(DroidCoreBlock.FACING).getNormal();
            Vector3dc facingWorld = ship.getShipToWorld().transformDirection(new Vector3d(facingShip.getX(), facingShip.getY(), facingShip.getZ()), new Vector3d());

            Vector3dc targetDir = target.pos.sub(shipCenter, new Vector3d());

            double dot = facingWorld.normalize(new Vector3d()).dot(targetDir.normalize(new Vector3d()));

            updateThrust(target.dist, dot);
        });
    }

    private void updateThrust(double dist, double dot) {
        int thrust = (int) Math.min(15, Math.max((dot * dist) - 32, 0.0) / 60);

        BlockState state = getBlockState();
        int currentPower = state.getValue(DroidCoreBlock.POWER);

        // Only update if power level changed
        if (currentPower != thrust) {
            level.setBlock(getBlockPos(), state.setValue(DroidCoreBlock.POWER, thrust), 3);

            // Notify neighbors of power change
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());

            // Also update the block behind (where power is output from)
            Direction facing = state.getValue(DroidCoreBlock.FACING);
            level.updateNeighborsAt(getBlockPos().relative(facing.getOpposite()), getBlockState().getBlock());
        }
    }


    private Optional<Target> updateTarget(Vector3dc shipCenter, Ship ship) {
        AABBd searchBox = new AABBd(
                shipCenter.sub(searchDistance, searchDistance, searchDistance, new Vector3d()),
                shipCenter.add(searchDistance, searchDistance, searchDistance, new Vector3d())
        );

        Target closestTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (Ship candidate : VSGameUtilsKt.getShipObjectWorld(level).getLoadedShips().getIntersecting(searchBox)) {
            if (candidate.getId() == ship.getId()) continue;

            Vector3dc candidateCenter = candidate.getWorldAABB().center(new Vector3d());
            double distance = candidateCenter.distance(shipCenter);

            if (distance < closestDistance && distance < searchDistance) {
                closestDistance = distance;
                closestTarget = new Target(candidate, candidateCenter, distance);
            }
        }

        return Optional.ofNullable(closestTarget);
    }

    private record Target(Ship ship, Vector3dc pos, double dist) {}
}
