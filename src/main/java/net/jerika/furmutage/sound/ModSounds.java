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
    
    // Tainted grass biome music
    public static final RegistryObject<SoundEvent> TAINTED_WHITE_GRASS_MUSIC = registerSoundEvent("tainted_white_grass_music");
    public static final RegistryObject<SoundEvent> TAINTED_DARK_GRASS_MUSIC = registerSoundEvent("tainted_dark_grass_music");

    public static final RegistryObject<SoundEvent> TSC_DRONE_AMBIENT = registerSoundEvent("tsc_drone_ambient");
    public static final RegistryObject<SoundEvent> TSC_DRONE_HURT = registerSoundEvent("tsc_drone_hurt");
    public static final RegistryObject<SoundEvent> TSC_DRONE_DEATH = registerSoundEvent("tsc_drone_death");
    public static final RegistryObject<SoundEvent> TSC_DRONE_BULLET_THROW = registerSoundEvent("tsc_drone_bullet_throw");
    public static final RegistryObject<SoundEvent> TSC_SHOCK_GRENADE_THROW = registerSoundEvent("tsc_shock_grenade_throw");

    // White Latex Horse sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_HORSE_AMBIENT = registerSoundEvent("white_latex_horse_ambient");

    // White Latex Cow sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_COW_AMBIENT = registerSoundEvent("white_latex_cow_ambient");
    public static final RegistryObject<SoundEvent> WHITE_LATEX_COW_HURT = registerSoundEvent("white_latex_cow_hurt");
    public static final RegistryObject<SoundEvent> WHITE_LATEX_COW_DEATH = registerSoundEvent("white_latex_cow_death");

    // White Latex Pig sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_PIG_AMBIENT = registerSoundEvent("white_latex_pig_ambient");
    public static final RegistryObject<SoundEvent> WHITE_LATEX_PIG_HURT = registerSoundEvent("white_latex_pig_hurt");
    public static final RegistryObject<SoundEvent> WHITE_LATEX_PIG_DEATH = registerSoundEvent("white_latex_pig_death");

    // White Latex Sheep sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_SHEEP_AMBIENT = registerSoundEvent("white_latex_sheep_ambient");

    // White Latex Chicken sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_CHICKEN_AMBIENT = registerSoundEvent("white_latex_chicken_ambient");

    // White Latex Rabbit sounds
    public static final RegistryObject<SoundEvent> WHITE_LATEX_RABBIT_AMBIENT = registerSoundEvent("white_latex_rabbit_ambient");

    // Latex Mutant Bomber sounds
    public static final RegistryObject<SoundEvent> LATEX_MUTANT_BOMBER_AMBIENT = registerSoundEvent("latex_mutant_bomber_ambient");
    public static final RegistryObject<SoundEvent> LATEX_MUTANT_BOMBER_HURT = registerSoundEvent("latex_mutant_bomber_hurt");
    public static final RegistryObject<SoundEvent> LATEX_MUTANT_BOMBER_DEATH = registerSoundEvent("latex_mutant_bomber_death");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(furmutage.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
