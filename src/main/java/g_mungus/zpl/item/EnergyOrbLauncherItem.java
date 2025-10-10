package g_mungus.zpl.item;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.entity.EnergyOrbEntity;
import g_mungus.zpl.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnergyOrbLauncherItem extends Item {

    public EnergyOrbLauncherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Get player's look direction
            Vec3 lookVector = player.getLookAngle();

            // Get dimension scale
            double scale = ZeroPointLabsMod.getDimensionScale(level);

            // Starting position (slightly in front of player's eyes, scaled)
            Vec3 eyePos = player.getEyePosition();
            Vec3 spawnPos = eyePos.add(lookVector.scale(0.5 * scale));

            // Projectile speed (scaled by dimension)
            double speed = 2.0 * scale;
            Vec3 velocity = lookVector.scale(speed);

            // Create and spawn the energy orb entity
            EnergyOrbEntity energyOrb = new EnergyOrbEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, velocity);
            energyOrb.setOwner(player);
            level.addFreshEntity(energyOrb);

            // Play sound effect
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.ENERGY_ORB_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            // Add cooldown (1 tick for testing, can increase later)
            player.getCooldowns().addCooldown(this, 1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
