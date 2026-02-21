package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TSCShockGrenadeProjectile extends ThrowableItemProjectile {
    private LivingEntity owner;


    private static final int CLOUD_DURATION_TICKS = 800;  // 30 seconds
    private static final int EFFECT_DURATION_TICKS = 300; // 15 seconds
    private static final float CLOUD_RADIUS = 6.0F;
    private static final float EXPLOSION_RADIUS = 4.0F;   // Explosion (sound + pooticles, no block damage)

    public TSCShockGrenadeProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TSCShockGrenadeProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.TSC_SHOCK_GRENADE_PROJECTILE.get(), shooter, level);
    }

    public TSCShockGrenadeProjectile(Level level, double x, double y, double z) {
        super(ModEntities.TSC_SHOCK_GRENADE_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TSC_PIPE_BOMB.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            Vec3 impactPos = result.getLocation();

            AreaEffectCloud cloud = new AreaEffectCloud(serverLevel, impactPos.x, impactPos.y, impactPos.z);
            cloud.setRadius(CLOUD_RADIUS);
            cloud.setRadiusOnUse(0F);
            cloud.setWaitTime(10);
            cloud.setDuration(CLOUD_DURATION_TICKS);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());

            // Slowness I and Weakness I for 10 seconds
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION_TICKS, 2, false, true, true));
            cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION_TICKS, 2, false, true, true));
            cloud.addEffect(new MobEffectInstance(ChangedEffects.SHOCK.get(), EFFECT_DURATION_TICKS, 2, false, true, true));

            serverLevel.addFreshEntity(cloud);

            // Small explosion (no block damage) for sound and particles
            serverLevel.explode(this, impactPos.x, impactPos.y, impactPos.z, EXPLOSION_RADIUS, false, Level.ExplosionInteraction.NONE);

            // Campfire smoke particles
            spawnCampfireSmoke(serverLevel, impactPos.x, impactPos.y, impactPos.z, CLOUD_RADIUS);

            this.discard();
        }
    }

    private static void spawnCampfireSmoke(ServerLevel level, double x, double y, double z, float radius) {
        for (int i = 0; i < 80; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 2 * radius;
            double oy = level.random.nextDouble() * radius;
            double oz = (level.random.nextDouble() - 0.5) * 2 * radius;
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x + ox, y + oy, z + oz, 1, 0.02, 0.1, 0.02, 0.02);
        }
    }
}

