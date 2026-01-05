package net.jerika.furmutage.item;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class modcreativemodetabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, furmutage.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FURMUTAGE_TAB = CREATIVE_MODE_TABS.register("furmutage",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.THUNDERIUM.get()))
                    .title(Component.translatable("creativetab.furmutage"))
                    .displayItems((itemDisplayParameters, output) -> {


                        output.accept(ModItems.PEALED_ORANGES.get());
                        output.accept(ModItems.ORANGE_PIE.get());
                        output.accept(ModItems.ORANGE_MUFFIN.get());
                        output.accept(ModItems.ORANGE_FOXYAS.get());
                        output.accept(ModItems.ORANGE_SWEETROLL.get());
                        output.accept(ModItems.ORANGE_ICECREAM.get());
                        output.accept(ModItems.ORANGE_SMOOTHY.get());
                        output.accept(ModItems.BOTTLED_ORANGE_JUICE.get());

                        output.accept(ModItems.THUNDERIUM.get());
                        output.accept(ModItems.THUNDERIUM_INGOT.get());
                        output.accept(ModItems.THUNDERIUM_NUGGET.get());
                        output.accept(ModItems.THUNDERIUM_RAW_NUGGET.get());



                        output.accept(ModItems.ROSELIGHT.get());
                        output.accept(ModItems.RAW_ROSELIGHT_NUGGET.get());
                        output.accept(ModItems.ROSELIGHT_NUGGET.get());
                        output.accept(ModItems.ROSELIGHT_INGOT.get());



                        output.accept(ModItems.THUNDERIUM_HELMET.get());
                        output.accept(ModItems.THUNDERIUM_CHESTPLATE.get());
                        output.accept(ModItems.THUNDERIUM_LEGGINGS.get());
                        output.accept(ModItems.THUNDERIUM_BOOTS.get());

                        output.accept(ModItems.TSC_FIBER.get());
                        output.accept(ModItems.RUBBER.get());
                        output.accept(ModItems.TSC_GLOVES.get());
                        output.accept(ModItems.TSC_RUBBER_SHOE_PADS.get());
                        output.accept(ModItems.TSC_WIRE.get());
                        output.accept(ModItems.TSC_ADVANCED_TECH.get());
                        output.accept(ModItems.TSC_BRACE_PAD.get());
                        output.accept(ModItems.TSC_ELECT_PAD.get());
                        output.accept(ModItems.TSC_ARMOR_PAD.get());
                        output.accept(ModItems.TSC_SHOULDER_PAD.get());
                        output.accept(ModItems.TSC_PADDING.get());
                        output.accept(ModItems.TSC_GEARS.get());
                        output.accept(ModItems.ROSELIGHT_BATTERY.get());
                        output.accept(ModItems.FAT_TSC_BATTERY.get());
                        output.accept(ModItems.TSC_BATTERY.get());
                        output.accept(ModItems.TSC_METAL_DETECTOR.get());
                        output.accept(ModItems.TSC_SHOCK_GRENADE.get());
                        output.accept(ModItems.TSC_EXPLOSIVE_GRENADE.get());
                        output.accept(ModItems.THUNDERIUMTAZER.get());

                        output.accept(ModItems.MUGLING_SPAWN_EGG.get());
                        output.accept(ModItems.LATEX_MUTANT_FAMILY_SPAWN_EGG.get());
                        output.accept(ModItems.LATEX_TENTICLE_LIMBS_MUTANT_SPAWN_EGG.get());
                        output.accept(ModItems.WITHERED_LATEX_PUDDING_SPAWN_EGG.get());
                        output.accept(ModItems.LATEX_BOMBER_MUTANT_SPAWN_EGG.get());
                        output.accept(ModItems.TSC_DRONE_SPAWN_EGG.get());
                        output.accept(ModItems.TSC_DRONE_BOSS_SPAWN_EGG.get());
                        output.accept(ModItems.LATEX_BACTERIA_SPAWN_EGG.get());
                        output.accept(ModItems.DARKLATEXBOTTLED.get());
                        output.accept(ModItems.WHITELATEXBOTTLED.get());

                        output.accept(ModItems.THUNDERIUM_SABER.get());
                        output.accept(ModItems.ROSELIGHT_PIC.get());
                        output.accept(ModItems.ROSELIGHT_AXE.get());
                        output.accept(ModItems.ROSELIGHT_HOE.get());
                        output.accept(ModItems.ROSELIGHT_SHOVEL.get());
                        output.accept(ModItems.THUNDERIUMBATTLEAXE.get());
                        output.accept(ModItems.THUNDERIUMDOOMSABER.get());
                        output.accept(ModItems.THUNDERIUMBATTLEMACE.get());
                        output.accept(ModItems.THUNDERIUMSHANKKNIFE.get());
                        output.accept(ModItems.THUNDERIUMSPEAR.get());
                        output.accept(ModItems.THUNDERIUMINFUSEDMACHETE.get());
                        output.accept(ModItems.THUNDERIUMLONGTAZER.get());
                        output.accept(ModItems.THUNDERIUMWARHAMMER.get());
                        output.accept(ModItems.THUNDERIUMHAMMER.get());
                        output.accept(ModItems.THUNDERIUMBARBBAT.get());
                        output.accept(ModItems.THUNDERIUMTHORNBAT.get());
                        output.accept(ModItems.THUNDERIUMSPIKEBAT.get());
                        output.accept(ModItems.THUNDERIUMSCYTHE.get());

                        output.accept(ModBlocks.RAW_THUNDERIUM.get());
                        output.accept(ModBlocks.RAW_ROSELIGHT.get());
                        output.accept(ModBlocks.ROSELIGHT_ORE.get());
                        output.accept(ModBlocks.THUNDERIUM_ORE.get());
                        output.accept(ModBlocks.CITY_PORTAL_BLOCK.get());
                        output.accept(ModBlocks.TAINTED_WHITE_GRASS.get());
                        output.accept(ModBlocks.TAINTED_DARK_GRASS.get());
                        output.accept(ModBlocks.ROSELIGHT_BLOCK.get());
                        output.accept(ModBlocks.THUNDERIUM_BLOCK.get());



                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
