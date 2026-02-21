package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Pipe bomb projectile. On impact creates an effect cloud (30 seconds)
 * that applies Slowness I and Weakness I for 10 seconds to entities inside.
 */
public class TSCPipeBombProjectile extends ThrowableItemProjectile {

    private static final int CLOUD_DURATION_TICKS = 600;  // 30 seconds
    private static final int EFFECT_DURATION_TICKS = 200; // 10 seconds
    private static final float CLOUD_RADIUS = 3.0F;
    private static final float EXPLOSION_RADIUS = 3.0F;   // Explosion (sound + particles, no block damage)

    public TSCPipeBombProjectile(EntityType<? extends TSCPipeBombProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TSCPipeBombProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.TSC_PIPE_BOMB_PROJECTILE.get(), shooter, level);
    }

    public TSCPipeBombProjectile(Level level, double x, double y, double z) {
        super(ModEntities.TSC_PIPE_BOMB_PROJECTILE.get(), x, y, z, level);
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
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION_TICKS, 0, false, true, true));
            cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION_TICKS, 0, false, true, true));

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
