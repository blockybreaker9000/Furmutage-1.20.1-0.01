package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.latex_beast_ai.WolfLatexGunkSpitGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

/**
 * Adds a chance-based ranged latex gunk spit to specific Changed wolf mobs.
 * - changed:pure_white_latex_wolf -> white latex gunk spit
 * - changed:dark_latex_wolf_male -> dark latex gunk spit
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexWolfSpitEvents {

    private static final Set<PathfinderMob> SPIT_GOAL_ADDED = java.util.Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof PathfinderMob mob)) {
            return;
        }

        ResourceLocation typeId = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType());
        if (typeId == null) {
            return;
        }

        String id = typeId.toString();
        WolfLatexGunkSpitGoal.Variant variant;
        if ("changed:pure_white_latex_wolf".equals(id)) {
            variant = WolfLatexGunkSpitGoal.Variant.WHITE;
        } else if ("changed:dark_latex_wolf_male".equals(id)) {
            variant = WolfLatexGunkSpitGoal.Variant.DARK;
        } else {
            return;
        }

        if (SPIT_GOAL_ADDED.contains(mob)) {
            return;
        }

        // Priority: after pathfinding tweaks, before default combat AI.
        mob.goalSelector.addGoal(4, new WolfLatexGunkSpitGoal(mob, variant));
        SPIT_GOAL_ADDED.add(mob);
    }
}

