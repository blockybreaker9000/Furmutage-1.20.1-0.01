package net.jerika.furmutage.team;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.minecraft.world.entity.LivingEntity;

/**
 * Utility class for identifying White Latex team members.
 * White Latex entities are enemies of Dark Latex and players.
 * Team assignments are configured in data/furmutage/latex_teams.json
 */
public class WhiteLatexTeam {
    
    /**
     * Checks if an entity belongs to the White Latex team.
     * Uses configuration from latex_teams.json file.
     */
    public static boolean isWhiteLatex(LivingEntity entity) {
        return LatexTeamConfig.isWhiteLatex(entity);
    }
}

