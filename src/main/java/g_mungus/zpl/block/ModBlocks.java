package g_mungus.zpl.block;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.droidcore.DroidCoreBlock;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeController;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeFrame;
import g_mungus.zpl.block.gyro.GyroscopeBlock;
import g_mungus.zpl.block.hover.MassSuspensionMatrixBlock;
import g_mungus.zpl.block.launcher.EnergyOrbLauncherBlock;
import g_mungus.zpl.block.assembly.LaunchButtonBlock;
import g_mungus.zpl.block.thruster.IonModulatorBlock;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(Registries.BLOCK, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<Block> THRUSTER_EXHAUST_BLOCK = BLOCKS.register("ion_thruster_exhaust",
        () -> new ThrusterExhaustBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> ION_MODULATOR_BLOCK = BLOCKS.register("ion_thrust_modulator",
        () -> new IonModulatorBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> GYROSCOPE_BLOCK = BLOCKS.register("gyroscope",
        () -> new GyroscopeBlock(BlockBehaviour.Properties.of()
            .strength(3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
    );

    public static final RegistryObject<Block> MASS_SUSPENSION_MATRIX_BLOCK = BLOCKS.register("mass_suspension_matrix",
            () -> new MassSuspensionMatrixBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ENERGY_ORB_LAUNCHER = BLOCKS.register("photon_pulse_blaster",
            () -> new EnergyOrbLauncherBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LAUNCH_PLATFORM = BLOCKS.register("launch_platform",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LAUNCH_BUTTON = BLOCKS.register("launch_button",
            () -> new LaunchButtonBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DROID_CORE_BLOCK = BLOCKS.register("droid_core",
            () -> new DroidCoreBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ADVANCED_GYROSCOPE_CONTROLLER = BLOCKS.register("advanced_gyroscope_controller",
            () -> new AdvancedGyroscopeController(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Block> ADVANCED_GYROSCOPE_FRAME = BLOCKS.register("advanced_gyroscope_frame",
            () -> new AdvancedGyroscopeFrame(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)));
}
