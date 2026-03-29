package g_mungus.zpl.block.advanced_gryo;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum GyroFrameState implements StringRepresentable {
    DISASSEMBLED( 0,  0,  0),
    // 4 vertical edges (N/S + E/W)
    EDGE_NORTHEAST    ( 1,  0, -1),
    EDGE_NORTHWEST    (-1,  0, -1),
    EDGE_SOUTHEAST    ( 1,  0,  1),
    EDGE_SOUTHWEST    (-1,  0,  1),
    // 4 top horizontal edges
    EDGE_TOP_NORTH    ( 0,  1, -1),
    EDGE_TOP_SOUTH    ( 0,  1,  1),
    EDGE_TOP_EAST     ( 1,  1,  0),
    EDGE_TOP_WEST     (-1,  1,  0),
    // 4 bottom horizontal edges
    EDGE_BOTTOM_NORTH ( 0, -1, -1),
    EDGE_BOTTOM_SOUTH ( 0, -1,  1),
    EDGE_BOTTOM_EAST  ( 1, -1,  0),
    EDGE_BOTTOM_WEST  (-1, -1,  0),
    // 8 corners
    CORNER_TOP_NORTHEAST    ( 1,  1, -1),
    CORNER_TOP_NORTHWEST    (-1,  1, -1),
    CORNER_TOP_SOUTHEAST    ( 1,  1,  1),
    CORNER_TOP_SOUTHWEST    (-1,  1,  1),
    CORNER_BOTTOM_NORTHEAST ( 1, -1, -1),
    CORNER_BOTTOM_NORTHWEST (-1, -1, -1),
    CORNER_BOTTOM_SOUTHEAST ( 1, -1,  1),
    CORNER_BOTTOM_SOUTHWEST (-1, -1,  1),
    ;

    public static final List<GyroFrameState> OUTLINE_POSITIONS = Arrays.stream(values())
            .filter(s -> s != DISASSEMBLED)
            .toList();

    public final int dx, dy, dz;

    GyroFrameState(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public BlockPos resolve(BlockPos center) {
        return center.offset(dx, dy, dz);
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
