package net.jerika.furmutage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.BooleanValue DISABLE_VANILLA_STRUCTURES;
    
    // Mob spawn control options
    public static final ForgeConfigSpec.BooleanValue ENABLE_ZOMBIE_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKELETON_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CREEPER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SPIDER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PILLAGER_SPAWN;
    
    static {
        BUILDER.push("World Generation Config");
        
        DISABLE_VANILLA_STRUCTURES = BUILDER
                .comment("Set to true to disable all vanilla structures (villages, strongholds, dungeons, etc.)")
                .define("disableVanillaStructures", false);
        
        BUILDER.pop();
        
        BUILDER.push("Mob Spawn Control");
        
        ENABLE_ZOMBIE_SPAWN = BUILDER
                .comment("Set to false to prevent zombies from spawning naturally")
                .define("enableZombieSpawn", false);
        
        ENABLE_SKELETON_SPAWN = BUILDER
                .comment("Set to false to prevent skeletons from spawning naturally")
                .define("enableSkeletonSpawn", false);
        
        ENABLE_CREEPER_SPAWN = BUILDER
                .comment("Set to false to prevent creepers from spawning naturally")
                .define("enableCreeperSpawn", false);
        
        ENABLE_SPIDER_SPAWN = BUILDER
                .comment("Set to false to prevent spiders from spawning naturally")
                .define("enableSpiderSpawn", false);
        
        ENABLE_PILLAGER_SPAWN = BUILDER
                .comment("Set to false to prevent pillagers (including patrols and outposts) from spawning naturally")
                .define("enablePillagerSpawn", false);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
