package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.projectiles.TSCPipeBombProjectile;
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
 * Throwable TSC Pipe Bomb. On impact spawns an effect cloud for 30 seconds
 * that applies Slowness I and Weakness I for 10 seconds to entities inside.
 */
public class TSCPipeBombItem extends Item {

    public TSCPipeBombItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.TSC_SHOCK_GRENADE_THROW.get(), SoundSource.NEUTRAL,
                0.7F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            TSCPipeBombProjectile projectile = new TSCPipeBombProjectile(level, player);
            projectile.setItem(itemstack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0F, 0.8F, 0.5F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        player.getCooldowns().addCooldown(this, 20);

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
