package net.jerika.furmutage;

import com.mojang.logging.LogUtils;
import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.block.entity.ModBlockEntities;
import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.menu.ModMenuTypes;
import net.jerika.furmutage.menu.client.EugenicsCraftingScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import java.util.Map;

import net.jerika.furmutage.entity.client.renderer.LatexBacteriaRenderer;
import net.jerika.furmutage.entity.client.renderer.LatexExoMutantRenderer;
import net.jerika.furmutage.entity.client.renderer.LatexMutantBomberRenderer;
import net.jerika.furmutage.entity.client.renderer.LatexTenticleLimbsMutantRenderer;
import net.jerika.furmutage.entity.client.renderer.MuglingRenderer;
import net.jerika.furmutage.entity.client.renderer.MutantFamilyRenderer;
import net.jerika.furmutage.entity.client.renderer.TSCDroneBossRenderer;
import net.jerika.furmutage.entity.client.renderer.TSCDroneRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexChickenRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexCowRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexHorseRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexPigRenderer;
import net.jerika.furmutage.entity.client.renderer.GiantPureWhiteLatexRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexRabbitRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexSheepRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexSquidRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexLlamaRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexDolphinRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexGoatRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexCowRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexPigRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexChickenRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexSheepRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexRabbitRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexHorseRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexSquidRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexLlamaRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexDolphinRenderer;
import net.jerika.furmutage.entity.client.renderer.DarkLatexGoatRenderer;
import net.jerika.furmutage.entity.client.renderer.DeepSlateLatexSquidDogRenderer;
import net.jerika.furmutage.entity.client.renderer.WitheredLatexPuddingRenderer;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.jerika.furmutage.item.modcreativemodetabs;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(furmutage.MOD_ID)
public class furmutage {
    public static final String MOD_ID = "furmutage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public furmutage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modcreativemodetabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);
        
        // Register dimension components
        net.jerika.furmutage.worldgen.dimension.ModBiomes.BIOMES.register(modEventBus);
        net.jerika.furmutage.worldgen.dimension.WastelandBiomeSource.BIOME_SOURCES.register(modEventBus);
        net.jerika.furmutage.worldgen.dimension.WastelandChunkGenerator.CHUNK_GENERATORS.register(modEventBus);
        net.jerika.furmutage.worldgen.dimension.ModDimensions.LEVEL_STEMS.register(modEventBus);
        
        // Register structures
        net.jerika.furmutage.worldgen.structure.LostCityStructure.STRUCTURE_TYPES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC, "furmutage-common.toml");

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Register axe stripping interaction for tainted logs using reflection.
            // Find field by type (Map<Block, Block>) so it works in both dev and production JAR (obfuscated names).
            try {
                for (java.lang.reflect.Field field : AxeItem.class.getDeclaredFields()) {
                    if (Map.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        Object map = field.get(null);
                        if (map instanceof Map<?, ?> m) {
                            @SuppressWarnings("unchecked")
                            Map<Block, Block> strippables = (Map<Block, Block>) map;
                            strippables.put(ModBlocks.TAINTED_WHITE_LOG.get(), ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get());
                            strippables.put(ModBlocks.TAINTED_DARK_LOG.get(), ModBlocks.STRIPPED_TAINTED_DARK_LOG.get());
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to register stripping interaction for tainted logs", e);
            }
            
            // Register spawn placement for DeepSlateLatexSquidDog
            net.minecraft.world.entity.SpawnPlacements.register(
                ModEntities.DEEPSLATE_LATEX_SQUID_DOG.get(),
                net.minecraft.world.entity.SpawnPlacements.Type.IN_WATER,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.jerika.furmutage.entity.custom.DeepSlateLatexSquidDog::checkDeepSlateSpawnRules
            );
            
            // Register spawn placement for DeepCaveHypnoCat
            net.minecraft.world.entity.SpawnPlacements.register(
                ModEntities.DEEP_CAVE_HYPNO_CAT.get(),
                net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.jerika.furmutage.entity.custom.DeepCaveHypnoCat::checkDeepCaveSpawnRules
            );
            
            // Register spawn placement for LatexBloodWormMutant
            net.minecraft.world.entity.SpawnPlacements.register(
                ModEntities.LATEX_BLOOD_WORM_MUTANT.get(),
                net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.jerika.furmutage.entity.custom.LatexBloodWormMutant::checkLatexBloodWormMutantSpawnRules
            );

            // Register spawn placement for Loose Behemoth Hand (deep underground, deepslate level or below)
            net.minecraft.world.entity.SpawnPlacements.register(
                ModEntities.LOOSE_BEHEMOTH_HAND.get(),
                net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.jerika.furmutage.entity.custom.LooseBehemothHand::checkLooseBehemothHandSpawnRules
            );
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.THUNDERIUM);
            event.accept(ModItems.ROSELIGHT);
        }
    }



    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.EUGENICS_CRAFTING_MENU.get(), EugenicsCraftingScreen::new);
            });
            
            EntityRenderers.register(ModEntities.MUGLING.get(), MuglingRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_MUTANT_FAMILY.get(), MutantFamilyRenderer::new);
            EntityRenderers.register(ModEntities.WITHERED_LATEX_PUDDING.get(), WitheredLatexPuddingRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_TENTICLE_LIMBS_MUTANT.get(), LatexTenticleLimbsMutantRenderer::new);
            EntityRenderers.register(ModEntities.TSC_DRONE.get(), TSCDroneRenderer::new);
            EntityRenderers.register(ModEntities.TSC_DRONE_BOSS.get(), TSCDroneBossRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_BACTERIA.get(), LatexBacteriaRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_MUTANT_BOMBER.get(), LatexMutantBomberRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_EXO_MUTANT.get(), LatexExoMutantRenderer::new);
            EntityRenderers.register(ModEntities.TSC_DRONE_BULLET_PROJECTILE.get(),
                    (context) -> new ThrownItemRenderer<>(context, 0.5f, true));
            EntityRenderers.register(ModEntities.DARK_LATEX_BOTTLE_PROJECTILE.get(), 
                    (context) -> new ThrownItemRenderer<>(context, 1.0f, true));
            EntityRenderers.register(ModEntities.WHITE_LATEX_BOTTLE_PROJECTILE.get(),
                    (context) -> new ThrownItemRenderer<>(context, 1.0f, true));
            EntityRenderers.register(ModEntities.TSC_SHOCK_GRENADE_PROJECTILE.get(), 
                    (context) -> new ThrownItemRenderer<>(context, 1.0f, true));
            EntityRenderers.register(ModEntities.TSC_EXPLOSIVE_GRENADE_PROJECTILE.get(),
                    (context) -> new ThrownItemRenderer<>(context, 1.0f, true));
            
            // Register renderers for White Latex infected passive mobs (using custom renderers with custom textures)
            EntityRenderers.register(ModEntities.WHITE_LATEX_COW.get(), WhiteLatexCowRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_PIG.get(), WhiteLatexPigRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_CHICKEN.get(), WhiteLatexChickenRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_SHEEP.get(), WhiteLatexSheepRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_RABBIT.get(), WhiteLatexRabbitRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_HORSE.get(), WhiteLatexHorseRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_SQUID.get(), WhiteLatexSquidRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_LLAMA.get(), WhiteLatexLlamaRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_DOLPHIN.get(), WhiteLatexDolphinRenderer::new);
            EntityRenderers.register(ModEntities.WHITE_LATEX_GOAT.get(), WhiteLatexGoatRenderer::new);
            
            // Register renderers for Dark Latex infected passive mobs
            EntityRenderers.register(ModEntities.DARK_LATEX_COW.get(), DarkLatexCowRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_PIG.get(), DarkLatexPigRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_CHICKEN.get(), DarkLatexChickenRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_SHEEP.get(), DarkLatexSheepRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_RABBIT.get(), DarkLatexRabbitRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_HORSE.get(), DarkLatexHorseRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_SQUID.get(), DarkLatexSquidRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_LLAMA.get(), DarkLatexLlamaRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_DOLPHIN.get(), DarkLatexDolphinRenderer::new);
            EntityRenderers.register(ModEntities.DARK_LATEX_GOAT.get(), DarkLatexGoatRenderer::new);
            
            EntityRenderers.register(ModEntities.GIANT_PURE_WHITE_LATEX.get(), GiantPureWhiteLatexRenderer::new);
            
            EntityRenderers.register(ModEntities.DEEPSLATE_LATEX_SQUID_DOG.get(), DeepSlateLatexSquidDogRenderer::new);
            
            EntityRenderers.register(ModEntities.DEEP_CAVE_HYPNO_CAT.get(), net.jerika.furmutage.entity.client.renderer.DeepCaveHypnoCatRenderer::new);
            
            EntityRenderers.register(ModEntities.LATEX_BLOOD_WORM_MUTANT.get(), net.jerika.furmutage.entity.client.renderer.LatexBloodWormMutantRenderer::new);
            EntityRenderers.register(ModEntities.LOOSE_BEHEMOTH_HAND.get(), net.jerika.furmutage.entity.client.renderer.LooseBehemothHandRenderer::new);

            // Register render types for vine blocks (cutout rendering)
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TAINTED_WHITE_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TAINTED_DARK_VINE.get(), RenderType.cutout());

        }
    }
}
