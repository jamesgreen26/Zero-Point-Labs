package g_mungus.zpl.item;

import g_mungus.zpl.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
            Vec3 lookVector = player.getLookAngle();

            Vec3 eyePos = player.getEyePosition();
            Vec3 spawnPos = eyePos.add(lookVector.scale(0.5));

            double speed = 3;
            Vec3 velocity = lookVector.scale(speed);

            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.sendParticles(
                ModParticles.ENERGY_ORB.get(),
                spawnPos.x, spawnPos.y, spawnPos.z,
                0,
                velocity.x, velocity.y, velocity.z,
                1.0
            );

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5F, 1.2F);

            player.getCooldowns().addCooldown(this, 1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
