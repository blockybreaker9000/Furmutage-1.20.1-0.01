package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.custom.DarkLatexChargerMutantEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DarkLatexChargerMutantSpawnEvents {
    private static final Set<DarkLatexChargerMutantEntity> processedEntities =
            java.util.Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        processedEntities.clear();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        processedEntities.clear();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof DarkLatexChargerMutantEntity charger) {
            if (processedEntities.contains(charger)) {
                return;
            }
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                spawnDarkLatexWolves(charger, serverLevel);
                processedEntities.add(charger);
            }
        }
    }

    private static void spawnDarkLatexWolves(DarkLatexChargerMutantEntity charger, ServerLevel level) {
        EntityType<?> maleType = ForgeRegistries.ENTITY_TYPES.getValue(
                ResourceLocation.tryParse("changed:dark_latex_wolf_male"));
        EntityType<?> femaleType = ForgeRegistries.ENTITY_TYPES.getValue(
                ResourceLocation.tryParse("changed:dark_latex_wolf_female"));

        for (int i = 0; i < 3; i++) {
            spawnWolf(charger, level, maleType);
        }
        for (int i = 0; i < 3; i++) {
            spawnWolf(charger, level, femaleType);
        }
    }

    private static void spawnWolf(DarkLatexChargerMutantEntity charger, ServerLevel level, EntityType<?> wolfType) {
        if (wolfType == null || !(wolfType.create(level) instanceof PathfinderMob)) {
            return;
        }
        double angle = (charger.getRandom().nextDouble() * 360.0) * Math.PI / 180.0;
        double offsetX = Math.cos(angle) * 2.0;
        double offsetZ = Math.sin(angle) * 2.0;

        PathfinderMob wolf = (PathfinderMob) wolfType.create(level);
        if (wolf != null) {
            wolf.moveTo(charger.getX() + offsetX, charger.getY(), charger.getZ() + offsetZ,
                    charger.getYRot(), 0.0F);
            try {
                wolf.finalizeSpawn(level, level.getCurrentDifficultyAt(wolf.blockPosition()),
                        MobSpawnType.EVENT, null, null);
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null && e.getMessage().contains("attack_knockback")) {
                    furmutage.LOGGER.debug("Ignoring attack_knockback attribute error: {}", e.getMessage());
                } else {
                    throw e;
                }
            }
            level.addFreshEntity(wolf);
        }
    }
}
