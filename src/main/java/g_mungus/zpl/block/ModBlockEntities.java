package g_mungus.zpl.block;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroInputModuleBlockEntity;
import g_mungus.zpl.block.droidcore.DroidCoreBlockEntity;
import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerBlockEntity;
import g_mungus.zpl.block.gyro.GyroscopeBlockEntity;
import g_mungus.zpl.block.hover.MassSuspensionMatrixBlockEntity;
import g_mungus.zpl.block.launcher.EnergyOrbLauncherBlockEntity;
import g_mungus.zpl.block.thruster.IonModulatorBlockEntity;
import g_mungus.zpl.block.thruster.ThrusterExhaustBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ZeroPointLabsMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ThrusterExhaustBlockEntity>> THRUSTER_EXHAUST_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("ion_thruster_exhaust_block_entity",
            () -> BlockEntityType.Builder.of(ThrusterExhaustBlockEntity::new, ModBlocks.THRUSTER_EXHAUST_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<GyroscopeBlockEntity>> GYROSCOPE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gyroscope_block_entity",
                    () -> BlockEntityType.Builder.of(GyroscopeBlockEntity::new, ModBlocks.GYROSCOPE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<IonModulatorBlockEntity>> ION_MODULATOR_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("ion_modulator_block_entity",
            () -> BlockEntityType.Builder.of(IonModulatorBlockEntity::new, ModBlocks.ION_MODULATOR_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<MassSuspensionMatrixBlockEntity>> MASS_SUSPENSION_MATRIX_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mass_suspension_matrix_block_entity",
                    () -> BlockEntityType.Builder.of(MassSuspensionMatrixBlockEntity::new, ModBlocks.MASS_SUSPENSION_MATRIX_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<EnergyOrbLauncherBlockEntity>> ENERGY_ORB_LAUNCHER =
            BLOCK_ENTITIES.register("photon_pulse_blaster_block_entity",
                    () -> BlockEntityType.Builder.of(EnergyOrbLauncherBlockEntity::new, ModBlocks.ENERGY_ORB_LAUNCHER.get()).build(null));

    public static final RegistryObject<BlockEntityType<DroidCoreBlockEntity>> DROID_CORE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("droid_core_block_entity",
                    () -> BlockEntityType.Builder.of(DroidCoreBlockEntity::new, ModBlocks.DROID_CORE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdvancedGyroscopeControllerBlockEntity>> ADVANCED_GYROSCOPE_CONTROLLER =
            BLOCK_ENTITIES.register("advanced_gyroscope_controller_block_entity",
                    () -> BlockEntityType.Builder.of(AdvancedGyroscopeControllerBlockEntity::new, ModBlocks.ADVANCED_GYROSCOPE_CONTROLLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdvancedGyroInputModuleBlockEntity>> ADVANCED_GYRO_INPUT_MODULE =
            BLOCK_ENTITIES.register("advanced_gyro_input_module_block_entity",
                    () -> BlockEntityType.Builder.of(AdvancedGyroInputModuleBlockEntity::new, ModBlocks.ADVANCED_GYROSCOPE_INPUT_MODULE.get()).build(null));
} 
