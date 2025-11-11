package g_mungus.zpl.block.droidcore;

import g_mungus.vlib.dimension.DimensionSettingsManager;
import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBd;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import javax.swing.text.html.Option;
import java.util.*;

public class DroidCoreBlockEntity extends BlockEntity {

    private final Map<Direction, Integer> powerLevels = new EnumMap<>(Direction.class);

    // Static blacklist shared across all droid cores
    private static final Set<Long> shipBlacklist = new HashSet<>();

    // Track this droid's ship ID for cleanup
    private Long ownShipId = null;

    public DroidCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DROID_CORE_BLOCK_ENTITY.get(), pos, state);
        // Initialize all power levels to 0
        for (Direction dir : Direction.values()) {
            powerLevels.put(dir, 0);
        }
    }

    private static final int searchDistance = 1024;

    public int getPowerForDirection(Direction direction) {
        return powerLevels.getOrDefault(direction, 0);
    }

    public void setPowerForDirection(Direction direction, int power) {
        int clampedPower = Math.max(0, Math.min(15, power));
        if (powerLevels.get(direction) != clampedPower) {
            powerLevels.put(direction, clampedPower);
            setChanged();

            // Notify neighbors when power changes
            if (level != null && !level.isClientSide()) {
                BlockPos poweredPos = getBlockPos().relative(direction);

                // Notify neighbors at this block's position
                level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());

                // Notify the powered block itself
                level.neighborChanged(poweredPos, getBlockState().getBlock(), getBlockPos());

                // Also update neighbors of the powered block
                level.updateNeighborsAt(poweredPos, getBlockState().getBlock());
            }
        }
    }


    public void tick() {
        if (level == null) return;

        Ship ship = VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        if (ship == null) return;

        // Register this ship to the blacklist on first tick
        if (ownShipId == null) {
            ownShipId = ship.getId();
            shipBlacklist.add(ownShipId);
        }

        Vector3dc shipCenter = ship.getWorldAABB().center(new Vector3d());

        updateTarget(shipCenter, ship).ifPresentOrElse(target -> {
            Direction facingDir = getBlockState().getValue(DroidCoreBlock.FACING);
            Vec3i facingShip = facingDir.getNormal();
            Direction.Axis facingAxis = facingDir.getAxis();
            Vector3dc facingWorld = ship.getShipToWorld().transformDirection(new Vector3d(facingShip.getX(), facingShip.getY(), facingShip.getZ()), new Vector3d());

            Vector3dc targetDir = target.pos.sub(shipCenter, new Vector3d()).normalize();

            List<Direction> sides = Arrays.stream(Direction.values()).filter(it -> it.getAxis() != facingAxis).toList();

            for (var side : sides) {
                updateSidePower(side, ship, target.ship, targetDir);
            }

            double dot = facingWorld.normalize(new Vector3d()).dot(targetDir);

            updateFront(dot, facingDir);

            updateThrust(target.dist / ship.getTransform().getShipToWorldScaling().x(), dot);
        }, () ->{
            for (var dir: Direction.values()) {
                setPowerForDirection(dir, 0);
            }
        });
    }

    private void updateFront(double dot, Direction facingDir) {
        if (dot > 0.985) {
            setPowerForDirection(facingDir, 15);
        } else {
            setPowerForDirection(facingDir, 0);
        }
    }

    private void updateSidePower(Direction side, Ship ship, Ship targetShip, Vector3dc targetDir) {
        Vec3i normal = side.getNormal();
        Vector3d normalD = new Vector3d(normal.getX(), normal.getY(), normal.getZ());
        Vector3dc normalDWorld = ship.getShipToWorld().transformDirection(normalD).normalize();

        // Calculate relative velocity to improve following accuracy
        Vector3dc ourVelocity = ship.getVelocity();
        Vector3dc targetVelocity = targetShip.getVelocity();
        Vector3dc relativeVelocity = targetVelocity.sub(ourVelocity, new Vector3d());

        // Lead time factor - adjust this to tune how much we lead the target
        // Higher values = more aggressive leading for fast-moving targets
        double leadTimeFactor = 0.25;

        // Calculate adjusted target direction accounting for relative velocity
        // This helps us steer towards where the target will be, not where it is
        Vector3dc adjustedTargetDir = new Vector3d(targetDir).add(
            relativeVelocity.mul(leadTimeFactor, new Vector3d())
        ).normalize();

        // Calculate perpendicular velocity component to prevent orbiting
        // Project our velocity onto the target direction to get parallel component
        double parallelVelocity = ourVelocity.dot(targetDir);
        Vector3dc parallelComponent = new Vector3d(targetDir).mul(parallelVelocity);

        // Perpendicular component is what causes orbiting - we need to cancel this
        Vector3dc perpendicularVelocity = ourVelocity.sub(parallelComponent, new Vector3d());

        // Damping factor - how aggressively we cancel perpendicular velocity
        double dampingFactor = 0.15;

        // Combine target steering with velocity damping
        // Subtract perpendicular velocity to create a corrective steering vector
        Vector3dc correctedTargetDir = new Vector3d(adjustedTargetDir).sub(
            perpendicularVelocity.mul(dampingFactor, new Vector3d())
        ).normalize();

        double dot = normalDWorld.dot(correctedTargetDir);
        int power = Math.max(Math.min(15, (int) (15 * Math.sqrt(dot))), 0);
        setPowerForDirection(side, power);
    }

    private void updateThrust(double dist, double dot) {
        int thrust = (int) Math.min(15, Math.max(((dot + 0.2) * dist) - 32, 0.0) / 32);

        Direction facing = getBlockState().getValue(DroidCoreBlock.FACING);

        // setPowerForDirection will handle neighbor notifications automatically
        setPowerForDirection(facing.getOpposite(), thrust);
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

            // Skip blacklisted ships
            if (shipBlacklist.contains(candidate.getId())) continue;

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

    @Override
    public void setRemoved() {
        super.setRemoved();
        // Remove this ship from the blacklist when the droid core is destroyed
        if (ownShipId != null) {
            shipBlacklist.remove(ownShipId);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag powerTag = new CompoundTag();
        for (Direction dir : Direction.values()) {
            powerTag.putInt(dir.getName(), powerLevels.get(dir));
        }
        tag.put("PowerLevels", powerTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("PowerLevels")) {
            CompoundTag powerTag = tag.getCompound("PowerLevels");
            for (Direction dir : Direction.values()) {
                if (powerTag.contains(dir.getName())) {
                    powerLevels.put(dir, powerTag.getInt(dir.getName()));
                }
            }
        }
    }
}
