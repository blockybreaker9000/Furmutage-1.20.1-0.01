package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MutantFamilySpawnEvents {
    // Track entities we've already processed to avoid duplicate spawns
    private static final Set<LatexMutantFamilyEntity> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if entity is a LatexMutantFamilyEntity
        if (event.getEntity() instanceof LatexMutantFamilyEntity mutantFamily) {
            // Skip if we've already processed this entity
            if (processedEntities.contains(mutantFamily)) {
                return;
            }
            
            // Only spawn companions on natural spawns (not from spawn eggs or commands)
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                spawnPureWhiteLatexCompanions(mutantFamily, serverLevel);
                processedEntities.add(mutantFamily);
            }
        }
    }
    
    private static void spawnPureWhiteLatexCompanions(LatexMutantFamilyEntity mutantFamily, ServerLevel level) {
        // Try to find the Pure White Latex entity type from Changed mod
        EntityType<?> pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                net.minecraft.resources.ResourceLocation.tryParse("changed:pure_white_latex_wolf")
        );
        
        // Fallback: try alternative names
        if (pureWhiteLatexType == null) {
            pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                    net.minecraft.resources.ResourceLocation.tryParse("changed:pure_white_latex_pup")
            );
        }
        
        if (pureWhiteLatexType == null) {
            // Try to find any entity with "pure_white" in the name
            for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                String name = entityType.getDescriptionId().toLowerCase();
                if (name.contains("pure_white") || name.contains("purewhite")) {
                    pureWhiteLatexType = entityType;
                    break;
                }
            }
        }
        
        if (pureWhiteLatexType != null && pureWhiteLatexType.create(level) instanceof PathfinderMob) {
            // Spawn 3 Pure White Latex entities around the Mutant Family
            for (int i = 0; i < 3; i++) {
                double angle = (i * 120.0) * Math.PI / 180.0; // 120 degrees apart
                double offsetX = Math.cos(angle) * 2.0; // 2 blocks away
                double offsetZ = Math.sin(angle) * 2.0;
                
                double spawnX = mutantFamily.getX() + offsetX;
                double spawnY = mutantFamily.getY();
                double spawnZ = mutantFamily.getZ() + offsetZ;
                
                PathfinderMob companion = (PathfinderMob) pureWhiteLatexType.create(level);
                if (companion != null) {
                    companion.moveTo(spawnX, spawnY, spawnZ, mutantFamily.getYRot(), 0.0F);
                    companion.finalizeSpawn(level, level.getCurrentDifficultyAt(companion.blockPosition()), 
                            MobSpawnType.EVENT, null, null);
                    level.addFreshEntity(companion);
                }
            }
        }
    }
}

