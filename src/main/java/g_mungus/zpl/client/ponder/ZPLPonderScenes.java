package g_mungus.zpl.client.ponder;

import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ZPLPonderScenes {
    public static void assemblyTutorial(SceneBuilder builder, SceneBuildingUtil util) {
        builder.configureBasePlate(0, 0, 13);
        builder.scaleSceneView(0.75f);
        builder.title("assembly", "Ship Assembly");
        builder.showBasePlate();
        builder.idle(5);

        Selection button = util.select().position(10, 2, 2);
        Selection ship = util.select().fromTo(0, 2, 0, 6, 6, 12).add(util.select().fromTo(6, 2, 4, 12, 6, 12));

        builder.overlay().showText(55).text("To assemble a ship, first build a Launch Platform.");
        builder.idle(10);
        builder.world().showSection(util.select().layer(1), Direction.DOWN);
        builder.idle(50);

        builder.overlay().showText(55).text("Next, build your ship on top.");
        builder.idle(10);
        builder.world().showSection(ship, Direction.DOWN);
        builder.idle(50);

        builder.overlay().showText(60).text("Then, place a Launch Button on the platform, and press it to assemble your ship.");
        builder.idle(10);
        builder.world().showSection(button, Direction.DOWN);
        builder.idle(20);
        builder.world().toggleRedstonePower(button);
        builder.idle(10);
        builder.world().toggleRedstonePower(button);
        builder.idle(2);

        builder.world().moveSection(builder.world().makeSectionIndependent(ship), new Vec3(0, 1, 0), 20);
        builder.idle(20);

    }

    public static void thrusterTutorial(SceneBuilder builder, SceneBuildingUtil util) {
        builder.configureBasePlate(0, 0, 13);
        builder.title("thruster", "Ion Thrusters");

        Selection floor = util.select().fromTo(0, 0, 0, 12, 0, 12);
        builder.addInstruction(new DisplayWorldSectionInstruction(0, Direction.UP, floor, builder.getScene()::getBaseWorldSection));
        builder.removeShadow();
        builder.overlay().showText(55).text("Ion Thrusters can be used to propel a ship.");

        Selection ship = util.select().everywhere().substract(floor);
        builder.world().showSection(ship, Direction.DOWN);

//        List<ElementLink<WorldSectionElement>> floorElements = new ArrayList<>(13);
//
//        for (int i = 0; i < 13; i++) {
//            Selection selection = util.select().fromTo(i, 0, 0, i, 0, 12);
//            floorElements.add(builder.world().makeSectionIndependent(selection));
//        }
//
//        int speed = 10;
//        int cycles = 10;
//
//        for (int j = 0; j < cycles; j++) {
//            for (int i = 0; i < 13; i++) {
//                builder.world().moveSection(floorElements.get(i), new Vec3(-1, 0, 0), speed);
//            }
//            builder.idle(speed);
//            builder.world().moveSection(floorElements.get(j), new Vec3(12, 0, 0), 0);
//        }
    }
}
