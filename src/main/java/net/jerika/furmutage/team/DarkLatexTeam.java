package net.jerika.furmutage.team;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.minecraft.world.entity.LivingEntity;

/**
 * Utility class for identifying Dark Latex team members.
 * Dark Latex entities are enemies of White Latex and players.
 * Team assignments are configured in data/furmutage/latex_teams.json
 */
public class DarkLatexTeam {
    
    /**
     * Checks if an entity belongs to the Dark Latex team.
     * Uses configuration from latex_teams.json file.
     */
    public static boolean isDarkLatex(LivingEntity entity) {
        return LatexTeamConfig.isDarkLatex(entity);
    }
}

