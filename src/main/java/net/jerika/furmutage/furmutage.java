package net.jerika.furmutage;

import com.mojang.logging.LogUtils;
import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.entity.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import java.util.Map;

import net.jerika.furmutage.entity.client.renderer.LatexBacteriaRenderer;
import net.jerika.furmutage.entity.client.renderer.LatexTenticleLimbsMutantRenderer;
import net.jerika.furmutage.entity.client.renderer.MuglingRenderer;
import net.jerika.furmutage.entity.client.renderer.MutantFamilyRenderer;
import net.jerika.furmutage.entity.client.renderer.TSCDroneBossRenderer;
import net.jerika.furmutage.entity.client.renderer.TSCDroneRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexChickenRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexCowRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexHorseRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexPigRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexRabbitRenderer;
import net.jerika.furmutage.entity.client.renderer.WhiteLatexSheepRenderer;
import net.jerika.furmutage.entity.client.renderer.WitheredLatexPuddingRenderer;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.jerika.furmutage.item.modcreativemodetabs;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
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

        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Register axe stripping interaction for tainted white log using reflection
            try {
                java.lang.reflect.Field strippablesField = AxeItem.class.getDeclaredField("STRIPPABLES");
                strippablesField.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<Block, Block> strippables = (Map<Block, Block>) strippablesField.get(null);
                strippables.put(ModBlocks.TAINTED_WHITE_LOG.get(), ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get());
            } catch (Exception e) {
                LOGGER.error("Failed to register stripping interaction for tainted white log", e);
            }
            
            // Note: Latex team config will be loaded via resource reload listener on server start
            LOGGER.info("[LatexTeams] Latex team system initialized - config will load on server start");
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.THUNDERIUM);
            event.accept(ModItems.ROSELIGHT);
        }
    }

    // Register the latex team config reload listener
    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new LatexTeamConfig());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Force load the config on server start using classpath (more reliable)
        LOGGER.info("[LatexTeams] Server starting - attempting to load config from classpath...");
        try {
            // Load directly from classpath: /data/furmutage/latex_teams.json
            java.io.InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("data/" + MOD_ID + "/latex_teams.json");
            
            if (inputStream != null) {
                try (inputStream) {
                    com.google.gson.JsonObject json = new com.google.gson.Gson()
                        .fromJson(new java.io.InputStreamReader(inputStream), com.google.gson.JsonObject.class);
                    LatexTeamConfig.forceLoadFromJson(json);
                    LOGGER.info("[LatexTeams] Config loaded successfully on server start from classpath!");
                }
            } else {
                LOGGER.error("[LatexTeams] Config file not found in classpath: data/{}/latex_teams.json", MOD_ID);
                // Try alternative: using resource manager
                try {
                    var resourceManager = event.getServer().getResourceManager();
                    ResourceLocation configLocation = new ResourceLocation(MOD_ID, "latex_teams");
                    var resource = resourceManager.getResource(configLocation);
                    if (resource.isPresent()) {
                        try (var altInputStream = resource.get().open()) {
                            com.google.gson.JsonObject json = new com.google.gson.Gson()
                                .fromJson(new java.io.InputStreamReader(altInputStream), com.google.gson.JsonObject.class);
                            LatexTeamConfig.forceLoadFromJson(json);
                            LOGGER.info("[LatexTeams] Config loaded successfully from resource manager!");
                        }
                    }
                } catch (Exception e2) {
                    LOGGER.error("[LatexTeams] Also failed to load from resource manager", e2);
                }
            }
        } catch (Exception e) {
            LOGGER.error("[LatexTeams] Error loading config on server start", e);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.MUGLING.get(), MuglingRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_MUTANT_FAMILY.get(), MutantFamilyRenderer::new);
            EntityRenderers.register(ModEntities.WITHERED_LATEX_PUDDING.get(), WitheredLatexPuddingRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_TENTICLE_LIMBS_MUTANT.get(), LatexTenticleLimbsMutantRenderer::new);
            EntityRenderers.register(ModEntities.TSC_DRONE.get(), TSCDroneRenderer::new);
            EntityRenderers.register(ModEntities.TSC_DRONE_BOSS.get(), TSCDroneBossRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_BACTERIA.get(), LatexBacteriaRenderer::new);
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

        }
    }
}
