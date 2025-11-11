package g_mungus.zpl.block.droidcore;

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

import java.util.*;

public class DroidCoreBlockEntity extends BlockEntity {

    private final Map<Direction, Integer> powerLevels = new EnumMap<>(Direction.class);

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

        Vector3dc shipCenter = ship.getWorldAABB().center(new Vector3d());

        updateTarget(shipCenter, ship).ifPresent(target -> {
            Vec3i facingShip = getBlockState().getValue(DroidCoreBlock.FACING).getNormal();
            Direction.Axis facingAxis = getBlockState().getValue(DroidCoreBlock.FACING).getAxis();
            Vector3dc facingWorld = ship.getShipToWorld().transformDirection(new Vector3d(facingShip.getX(), facingShip.getY(), facingShip.getZ()), new Vector3d());

            Vector3dc targetDir = target.pos.sub(shipCenter, new Vector3d()).normalize();

            List<Direction> sides = Arrays.stream(Direction.values()).filter(it -> it.getAxis() != facingAxis).toList();

            for (var side : sides) {
                updateSidePower(side, ship, targetDir);
            }

            double dot = facingWorld.normalize(new Vector3d()).dot(targetDir);

            updateThrust(target.dist, dot);
        });
    }

    private void updateSidePower(Direction side, Ship ship, Vector3dc targetDir) {
        Vec3i normal = side.getNormal();
        Vector3d normalD = new Vector3d(normal.getX(), normal.getY(), normal.getZ());
        Vector3dc normalDWorld = ship.getShipToWorld().transformDirection(normalD).normalize();
        double dot = normalDWorld.dot(targetDir);
        int power = Math.max(Math.min(15, (int) (15 * dot)), 0);
        setPowerForDirection(side, power);
    }

    private void updateThrust(double dist, double dot) {
        int thrust = (int) Math.min(15, Math.max((dot * dist) - 32, 0.0) / 32);

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
