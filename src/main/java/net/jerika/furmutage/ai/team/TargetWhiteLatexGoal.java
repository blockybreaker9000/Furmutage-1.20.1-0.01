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
 * AI goal that makes entities target White Latex team members.
 * Only entities from Dark Latex team should use this goal.
 */
public class TargetWhiteLatexGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Predicate<LivingEntity> WHITE_LATEX_PREDICATE = (entity) -> {
        // Only target white latex entities
        boolean isWhite = WhiteLatexTeam.isWhiteLatex(entity);
        if (isWhite) {
            LOGGER.debug("[LatexTeams] TargetWhiteLatexGoal: Found White Latex target: {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
        }
        return isWhite;
    };

    public TargetWhiteLatexGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, true, WHITE_LATEX_PREDICATE);
    }
    
    @Override
    protected void findTarget() {
        super.findTarget();
        if (this.target != null) {
            LOGGER.debug("[LatexTeams] TargetWhiteLatexGoal: Found target {} for mob {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(this.target.getType()),
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(this.mob.getType()));
        }
    }
    
    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
        // Prevent same-team attacks
        if (WhiteLatexTeam.isWhiteLatex(this.mob)) {
            LOGGER.debug("[LatexTeams] TargetWhiteLatexGoal: Blocked attack - mob is White Latex");
            return false; // White latex shouldn't attack white latex
        }
        boolean canAttack = super.canAttack(target, conditions);
        if (canAttack) {
            LOGGER.debug("[LatexTeams] TargetWhiteLatexGoal: Can attack target {}", 
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(target.getType()));
        }
        return canAttack;
    }
}

