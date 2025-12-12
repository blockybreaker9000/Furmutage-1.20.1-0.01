package net.jerika.furmutage.entity;

import net.jerika.furmutage.item.ModItems;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3f;
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
            cloud.addEffect(new MobEffectInstance(ChangedEffects.SHOCK.get(), 60, 0, false, true, true));
            
            // Add slowness 2 effect
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2, false, true, true));
            cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 1, false, true, true));
            serverLevel.addFreshEntity(cloud);
            
            // Do explosive damage without breaking blocks
            AABB damageArea = new AABB(
                    impactPos.x - radius, impactPos.y - radius, impactPos.z - radius,
                    impactPos.x + radius, impactPos.y + radius, impactPos.z + radius
            );
            
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, damageArea, 
                    (entity) -> entity != this.owner && entity.isAlive());
            
            for (LivingEntity entity : entities) {
                // Don't damage TSCDroneEntity (immune to shock grenades)
                if (entity instanceof net.jerika.furmutage.entity.custom.TSCDroneEntity) {
                    continue;
                }
                
                double distance = entity.distanceToSqr(impactPos.x, impactPos.y, impactPos.z);
                if (distance <= radius * radius) {
                    // Damage decreases with distance
                    float damage = 4.0F * (1.0F - (float)(Math.sqrt(distance) / radius));
                    entity.hurt(serverLevel.damageSources().explosion(this, this.owner), damage);
                }
            }
            
            // Spawn explosion particles and sound (without block damage)
            serverLevel.explode(this, this.owner != null ? this.owner.getLastDamageSource() : null, null, impactPos.x, impactPos.y, impactPos.z,
                    2.0F, false, Level.ExplosionInteraction.NONE);

            // Spawn cyan colored explosion particles
            // Cyan color: RGB(0, 255, 255) normalized to 0-2 range
            DustParticleOptions cyanParticle = new DustParticleOptions(
                    new Vector3f(0.0f, 1.0f, 1.0f), // Cyan color (R=0, G=1, B=1)
                    2.0f // Scale
            );

            // Spawn multiple cyan particles in a sphere pattern around explosion
            for (int i = 0; i < 200; i++) {
                double angleX = serverLevel.random.nextDouble() * Math.PI * 2;
                double angleY = serverLevel.random.nextDouble() * Math.PI;
                double distance = serverLevel.random.nextDouble() * radius;
                double offsetX = Math.sin(angleY) * Math.cos(angleX) * distance;
                double offsetY = Math.cos(angleY) * distance;
                double offsetZ = Math.sin(angleY) * Math.sin(angleX) * distance;

                // Spawn cyan dust particles
                serverLevel.sendParticles(cyanParticle,
                        impactPos.x + offsetX, impactPos.y + offsetY, impactPos.z + offsetZ,
                        1, 0, 0, 0, 0);
            }

            // Also spawn cyan splash particles
            this.level().levelEvent(6002, this.blockPosition(), 65535);

            this.discard();
        }
    }
}

