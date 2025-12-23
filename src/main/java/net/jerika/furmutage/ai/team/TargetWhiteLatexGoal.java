package net.jerika.furmutage.ai.team;

import net.jerika.furmutage.team.DarkLatexTeam;
import net.jerika.furmutage.team.WhiteLatexTeam;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.function.Predicate;

/**
 * AI goal that makes entities target White Latex team members.
 * Only entities from Dark Latex team should use this goal.
 */
public class TargetWhiteLatexGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private static final Predicate<LivingEntity> WHITE_LATEX_PREDICATE = (entity) -> {
        // Only target white latex entities
        if (!WhiteLatexTeam.isWhiteLatex(entity)) {
            return false;
        }
        // Don't target if the attacker is also white latex (same team)
        return true;
    };

    public TargetWhiteLatexGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, false, WHITE_LATEX_PREDICATE);
    }
    
    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
        // Prevent same-team attacks
        if (WhiteLatexTeam.isWhiteLatex(this.mob)) {
            return false; // White latex shouldn't attack white latex
        }
        return super.canAttack(target, conditions);
    }
}

