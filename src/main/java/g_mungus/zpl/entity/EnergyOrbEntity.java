package g_mungus.zpl.entity;

import g_mungus.zpl.ZeroPointLabsMod;
import g_mungus.zpl.particle.ModParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class EnergyOrbEntity extends Projectile {
    private static final EntityDataAccessor<Long> EXCLUDED_SHIP_ID =
        SynchedEntityData.defineId(EnergyOrbEntity.class, EntityDataSerializers.LONG);

    private static final long NO_SHIP_ID = -1L;

    public EnergyOrbEntity(EntityType<? extends EnergyOrbEntity> entityType, Level level) {
        super(entityType, level);
    }

    public EnergyOrbEntity(Level level, double x, double y, double z, Vec3 velocity) {
        super(ModEntities.ENERGY_ORB.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(velocity);
    }

    public void setExcludedShipId(@Nullable Long shipId) {
        this.entityData.set(EXCLUDED_SHIP_ID, shipId != null ? shipId : NO_SHIP_ID);
    }

    @Nullable
    public Long getExcludedShipId() {
        long shipId = this.entityData.get(EXCLUDED_SHIP_ID);
        return shipId == NO_SHIP_ID ? null : shipId;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EXCLUDED_SHIP_ID, NO_SHIP_ID);
    }

    @Override
    public void tick() {
        super.tick();

        // Raycast for collisions
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        // Check if we hit a block on our excluded ship
        Long excludedShipId = this.getExcludedShipId();
        if (hitResult.getType() == HitResult.Type.BLOCK && excludedShipId != null) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            Ship ship = VSGameUtilsKt.getShipManagingPos(this.level(), blockHit.getBlockPos());

            // If the block is part of our excluded ship, ignore the hit
            if (ship != null && ship.getId() == excludedShipId) {
                // Don't trigger hit, just continue
            } else {
                this.onHit(hitResult);
            }
        } else if (hitResult.getType() != HitResult.Type.MISS) {
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

        // Remove after 8 seconds (160 ticks)
        if (this.tickCount > 160) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.explode(result.getBlockPos().getCenter());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        // Deal damage to the entity
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 5.0F);

        this.explode(result.getEntity().position());
    }

    private void explode(Vec3 position) {
        if (!this.level().isClientSide) {
            this.level().explode(this, position.x, position.y, position.z, 2.0f, Level.ExplosionInteraction.BLOCK);
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
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("ExcludedShipId")) {
            this.setExcludedShipId(tag.getLong("ExcludedShipId"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        Long shipId = this.getExcludedShipId();
        if (shipId != null) {
            tag.putLong("ExcludedShipId", shipId);
        }
    }
}
