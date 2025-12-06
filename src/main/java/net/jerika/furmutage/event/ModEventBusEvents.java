package net.jerika.furmutage.event;


import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.entity.custom.MuglingEntity;
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


        }
}
