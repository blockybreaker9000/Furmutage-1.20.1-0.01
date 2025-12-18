package net.jerika.furmutage.block;

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
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_TAINTED_WHITE_LOG = registerBlock("stripped_tainted_white_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> TAINTED_WHITE_PLANKS = registerBlock("tainted_white_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> TAINTED_WHITE_LEAF = registerBlock("tainted_white_leaf",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TAINTED_WHITE_SAPLING = registerBlock("tainted_white_sapling",
            () -> new SaplingBlock(new TaintedWhiteTreeGrower(), BlockBehaviour.Properties.copy(Blocks.MANGROVE_PROPAGULE)));


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
