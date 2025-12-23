package net.jerika.furmutage.ai.team;

import net.jerika.furmutage.team.DarkLatexTeam;
import net.jerika.furmutage.team.WhiteLatexTeam;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.function.Predicate;

/**
 * AI goal that makes entities target Dark Latex team members.
 * Only entities from White Latex team should use this goal.
 */
public class TargetDarkLatexTeamGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private static final Predicate<LivingEntity> DARK_LATEX_PREDICATE = (entity) -> {
        // Only target dark latex entities
        if (!DarkLatexTeam.isDarkLatex(entity)) {
            return false;
        }
        // Don't target if the attacker is also dark latex (same team)
        return true;
    };

    public TargetDarkLatexTeamGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, false, DARK_LATEX_PREDICATE);
    }
    
    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
        // Prevent same-team attacks
        if (DarkLatexTeam.isDarkLatex(this.mob)) {
            return false; // Dark latex shouldn't attack dark latex
        }
        return super.canAttack(target, conditions);
    }
}

