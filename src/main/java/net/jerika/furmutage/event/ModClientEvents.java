package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static long lastNightSoundTime = -1;
    private static final long NIGHT_SOUND_INTERVAL = 9000; // Rare ambient pacing
    private static final long NIGHT_START_DELAY = 1000; // Delay before regular ambient sounds can play (after night starts)
    private static final float NIGHT_SOUND_CHANCE = 0.08f; // Chance when interval is met
    private static final int NIGHT_SOUND_MIN_DISTANCE = 14;
    private static final int NIGHT_SOUND_MAX_DISTANCE = 28;
    /** One random distant night ambient ({@link #playRandomNightSound}) at midnight (18000) per day cycle. */
    private static long lastMidnightRandomNightSoundCycle = -1L;
    /** After loading a world or changing dimension, suppress midnight night ambient for this many client ticks. */
    private static final int NIGHT_CHIME_JOIN_MUTE_TICKS = 2000;
    private static int nightChimeJoinMuteTicksLeft = 0;
    @Nullable
    private static Level nightChimeJoinMuteLevelRef = null;

    // Tainted grass biome music (white and dark)
    private static boolean wasOnTaintedGrass = false;
    private static boolean wasOnTaintedWhiteGrass = false; // Track which type was playing
    private static long lastTaintedMusicTime = -1;
    private static final long TAINTED_MUSIC_CHECK_INTERVAL = 100; // Check every 5 seconds (100 ticks)
    private static final long TAINTED_MUSIC_PLAY_INTERVAL = 36000; // Play music every ~30 minutes (assuming music track length)
    private static final int MUSIC_DETECTION_RANGE = 10; // Music plays when tainted grass is within 10 blocks
    private static SimpleSoundInstance currentMusicInstance = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        LocalPlayer player = mc.player;
        Level level = player.level();

        if (level.isClientSide) {
            if (mc.level != nightChimeJoinMuteLevelRef) {
                nightChimeJoinMuteLevelRef = mc.level;
                nightChimeJoinMuteTicksLeft = NIGHT_CHIME_JOIN_MUTE_TICKS;
            }
            boolean muteNightAmbientOnJoin = nightChimeJoinMuteTicksLeft > 0;
            if (nightChimeJoinMuteTicksLeft > 0) {
                nightChimeJoinMuteTicksLeft--;
            }

            long dayTime = level.getDayTime() % 24000; // Get time of day (0-24000)
            long dayCycle = level.getDayTime() / 24000;

            // Midnight: random distant night ambient
            if (dayTime >= 18000 && dayTime < 18100) {
                if (!muteNightAmbientOnJoin && lastMidnightRandomNightSoundCycle != dayCycle) {
                    playRandomNightSound(level, player);
                    lastMidnightRandomNightSoundCycle = dayCycle;
                    lastNightSoundTime = dayTime;
                }
            }
            
            // Night ambient sounds (between 13000 and 23000 ticks, which is 6:30 PM to 5:30 AM)
            // Brief delay after night starts; surface darkness only for distant ambients
            if (dayTime >= (13000 + NIGHT_START_DELAY) && dayTime < 23000) {
                boolean inSurfaceDarkness = isPlayerInDarkness(level, player);

                // Check if enough time has passed since last night sound
                if (lastNightSoundTime == -1 || (dayTime - lastNightSoundTime) >= NIGHT_SOUND_INTERVAL || 
                    (dayTime < lastNightSoundTime && (dayTime + 24000 - lastNightSoundTime) >= NIGHT_SOUND_INTERVAL)) {
                    
                    // Surface-night ambient: only when the player is in darkness.
                    if (inSurfaceDarkness && level.random.nextFloat() < NIGHT_SOUND_CHANCE) {
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

            // Handle tainted grass biome music (white and dark)
            handleTaintedGrassMusic(mc, player, level);
        }
    }
    
    private static void handleTaintedGrassMusic(Minecraft mc, LocalPlayer player, Level level) {
        // Check periodically if player is on or near tainted grass blocks (white or dark)
        if (level.getGameTime() % TAINTED_MUSIC_CHECK_INTERVAL != 0) {
            return; // Only check every interval to reduce overhead
        }
        
        BlockPos playerPos = player.blockPosition();
        BlockPos nearestTaintedWhitePos = findNearestTaintedWhiteGrass(level, playerPos, MUSIC_DETECTION_RANGE);
        BlockPos nearestTaintedDarkPos = findNearestTaintedDarkGrass(level, playerPos, MUSIC_DETECTION_RANGE);
        
        boolean isOnTaintedWhiteGrass = nearestTaintedWhitePos != null;
        boolean isOnTaintedDarkGrass = nearestTaintedDarkPos != null;
        boolean isOnTaintedGrass = isOnTaintedWhiteGrass || isOnTaintedDarkGrass;
        long currentTime = level.getGameTime();
        
        if (isOnTaintedGrass) {
            // Determine which music to play (white takes priority if both are present)
            boolean shouldPlayWhite = isOnTaintedWhiteGrass;
            boolean shouldPlayDark = isOnTaintedDarkGrass && !isOnTaintedWhiteGrass;
            BlockPos musicSourcePos = shouldPlayWhite ? nearestTaintedWhitePos : nearestTaintedDarkPos;
            
            // Check if we need to switch music or if enough time has passed to play music again
            boolean musicChanged = (shouldPlayWhite && !wasOnTaintedWhiteGrass) || (shouldPlayDark && wasOnTaintedWhiteGrass);
            boolean shouldPlayMusic = lastTaintedMusicTime == -1 || 
                                     (currentTime - lastTaintedMusicTime) >= TAINTED_MUSIC_PLAY_INTERVAL ||
                                     musicChanged;
            
            if (shouldPlayMusic) {
                SoundManager soundManager = mc.getSoundManager();
                if (soundManager != null) {
                    // Stop the previous music if switching types or if already playing
                    if (currentMusicInstance != null) {
                        soundManager.stop(currentMusicInstance);
                        currentMusicInstance = null;
                    }
                    if (musicChanged) {
                        if (wasOnTaintedWhiteGrass) {
                            soundManager.stop(ModSounds.TAINTED_WHITE_GRASS_MUSIC.get().getLocation(), SoundSource.MUSIC);
                        } else {
                            soundManager.stop(ModSounds.TAINTED_DARK_GRASS_MUSIC.get().getLocation(), SoundSource.MUSIC);
                        }
                    }
                    
                    // Play the appropriate biome music as a positional sound from the tainted grass location
                    SoundEvent musicToPlay = shouldPlayWhite ? ModSounds.TAINTED_WHITE_GRASS_MUSIC.get() : ModSounds.TAINTED_DARK_GRASS_MUSIC.get();
                    
                    // Create a positional sound instance that can be heard from 10 blocks away
                    // Using LINEAR attenuation with volume 0.625f (10/16) allows it to be heard from ~10 blocks
                    // Minecraft's LINEAR attenuation makes sounds inaudible beyond 16 blocks at volume 1.0
                    // To hear from 10 blocks, we need volume = 10/16 ≈ 0.625f
                    currentMusicInstance = new SimpleSoundInstance(
                            musicToPlay.getLocation(),
                            SoundSource.MUSIC,
                            0.625f, // volume - calculated to reach ~10 blocks (10/16)
                            1.0f, // pitch
                            level.getRandom(),
                            true, // looping (music typically loops)
                            0, // delay
                            SimpleSoundInstance.Attenuation.LINEAR, // Linear attenuation for distance-based volume
                            musicSourcePos.getX() + 0.5, // Center of the block
                            musicSourcePos.getY() + 0.5,
                            musicSourcePos.getZ() + 0.5,
                            false // not relative
                    );
                    
                    soundManager.play(currentMusicInstance);
                    lastTaintedMusicTime = currentTime;
                    wasOnTaintedWhiteGrass = shouldPlayWhite;
                }
            }
            wasOnTaintedGrass = true;
        } else {
            // Stop music when player leaves tainted grass area
            if (wasOnTaintedGrass) {
                SoundManager soundManager = mc.getSoundManager();
                if (soundManager != null) {
                    // Stop the positional sound instance
                    if (currentMusicInstance != null) {
                        soundManager.stop(currentMusicInstance);
                        currentMusicInstance = null;
                    }
                    // Also stop by location in case
                    if (wasOnTaintedWhiteGrass) {
                        soundManager.stop(ModSounds.TAINTED_WHITE_GRASS_MUSIC.get().getLocation(), SoundSource.MUSIC);
                    } else {
                        soundManager.stop(ModSounds.TAINTED_DARK_GRASS_MUSIC.get().getLocation(), SoundSource.MUSIC);
                    }
                }
                lastTaintedMusicTime = -1; // Reset timer so music can play again when they return
                wasOnTaintedGrass = false;
                wasOnTaintedWhiteGrass = false;
            }
        }
    }
    
    private static BlockPos findNearestTaintedWhiteGrass(Level level, BlockPos pos, int range) {
        BlockPos nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        
        // Check blocks around the player within the specified range
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    Block block = level.getBlockState(checkPos).getBlock();
                    
                    // Check if it's a tainted white grass block or related tainted white blocks
                    if (block == ModBlocks.TAINTED_WHITE_GRASS.get() ||
                        block == ModBlocks.TAINTED_WHITE_DIRT.get() ||
                        block == ModBlocks.TAINTED_WHITE_SAND.get() ||
                        block == ModBlocks.TAINTED_WHITE_LOG.get() ||
                        block == ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get() ||
                        block == ModBlocks.TAINTED_WHITE_PLANKS.get() ||
                        block == ModBlocks.TAINTED_WHITE_LEAF.get()) {
                        
                        double distSq = pos.distSqr(checkPos);
                        if (distSq < nearestDistSq) {
                            nearest = checkPos;
                            nearestDistSq = distSq;
                        }
                    }
                }
            }
        }
        return nearest;
    }
    
    private static BlockPos findNearestTaintedDarkGrass(Level level, BlockPos pos, int range) {
        BlockPos nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        
        // Check blocks around the player within the specified range
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    Block block = level.getBlockState(checkPos).getBlock();
                    
                    // Check if it's a tainted dark grass block or related tainted dark blocks
                    if (block == ModBlocks.TAINTED_DARK_GRASS.get() ||
                        block == ModBlocks.TAINTED_DARK_DIRT.get() ||
                        block == ModBlocks.TAINTED_DARK_SAND.get() ||
                        block == ModBlocks.TAINTED_DARK_LOG.get() ||
                        block == ModBlocks.STRIPPED_TAINTED_DARK_LOG.get() ||
                        block == ModBlocks.TAINTED_DARK_PLANKS.get() ||
                        block == ModBlocks.TAINTED_DARK_LEAF.get()) {
                        
                        double distSq = pos.distSqr(checkPos);
                        if (distSq < nearestDistSq) {
                            nearest = checkPos;
                            nearestDistSq = distSq;
                        }
                    }
                }
            }
        }
        return nearest;
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
        
        // Play like cave ambience: pick a distant random spot around the player.
        int distance = NIGHT_SOUND_MIN_DISTANCE + level.random.nextInt(NIGHT_SOUND_MAX_DISTANCE - NIGHT_SOUND_MIN_DISTANCE + 1);
        double angle = level.random.nextDouble() * (Math.PI * 2.0);
        double sx = player.getX() + Math.cos(angle) * distance;
        double sz = player.getZ() + Math.sin(angle) * distance;
        double sy = player.getY() + (level.random.nextInt(7) - 3); // slight vertical drift

        level.playLocalSound(sx, sy, sz, soundToPlay, SoundSource.AMBIENT, 0.125f, 1.0f, false);
    }

    private static boolean isPlayerInDarkness(Level level, LocalPlayer player) {
        BlockPos pos = player.blockPosition();
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        boolean isSurfaceNight = level.canSeeSky(pos.above());
        return isSurfaceNight && blockLight <= 2 && skyLight <= 7;
    }
}


