package net.jerika.furmutage.entity;

import net.jerika.furmutage.entity.custom.LatexBacteriaEntity;
import net.jerika.furmutage.entity.custom.LatexExoMutantEntity;
import net.jerika.furmutage.entity.custom.LatexMutantBomberEntity;
import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.entity.custom.LatexTenticleLimbsMutantEntity;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.jerika.furmutage.entity.custom.TSCDroneBossEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexCowEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexPigEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity;
import net.jerika.furmutage.entity.custom.DeepLatexSquidEntity;
import net.jerika.furmutage.entity.projectiles.*;
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

    public static final RegistryObject<EntityType<TSCDroneBossEntity>> TSC_DRONE_BOSS =
            ENTITY_TYPES.register("tsc_drone_boss", () -> EntityType.Builder.of(TSCDroneBossEntity::new, MobCategory.MONSTER)
                    .sized(1.6f, 1.6f).build("tsc_drone_boss"));

    public static final RegistryObject<EntityType<LatexBacteriaEntity>> LATEX_BACTERIA =
            ENTITY_TYPES.register("latex_bacteria", () -> EntityType.Builder.of(LatexBacteriaEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 0.8f).build("latex_bacteria"));

    public static final RegistryObject<EntityType<LatexMutantBomberEntity>> LATEX_MUTANT_BOMBER =
            ENTITY_TYPES.register("latex_mutant_bomber", () -> EntityType.Builder.of(LatexMutantBomberEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f).build("latex_mutant_bomber"));

    public static final RegistryObject<EntityType<LatexExoMutantEntity>> LATEX_EXO_MUTANT =
            ENTITY_TYPES.register("latex_exo_mutant", () -> EntityType.Builder.of(LatexExoMutantEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f).build("latex_exo_mutant"));

    // White Latex Infected Passive Mobs
    public static final RegistryObject<EntityType<WhiteLatexCowEntity>> WHITE_LATEX_COW =
            ENTITY_TYPES.register("white_latex_cow", () -> EntityType.Builder.of(WhiteLatexCowEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.4f).build("white_latex_cow"));
    
    public static final RegistryObject<EntityType<WhiteLatexPigEntity>> WHITE_LATEX_PIG =
            ENTITY_TYPES.register("white_latex_pig", () -> EntityType.Builder.of(WhiteLatexPigEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f).build("white_latex_pig"));
    
    public static final RegistryObject<EntityType<WhiteLatexChickenEntity>> WHITE_LATEX_CHICKEN =
            ENTITY_TYPES.register("white_latex_chicken", () -> EntityType.Builder.of(WhiteLatexChickenEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.7f).build("white_latex_chicken"));
    
    public static final RegistryObject<EntityType<WhiteLatexSheepEntity>> WHITE_LATEX_SHEEP =
            ENTITY_TYPES.register("white_latex_sheep", () -> EntityType.Builder.of(WhiteLatexSheepEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.3f).build("white_latex_sheep"));
    
    public static final RegistryObject<EntityType<WhiteLatexRabbitEntity>> WHITE_LATEX_RABBIT =
            ENTITY_TYPES.register("white_latex_rabbit", () -> EntityType.Builder.of(WhiteLatexRabbitEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.5f).build("white_latex_rabbit"));
    
    public static final RegistryObject<EntityType<WhiteLatexHorseEntity>> WHITE_LATEX_HORSE =
            ENTITY_TYPES.register("white_latex_horse", () -> EntityType.Builder.of(WhiteLatexHorseEntity::new, MobCategory.CREATURE)
                    .sized(1.3964844f, 1.6f).build("white_latex_horse"));
    
    public static final RegistryObject<EntityType<net.jerika.furmutage.entity.custom.DeepLatexSquidEntity>> DEEP_LATEX_SQUID =
            ENTITY_TYPES.register("deep_latex_squid", () -> EntityType.Builder.of(net.jerika.furmutage.entity.custom.DeepLatexSquidEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.8f).build("deep_latex_squid"));

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

    public static final RegistryObject<EntityType<TSCExplosiveGrenadeProjectile>> TSC_EXPLOSIVE_GRENADE_PROJECTILE =
            ENTITY_TYPES.register("tsc_explosive_grenade_projectile", () -> EntityType.Builder.<TSCExplosiveGrenadeProjectile>of(
                    TSCExplosiveGrenadeProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("tsc_explosive_grenade_projectile"));
    
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
