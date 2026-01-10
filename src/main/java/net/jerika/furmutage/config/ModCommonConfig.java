package net.jerika.furmutage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.BooleanValue DISABLE_VANILLA_STRUCTURES;
    
    static {
        BUILDER.push("World Generation Config");
        
        DISABLE_VANILLA_STRUCTURES = BUILDER
                .comment("Set to true to disable all vanilla structures (villages, strongholds, dungeons, etc.)")
                .define("disableVanillaStructures", false);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
