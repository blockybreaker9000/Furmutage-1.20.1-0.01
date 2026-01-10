package net.jerika.furmutage.event;

import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaStructureDisableEvents {
    
    private static final Set<ResourceLocation> VANILLA_STRUCTURE_SETS = new HashSet<>();
    
    static {
        // Add all vanilla structure sets
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:villages"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:desert_pyramids"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:igloos"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:jungle_temples"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:swamp_huts"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:pillager_outposts"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:strongholds"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:ocean_monuments"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:woodland_mansions"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:buried_treasures"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:mineshafts"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:ruined_portals"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:shipwrecks"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:ocean_ruins"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:nether_complexes"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:nether_fossils"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:end_cities"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:ancient_cities"));
        VANILLA_STRUCTURE_SETS.add(new ResourceLocation("minecraft:trail_ruins"));
    }
    
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!ModCommonConfig.DISABLE_VANILLA_STRUCTURES.get()) {
            return;
        }
        
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        
        // The datapack JSON files in minecraft namespace will override vanilla structure sets
        // with empty structure lists, effectively disabling them
        Registry<StructureSet> structureSetRegistry = serverLevel.registryAccess().registry(Registries.STRUCTURE_SET).orElse(null);
        if (structureSetRegistry != null) {
            int disabledCount = 0;
            for (ResourceLocation structureSetId : VANILLA_STRUCTURE_SETS) {
                ResourceKey<StructureSet> key = ResourceKey.create(Registries.STRUCTURE_SET, structureSetId);
                if (structureSetRegistry.containsKey(key)) {
                    StructureSet structureSet = structureSetRegistry.get(key);
                    if (structureSet != null && structureSet.structures().isEmpty()) {
                        disabledCount++;
                    }
                }
            }
            furmutage.LOGGER.info("Vanilla structures are disabled via config. Disabled " + disabledCount + " structure sets.");
        }
    }
    
    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(furmutage.MOD_ID)) {
            furmutage.LOGGER.info("Vanilla structures disabled: " + ModCommonConfig.DISABLE_VANILLA_STRUCTURES.get());
        }
    }
}
