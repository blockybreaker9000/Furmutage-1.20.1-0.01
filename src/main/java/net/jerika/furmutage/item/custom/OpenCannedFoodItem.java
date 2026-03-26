package net.jerika.furmutage.item.custom;

import net.jerika.furmutage.item.ModItems;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * Consumable open canned food. Uses drink animation and honey drink sound (like orange juice),
 * so no food-eating particles. Gives one empty can when finished (if not creative).
 */
public class OpenCannedFoodItem extends Item {

    // How much opened canned food reduces the Changed "transfur bar" progress.
    // This value is in ProcessTransfur's transfurProgress units, not percent.
    private static final float TRANSFUR_PROGRESS_REDUCTION = 0.3F;

    public OpenCannedFoodItem(Properties properties) {
        this(properties, 0.0F, 0, 0);
    }

    // Optional downside: apply vanilla Hunger effect with a fixed chance.
    private final float hungerChance;
    private final int hungerDurationTicks;
    private final int hungerAmplifier;

    public OpenCannedFoodItem(Properties properties, float hungerChance, int hungerDurationTicks, int hungerAmplifier) {
        super(properties);
        this.hungerChance = hungerChance;
        this.hungerDurationTicks = hungerDurationTicks;
        this.hungerAmplifier = hungerAmplifier;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && !level.isClientSide) {
            // Make opened canned foods slightly reduce exhaustion instead of increasing it.
            player.getFoodData().addExhaustion(-0.3F);

            // Reduce the Changed mod transfur bar progress so it doesn't only fill up.
            float current = ProcessTransfur.getPlayerTransfurProgress(player);
            float max = (float) ProcessTransfur.getEntityTransfurTolerance(player);

            // Reduce whenever there's progress so we still drain near/at the tolerance cap.
            if (current > 0.0F) {
                float next = current - TRANSFUR_PROGRESS_REDUCTION;
                next = Math.max(0.0F, next);
                next = Math.min(max, next);

                // Avoid unnecessary sync/network packet spam.
                if (next < current) {
                    ProcessTransfur.setPlayerTransfurProgress(player, next);
                }
            }

            // Cat/Dog opened canned food downside: random Hunger debuff.
            if (hungerChance > 0.0F && level.random.nextFloat() < hungerChance) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hungerDurationTicks, hungerAmplifier));
            }
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack emptyCan = new ItemStack(ModItems.EMPTY_CAN.get());
            if (!player.getInventory().add(emptyCan)) {
                player.drop(emptyCan, false);
            }
        }
        return result;
    }
}
