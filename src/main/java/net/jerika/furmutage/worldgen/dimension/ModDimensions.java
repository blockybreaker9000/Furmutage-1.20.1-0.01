package net.jerika.furmutage.worldgen.dimension;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModDimensions {
    public static final DeferredRegister<LevelStem> LEVEL_STEMS = 
            DeferredRegister.create(Registries.LEVEL_STEM, furmutage.MOD_ID);
    
    public static final ResourceKey<Level> WASTELAND_LEVEL_KEY = 
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(furmutage.MOD_ID, "wasteland"));
    
    public static final ResourceKey<DimensionType> WASTELAND_DIM_TYPE = 
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(furmutage.MOD_ID, "wasteland"));
    
    public static final ResourceKey<LevelStem> WASTELAND_STEM = 
            ResourceKey.create(Registries.LEVEL_STEM, new ResourceLocation(furmutage.MOD_ID, "wasteland"));
}
