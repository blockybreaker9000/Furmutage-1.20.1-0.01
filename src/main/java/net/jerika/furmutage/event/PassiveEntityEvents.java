package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.DistantStareAtPlayerGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PassiveEntityEvents {
    // Track entities we've already modified to avoid duplicate goals
    private static final Set<LivingEntity> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if entity is an Animal (passive entity)
        if (event.getEntity() instanceof Animal animal) {
            // Skip if we've already processed this entity
            if (processedEntities.contains(animal)) {
                return;
            }
            
            // Add distant stare goal to all animals
            // This gives them a very small chance to stare at players from far away
            animal.goalSelector.addGoal(7, new DistantStareAtPlayerGoal(animal));
            
            processedEntities.add(animal);
        }
    }
}

