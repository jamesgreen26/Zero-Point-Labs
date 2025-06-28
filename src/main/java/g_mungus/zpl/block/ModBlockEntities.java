package g_mungus.zpl.block;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.gyro.GyroscopeBlockEntity;
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
} 