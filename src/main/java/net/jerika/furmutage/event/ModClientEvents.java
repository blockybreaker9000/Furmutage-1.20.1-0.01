package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static long lastNightSoundTime = -1;
    private static long lastMorningChimeTime = -1;
    private static final long NIGHT_SOUND_INTERVAL = 12000; // Play every 10 minutes (in ticks)
    private static final long MORNING_CHIME_COOLDOWN = 24000; // Only play once per day cycle

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        LocalPlayer player = mc.player;
        Level level = player.level();

        if (level.isClientSide) {
            long dayTime = level.getDayTime() % 24000; // Get time of day (0-24000)
            
            // Night ambient sounds (between 13000 and 23000 ticks, which is 6:30 PM to 5:30 AM)
            if (dayTime >= 13000 && dayTime < 23000) {
                // Check if enough time has passed since last night sound
                if (lastNightSoundTime == -1 || (dayTime - lastNightSoundTime) >= NIGHT_SOUND_INTERVAL || 
                    (dayTime < lastNightSoundTime && (dayTime + 24000 - lastNightSoundTime) >= NIGHT_SOUND_INTERVAL)) {
                    
                    // Random chance to play (30% chance each tick when interval is met)
                    if (level.random.nextFloat() < 0.3f) {
                        playRandomNightSound(level, player);
                        lastNightSoundTime = dayTime;
                    }
                }
            } else {
                // Reset when not in nighttime
                if (dayTime < 13000) {
                    lastNightSoundTime = -1;
                }
            }

            // Morning chime (between 0 and 1000 ticks, which is 6:00 AM to 6:30 AM)
            if (dayTime >= 0 && dayTime < 1000) {
                // Only play once per day cycle
                long dayCycle = level.getDayTime() / 24000;
                if (lastMorningChimeTime != dayCycle) {
                    // Play chime at the start of dawn (around 0-100 ticks, with small random delay)
                    if (dayTime < 100) {
                        // Play once when we first enter dawn time
                        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                                ModSounds.MORNING_CHIME.get(), SoundSource.AMBIENT, 0.5f, 1.0f);
                        lastMorningChimeTime = dayCycle;
                    }
                }
            }
        }
    }

    private static void playRandomNightSound(Level level, LocalPlayer player) {
        int soundChoice = level.random.nextInt(3);
        @NotNull SoundEvent soundToPlay;
        
        switch (soundChoice) {
            case 0:
                soundToPlay = ModSounds.NIGHT_AMBIENT_1.get();
                break;
            case 1:
                soundToPlay = ModSounds.NIGHT_AMBIENT_2.get();
                break;
            default:
                soundToPlay = ModSounds.NIGHT_AMBIENT_3.get();
                break;
        }
        
        // Play sound at player location with ambient volume
        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                soundToPlay, SoundSource.AMBIENT, 0.3f, 0.8f + level.random.nextFloat() * 0.4f);
    }
}

