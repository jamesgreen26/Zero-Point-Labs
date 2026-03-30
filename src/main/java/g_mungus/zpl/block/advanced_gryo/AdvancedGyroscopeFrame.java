package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AdvancedGyroscopeFrame extends AdvancedGyroscopeComponent {

    public static final EnumProperty<GyroFrameState> FRAME_STATE = EnumProperty.create("frame_state", GyroFrameState.class);
    private static final VoxelShape EDGE_SHAPE = box(0, 0, 0, 16, 8, 8);
    private static final VoxelShape EDGE_VERT_SHAPE = box(0, 0, 8, 8, 16, 16);
    private static final VoxelShape CORNER_SHAPE = Shapes.or(
            box(8, 0, 0, 16, 8, 8),
            box(0, 0, 8, 8, 8, 16),
            box(0, 8, 0, 8, 16, 8),
            box(0, 0, 0, 8, 8, 8)
    );
    private static final Map<GyroFrameState, VoxelShape> SHAPES = buildShapes();

    public AdvancedGyroscopeFrame(Properties arg) {
        super(arg);
        registerDefaultState(stateDefinition.any().setValue(FRAME_STATE, GyroFrameState.DISASSEMBLED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME_STATE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        GyroFrameState frameState = state.getValue(FRAME_STATE);
        return SHAPES.getOrDefault(frameState, Shapes.block());
    }

    private static Map<GyroFrameState, VoxelShape> buildShapes() {
        Map<GyroFrameState, VoxelShape> shapes = new EnumMap<>(GyroFrameState.class);
        shapes.put(GyroFrameState.DISASSEMBLED, Shapes.block());

        shapes.put(GyroFrameState.EDGE_BOTTOM_NORTH, EDGE_SHAPE);
        shapes.put(GyroFrameState.EDGE_BOTTOM_SOUTH, rotate(EDGE_SHAPE, 0, 180));
        shapes.put(GyroFrameState.EDGE_BOTTOM_EAST, rotate(EDGE_SHAPE, 0, 90));
        shapes.put(GyroFrameState.EDGE_BOTTOM_WEST, rotate(EDGE_SHAPE, 0, 270));

        shapes.put(GyroFrameState.EDGE_TOP_NORTH, rotate(EDGE_SHAPE, 180, 180));
        shapes.put(GyroFrameState.EDGE_TOP_SOUTH, rotate(EDGE_SHAPE, 180, 0));
        shapes.put(GyroFrameState.EDGE_TOP_EAST, rotate(EDGE_SHAPE, 180, 270));
        shapes.put(GyroFrameState.EDGE_TOP_WEST, rotate(EDGE_SHAPE, 180, 90));

        shapes.put(GyroFrameState.EDGE_SOUTHWEST, EDGE_VERT_SHAPE);
        shapes.put(GyroFrameState.EDGE_NORTHWEST, rotate(EDGE_VERT_SHAPE, 0, 90));
        shapes.put(GyroFrameState.EDGE_NORTHEAST, rotate(EDGE_VERT_SHAPE, 0, 180));
        shapes.put(GyroFrameState.EDGE_SOUTHEAST, rotate(EDGE_VERT_SHAPE, 0, 270));

        shapes.put(GyroFrameState.CORNER_BOTTOM_NORTHWEST, CORNER_SHAPE);
        shapes.put(GyroFrameState.CORNER_BOTTOM_NORTHEAST, rotate(CORNER_SHAPE, 0, 90));
        shapes.put(GyroFrameState.CORNER_BOTTOM_SOUTHEAST, rotate(CORNER_SHAPE, 0, 180));
        shapes.put(GyroFrameState.CORNER_BOTTOM_SOUTHWEST, rotate(CORNER_SHAPE, 0, 270));

        shapes.put(GyroFrameState.CORNER_TOP_SOUTHWEST, rotate(CORNER_SHAPE, 180, 0));
        shapes.put(GyroFrameState.CORNER_TOP_NORTHWEST, rotate(CORNER_SHAPE, 180, 90));
        shapes.put(GyroFrameState.CORNER_TOP_NORTHEAST, rotate(CORNER_SHAPE, 180, 180));
        shapes.put(GyroFrameState.CORNER_TOP_SOUTHEAST, rotate(CORNER_SHAPE, 180, 270));

        return shapes;
    }

    private static VoxelShape rotate(VoxelShape shape, int xRot, int yRot) {
        int xTurns = ((xRot % 360) + 360) % 360 / 90;
        int yTurns = ((yRot % 360) + 360) % 360 / 90;
        VoxelShape rotated = shape;
        for (int i = 0; i < xTurns; i++) {
            rotated = rotateX(rotated);
        }
        for (int i = 0; i < yTurns; i++) {
            rotated = rotateY(rotated);
        }
        return rotated;
    }

    private static VoxelShape rotateY(VoxelShape shape) {
        return rotateShape(shape, RotationAxis.Y);
    }

    private static VoxelShape rotateX(VoxelShape shape) {
        return rotateShape(shape, RotationAxis.X);
    }

    private static VoxelShape rotateShape(VoxelShape shape, RotationAxis axis) {
        List<AABB> boxes = shape.toAabbs();
        VoxelShape result = Shapes.empty();
        for (AABB box : boxes) {
            AABB rotated = axis == RotationAxis.Y ? rotateY90(box) : rotateX90(box);
            result = Shapes.or(result, Shapes.create(rotated));
        }
        return result;
    }

    private static AABB rotateY90(AABB box) {
        double minX = 1.0;
        double minZ = 1.0;
        double maxX = 0.0;
        double maxZ = 0.0;
        double[] xs = {box.minX, box.maxX};
        double[] zs = {box.minZ, box.maxZ};
        for (double x : xs) {
            for (double z : zs) {
                double rx = 1.0 - z;
                double rz = x;
                minX = Math.min(minX, rx);
                minZ = Math.min(minZ, rz);
                maxX = Math.max(maxX, rx);
                maxZ = Math.max(maxZ, rz);
            }
        }
        return new AABB(minX, box.minY, minZ, maxX, box.maxY, maxZ);
    }

    private static AABB rotateX90(AABB box) {
        double minY = 1.0;
        double minZ = 1.0;
        double maxY = 0.0;
        double maxZ = 0.0;
        double[] ys = {box.minY, box.maxY};
        double[] zs = {box.minZ, box.maxZ};
        for (double y : ys) {
            for (double z : zs) {
                double ry = 1.0 - z;
                double rz = y;
                minY = Math.min(minY, ry);
                minZ = Math.min(minZ, rz);
                maxY = Math.max(maxY, ry);
                maxZ = Math.max(maxZ, rz);
            }
        }
        return new AABB(box.minX, minY, minZ, box.maxX, maxY, maxZ);
    }

    private enum RotationAxis {
        X,
        Y
    }
}
