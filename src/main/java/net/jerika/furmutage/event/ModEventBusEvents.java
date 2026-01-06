package net.jerika.furmutage.event;


import net.jerika.furmutage.entity.ModEntities;
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
import net.jerika.furmutage.entity.custom.GiantPureWhiteLatexEntity;
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
import net.jerika.furmutage.entity.custom.DarkLatexGoatEntity;
import net.jerika.furmutage.entity.custom.WitheredLatexPuddingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MUGLING.get(), MuglingEntity.createMobAttributes().build());
        event.put(ModEntities.LATEX_MUTANT_FAMILY.get(), LatexMutantFamilyEntity.createMobAttributes().build());
        event.put(ModEntities.WITHERED_LATEX_PUDDING.get(), WitheredLatexPuddingEntity.createMobAttributes().build());
        event.put(ModEntities.LATEX_TENTICLE_LIMBS_MUTANT.get(), LatexTenticleLimbsMutantEntity.createMobAttributes().build());
        event.put(ModEntities.TSC_DRONE.get(), TSCDroneEntity.createMobAttributes().build());
        event.put(ModEntities.TSC_DRONE_BOSS.get(), TSCDroneBossEntity.createBossAttributes().build());
        event.put(ModEntities.LATEX_BACTERIA.get(), LatexBacteriaEntity.createMobAttributes().build());
        event.put(ModEntities.LATEX_MUTANT_BOMBER.get(), LatexMutantBomberEntity.createMobAttributes().build());
        event.put(ModEntities.LATEX_EXO_MUTANT.get(), LatexExoMutantEntity.createMobAttributes().build());
        
        // White Latex Infected Passive Mobs
        event.put(ModEntities.WHITE_LATEX_COW.get(), WhiteLatexCowEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_PIG.get(), WhiteLatexPigEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_CHICKEN.get(), WhiteLatexChickenEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_SHEEP.get(), WhiteLatexSheepEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_RABBIT.get(), WhiteLatexRabbitEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_HORSE.get(), WhiteLatexHorseEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_SQUID.get(), WhiteLatexSquidEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_LLAMA.get(), WhiteLatexLlamaEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_DOLPHIN.get(), WhiteLatexDolphinEntity.createAttributes().build());
        event.put(ModEntities.WHITE_LATEX_GOAT.get(), WhiteLatexGoatEntity.createAttributes().build());
        
        // Dark Latex Infected Passive Mobs
        event.put(ModEntities.DARK_LATEX_COW.get(), DarkLatexCowEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_PIG.get(), DarkLatexPigEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_CHICKEN.get(), DarkLatexChickenEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_SHEEP.get(), DarkLatexSheepEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_RABBIT.get(), DarkLatexRabbitEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_HORSE.get(), DarkLatexHorseEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_SQUID.get(), DarkLatexSquidEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_LLAMA.get(), DarkLatexLlamaEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_DOLPHIN.get(), DarkLatexDolphinEntity.createAttributes().build());
        event.put(ModEntities.DARK_LATEX_GOAT.get(), DarkLatexGoatEntity.createAttributes().build());
        
        event.put(ModEntities.GIANT_PURE_WHITE_LATEX.get(), GiantPureWhiteLatexEntity.createAttributes().build());
    }
}
