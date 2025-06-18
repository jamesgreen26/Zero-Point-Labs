package g_mungus.zpl.block.thruster;

import g_mungus.zpl.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonModulatorBlockEntity extends BlockEntity implements ICapabilityProvider<IonModulatorBlockEntity, Direction, IEnergyStorage> {
    
    private final EnergyStorage energyStorage;
    private static final int MAX_ENERGY = 4000;
    private static final int MAX_TRANSFER = 2000;
    
    public IonModulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ION_MODULATOR_BLOCK_ENTITY.get(), pos, state);
        this.energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER, MAX_TRANSFER);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.ION_MODULATOR_BLOCK_ENTITY.get(),
                (be, context) -> be.getCapability(be, context)
        );
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Energy", CompoundTag.TAG_INT)) {
            energyStorage.deserializeNBT(provider, tag.get("Energy"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Energy", energyStorage.serializeNBT(provider));
    }

    @Override
    public IEnergyStorage getCapability(IonModulatorBlockEntity blockEntity, Direction side) {
        return blockEntity.energyStorage;
    }


} 