package g_mungus.zpl.entity;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.particle.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnergyOrbEntity extends Projectile {

    public EnergyOrbEntity(EntityType<? extends EnergyOrbEntity> entityType, Level level) {
        super(entityType, level);
    }

    public EnergyOrbEntity(Level level, double x, double y, double z, Vec3 velocity) {
        super(ModEntities.ENERGY_ORB.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(velocity);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        // Raycast for collisions
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        // Move the entity
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);

        // Spawn trail particles (client-side will see these)
        if (this.level().isClientSide) {
            double scale = ZeroPointLabsMod.getDimensionScale(this.level());

            // Pass scale through velocity parameters (will be used for sizing)
            this.level().addParticle(
                    ModParticles.ENERGY_ORB.get(),
                    this.xOld, this.yOld, this.zOld,
                    scale, 0, 0  // Pass scale in xSpeed parameter
            );
        }

        // Remove after 5 seconds (100 ticks)
        if (this.tickCount > 100) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.explode();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        // Deal damage to the entity
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 5.0F);

        this.explode();
    }

    private void explode() {
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0f, Level.ExplosionInteraction.BLOCK);
        }

        this.discard();
    }

    @Override
    protected boolean canHitEntity(net.minecraft.world.entity.Entity target) {
        // Don't hit the owner
        return super.canHitEntity(target) && !target.equals(this.getOwner());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        // Render at longer distances
        return distance < 4096.0D;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        // No additional data to read
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // No additional data to save
    }
}
