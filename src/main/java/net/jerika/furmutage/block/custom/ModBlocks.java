package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.jerika.furmutage.worldgen.tree.TaintedWhiteTreeGrower;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, furmutage.MOD_ID);

    public static final RegistryObject<Block> RAW_THUNDERIUM = registerBlock("raw_thunderium",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> RAW_ROSELIGHT = registerBlock("raw_roselight",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> ROSELIGHT_ORE = registerBlock("roselight_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(5, 7)));
    public static final RegistryObject<Block> THUNDERIUM_ORE = registerBlock("thunderium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(5, 7)));
    public static final RegistryObject<Block> CITY_PORTAL_BLOCK = registerBlock("city_portal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).sound(SoundType.SCULK_CATALYST)));
    public static final RegistryObject<Block> THUNDERIUM_BLOCK = registerBlock("thunderium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ROSELIGHT_BLOCK = registerBlock("roselight_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> TAINTED_WHITE_LOG = registerBlock("tainted_white_log",
            () -> new TaintedWhiteLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).randomTicks()));
    public static final RegistryObject<Block> STRIPPED_TAINTED_WHITE_LOG = registerBlock("stripped_tainted_white_log",
            () -> new TaintedWhiteLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).randomTicks()));
    public static final RegistryObject<Block> TAINTED_WHITE_PLANKS = registerBlock("tainted_white_planks",
            () -> new TaintedWhitePlanksBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).randomTicks()));
    public static final RegistryObject<Block> TAINTED_WHITE_LEAF = registerBlock("tainted_white_leaf",
            () -> new TaintedWhiteLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TAINTED_WHITE_SAPLING = registerBlock("tainted_white_sapling",
            () -> new SaplingBlock(new TaintedWhiteTreeGrower(), BlockBehaviour.Properties.copy(Blocks.MANGROVE_PROPAGULE)));
    public static final RegistryObject<Block> TAINTED_WHITE_GRASS = registerBlock("tainted_white_grass",
            () -> new TaintedWhiteGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistryObject<Block> TAINTED_WHITE_DIRT = registerBlock("tainted_white_dirt",
            () -> new TaintedWhiteDirtBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).randomTicks()));
    public static final RegistryObject<Block> TAINTED_WHITE_SAND = registerBlock("tainted_white_sand",
            () -> new TaintedWhiteSandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks()));
    public static final RegistryObject<Block> TAINTED_WHITE_GRASS_FOLIAGE = registerBlock("tainted_white_grass_foliage",
            () -> new TaintedWhiteGrassFoliageBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_TALL_GRASS = registerBlock("tainted_white_tall_grass",
            () -> new TaintedWhiteTallGrassBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_SPOTTED_MUSHROOM = registerBlock("tainted_white_spotted_mushroom",
            () -> new TaintedWhiteSpottedMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_DRIP_MUSHROOM = registerBlock("tainted_white_drip_mushroom",
            () -> new TaintedWhiteDripMushroomBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_ROSELIGHT_FLOWER = registerBlock("tainted_white_roselight_flower",
            () -> new TaintedWhiteRoselightFlowerBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_CRYSTAL_BLUE_FLOWER = registerBlock("tainted_white_crystal_blue_flower",
            () -> new TaintedWhiteCrystalBlueFlowerBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TSC_PLASTIC_WASTE_ORE = registerBlock("tsc_plastic_waste_ore",
            () -> new TSCPlasticWasteOreBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(3f).requiresCorrectToolForDrops()));
    
    // Tainted White Reed - declare as mutable to break circular dependency, then assign
    public static RegistryObject<Block> TAINTED_WHITE_REED;
    public static RegistryObject<Block> TAINTED_WHITE_REED_PLANT;
    
    static {
        // Register body block first (without item)
        TAINTED_WHITE_REED_PLANT = BLOCKS.register("tainted_white_reed_plant",
            () -> new TaintedWhiteReedPlantBlock(
                BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE).noOcclusion().instabreak().sound(SoundType.GRASS),
                () -> (GrowingPlantHeadBlock) TAINTED_WHITE_REED.get()
            ));
        
        // Register head block (with item)
        TAINTED_WHITE_REED = registerBlock("tainted_white_reed",
            () -> new TaintedWhiteReedBlock(
                BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE).noOcclusion().instabreak().sound(SoundType.GRASS).randomTicks(),
                () -> TAINTED_WHITE_REED_PLANT.get()
            ));
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
