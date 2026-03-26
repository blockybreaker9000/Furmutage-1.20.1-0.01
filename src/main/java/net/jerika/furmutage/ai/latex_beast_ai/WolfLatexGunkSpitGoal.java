package net.jerika.furmutage.ai.latex_beast_ai;

import net.jerika.furmutage.entity.projectiles.DarkLatexGunkSpitProjectile;
import net.jerika.furmutage.entity.projectiles.WhiteLatexGunkSpitProjectile;
import net.jerika.furmutage.item.ModItems;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Night/Chance-based ranged attack for specific Changed latex wolves.
 * When it succeeds, the wolf spawns and throws the matching gunk spit projectile.
 */
public class WolfLatexGunkSpitGoal extends Goal {

    public enum Variant {
        WHITE,
        DARK
    }

    private static final double DEFAULT_RANGE = 18.0D;
    private static final double DEFAULT_RANGE_SQR = DEFAULT_RANGE * DEFAULT_RANGE;

    private static final double SPIT_CHANCE = 0.30D;

    // Cooldown after a successful spit to avoid spamming projectiles.
    private static final int SPIT_COOLDOWN_TICKS = 40;

    private final PathfinderMob mob;
    private final Variant variant;
    private LivingEntity target;
    private int spitCooldown = 0;

    public WolfLatexGunkSpitGoal(PathfinderMob mob, Variant variant) {
        this.mob = mob;
        this.variant = variant;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (mob.distanceToSqr(target) > DEFAULT_RANGE_SQR) {
            return false;
        }
        // Respect x-ray config via hasLineOfSight mixin.
        return mob.getSensing().hasLineOfSight(target);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        // Don't fire immediately on start; rely on cooldown for pacing.
    }

    @Override
    public void tick() {
        if (spitCooldown > 0) {
            spitCooldown--;
            return;
        }

        if (target == null || !target.isAlive()) {
            return;
        }

        if (mob.distanceToSqr(target) > DEFAULT_RANGE_SQR) {
            return;
        }

        // Roll the 30% chance to throw.
        if (mob.getRandom().nextDouble() < SPIT_CHANCE) {
            Level level = mob.level();
            if (!level.isClientSide) {
                tryThrowProjectile(level, target);
            }
            spitCooldown = SPIT_COOLDOWN_TICKS;
        }
    }

    private void tryThrowProjectile(Level level, LivingEntity target) {
        // Look at target for nicer behavior.
        Vec3 targetEye = target.getEyePosition(1.0F);
        mob.getLookControl().setLookAt(targetEye.x, targetEye.y, targetEye.z, 30.0F, 30.0F);

        if (variant == Variant.WHITE) {
            WhiteLatexGunkSpitProjectile proj = new WhiteLatexGunkSpitProjectile(level, mob);
            proj.setItem(new ItemStack(ModItems.WHITE_LATEX_GUNK_SPIT.get()));
            proj.shootFromRotation(mob, mob.getXRot(), mob.getYRot(), 0.0F, 1.2F, 1.0F);
            level.addFreshEntity(proj);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), ModSounds.LATEX_GUNK_SPIT.get(), SoundSource.NEUTRAL,
                    0.5F, 0.6F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        } else {
            DarkLatexGunkSpitProjectile proj = new DarkLatexGunkSpitProjectile(level, mob);
            proj.setItem(new ItemStack(ModItems.DARK_LATEX_GUNK_SPIT.get()));
            proj.shootFromRotation(mob, mob.getXRot(), mob.getYRot(), 0.0F, 1.2F, 1.0F);
            level.addFreshEntity(proj);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), ModSounds.LATEX_GUNK_SPIT.get(), SoundSource.NEUTRAL,
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }
}

