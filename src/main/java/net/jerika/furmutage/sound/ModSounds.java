package net.jerika.furmutage.sound;

import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, furmutage.MOD_ID);

    // Night ambient sounds
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_1 = registerSoundEvent("night_ambient_1");
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_2 = registerSoundEvent("night_ambient_2");
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_3 = registerSoundEvent("night_ambient_3");
    
    // Morning chime
    public static final RegistryObject<SoundEvent> MORNING_CHIME = registerSoundEvent("morning_chime");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(furmutage.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
