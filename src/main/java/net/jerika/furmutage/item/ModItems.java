package net.jerika.furmutage.item;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.custom.*;
import net.jerika.furmutage.item.DarkLatexBottleItem;
import net.jerika.furmutage.item.TSCDroneBulletItem;
import net.jerika.furmutage.item.TSCShockGrenadeItem;
import net.jerika.furmutage.item.custom.diamond.*;
import net.jerika.furmutage.item.custom.iron.*;
import net.jerika.furmutage.item.custom.post_netherite.ThunderiumDoomSaber;
import net.jerika.furmutage.item.custom.post_netherite.ThunderiumScythe;
import net.jerika.furmutage.item.custom.post_netherite.ThunderiumWarHammer;
import net.jerika.furmutage.item.custom.stone.THUNDERIUMTAZERITEM;
import net.jerika.furmutage.item.custom.stone.ThunderiumBarbBat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, furmutage.MOD_ID);

    public static final RegistryObject<Item> THUNDERIUM = ITEMS.register("thunderium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_INGOT = ITEMS.register("thunderium_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_RAW_NUGGET = ITEMS.register("thunderium_raw_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_NUGGET = ITEMS.register("thunderium_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSELIGHT = ITEMS.register("roselight",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSELIGHT_INGOT = ITEMS.register("roselight_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSELIGHT_NUGGET = ITEMS.register("roselight_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ROSELIGHT_NUGGET = ITEMS.register("raw_roselight_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_FIBER = ITEMS.register("tsc_fiber",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_PLASTIC_WASTE_CLUMP = ITEMS.register("tsc_plastic_waste_clump",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_GLOVES = ITEMS.register("tsc_gloves",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_RUBBER_SHOE_PADS = ITEMS.register("tsc_rubber_shoe_pads",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_WIRE = ITEMS.register("tsc_wire",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_ADVANCED_TECH = ITEMS.register("tsc_advanced_tech",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_BRACE_PAD = ITEMS.register("tsc_brace_pad",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_ELECT_PAD = ITEMS.register("tsc_elect_pad",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_ARMOR_PAD = ITEMS.register("tsc_armor_pad",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_SHOULDER_PAD = ITEMS.register("tsc_shoulder_pad",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_PADDING = ITEMS.register("tsc_padding",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_GEARS = ITEMS.register("tsc_gears",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSELIGHT_BATTERY = ITEMS.register("roselight_battery",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FAT_TSC_BATTERY = ITEMS.register("fat_tsc_battery",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_BATTERY = ITEMS.register("tsc_battery",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSC_SHOCK_GRENADE = ITEMS.register("tsc_shock_grenade",
            () -> new TSCShockGrenadeItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> TSC_EXPLOSIVE_GRENADE = ITEMS.register("tsc_explosive_grenade",
            () -> new TSCExplosiveGrenadeItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> TSC_DRONE_BULLET = ITEMS.register("tsc_drone_bullet",
            () -> new TSCDroneBulletItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DARKLATEXBOTTLED = ITEMS.register("darklatex_bottle",
            () -> new DarkLatexBottleItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> WHITELATEXBOTTLED = ITEMS.register("whitelatex_bottle",
            () -> new WhiteLatexBottleItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> TSC_METAL_DETECTOR = ITEMS.register("tsc_metal_detector",
            () -> new TSCMetalDetectorItem(new Item.Properties().durability(100)));
    public static final RegistryObject<Item> PEALED_ORANGES = ITEMS.register("pealed_oranges",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.5f)
                    .build())));
    public static final RegistryObject<Item> BOTTLED_ORANGE_JUICE = ITEMS.register("bottled_orange_juice",
            () -> new BottledOrangeJuiceItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .build())));
    public static final RegistryObject<Item> ORANGE_PIE = ITEMS.register("orange_pie",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationMod(2.5f)
                    .build())));
    public static final RegistryObject<Item> ORANGE_MUFFIN = ITEMS.register("orange_muffin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(1.5f)
                    .build())));
    public static final RegistryObject<Item> ORANGE_FOXYAS = ITEMS.register("orange_foxyas_cookie",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(2.5f)
                    .build())));
    public static final RegistryObject<Item> ORANGE_SWEETROLL = ITEMS.register("orange_sweetroll",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(2.5f)
                    .build())));
    public static final RegistryObject<Item> ORANGE_ICECREAM = ITEMS.register("orange_icecream",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(2.5f)
                    .build())));
    public static final RegistryObject<Item> ORANGE_SMOOTHY = ITEMS.register("orange_smoothy",
            () -> new OrangeSmoothy(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(2.5f)
                    .build())));


    public static final RegistryObject<Item> THUNDERIUMTAZER = ITEMS.register("thunderium_tazer", THUNDERIUMTAZERITEM::new);
    public static final RegistryObject<Item> THUNDERIUMBATTLEAXE = ITEMS.register("thunderium_battle_axe", ThunderiumBattleAxe::new);
    public static final RegistryObject<Item> THUNDERIUMBATTLEMACE = ITEMS.register("thunderium_mace", ThunderiumBattleMace::new);
    public static final RegistryObject<Item> THUNDERIUMSHANKKNIFE = ITEMS.register("thunderium_shank_knife", ThunderiumShankKnife::new);
    public static final RegistryObject<Item> THUNDERIUMINFUSEDMACHETE = ITEMS.register("thunderium_infused_machete", ThunderiumInfusedMachete::new);
    public static final RegistryObject<Item> THUNDERIUMSPEAR = ITEMS.register("thunderium_spear", ThunderiumSpear::new);
    public static final RegistryObject<Item> THUNDERIUMDOOMSABER = ITEMS.register("thunderium_doom_saber", ThunderiumDoomSaber::new);
    public static final RegistryObject<Item> THUNDERIUM_SABER = ITEMS.register("thunderium_saber", ThunderiumSaber::new);
    public static final RegistryObject<Item> THUNDERIUMLONGTAZER = ITEMS.register("thunderium_long_tazer", ThunderiumLongTazer::new);
    public static final RegistryObject<Item> THUNDERIUMWARHAMMER = ITEMS.register("thunderium_war_hammer", ThunderiumWarHammer::new);
    public static final RegistryObject<Item> THUNDERIUMSCYTHE = ITEMS.register("thunderium_scythe", ThunderiumScythe::new);
    public static final RegistryObject<Item> THUNDERIUMHAMMER = ITEMS.register("thunderium_hammer", ThunderiumHammer::new);
    public static final RegistryObject<Item> THUNDERIUMSPIKEBAT = ITEMS.register("thunderium_spike_bat", ThunderiumSpikeBat::new);
    public static final RegistryObject<Item> THUNDERIUMBARBBAT = ITEMS.register("thunderium_barb_bat", ThunderiumBarbBat::new);
    public static final RegistryObject<Item> THUNDERIUMTHORNBAT = ITEMS.register("thunderium_thorn_bat", ThunderiumThornBat::new);


    public static final RegistryObject<Item> ROSELIGHT_PIC = ITEMS.register("roselight_pic",
            () -> new PickaxeItem(ModToolTiers.ROSELIGHT, -1, -2, new Item.Properties().durability(800)));
    public static final RegistryObject<Item> ROSELIGHT_GLASS_SHARD = ITEMS.register("roselight_glass_shard",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_GLASS_SHARD = ITEMS.register("thunderium_glass_shard",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSELIGHT_AXE = ITEMS.register("roselight_axe",
            () -> new AxeItem(ModToolTiers.ROSELIGHT, 9, -2, new Item.Properties().durability(800)));
    public static final RegistryObject<Item> ROSELIGHT_SHOVEL = ITEMS.register("roselight_shovel",
            () -> new net.jerika.furmutage.item.custom.RoselightChainMiningShovel());
    public static final RegistryObject<Item> ROSELIGHT_HOE = ITEMS.register("roselight_hoe",
            () -> new HoeItem(ModToolTiers.ROSELIGHT, -1, -2, new Item.Properties().durability(800)));


    public static final RegistryObject<Item> THUNDERIUM_HELMET = ITEMS.register("thunderium_helmet",
            () -> new ArmorItem(TSCModArmorMaterials.THUNDERIUM_INGOT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_CHESTPLATE = ITEMS.register("thunderium_chestplate",
            () -> new ArmorItem(TSCModArmorMaterials.THUNDERIUM_INGOT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_LEGGINGS = ITEMS.register("thunderium_leggings",
            () -> new ArmorItem(TSCModArmorMaterials.THUNDERIUM_INGOT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> THUNDERIUM_BOOTS = ITEMS.register("thunderium_boots",
            () -> new ArmorItem(TSCModArmorMaterials.THUNDERIUM_INGOT, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> MUGLING_SPAWN_EGG = ITEMS.register("mugling_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MUGLING, 0x4D72, 0xc5d,
                    new Item.Properties()));
    public static final RegistryObject<Item> LATEX_MUTANT_FAMILY_SPAWN_EGG = ITEMS.register("latex_mutant_family_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LATEX_MUTANT_FAMILY, 0xDDEAEA, 0xFF0000,
                    new Item.Properties()));
    public static final RegistryObject<Item> LATEX_TENTICLE_LIMBS_MUTANT_SPAWN_EGG = ITEMS.register("latex_tenticle_limbs_mutant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LATEX_TENTICLE_LIMBS_MUTANT, 0xDDEAEA, 0xFF0000,
                    new Item.Properties()));
    public static final RegistryObject<Item> WITHERED_LATEX_PUDDING_SPAWN_EGG = ITEMS.register("withered_latex_pudding_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WITHERED_LATEX_PUDDING, 0xDDEAEA, 0xFF0000,
                    new Item.Properties()));
    public static final RegistryObject<Item> TSC_DRONE_SPAWN_EGG = ITEMS.register("tsc_drone_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TSC_DRONE, 0xDDEAEA, 0xFF0000,
                    new Item.Properties()));
    public static final RegistryObject<Item> TSC_DRONE_BOSS_SPAWN_EGG = ITEMS.register("tsc_drone_boss_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TSC_DRONE_BOSS, 0x222222, 0x00FFFF,
                    new Item.Properties()));
    public static final RegistryObject<Item> LATEX_BACTERIA_SPAWN_EGG = ITEMS.register("latex_bacteria_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LATEX_BACTERIA, 0xDDEAEA, 0xDDEAEA,
                    new Item.Properties()));
    public static final RegistryObject<Item> LATEX_BOMBER_MUTANT_SPAWN_EGG = ITEMS.register("latex_bomber_mutant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LATEX_MUTANT_BOMBER, 0xDDEAEA, 0xDDEAEA,
                    new Item.Properties()));
    public static final RegistryObject<Item> GIANT_PURE_WHITE_SPAWN_EGG = ITEMS.register("giant_pure_white_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GIANT_PURE_WHITE_LATEX, 0xDDEAEA, 0xDDEAEA,
                    new Item.Properties()));
    public static final RegistryObject<Item> EXO_MUTANT_SPAWN_EGG = ITEMS.register("exo_mutant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LATEX_EXO_MUTANT, 0xDDEAEA, 0xDDEAEA,
                    new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_RED_ROSE_APPLE = ITEMS.register("tainted_red_rose_apple",
            () -> new TaintedRedRoseAppleItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.3f)
                    .build())));
    public static final RegistryObject<Item> PHAGE_BLUEBERRY = ITEMS.register("phage_blueberry",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.2f)
                    .build())));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
