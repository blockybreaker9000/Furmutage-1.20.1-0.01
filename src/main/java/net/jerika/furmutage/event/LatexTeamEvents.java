package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.team.TargetDarkLatexTeamGoal;
import net.jerika.furmutage.ai.team.TargetWhiteLatexGoal;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.team.DarkLatexTeam;
import net.jerika.furmutage.team.WhiteLatexTeam;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
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
    private static final Logger LOGGER = LogUtils.getLogger();
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
        
        // Only check entities that are in the config file
        if (!net.jerika.furmutage.config.LatexTeamConfig.isEntityInConfig(pathfinderMob)) {
            return; // Entity not in config, skip processing
        }
        
        // Check if entity is White Latex
        boolean isWhiteLatex = WhiteLatexTeam.isWhiteLatex(pathfinderMob);
        
        // Check if entity is Dark Latex
        boolean isDarkLatex = DarkLatexTeam.isDarkLatex(pathfinderMob);
        
        // Debug: Log entity info for troubleshooting
        String entityTypeId = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(pathfinderMob.getType()).toString();
        String className = pathfinderMob.getClass().getName();
        LOGGER.info("[LatexTeams] Processing entity from config: {} (class: {}) - White: {}, Dark: {}", 
            entityTypeId, className, isWhiteLatex, isDarkLatex);
        
        // Only process entities that belong to one of the teams
        if (!isWhiteLatex && !isDarkLatex) {
            LOGGER.warn("[LatexTeams] Entity {} matched config but is not assigned to any team!", entityTypeId);
            return;
        }
        
        // Add targeting goals based on team
        // Use priority 0 for highest priority (will override most other goals)
        if (isWhiteLatex) {
            // White Latex targets Dark Latex and players
            pathfinderMob.targetSelector.addGoal(0, new TargetDarkLatexTeamGoal(pathfinderMob));
            pathfinderMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
            // Add HurtByTargetGoal to retaliate when attacked
            pathfinderMob.targetSelector.addGoal(2, new HurtByTargetGoal(pathfinderMob));
            
            // Ensure entity has a melee attack goal if it doesn't already have one
            boolean hasMeleeAttack = pathfinderMob.goalSelector.getAvailableGoals().stream()
                .anyMatch(goal -> goal.getGoal() instanceof MeleeAttackGoal);
            if (!hasMeleeAttack) {
                pathfinderMob.goalSelector.addGoal(2, new MeleeAttackGoal(pathfinderMob, 1.0D, true));
            }
            
            LOGGER.info("[LatexTeams] Assigned White Latex team to entity: {} ({})", 
                entityTypeId, className);
        } else if (isDarkLatex) {
            // Dark Latex targets White Latex and players
            pathfinderMob.targetSelector.addGoal(0, new TargetWhiteLatexGoal(pathfinderMob));
            pathfinderMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
            // Add HurtByTargetGoal to retaliate when attacked
            pathfinderMob.targetSelector.addGoal(2, new HurtByTargetGoal(pathfinderMob));
            
            // Ensure entity has a melee attack goal if it doesn't already have one
            boolean hasMeleeAttack = pathfinderMob.goalSelector.getAvailableGoals().stream()
                .anyMatch(goal -> goal.getGoal() instanceof MeleeAttackGoal);
            if (!hasMeleeAttack) {
                pathfinderMob.goalSelector.addGoal(2, new MeleeAttackGoal(pathfinderMob, 1.0D, true));
            }
            
            LOGGER.info("[LatexTeams] Assigned Dark Latex team to entity: {} ({})", 
                entityTypeId, className);
        }
        
        // Mark entity as processed
        processedEntities.add(pathfinderMob);
    }
}

