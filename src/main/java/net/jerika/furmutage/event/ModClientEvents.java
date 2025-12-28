package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static long lastNightSoundTime = -1;
    private static long lastMorningChimeTime = -1;
    private static long lastMidnightChimeTime = -1;
    private static long lastNightChimeTime = -1; // Track when night chime was played
    private static long previousDayTime = -1;
    private static final long NIGHT_SOUND_INTERVAL = 6000; // Play every 5 minutes (in ticks)
    private static final long NIGHT_START_DELAY = 1000; // Delay before regular ambient sounds can play (after night starts)
    
    // Tainted white grass biome music
    private static boolean wasOnTaintedGrass = false;
    private static long lastTaintedMusicTime = -1;
    private static final long TAINTED_MUSIC_CHECK_INTERVAL = 100; // Check every 5 seconds (100 ticks)
    private static final long TAINTED_MUSIC_PLAY_INTERVAL = 36000; // Play music every ~30 minutes (assuming music track length)

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        LocalPlayer player = mc.player;
        Level level = player.level();

        if (level.isClientSide) {
            long dayTime = level.getDayTime() % 24000; // Get time of day (0-24000)
            long dayCycle = level.getDayTime() / 24000;
            
            // Night chime at the start of night (around 13000 ticks, which is 6:30 PM)
            if (dayTime >= 13000 && dayTime < 13100) {
                // Only play once per day cycle when night starts
                if (lastNightChimeTime != dayCycle) {
                    level.playSound(player, player.getX(), player.getY(), player.getZ(),
                            ModSounds.NIGHT_CHIME.get(), SoundSource.AMBIENT, 0.5f, 1.0f);
                    lastNightChimeTime = dayCycle;
                }
            }
            
            // Night ambient sounds (between 13000 and 23000 ticks, which is 6:30 PM to 5:30 AM)
            // But only after a delay from night start (to avoid playing at the same time as night chime)
            if (dayTime >= (13000 + NIGHT_START_DELAY) && dayTime < 23000) {
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

            // Midnight chime (at 18000 ticks, which is 12:00 AM)
            if (dayTime >= 18000 && dayTime < 18100) {
                // Only play once per day cycle
                if (lastMidnightChimeTime != dayCycle) {
                    // Play chime at midnight
                    level.playSound(player, player.getX(), player.getY(), player.getZ(),
                            ModSounds.NIGHT_AMBIENT_2.get(), SoundSource.AMBIENT, 0.5f, 1.0f);
                    lastMidnightChimeTime = dayCycle;
                }
            }

            // Morning chime (only at exactly 0 ticks, which is 6:00 AM)
            // Check if we just crossed from night to exactly 0, or if we're at exactly 0
            boolean justCrossedDawn = previousDayTime > 23000 && dayTime == 23000;
            if (dayTime == 23000 || justCrossedDawn) {
                // Only play once per day cycle
                if (lastMorningChimeTime != dayCycle) {
                    // Play chime at exactly dawn (time 0)
                    level.playSound(player, player.getX(), player.getY(), player.getZ(),
                            ModSounds.MORNING_CHIME.get(), SoundSource.AMBIENT, 0.4f, 1.0f);
                    lastMorningChimeTime = dayCycle;
                }
            }

            // Update previous day time for wrap-around detection
            previousDayTime = dayTime;
            
            // Handle tainted white grass biome music
            handleTaintedWhiteGrassMusic(mc, player, level);
        }
    }
    
    private static void handleTaintedWhiteGrassMusic(Minecraft mc, LocalPlayer player, Level level) {
        // Check periodically if player is on or near tainted white grass blocks
        if (level.getGameTime() % TAINTED_MUSIC_CHECK_INTERVAL != 0) {
            return; // Only check every interval to reduce overhead
        }
        
        BlockPos playerPos = player.blockPosition();
        boolean isOnTaintedGrass = isOnTaintedWhiteGrass(level, playerPos);
        long currentTime = level.getGameTime();
        
        if (isOnTaintedGrass) {
            // Check if enough time has passed to play music again
            if (lastTaintedMusicTime == -1 || (currentTime - lastTaintedMusicTime) >= TAINTED_MUSIC_PLAY_INTERVAL) {
                // Play biome music with MUSIC source (this is handled by Minecraft's music system)
                level.playSound(player, player.getX(), player.getY(), player.getZ(),
                        ModSounds.TAINTED_WHITE_GRASS_MUSIC.get(), SoundSource.MUSIC, 1.0f, 1.0f);
                lastTaintedMusicTime = currentTime;
            }
            wasOnTaintedGrass = true;
        } else {
            // Stop music when player leaves tainted white grass area
            if (wasOnTaintedGrass) {
                // Stop our specific tainted grass music by stopping all MUSIC source sounds
                // This will stop our music and allow normal biome music to resume
                if (mc.getSoundManager() != null) {
                    // Stop all MUSIC category sounds (our tainted grass music uses MUSIC source)
                    // Note: This is necessary since playSound doesn't return a trackable instance
                    mc.getSoundManager().stop(ModSounds.TAINTED_WHITE_GRASS_MUSIC.get().getLocation(), SoundSource.MUSIC);
                }
                lastTaintedMusicTime = -1; // Reset timer so music can play again when they return
                wasOnTaintedGrass = false;
            }
        }
    }
    
    private static boolean isOnTaintedWhiteGrass(Level level, BlockPos pos) {
        // Check blocks around the player (including below and in a small area)
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    Block block = level.getBlockState(checkPos).getBlock();
                    
                    // Check if it's a tainted white grass block or related tainted blocks
                    if (block == ModBlocks.TAINTED_WHITE_GRASS.get() ||
                        block == ModBlocks.TAINTED_WHITE_DIRT.get() ||
                        block == ModBlocks.TAINTED_WHITE_SAND.get() ||
                        block == ModBlocks.TAINTED_WHITE_LOG.get() ||
                        block == ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get() ||
                        block == ModBlocks.TAINTED_WHITE_PLANKS.get() ||
                        block == ModBlocks.TAINTED_WHITE_LEAF.get()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void playRandomNightSound(Level level, LocalPlayer player) {
        int soundChoice = level.random.nextInt(6);
        @NotNull SoundEvent soundToPlay;
        
        switch (soundChoice) {
            case 0:
                soundToPlay = ModSounds.NIGHT_AMBIENT_1.get();
                break;
            case 1:
                soundToPlay = ModSounds.NIGHT_AMBIENT_2.get();
                break;
            case 2:
                soundToPlay = ModSounds.NIGHT_AMBIENT_3.get();
                break;
            case 3:
                soundToPlay = ModSounds.NIGHT_AMBIENT_4.get();
                break;
            case 4:
                soundToPlay = ModSounds.NIGHT_AMBIENT_5.get();
                break;
            default:
                soundToPlay = ModSounds.NIGHT_AMBIENT_6.get();
                break;
        }
        
        // Play sound at player location with ambient volume
        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                soundToPlay, SoundSource.AMBIENT, 0.3f, 1.0f);
    }
}


