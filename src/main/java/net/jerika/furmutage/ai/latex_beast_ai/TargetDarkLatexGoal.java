package net.jerika.furmutage.ai.latex_beast_ai;

import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import java.util.function.Predicate;

public class TargetDarkLatexGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private static final Predicate<LivingEntity> DARK_LATEX_PREDICATE = (entity) -> {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        // Check if entity is a DarkLatexEntity from Changed mod
        String className = entity.getClass().getName();
        return className.startsWith("net.ltxprogrammer.changed.entity.beast.DarkLatex");
    };

    public TargetDarkLatexGoal(LatexMutantFamilyEntity mob) {
        super(mob, LivingEntity.class, 10, true, false, DARK_LATEX_PREDICATE);
    }
}

