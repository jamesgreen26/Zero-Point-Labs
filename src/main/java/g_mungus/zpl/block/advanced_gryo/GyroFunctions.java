package g_mungus.zpl.block.advanced_gryo;

/**
 * Holds the 8 computed function values produced by an AdvancedGyroscopeController each tick.
 * Each value is a redstone signal strength in [0, 15].
 */
public record GyroFunctions(
        int rotXPos,
        int rotXNeg,
        int rotYPos,
        int rotYNeg,
        int rotZPos,
        int rotZNeg,
        int stabilize,
        int bypass
) {
    public static final GyroFunctions ZERO = new GyroFunctions(0, 0, 0, 0, 0, 0, 0, 0);

    /** Build a GyroFunctions from the raw int[8] array produced by the controller tick. */
    public static GyroFunctions fromArray(int[] values) {
        return new GyroFunctions(
                values[0], values[1], values[2], values[3],
                values[4], values[5], values[6], values[7]
        );
    }
}
