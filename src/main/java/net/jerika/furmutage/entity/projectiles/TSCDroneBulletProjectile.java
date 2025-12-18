package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class TSCDroneBulletProjectile extends ThrowableItemProjectile {
    public TSCDroneBulletProjectile(EntityType<? extends TSCDroneBulletProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TSCDroneBulletProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.TSC_DRONE_BULLET_PROJECTILE.get(), shooter, level);
    }

    public TSCDroneBulletProjectile(Level level, double x, double y, double z) {
        super(ModEntities.TSC_DRONE_BULLET_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TSC_DRONE_BULLET.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof LivingEntity livingEntity && this.getOwner() instanceof LivingEntity owner) {
                // Don't damage TSCDroneEntity (immune to bullets)
                if (livingEntity instanceof TSCDroneEntity) {
                    return;
                }
                // Deal damage to the hit entity
                livingEntity.hurt(this.level().damageSources().mobProjectile(this, owner), 3.0F);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // Spawn impact particles
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            this.discard();
        }
    }
}

