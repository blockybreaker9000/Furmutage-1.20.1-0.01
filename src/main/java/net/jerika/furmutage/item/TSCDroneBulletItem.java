package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.TSCDroneBulletProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TSCDroneBulletItem extends Item {
    public TSCDroneBulletItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        
        if (!level.isClientSide) {
            TSCDroneBulletProjectile bullet = new TSCDroneBulletProjectile(level, player);
            bullet.setItem(itemstack);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 
                    0.0F, 1.5F, 1.0F);
            level.addFreshEntity(bullet);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}

