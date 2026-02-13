package net.jerika.furmutage.event;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.jerika.furmutage.furmutage;

/**
 * Vanilla mob spawn control is now DATA-DRIVEN.
 * Use the JSON files in data/furmutage/forge/biome_modifier/vanilla_mob_control/
 * to control which vanilla mobs spawn. Delete a no_&lt;mob&gt;.json file to allow that mob.
 * See README.txt in that folder for details.
 * This event is kept but disabled - no more spawn-then-discard.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaMobSpawnControlEvents {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Spawn control handled by forge:remove_spawns in vanilla_mob_control/
    }
}
