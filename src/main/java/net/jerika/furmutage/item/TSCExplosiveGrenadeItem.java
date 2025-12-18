package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.TSCExplosiveGrenadeProjectile;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Throwable item for the TSC explosive grenade.
 * On use, throws an explosive grenade projectile that explodes without breaking blocks
 * and spawns 5 shock grenades in random directions.
 */
public class TSCExplosiveGrenadeItem extends Item {

    public TSCExplosiveGrenadeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Reuse shock grenade throw sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.TSC_SHOCK_GRENADE_THROW.get(), SoundSource.NEUTRAL,
                0.7F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            TSCExplosiveGrenadeProjectile projectile = new TSCExplosiveGrenadeProjectile(level, player);
            projectile.setItem(itemstack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0F, 0.8F, 0.5F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        // Small cooldown so it can't be spammed too hard (e.g. 40 ticks = 2 seconds)
        player.getCooldowns().addCooldown(this, 40);

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}


