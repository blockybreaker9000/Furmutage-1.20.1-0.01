package net.jerika.furmutage.event;


import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.entity.custom.LatexTenticleLimbsMutantEntity;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
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


        }
}
