package g_mungus.zpl.block.launcher;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.block.ModBlockEntities;
import g_mungus.zpl.entity.EnergyOrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnergyOrbLauncherBlockEntity extends BlockEntity {
    private boolean wasPowered = false;

    public EnergyOrbLauncherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ENERGY_ORB_LAUNCHER.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnergyOrbLauncherBlockEntity blockEntity) {
        if (level.isClientSide) return;

        // Check if block is receiving redstone power
        boolean isPowered = level.hasNeighborSignal(pos);

        // Fire on rising edge (when power turns on)
        if (isPowered && !blockEntity.wasPowered) {
            blockEntity.fireEnergyOrb(level, pos, state);
        }

        blockEntity.wasPowered = isPowered;
    }

    private void fireEnergyOrb(Level level, BlockPos pos, BlockState state) {
        // Get the facing direction
        Direction facing = state.getValue(EnergyOrbLauncherBlock.FACING);
        Vec3 direction = Vec3.atLowerCornerOf(facing.getNormal());

        // Get dimension scale
        double scale = ZeroPointLabsMod.getDimensionScale(level);

        // Calculate spawn position (center of block + offset in facing direction)
        Vec3 blockCenter = Vec3.atCenterOf(pos);
        Vec3 spawnPos = blockCenter.add(direction.scale(0.6 * scale));

        // Calculate velocity (scaled by dimension)
        double speed = 2.0 * scale;
        Vec3 velocity = direction.scale(speed);

        // Create and spawn the energy orb
        EnergyOrbEntity energyOrb = new EnergyOrbEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, velocity);
        level.addFreshEntity(energyOrb);

        // Play sound effect
        level.playSound(
            null,
            pos,
            SoundEvents.FIRECHARGE_USE,
            SoundSource.BLOCKS,
            1.0F,
            1.0F
        );
    }
}
