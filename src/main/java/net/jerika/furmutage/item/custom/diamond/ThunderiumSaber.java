package net.jerika.furmutage.item.custom.diamond;

import net.jerika.furmutage.item.TSCElectriumWeaponSwords;
import net.jerika.furmutage.item.TSCElectriumWeaponTazers;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ThunderiumSaber extends TSCElectriumWeaponSwords {
    public ThunderiumSaber() {
        super(new Properties().durability(700));
    }
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity enemy, LivingEntity source) {
        sweepWeapon(source, attackRange());
        applyShock(enemy, 8);
        // Sweep attack: hit up to 10 nearby mobs
        if (source.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            double sweepRange = attackRange() * 2.0; // Sweep range is double the attack range
            AABB sweepArea = source.getBoundingBox().inflate(sweepRange, 1.0, sweepRange);

            List<LivingEntity> nearbyEntities = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    sweepArea,
                    (entity) -> entity != source &&
                            entity != enemy &&
                            entity.isAlive() &&
                            !entity.isAlliedTo(source) &&
                            source.canAttack(entity)
            );

            // Limit to 10 mobs (excluding the primary target)
            int maxSweepTargets = Math.min(10, nearbyEntities.size());
            for (int i = 0; i < maxSweepTargets; i++) {
                LivingEntity target = nearbyEntities.get(i);
                // Calculate damage based on distance (slightly reduced for sweep targets)
                double distance = source.distanceTo(target);
                float damageMultiplier = 1.0f - (float)(distance / (sweepRange * 2.0)) * 0.3f; // Up to 30% damage reduction
                float sweepDamage = (float)(attackDamage() * damageMultiplier);

                // Apply damage using generic mob attack
                target.hurt(source.damageSources().mobAttack(source), sweepDamage);
                // Apply shock effect
                applyShock(target, 8);
            }
        }
        itemStack.hurtAndBreak(1, source, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return blockState.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
    }

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        if (blockState.getDestroySpeed(level, blockPos) != 0.0F) {
            itemStack.hurtAndBreak(2, entity, (living) -> {
                living.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.swingTime > 0)
            return true;
        sweepWeapon(entity, attackRange());
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public int attackStun() {return 8;}

    @Override
    public double attackDamage() {
        return 7 + Tiers.DIAMOND.getAttackDamageBonus();
    }

    @Override
    public double attackSpeed() {
        return -1.4;
    }

    public double attackRange() {
        return (double)0.3F;
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.GOLD.getEnchantmentValue();
    }
}
