package net.jerika.furmutage.entity;

import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.entity.custom.LatexTenticleLimbsMutantEntity;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.jerika.furmutage.entity.DarkLatexBottleProjectile;
import net.jerika.furmutage.entity.TSCDroneBulletProjectile;
import net.jerika.furmutage.entity.TSCShockGrenadeProjectile;
import net.jerika.furmutage.entity.custom.WitheredLatexPuddingEntity;
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
            .sized(2.4f, 3.1f).build("latex_mutant_family"));

    public static final RegistryObject<EntityType<WitheredLatexPuddingEntity>> WITHERED_LATEX_PUDDING =
            ENTITY_TYPES.register("withered_latex_pudding", () -> EntityType.Builder.of(WitheredLatexPuddingEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 2.0f).build("withered_latex_pudding"));

    public static final RegistryObject<EntityType<LatexTenticleLimbsMutantEntity>> LATEX_TENTICLE_LIMBS_MUTANT =
            ENTITY_TYPES.register("latex_tenticle_limbs_mutant", () -> EntityType.Builder.of(LatexTenticleLimbsMutantEntity::new, MobCategory.CREATURE)
                    .sized(2.4f, 2.5f).build("latex_tenticle_limbs_mutant"));

    public static final RegistryObject<EntityType<TSCDroneEntity>> TSC_DRONE =
            ENTITY_TYPES.register("tsc_drone", () -> EntityType.Builder.of(TSCDroneEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 0.8f).build("tsc_drone"));
    
    public static final RegistryObject<EntityType<DarkLatexBottleProjectile>> DARK_LATEX_BOTTLE_PROJECTILE =
            ENTITY_TYPES.register("dark_latex_bottle_projectile", () -> EntityType.Builder.<DarkLatexBottleProjectile>of(
                    DarkLatexBottleProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("dark_latex_bottle_projectile"));
    public static final RegistryObject<EntityType<WhiteLatexBottleProjectile>> WHITE_LATEX_BOTTLE_PROJECTILE =
            ENTITY_TYPES.register("white_latex_bottle_projectile", () -> EntityType.Builder.<WhiteLatexBottleProjectile>of(
                            WhiteLatexBottleProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("white_latex_bottle_projectile"));
    
    public static final RegistryObject<EntityType<TSCShockGrenadeProjectile>> TSC_SHOCK_GRENADE_PROJECTILE =
            ENTITY_TYPES.register("tsc_shock_grenade_projectile", () -> EntityType.Builder.<TSCShockGrenadeProjectile>of(
                    TSCShockGrenadeProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("tsc_shock_grenade_projectile"));
    
    public static final RegistryObject<EntityType<TSCDroneBulletProjectile>> TSC_DRONE_BULLET_PROJECTILE =
            ENTITY_TYPES.register("tsc_drone_bullet_projectile", () -> EntityType.Builder.<TSCDroneBulletProjectile>of(
                    TSCDroneBulletProjectile::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("tsc_drone_bullet_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
