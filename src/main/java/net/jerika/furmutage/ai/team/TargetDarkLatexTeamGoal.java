package net.jerika.furmutage.ai.team;

import com.mojang.logging.LogUtils;
import net.jerika.furmutage.team.DarkLatexTeam;
import net.jerika.furmutage.team.WhiteLatexTeam;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.slf4j.Logger;

import java.util.function.Predicate;

/**
 * AI goal that makes entities target Dark Latex team members.
 * Only entities from White Latex team should use this goal.
 */
public class TargetDarkLatexTeamGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Predicate<LivingEntity> DARK_LATEX_PREDICATE = (entity) -> {
        // Only target dark latex entities
        boolean isDark = DarkLatexTeam.isDarkLatex(entity);
        if (isDark) {
            LOGGER.debug("[LatexTeams] TargetDarkLatexTeamGoal: Found Dark Latex target: {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
        }
        return isDark;
    };

    public TargetDarkLatexTeamGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, true, DARK_LATEX_PREDICATE);
    }
    
    @Override
    protected void findTarget() {
        super.findTarget();
        if (this.target != null) {
            LOGGER.debug("[LatexTeams] TargetDarkLatexTeamGoal: Found target {} for mob {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(this.target.getType()),
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(this.mob.getType()));
        }
    }
    
    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
        // Prevent same-team attacks
        if (DarkLatexTeam.isDarkLatex(this.mob)) {
            LOGGER.debug("[LatexTeams] TargetDarkLatexTeamGoal: Blocked attack - mob is Dark Latex");
            return false; // Dark latex shouldn't attack dark latex
        }
        boolean canAttack = super.canAttack(target, conditions);
        if (canAttack) {
            LOGGER.debug("[LatexTeams] TargetDarkLatexTeamGoal: Can attack target {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(target.getType()));
        }
        return canAttack;
    }
}

