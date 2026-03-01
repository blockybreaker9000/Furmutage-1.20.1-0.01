package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.jerika.furmutage.sound.ModSounds;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Large variant: spreads dark latex in a 10-block radius on impact.
 */
public class LargeDarkLatexGunkSpitProjectile extends ThrowableItemProjectile {

    private static final int SPLAT_RADIUS = 5;
    private static final int SPLAT_RADIUS_SQ = SPLAT_RADIUS * SPLAT_RADIUS;

    public LargeDarkLatexGunkSpitProjectile(EntityType<? extends LargeDarkLatexGunkSpitProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public LargeDarkLatexGunkSpitProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.LARGE_DARK_LATEX_GUNK_SPIT_PROJECTILE.get(), shooter, level);
    }

    public LargeDarkLatexGunkSpitProjectile(Level level, double x, double y, double z) {
        super(ModEntities.LARGE_DARK_LATEX_GUNK_SPIT_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.LARGE_DARK_LATEX_GUNK_SPIT.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (level().isClientSide || !(level() instanceof ServerLevel serverLevel))
            return;

        BlockPos center;
        if (result.getType() == HitResult.Type.BLOCK) {
            center = ((BlockHitResult) result).getBlockPos();
        } else if (result.getType() == HitResult.Type.ENTITY) {
            center = ((EntityHitResult) result).getEntity().blockPosition();
        } else {
            center = blockPosition();
        }

        spreadLatexAt(serverLevel, center);
        serverLevel.playSound(null, getX(), getY(), getZ(), ModSounds.LARGE_DARK_LATEX_GUNK_SPLAT.get(), SoundSource.NEUTRAL, 6.0f, 1.0f + serverLevel.getRandom().nextFloat() * 0.15f);
        discard();
    }

    private void spreadLatexAt(ServerLevel level, BlockPos center) {
        var type = ChangedLatexTypes.DARK_LATEX.get();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int dx = -SPLAT_RADIUS; dx <= SPLAT_RADIUS; dx++) {
            for (int dy = -SPLAT_RADIUS; dy <= SPLAT_RADIUS; dy++) {
                for (int dz = -SPLAT_RADIUS; dz <= SPLAT_RADIUS; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= SPLAT_RADIUS_SQ) {
                        mutable.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        AbstractLatexBlock.tryCover(level, mutable, type);
                    }
                }
            }
        }
    }
}
