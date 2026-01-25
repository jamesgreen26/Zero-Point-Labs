package g_mungus.zpl.block.datagen;

public class BlockItemGenerator {
    public static void generate(String name) {
        String json = """
            {
              "parent": "zpl:block/%1$s"
            }
            """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/models/item/" + name + ".json";

        FileWriter.writeFile(path, json);
    }
}
