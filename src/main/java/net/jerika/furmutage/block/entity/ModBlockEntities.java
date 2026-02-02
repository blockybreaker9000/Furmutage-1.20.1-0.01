package net.jerika.furmutage.block.entity;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, furmutage.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<EugenicsCraftingBlockEntity>> EUGENICS_CRAFTING_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("eugenics_crafting_block_entity",
                    () -> BlockEntityType.Builder.of(EugenicsCraftingBlockEntity::new,
                            net.jerika.furmutage.block.custom.ModBlocks.EUGENICS_CRAFTING_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<EugenicsSmelteryBlockEntity>> EUGENICS_SMELTERY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("eugenics_smeltery_block_entity",
                    () -> BlockEntityType.Builder.of(EugenicsSmelteryBlockEntity::new,
                            net.jerika.furmutage.block.custom.ModBlocks.EUGENICS_SMELTERY_OVEN.get()).build(null));
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
