package g_mungus.zpl.block.datagen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class BlockDataGenerator {

    public static String FOLDER = "src/main/resources/";
    private static final Logger log = LoggerFactory.getLogger(BlockDataGenerator.class);

    public static final Map<String, List<BlockType>> blocksToDatagen = new HashMap<>();
    static {
        blocksToDatagen.put("red_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("orange_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("yellow_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));

        blocksToDatagen.put("green_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("lime_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("cyan_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("blue_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("light_blue_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));

        blocksToDatagen.put("purple_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("magenta_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));

        blocksToDatagen.put("pink_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));

        blocksToDatagen.put("black_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("brown_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("gray_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("light_gray_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("chrome_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
        blocksToDatagen.put("agate_space_plating", List.of(BlockType.simple, BlockType.stairs, BlockType.slab, BlockType.wall));
    }


    public static void main(String[] args) {
        blocksToDatagen.forEach(BlockDataGenerator::generate);
    }

    private static void generate(String name, List<BlockType> types) {
        try {
            for (var type : types) {
                switch (type) {
                    case slab -> SlabGenerator.generateSlab(name);
                    case stairs -> StairGenerator.generateStair(name);
                    case simple -> SimpleBlockGenerator.generateSimpleBlock(name);
                    case wall -> WallGenerator.generateWall(name);
                    default -> {}
                }
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
