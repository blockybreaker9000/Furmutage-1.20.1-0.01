package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.jerika.furmutage.worldgen.tree.TaintedWhiteTreeGrower;
import net.jerika.furmutage.worldgen.tree.TaintedDarkTreeGrower;
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
    public static final RegistryObject<Block> PLATINUM_ORE_BLOCK = registerBlock("platinum_ore_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));
    public static final RegistryObject<Block> THUNDERIUM_BLOCK = registerBlock("thunderium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ROSELIGHT_BLOCK = registerBlock("roselight_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> TAINTED_WHITE_LOG = registerBlock("tainted_white_log",
            () -> new TaintedWhiteLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_TAINTED_WHITE_LOG = registerBlock("stripped_tainted_white_log",
            () -> new TaintedWhiteLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_PLANKS = registerBlock("tainted_white_planks",
            () -> new TaintedWhitePlanksBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_SLAB = registerBlock("tainted_white_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_STAIRS = registerBlock("tainted_white_stairs",
            () -> new StairBlock(() -> TAINTED_WHITE_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_FENCE = registerBlock("tainted_white_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_FENCE_GATE = registerBlock("tainted_white_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.WoodType.OAK));
    public static final RegistryObject<Block> TAINTED_WHITE_DOOR = registerBlock("tainted_white_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_WHITE_TRAPDOOR = registerBlock("tainted_white_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_WHITE_BUTTON = registerBlock("tainted_white_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> TAINTED_WHITE_PRESSURE_PLATE = registerBlock("tainted_white_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_WHITE_LADDER = registerBlock("tainted_white_ladder",
            () -> new LadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_LEAF = registerBlock("tainted_white_leaf",
            () -> new TaintedWhiteLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TAINTED_WHITE_SAPLING = registerBlock("tainted_white_sapling",
            () -> new TaintedWhiteSaplingBlock(new TaintedWhiteTreeGrower(), BlockBehaviour.Properties.copy(Blocks.MANGROVE_PROPAGULE)));
    public static final RegistryObject<Block> TAINTED_WHITE_GRASS = registerBlock("tainted_white_grass",
            () -> new TaintedWhiteGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).sound(SoundType.MUD).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_DIRT = registerBlock("tainted_white_dirt",
            () -> new TaintedWhiteDirtBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_WHITE_SAND = registerBlock("tainted_white_sand",
            () -> new TaintedWhiteSandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks().requiresCorrectToolForDrops()));
    
    // Tainted Dark Blocks
    public static final RegistryObject<Block> TAINTED_DARK_LOG = registerBlock("tainted_dark_log",
            () -> new TaintedDarkLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_TAINTED_DARK_LOG = registerBlock("stripped_tainted_dark_log",
            () -> new TaintedDarkLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_PLANKS = registerBlock("tainted_dark_planks",
            () -> new TaintedDarkPlanksBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_SLAB = registerBlock("tainted_dark_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_STAIRS = registerBlock("tainted_dark_stairs",
            () -> new StairBlock(() -> TAINTED_DARK_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_FENCE = registerBlock("tainted_dark_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_FENCE_GATE = registerBlock("tainted_dark_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.WoodType.OAK));
    public static final RegistryObject<Block> TAINTED_DARK_DOOR = registerBlock("tainted_dark_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_DARK_TRAPDOOR = registerBlock("tainted_dark_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_DARK_BUTTON = registerBlock("tainted_dark_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> TAINTED_DARK_PRESSURE_PLATE = registerBlock("tainted_dark_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
    public static final RegistryObject<Block> TAINTED_DARK_LADDER = registerBlock("tainted_dark_ladder",
            () -> new LadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_LEAF = registerBlock("tainted_dark_leaf",
            () -> new TaintedDarkLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TAINTED_DARK_GRASS = registerBlock("tainted_dark_grass",
            () -> new TaintedDarkGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).sound(SoundType.MUD).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_DIRT = registerBlock("tainted_dark_dirt",
            () -> new TaintedDarkDirtBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_SAND = registerBlock("tainted_dark_sand",
            () -> new TaintedDarkSandBlock(0x1a1a1a, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TAINTED_DARK_TALL_GRASS = registerBlock("tainted_dark_tall_grass",
            () -> new TaintedDarkTallGrassBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_DARK_GRASS_FOLIAGE = registerBlock("tainted_dark_grass_foliage",
            () -> new TaintedDarkGrassFoliageBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().instabreak().sound(SoundType.GRASS).randomTicks()));
    public static final RegistryObject<Block> TAINTED_DARK_ROSELIGHT_FLOWER = registerBlock("tainted_dark_roselight_flower",
            () -> new TaintedDarkRoselightFlowerBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_DARK_CRYSTAL_BLUE_FLOWER = registerBlock("tainted_dark_crystal_blue_flower",
            () -> new TaintedDarkCrystalBlueFlowerBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_DARK_SAPLING = registerBlock("tainted_dark_sapling",
            () -> new TaintedDarkSaplingBlock(new TaintedDarkTreeGrower(), BlockBehaviour.Properties.copy(Blocks.MANGROVE_PROPAGULE)));
    public static final RegistryObject<Block> DARK_LYNCHING_VINE = registerBlock("dark_lynching_vine",
            () -> new DarkLynchingVineBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES).randomTicks()));
    public static final RegistryObject<Block> DARK_LYNCHING_VINE_PLANT = BLOCKS.register("dark_lynching_vine_plant",
            () -> new DarkLynchingVinePlantBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES_PLANT).randomTicks()));
    public static final RegistryObject<Block> TAINTED_WHITE_VINE = registerBlock("tainted_white_vine",
            () -> new TaintedWhiteVineBlock(BlockBehaviour.Properties.copy(Blocks.VINE).randomTicks()));
    public static final RegistryObject<Block> TAINTED_DARK_VINE = registerBlock("tainted_dark_vine",
            () -> new TaintedDarkVineBlock(BlockBehaviour.Properties.copy(Blocks.VINE).randomTicks()));
    public static final RegistryObject<Block> ROSELIGHT_CRYSTAL_SHARDS = registerBlock("roselight_crystal_shards",
            () -> new RoselightCrystalShardBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion().instabreak().sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> THUNDERIUM_CRYSTAL_SHARDS = registerBlock("thunderium_crystal_shards",
            () -> new ThunderiumCrystalShardBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion().instabreak().sound(SoundType.AMETHYST)));
    
    public static final RegistryObject<Block> TAINTED_WHITE_GRASS_FOLIAGE = registerBlock("tainted_white_grass_foliage",
            () -> new TaintedWhiteGrassFoliageBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> TAINTED_WHITE_TALL_GRASS = registerBlock("tainted_white_tall_grass",
            () -> new TaintedWhiteTallGrassBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS).noOcclusion().instabreak().sound(SoundType.GRASS).randomTicks()));
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
    
    // Brushable ocean/river plastic waste blocks
    public static final RegistryObject<Block> PLASTIC_WASTE_SAND = registerBlock("plastic_waste_sand",
            () -> new PlasticWasteSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final RegistryObject<Block> PLASTIC_WASTE_GRAVEL = registerBlock("plastic_waste_gravel",
            () -> new PlasticWasteGravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    
    public static final RegistryObject<Block> EUGENICS_CRAFTING_BLOCK = registerBlock("eugenics_crafting_block",
            () -> new EugenicsCraftingBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)
                    .strength(2.5f)));

    public static final RegistryObject<Block> EUGENICS_SMELTERY_OVEN = registerBlock("eugenics_smeltery_oven",
            () -> new EugenicsSmelteryBlock(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)
                    .strength(3.5f)));
    
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
