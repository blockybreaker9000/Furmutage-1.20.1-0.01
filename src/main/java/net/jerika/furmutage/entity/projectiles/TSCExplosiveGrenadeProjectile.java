package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.HitResult;

/**
 * Explosive grenade projectile:
 * - Creates a relatively large explosion that does NOT destroy blocks.
 * - On detonation, spawns 5 TSC shock grenades that fly off in random directions.
 */
public class TSCExplosiveGrenadeProjectile extends ThrowableItemProjectile {

    private LivingEntity owner;

    public TSCExplosiveGrenadeProjectile(EntityType<? extends TSCExplosiveGrenadeProjectile> type, Level level) {
        super(type, level);
    }

    public TSCExplosiveGrenadeProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.TSC_EXPLOSIVE_GRENADE_PROJECTILE.get(), shooter, level);
        this.owner = shooter;
    }

    public TSCExplosiveGrenadeProjectile(Level level, double x, double y, double z) {
        super(ModEntities.TSC_EXPLOSIVE_GRENADE_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TSC_EXPLOSIVE_GRENADE.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            // "Somewhat big" explosion, no block damage
            float explosionRadius = 4.0F;
            serverLevel.explode(this, x, y, z, explosionRadius, false, ExplosionInteraction.NONE);

            // Spawn 5 shock grenades flying in random directions
            for (int i = 0; i < 5; i++) {
                TSCShockGrenadeProjectile shock = new TSCShockGrenadeProjectile(serverLevel, x, y, z);
                shock.setItem(new ItemStack(ModItems.TSC_SHOCK_GRENADE.get()));

                // Random direction and speed
                double vx = serverLevel.random.nextGaussian();
                double vy = serverLevel.random.nextGaussian() * 0.5D + 0.2D; // bias slightly upward
                double vz = serverLevel.random.nextGaussian();

                double speed = 0.8D;
                double length = Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (length > 0.0D) {
                    vx = vx / length * speed;
                    vy = vy / length * speed;
                    vz = vz / length * speed;
                }

                shock.setDeltaMovement(vx, vy, vz);
                serverLevel.addFreshEntity(shock);
            }

            this.discard();
        }
    }
}


