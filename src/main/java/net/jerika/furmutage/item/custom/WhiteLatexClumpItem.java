 package net.jerika.furmutage.item.custom;

import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhiteLatexClumpItem extends Item {
    public WhiteLatexClumpItem(Properties properties) {
        super(properties.food(Foods.DRIED_KELP));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(entity), (player, variant) -> {
            if (variant.getLatexType().isHostileTo(ChangedLatexTypes.WHITE_LATEX.get()))
                player.getFoodData().eat(Foods.DRIED_KELP.getNutrition(), Foods.DRIED_KELP.getSaturationModifier());
        });
        final var variant = ChangedLatexTypes.WHITE_LATEX.get().getTransfurVariant(TransfurCause.ATE_LATEX, level.random);
        ProcessTransfur.progressTransfur(entity, 11.0f, variant, TransfurContext.hazard(TransfurCause.ATE_LATEX));
        return super.finishUsingItem(itemStack, level, entity);
    }
}