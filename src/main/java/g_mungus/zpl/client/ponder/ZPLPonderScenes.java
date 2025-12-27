package g_mungus.zpl.client.ponder;

import g_mungus.zpl.block.ModBlocks;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
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

        Selection floor = util.select().fromTo(0, 0, 0, 12, 1, 12);
        builder.removeShadow();

        Selection ship = util.select().everywhere().substract(floor);

        builder.world().showSection(ship, Direction.EAST);

        for (int i = 0; i < 80; i++) {
            if (i == 10) {
                builder.overlay().showText(55).text("Ion Thrusters can be used to propel a ship.");
            }
            particle(builder);
            builder.idle(1);
        }

        builder.world().hideSection(ship, Direction.WEST);
        builder.idle(20);

        builder.world().showSection(floor, Direction.UP);
        builder.idle(10);

        builder.world().setBlock(new BlockPos(6, 1, 4), ModBlocks.ION_MODULATOR_BLOCK.get().defaultBlockState().setValue(IonModulatorBlock.FACING, Direction.NORTH), true);

        builder.idle(10);
        builder.world().setBlock(new BlockPos(6, 1, 3), ModBlocks.THRUSTER_EXHAUST_BLOCK.get().defaultBlockState().setValue(ThrusterExhaustBlock.FACING, Direction.NORTH), true);
        builder.idle(10);

        builder.overlay().showText(75).text("To set one up, place an Ion Thrust Modulator in front of an Ion Thruster Exhaust.");

        builder.idle(80);

        builder.overlay().showText(60).text("Then supply FE and a Redstone Signal to the Ion Thrust Modulator block.");

        builder.idle(10);

        builder.effects().emitParticles(new Vec3(6.5, 2.5, 4.5), builder.effects().simpleParticleEmitter(ParticleTypes.ELECTRIC_SPARK, new Vec3(0, -1, 0)), 2, 20);

        builder.idle(20);

        builder.world().setBlock(new BlockPos(5, 1, 4), Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), true);

        builder.idle(5);

        builder.world().setBlock(new BlockPos(4, 1, 4), Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACE, AttachFace.FLOOR), true);

        builder.idle(10);
        builder.world().toggleRedstonePower(util.select().position(5, 1, 4));
        builder.world().toggleRedstonePower(util.select().position(4, 1, 4));

        for (int i = 0; i < 16; i++) {
            builder.world().setBlock(new BlockPos(6, 1, 3), ModBlocks.THRUSTER_EXHAUST_BLOCK.get().defaultBlockState().setValue(ThrusterExhaustBlock.FACING, Direction.NORTH).setValue(ThrusterExhaustBlock.POWER, i), false);
            builder.idle(1);
        }

    }

    private static void particle(SceneBuilder builder) {
        builder.effects().emitParticles(new Vec3(7 + Math.random() * 7, Math.random() * 7, Math.random() * 13), builder.effects().simpleParticleEmitter(ParticleTypes.END_ROD, new Vec3(-2.5, (Math.random() - 0.5) / 10, (Math.random() - 0.5) / 10)), 1, 1);
    }
}
