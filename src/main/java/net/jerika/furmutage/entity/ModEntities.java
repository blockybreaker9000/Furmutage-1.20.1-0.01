package net.jerika.furmutage.entity;

import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, furmutage.MOD_ID);

    public static final RegistryObject<EntityType<MuglingEntity>> MUGLING =
            ENTITY_TYPES.register("mugling", () -> EntityType.Builder.of(MuglingEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f).build("mugling"));
    public static final RegistryObject<EntityType<LatexMutantFamilyEntity>> LATEX_MUTANT_FAMILY =
                ENTITY_TYPES.register("latex_mutant_family", () -> EntityType.Builder.of(LatexMutantFamilyEntity::new, MobCategory.CREATURE)
            .sized(0.5f, 0.5f).build("latex_mutant_family"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
