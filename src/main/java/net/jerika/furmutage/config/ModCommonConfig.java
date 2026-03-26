package net.jerika.furmutage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.BooleanValue DISABLE_VANILLA_STRUCTURES;
    
    /** When true: Furmutage ore progression is active. When false (default): vanilla tool rules. */
    public static final ForgeConfigSpec.BooleanValue ENABLE_ORE_PROGRESSION;
    
    /** When true (default): Changed entities can see through up to 5 blocks. When false: normal line of sight. */
    public static final ForgeConfigSpec.BooleanValue ENABLE_CHANGED_SEE_THROUGH_WALLS;
    /**
     * When true (default: false): apply both long-range targeting and swim follow-range tweaks
     * for Changed entities.
     *
     * - Long-range targeting: force Changed entities to have at least 30 FOLLOW_RANGE.
     * - Swim follow-range override: apply swim follow-range FOLLOW_RANGE adjustment (detection behavior).
     */
    public static final ForgeConfigSpec.BooleanValue ENABLE_CHANGED_LONG_RANGE_TARGETING_AND_SWIM_FOLLOW_RANGE;
    
    // Mob spawn control options
    public static final ForgeConfigSpec.BooleanValue ENABLE_ZOMBIE_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKELETON_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CREEPER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SPIDER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PILLAGER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ENDERMAN_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STRAY_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ZOMBIFIED_PIGLIN_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PIGLIN_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_VINDICATOR_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_EVOKER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RAVAGER_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GUARDIAN_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ELDER_GUARDIAN_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HUSK_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_WITCH_SPAWN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_DROWNED_SPAWN;
    
    static {
        BUILDER.push("World Generation Config");
        
        DISABLE_VANILLA_STRUCTURES = BUILDER
                .comment("When true: vanilla structure generation is disabled (villages, strongholds, monuments, etc.).",
                        "When false (DEFAULT): all vanilla structures are ENABLED and generate normally.",
                        "Pack makers can set this to true only if they want to disable vanilla structures.")
                .define("disableVanillaStructures", false);
        
        BUILDER.pop();
        
        BUILDER.push("Ore Progression");
        ENABLE_ORE_PROGRESSION = BUILDER
                .comment("When true: Furmutage ore progression is active (iron needs TSC Emergency pickaxe, gold needs Roselight pickaxe, diamond needs gold pickaxe).",
                        "When false (DEFAULT): vanilla tool rules apply and no ore hint overlay is shown.")
                .define("enableOreProgression", false);
        BUILDER.pop();
        
        BUILDER.push("Changed / Latex Entities");
        ENABLE_CHANGED_SEE_THROUGH_WALLS = BUILDER
                .comment("When true: Changed/latex entities can see targets through up to 5 blocks of walls.",
                        "When false (DEFAULT): they use normal line of sight and cannot see through solid blocks.")
                .define("enableChangedSeeThroughWalls", false);
        ENABLE_CHANGED_LONG_RANGE_TARGETING_AND_SWIM_FOLLOW_RANGE = BUILDER
                .comment("When true: enable both long-range targeting and swim follow-range adjustments for Changed entities.",
                        "When false (DEFAULT): no forced follow-range tuning is applied.")
                .define("enableChangedLongRangeTargetingAndSwimFollowRange", false);
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
        
        ENABLE_ENDERMAN_SPAWN = BUILDER
                .comment("Set to false to prevent endermen from spawning naturally")
                .define("enableEndermanSpawn", false);
        
        ENABLE_STRAY_SPAWN = BUILDER
                .comment("Set to false to prevent strays from spawning naturally")
                .define("enableStraySpawn", false);
        
        ENABLE_ZOMBIFIED_PIGLIN_SPAWN = BUILDER
                .comment("Set to false to prevent zombified piglins (zombie pigmen) from spawning naturally")
                .define("enableZombifiedPiglinSpawn", false);
        
        ENABLE_PIGLIN_SPAWN = BUILDER
                .comment("Set to false to prevent piglins and piglin brutes from spawning naturally")
                .define("enablePiglinSpawn", false);
        
        ENABLE_VINDICATOR_SPAWN = BUILDER
                .comment("Set to false to prevent vindicators from spawning naturally")
                .define("enableVindicatorSpawn", false);
        
        ENABLE_EVOKER_SPAWN = BUILDER
                .comment("Set to false to prevent evokers from spawning naturally")
                .define("enableEvokerSpawn", false);
        
        ENABLE_RAVAGER_SPAWN = BUILDER
                .comment("Set to false to prevent ravagers from spawning naturally")
                .define("enableRavagerSpawn", false);
        
        ENABLE_GUARDIAN_SPAWN = BUILDER
                .comment("Set to false to prevent guardians from spawning naturally")
                .define("enableGuardianSpawn", false);
        
        ENABLE_ELDER_GUARDIAN_SPAWN = BUILDER
                .comment("Set to false to prevent elder guardians from spawning naturally")
                .define("enableElderGuardianSpawn", false);
        
        ENABLE_HUSK_SPAWN = BUILDER
                .comment("Set to false to prevent husks from spawning naturally")
                .define("enableHuskSpawn", false);
        
        ENABLE_WITCH_SPAWN = BUILDER
                .comment("Set to false to prevent witches from spawning naturally")
                .define("enableWitchSpawn", false);
        
        ENABLE_DROWNED_SPAWN = BUILDER
                .comment("Set to false to prevent drowned from spawning naturally")
                .define("enableDrownedSpawn", false);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
