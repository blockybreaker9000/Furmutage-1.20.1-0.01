package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.projectiles.TSCShockGrenadeProjectile;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TSCShockGrenadeItem extends Item {
    public TSCShockGrenadeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                ModSounds.TSC_SHOCK_GRENADE_THROW.get(), SoundSource.NEUTRAL, 
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        
        if (!level.isClientSide) {
            TSCShockGrenadeProjectile projectile = new TSCShockGrenadeProjectile(level, player);
            projectile.setItem(itemstack);
            //velocity
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 
                    0.0F, 0.8F, 0.5F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        
        //second cooldown
        player.getCooldowns().addCooldown(this, 50);

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}

