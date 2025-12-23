package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.team.TargetDarkLatexTeamGoal;
import net.jerika.furmutage.ai.team.TargetWhiteLatexGoal;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.team.DarkLatexTeam;
import net.jerika.furmutage.team.WhiteLatexTeam;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.WeakHashMap;

/**
 * Event handler that assigns teams to entities and makes them enemies.
 * - White Latex and Dark Latex are enemies
 * - Both teams are enemies of players
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexTeamEvents {
    // Track entities we've already processed to avoid duplicate goals
    private static final Set<LivingEntity> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Only process PathfinderMob entities (entities with AI)
        if (!(event.getEntity() instanceof PathfinderMob pathfinderMob)) {
            return;
        }
        
        // Skip if we've already processed this entity
        if (processedEntities.contains(pathfinderMob)) {
            return;
        }
        
        // Check if entity is White Latex
        boolean isWhiteLatex = WhiteLatexTeam.isWhiteLatex(pathfinderMob);
        
        // Check if entity is Dark Latex
        boolean isDarkLatex = DarkLatexTeam.isDarkLatex(pathfinderMob);
        
        // Only process entities that belong to one of the teams
        if (!isWhiteLatex && !isDarkLatex) {
            return;
        }
        
        // Add targeting goals based on team
        if (isWhiteLatex) {
            // White Latex targets Dark Latex and players
            pathfinderMob.targetSelector.addGoal(1, new TargetDarkLatexTeamGoal(pathfinderMob));
            pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
        } else if (isDarkLatex) {
            // Dark Latex targets White Latex and players
            pathfinderMob.targetSelector.addGoal(1, new TargetWhiteLatexGoal(pathfinderMob));
            pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
        }
        
        // Mark entity as processed
        processedEntities.add(pathfinderMob);
    }
}

