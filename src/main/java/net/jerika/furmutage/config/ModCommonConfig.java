package net.jerika.furmutage.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.BooleanValue DISABLE_VANILLA_STRUCTURES;
    
    // White latex spawn config - entities in this list spawn on surface at night and in caves (simple Monster rules)
    public static final ForgeConfigSpec.BooleanValue ENABLE_WHITE_LATEX_SPAWN;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITE_LATEX_SPAWN_ENTITIES;
    
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
    
    static {
        BUILDER.push("World Generation Config");
        
        DISABLE_VANILLA_STRUCTURES = BUILDER
                .comment("Set to true to disable all vanilla structures (villages, strongholds, dungeons, etc.)")
                .define("disableVanillaStructures", false);
        
        BUILDER.pop();
        
        BUILDER.push("White Latex Spawn");
        
        ENABLE_WHITE_LATEX_SPAWN = BUILDER
                .comment("Enable simple spawning for Changed white latex entities. When enabled, entities listed below spawn on surface at night and in caves (same rules as zombies/skeletons).")
                .define("enableWhiteLatexSpawn", true);
        
        List<String> defaultWhiteLatexSpawnEntities = Arrays.asList(
                "changed:pure_white_latex_wolf",
                "changed:latex_mutant_bloodcell_wolf",
                "changed:latex_snake",
                "changed:headless_knight",
                "changed:white_latex_knight_fusion",
                "changed:white_latex_wolf_female",
                "changed:white_latex_wolf_male",
                "changed:white_latex_knight",
                "changed:white_latex_centaur",
                "changed:milk_pudding",
                "changed:pure_white_latex_wolf_pup"
        );
        
        WHITE_LATEX_SPAWN_ENTITIES = BUILDER
                .comment("Entity IDs that use simple spawn rules (surface at night, caves). Add/remove to control which white latex mobs spawn naturally.")
                .defineList("whiteLatexSpawnEntities", defaultWhiteLatexSpawnEntities, obj -> obj instanceof String);
        
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
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
