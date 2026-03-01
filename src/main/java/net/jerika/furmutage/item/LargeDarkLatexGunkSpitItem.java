package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.projectiles.LargeDarkLatexGunkSpitProjectile;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LargeDarkLatexGunkSpitItem extends Item {

    public LargeDarkLatexGunkSpitItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.LARGE_DARK_LATEX_GUNK_SPIT.get(), SoundSource.NEUTRAL,
                2.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            LargeDarkLatexGunkSpitProjectile projectile = new LargeDarkLatexGunkSpitProjectile(level, player);
            projectile.setItem(stack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
