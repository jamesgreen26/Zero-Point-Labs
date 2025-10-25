package g_mungus.zpl.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;


public class FastProjectileEntity extends Projectile {

    protected static final EntityDataAccessor<Vector3f> DELTA_MOVEMENT_SYNCED =
            SynchedEntityData.defineId(FastProjectileEntity.class, EntityDataSerializers.VECTOR3);

    private Vec3 localDeltaMovement = Vec3.ZERO;

    protected FastProjectileEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DELTA_MOVEMENT_SYNCED, new Vector3f());
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        if (!this.level().isClientSide()) {
            return this.localDeltaMovement;
        }

        Vector3f movement = this.entityData.get(DELTA_MOVEMENT_SYNCED);
        return new Vec3(movement.x, movement.y, movement.z);
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        setDeltaMovement(new Vec3(x, y, z));
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 vec) {
        this.localDeltaMovement = vec;
        super.setDeltaMovement(vec);

        if (this.level().isClientSide()) {
            return;
        }

        Vector3f current = this.entityData.get(DELTA_MOVEMENT_SYNCED);
        if (current.x != (float) vec.x || current.y != (float) vec.y || current.z != (float) vec.z) {
            this.entityData.set(DELTA_MOVEMENT_SYNCED, new Vector3f((float) vec.x, (float) vec.y, (float) vec.z));
        }
    }
}

