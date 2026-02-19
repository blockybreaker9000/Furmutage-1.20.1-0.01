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
import net.jerika.furmutage.entity.custom.GiantPureWhiteLatexEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexSquidEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexLlamaEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexDolphinEntity;
import net.jerika.furmutage.entity.custom.WhiteLatexGoatEntity;
import net.jerika.furmutage.entity.custom.DarkLatexCowEntity;
import net.jerika.furmutage.entity.custom.DarkLatexPigEntity;
import net.jerika.furmutage.entity.custom.DarkLatexChickenEntity;
import net.jerika.furmutage.entity.custom.DarkLatexSheepEntity;
import net.jerika.furmutage.entity.custom.DarkLatexRabbitEntity;
import net.jerika.furmutage.entity.custom.DarkLatexHorseEntity;
import net.jerika.furmutage.entity.custom.DarkLatexSquidEntity;
import net.jerika.furmutage.entity.custom.DarkLatexLlamaEntity;
import net.jerika.furmutage.entity.custom.DarkLatexDolphinEntity;
import net.jerika.furmutage.entity.custom.DarkLatexChargerMutantEntity;
import net.jerika.furmutage.entity.custom.DarkLatexGoatEntity;
import net.jerika.furmutage.entity.custom.DeepSlateLatexSquidDog;
import net.jerika.furmutage.entity.custom.DeepCaveHypnoCat;
import net.jerika.furmutage.entity.custom.LatexBloodWormMutant;
import net.jerika.furmutage.entity.custom.LooseBehemothHand;
import net.jerika.furmutage.entity.custom.LooseSquidDogLimbEntity;
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

    public static final RegistryObject<EntityType<DarkLatexChargerMutantEntity>> DARK_LATEX_CHARGER_MUTANT =
            ENTITY_TYPES.register("dark_latex_charger_mutant", () -> EntityType.Builder.of(DarkLatexChargerMutantEntity::new, MobCategory.MONSTER)
                    .sized(2.4f, 3.1f).build("dark_latex_charger_mutant"));

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
    
    public static final RegistryObject<EntityType<WhiteLatexSquidEntity>> WHITE_LATEX_SQUID =
            ENTITY_TYPES.register("white_latex_squid", () -> EntityType.Builder.of(WhiteLatexSquidEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.8f).build("white_latex_squid"));
    
    public static final RegistryObject<EntityType<WhiteLatexLlamaEntity>> WHITE_LATEX_LLAMA =
            ENTITY_TYPES.register("white_latex_llama", () -> EntityType.Builder.of(WhiteLatexLlamaEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.87f).build("white_latex_llama"));
    
    public static final RegistryObject<EntityType<WhiteLatexDolphinEntity>> WHITE_LATEX_DOLPHIN =
            ENTITY_TYPES.register("white_latex_dolphin", () -> EntityType.Builder.of(WhiteLatexDolphinEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.9f, 0.6f).build("white_latex_dolphin"));
    
    public static final RegistryObject<EntityType<WhiteLatexGoatEntity>> WHITE_LATEX_GOAT =
            ENTITY_TYPES.register("white_latex_goat", () -> EntityType.Builder.of(WhiteLatexGoatEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.3f).build("white_latex_goat"));
    
    // Dark Latex Infected Passive Mobs
    public static final RegistryObject<EntityType<DarkLatexCowEntity>> DARK_LATEX_COW =
            ENTITY_TYPES.register("dark_latex_cow", () -> EntityType.Builder.of(DarkLatexCowEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.4f).build("dark_latex_cow"));
    
    public static final RegistryObject<EntityType<DarkLatexPigEntity>> DARK_LATEX_PIG =
            ENTITY_TYPES.register("dark_latex_pig", () -> EntityType.Builder.of(DarkLatexPigEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f).build("dark_latex_pig"));
    
    public static final RegistryObject<EntityType<DarkLatexChickenEntity>> DARK_LATEX_CHICKEN =
            ENTITY_TYPES.register("dark_latex_chicken", () -> EntityType.Builder.of(DarkLatexChickenEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.7f).build("dark_latex_chicken"));
    
    public static final RegistryObject<EntityType<DarkLatexSheepEntity>> DARK_LATEX_SHEEP =
            ENTITY_TYPES.register("dark_latex_sheep", () -> EntityType.Builder.of(DarkLatexSheepEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.3f).build("dark_latex_sheep"));
    
    public static final RegistryObject<EntityType<DarkLatexRabbitEntity>> DARK_LATEX_RABBIT =
            ENTITY_TYPES.register("dark_latex_rabbit", () -> EntityType.Builder.of(DarkLatexRabbitEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.5f).build("dark_latex_rabbit"));
    
    public static final RegistryObject<EntityType<DarkLatexHorseEntity>> DARK_LATEX_HORSE =
            ENTITY_TYPES.register("dark_latex_horse", () -> EntityType.Builder.of(DarkLatexHorseEntity::new, MobCategory.CREATURE)
                    .sized(1.3964844f, 1.6f).build("dark_latex_horse"));
    
    public static final RegistryObject<EntityType<DarkLatexSquidEntity>> DARK_LATEX_SQUID =
            ENTITY_TYPES.register("dark_latex_squid", () -> EntityType.Builder.of(DarkLatexSquidEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.8f).build("dark_latex_squid"));
    
    public static final RegistryObject<EntityType<DarkLatexLlamaEntity>> DARK_LATEX_LLAMA =
            ENTITY_TYPES.register("dark_latex_llama", () -> EntityType.Builder.of(DarkLatexLlamaEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.87f).build("dark_latex_llama"));
    
    public static final RegistryObject<EntityType<DarkLatexDolphinEntity>> DARK_LATEX_DOLPHIN =
            ENTITY_TYPES.register("dark_latex_dolphin", () -> EntityType.Builder.of(DarkLatexDolphinEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.9f, 0.6f).build("dark_latex_dolphin"));
    
    public static final RegistryObject<EntityType<DarkLatexGoatEntity>> DARK_LATEX_GOAT =
            ENTITY_TYPES.register("dark_latex_goat", () -> EntityType.Builder.of(DarkLatexGoatEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.3f).build("dark_latex_goat"));
    
    public static final RegistryObject<EntityType<GiantPureWhiteLatexEntity>> GIANT_PURE_WHITE_LATEX =
            ENTITY_TYPES.register("giant_pure_white_latex", () -> EntityType.Builder.of(GiantPureWhiteLatexEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 6.0f).build("giant_pure_white_latex"));

    public static final RegistryObject<EntityType<DeepSlateLatexSquidDog>> DEEPSLATE_LATEX_SQUID_DOG =
            ENTITY_TYPES.register("deepslate_latex_squid_dog", () -> EntityType.Builder.of(DeepSlateLatexSquidDog::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 2.1f).clientTrackingRange(10).build("deepslate_latex_squid_dog"));

    public static final RegistryObject<EntityType<DeepCaveHypnoCat>> DEEP_CAVE_HYPNO_CAT =
            ENTITY_TYPES.register("deep_cave_hypno_cat", () -> EntityType.Builder.of(DeepCaveHypnoCat::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.93f).clientTrackingRange(10).build("deep_cave_hypno_cat"));

    public static final RegistryObject<EntityType<LatexBloodWormMutant>> LATEX_BLOOD_WORM_MUTANT =
            ENTITY_TYPES.register("latex_blood_worm_mutant", () -> EntityType.Builder.of(LatexBloodWormMutant::new, MobCategory.MONSTER)
                    .sized(0.7F, 1.64F).clientTrackingRange(10).build("latex_blood_worm_mutant"));

    public static final RegistryObject<EntityType<LooseBehemothHand>> LOOSE_BEHEMOTH_HAND =
            ENTITY_TYPES.register("loose_behemoth_hand", () -> EntityType.Builder.of(LooseBehemothHand::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.2f).clientTrackingRange(8).build("loose_behemoth_hand"));

    public static final RegistryObject<EntityType<LooseSquidDogLimbEntity>> LOOSE_SQUID_DOG_LIMB =
            ENTITY_TYPES.register("loose_squid_dog_limb", () -> EntityType.Builder.of(LooseSquidDogLimbEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 0.8f).clientTrackingRange(10).build("loose_squid_dog_limb"));

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

    public static final RegistryObject<EntityType<TSCPipeBombProjectile>> TSC_PIPE_BOMB_PROJECTILE =
            ENTITY_TYPES.register("tsc_pipe_bomb_projectile", () -> EntityType.Builder.<TSCPipeBombProjectile>of(
                    TSCPipeBombProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("tsc_pipe_bomb_projectile"));
    
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
