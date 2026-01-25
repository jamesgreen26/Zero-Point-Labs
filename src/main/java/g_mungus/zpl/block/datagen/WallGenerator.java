package g_mungus.zpl.block.datagen;

public class WallGenerator {
    public static void generateWall(String name) {
        generateBlockState(name);
        generateBlockModel(name);
        generateBlockModel(name);
        generateLootTable(name);
        generateItemModel(name);
    }

    private static void generateBlockModel(String name) {
        generateSideModel(name);
        generateSideTallModel(name);
        generatePostModel(name);
    }

    private static void generateSideModel(String name) {
        String json = """
                {
                  "parent": "minecraft:block/template_wall_side",
                  "textures": {
                    "wall": "zpl:block/%1$s"
                  }
                }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/models/block/" + name + "_wall_side.json";

        FileWriter.writeFile(path, json);
    }

    private static void generatePostModel(String name) {
        String json = """
                {
                  "parent": "minecraft:block/template_wall_post",
                  "textures": {
                    "wall": "zpl:block/%1$s"
                  }
                }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/models/block/" + name + "_wall_post.json";

        FileWriter.writeFile(path, json);
    }

    private static void generateSideTallModel(String name) {
        String json = """
                {
                  "parent": "minecraft:block/template_wall_side_tall",
                  "textures": {
                    "wall": "zpl:block/%1$s"
                  }
                }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/models/block/" + name + "_wall_side_tall.json";

        FileWriter.writeFile(path, json);
    }

    private static void generateItemModel(String name) {
        String json = """
                {
                  "parent": "minecraft:block/wall_inventory",
                  "textures": {
                    "wall": "zpl:block/%1$s"
                  }
                }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/models/item/" + name + "_wall.json";

        FileWriter.writeFile(path, json);
    }

    private static void generateBlockState(String name) {
        String json = """
            {
              "multipart": [
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_post"
                  },
                  "when": {
                    "up": "true"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side",
                    "uvlock": true
                  },
                  "when": {
                    "north": "low"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side",
                    "uvlock": true,
                    "y": 90
                  },
                  "when": {
                    "east": "low"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side",
                    "uvlock": true,
                    "y": 180
                  },
                  "when": {
                    "south": "low"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side",
                    "uvlock": true,
                    "y": 270
                  },
                  "when": {
                    "west": "low"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side_tall",
                    "uvlock": true
                  },
                  "when": {
                    "north": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side_tall",
                    "uvlock": true,
                    "y": 90
                  },
                  "when": {
                    "east": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side_tall",
                    "uvlock": true,
                    "y": 180
                  },
                  "when": {
                    "south": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "zpl:block/%1$s_wall_side_tall",
                    "uvlock": true,
                    "y": 270
                  },
                  "when": {
                    "west": "tall"
                  }
                }
              ]
            }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "assets/zpl/blockstates/" + name + "_wall.json";

        FileWriter.writeFile(path, json);
    }

    private static void generateLootTable(String name) {
        String json = """
                        {
                          "type": "minecraft:block",
                          "pools": [
                            {
                              "bonus_rolls": 0.0,
                              "rolls": 1.0,
                              "entries": [
                                {
                                  "type": "minecraft:item",
                                  "name": "zpl:%1$s_wall"
                                }
                              ],
                              "conditions": [
                                {
                                  "condition": "minecraft:survives_explosion"
                                }
                              ]
                            }
                          ]
                        }
              """.formatted(name);
        String path = BlockDataGenerator.FOLDER + "data/zpl/loot_tables/blocks/" + name + "_wall.json";

        FileWriter.writeFile(path, json);
    }
}