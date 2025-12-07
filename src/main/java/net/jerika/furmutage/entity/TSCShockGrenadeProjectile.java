package net.jerika.furmutage.entity;

import net.jerika.furmutage.item.ModItems;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TSCShockGrenadeProjectile extends ThrowableItemProjectile {
    private LivingEntity owner;
    
    public TSCShockGrenadeProjectile(EntityType<? extends TSCShockGrenadeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TSCShockGrenadeProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.TSC_SHOCK_GRENADE_PROJECTILE.get(), shooter, level);
        this.owner = shooter;
    }

    public TSCShockGrenadeProjectile(Level level, double x, double y, double z) {
        super(ModEntities.TSC_SHOCK_GRENADE_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TSC_SHOCK_GRENADE.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            Vec3 impactPos = result.getLocation();
            float radius = 3.0F; // 3 block radius
            
            // Create area effect cloud with shock and slowness effects
            AreaEffectCloud cloud = new AreaEffectCloud(serverLevel, impactPos.x, impactPos.y, impactPos.z);
            cloud.setRadius(radius);
            cloud.setRadiusOnUse(-0.5F); // Shrinks over time
            cloud.setWaitTime(10); // Wait 10 ticks before applying effects
            cloud.setDuration(300); // 30 seconds (600 ticks)
            cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
            
            // Add shock effect from Changed mod
            cloud.addEffect(new MobEffectInstance(ChangedEffects.SHOCK.get(), 50, 0, false, true, true));
            
            // Add slowness 2 effect
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 1, false, true, true));
            cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 2, false, true, true));
            serverLevel.addFreshEntity(cloud);
            
            // Do explosive damage without breaking blocks
            AABB damageArea = new AABB(
                    impactPos.x - radius, impactPos.y - radius, impactPos.z - radius,
                    impactPos.x + radius, impactPos.y + radius, impactPos.z + radius
            );
            
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, damageArea, 
                    (entity) -> entity != this.owner && entity.isAlive());
            
            for (LivingEntity entity : entities) {
                double distance = entity.distanceToSqr(impactPos.x, impactPos.y, impactPos.z);
                if (distance <= radius * radius) {
                    // Damage decreases with distance
                    float damage = 4.0F * (1.0F - (float)(Math.sqrt(distance) / radius));
                    entity.hurt(serverLevel.damageSources().explosion(this, this.owner), damage);
                }
            }
            
            // Spawn explosion particles and sound (without block damage)
            serverLevel.explode(this, this.owner.getLastDamageSource(), null, impactPos.x, impactPos.y, impactPos.z,
                    0.0F, false, Level.ExplosionInteraction.NONE);
            
            this.discard();
        }
    }
}

