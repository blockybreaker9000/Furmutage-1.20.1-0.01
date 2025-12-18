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
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_4 = registerSoundEvent("night_ambient_4");
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_5 = registerSoundEvent("night_ambient_5");
    public static final RegistryObject<SoundEvent> NIGHT_AMBIENT_6 = registerSoundEvent("night_ambient_6");
    public static final RegistryObject<SoundEvent> NIGHT_CHIME = registerSoundEvent("night_chime");
    
    // Morning chime
    public static final RegistryObject<SoundEvent> MORNING_CHIME = registerSoundEvent("morning_chime");
    

    public static final RegistryObject<SoundEvent> TSC_DRONE_AMBIENT = registerSoundEvent("tsc_drone_ambient");
    public static final RegistryObject<SoundEvent> TSC_DRONE_HURT = registerSoundEvent("tsc_drone_hurt");
    public static final RegistryObject<SoundEvent> TSC_DRONE_DEATH = registerSoundEvent("tsc_drone_death");
    public static final RegistryObject<SoundEvent> TSC_DRONE_BULLET_THROW = registerSoundEvent("tsc_drone_bullet_throw");
    public static final RegistryObject<SoundEvent> TSC_SHOCK_GRENADE_THROW = registerSoundEvent("tsc_shock_grenade_throw");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(furmutage.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
