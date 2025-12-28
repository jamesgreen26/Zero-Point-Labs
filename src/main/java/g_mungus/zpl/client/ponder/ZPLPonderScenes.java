package g_mungus.zpl.client.ponder;

import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.Vec3;

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
        int size = 13;
        builder.configureBasePlate(0, 0, size);
        builder.setSceneOffsetY(-1.5f);
        builder.title("thruster", "Ion Thrusters");

        Selection exhaust = util.select().position(2, 4, 6);

        Selection allExhausts = exhaust
                .add(util.select().position(2, 2, 6))
                .add(util.select().position(2, 3, 4))
                .add(util.select().position(2, 3, 8));

        Selection modulator = util.select().position(3, 4, 6);

        Selection floor = util.select().fromTo(0, 0, 0, 12, 1, 12);
        builder.removeShadow();

        Selection ship = util.select().everywhere().substract(floor);
        ElementLink<WorldSectionElement> shipElement = null;

        builder.world().showSection(ship, Direction.UP);

        for (int i = 0; i < 275; i++) {
            if (i == 10) {
                builder.overlay().showText(55).text("Ion Thrusters can be used to propel a ship.");
            } else if (i == 70) {
                builder.overlay().showOutlineWithText(exhaust.add(modulator), 65).text("To set one up, place an Ion Thrust Modulator in front of an Ion Thruster Exhaust.");
            } else if (i == 150) {
                builder.overlay().showOutlineWithText(modulator, 65).text("Activate the thruster by providing FE and a Redstone Signal to the Ion Thrust Modulator block.");
                shipElement = builder.world().makeSectionIndependent(ship);
            }

            int moveStart = 230;
            if (i >= moveStart) {

                int power = Math.min(Math.max(i - moveStart, 0), 15);
                if (i < moveStart + 16) {
                    builder.world().setBlocks(allExhausts, ModBlocks.THRUSTER_EXHAUST_BLOCK.get().defaultBlockState().setValue(ThrusterExhaustBlock.FACING, Direction.WEST).setValue(ThrusterExhaustBlock.POWER, power), false);
                }

                assert shipElement != null;
                builder.world().moveSection(shipElement, new Vec3((i - moveStart) / 15f, 0, 0), 1);
            }
            builder.idle(1);
        }
    }
}
