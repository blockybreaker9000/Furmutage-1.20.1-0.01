package net.jerika.furmutage;

import com.mojang.logging.LogUtils;
import net.jerika.furmutage.block.ModBlocks;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.entity.client.renderer.MuglingRenderer;
import net.jerika.furmutage.entity.client.renderer.MutantFamilyRenderer;
import net.jerika.furmutage.item.ModItems;
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
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.THUNDERIUM);
            event.accept(ModItems.ROSELIGHT);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.MUGLING.get(), MuglingRenderer::new);
            EntityRenderers.register(ModEntities.LATEX_MUTANT_FAMILY.get(), MutantFamilyRenderer::new);

        }
    }
}
