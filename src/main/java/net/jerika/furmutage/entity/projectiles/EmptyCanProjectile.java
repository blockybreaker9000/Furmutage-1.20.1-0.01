package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Throwable empty can projectile. Deals half a heart (1 damage) on entity hit.
 * On land/hit: plays sound, spawns item-texture particles, drops the can as an item.
 */
public class EmptyCanProjectile extends ThrowableItemProjectile {

    private static final float DAMAGE = 1.0f; // half heart

    public EmptyCanProjectile(EntityType<? extends EmptyCanProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public EmptyCanProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.EMPTY_CAN_PROJECTILE.get(), shooter, level);
    }

    public EmptyCanProjectile(Level level, double x, double y, double z) {
        super(ModEntities.EMPTY_CAN_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EMPTY_CAN.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level().isClientSide) return;

        Level level = level();
        ItemStack canStack = getItem().copy();
        if (canStack.isEmpty()) canStack = new ItemStack(ModItems.EMPTY_CAN.get());

        // Sound
        level.playSound(null, getX(), getY(), getZ(),
                ModSounds.EMPTY_CAN_HIT.get(), SoundSource.NEUTRAL,
                0.4f, 0.9f + level.getRandom().nextFloat() * 0.2f);

        // Entity damage
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) result;
            if (entityHit.getEntity() instanceof LivingEntity living) {
                living.hurt(level.damageSources().thrown(this, getOwner()), DAMAGE);
            }
        }

        // Item-texture particles (use can's texture colors)
        if (level instanceof ServerLevel serverLevel) {
            ItemParticleOption particleData = new ItemParticleOption(ParticleTypes.ITEM, canStack);
            for (int i = 0; i < 8; i++) {
                double ox = level.getRandom().nextGaussian() * 0.08;
                double oy = level.getRandom().nextGaussian() * 0.08;
                double oz = level.getRandom().nextGaussian() * 0.08;
                serverLevel.sendParticles(particleData, getX(), getY(), getZ(), 1, ox, oy, oz, 0.15);
            }
        }

        // Drop the can as item
        ItemEntity itemEntity = new ItemEntity(level, getX(), getY(), getZ(), canStack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);

        discard();
    }
}
