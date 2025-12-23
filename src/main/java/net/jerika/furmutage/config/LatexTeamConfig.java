package net.jerika.furmutage.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration loader for Latex team assignments.
 * Loads entity identifiers from data/furmutage/latex_teams.json
 */
public class LatexTeamConfig extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    // Store team assignments
    private static final Set<String> whiteLatexIdentifiers = new HashSet<>();
    private static final Set<String> darkLatexIdentifiers = new HashSet<>();
    
    public LatexTeamConfig() {
        super(GSON, "latex_teams");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        whiteLatexIdentifiers.clear();
        darkLatexIdentifiers.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            try {
                JsonObject json = entry.getValue().getAsJsonObject();
                
                // Load white latex team
                if (json.has("white_latex_team") && json.get("white_latex_team").isJsonArray()) {
                    json.getAsJsonArray("white_latex_team").forEach(element -> {
                        if (element.isJsonPrimitive()) {
                            whiteLatexIdentifiers.add(element.getAsString());
                        }
                    });
                }
                
                // Load dark latex team
                if (json.has("dark_latex_team") && json.get("dark_latex_team").isJsonArray()) {
                    json.getAsJsonArray("dark_latex_team").forEach(element -> {
                        if (element.isJsonPrimitive()) {
                            darkLatexIdentifiers.add(element.getAsString());
                        }
                    });
                }
                
                LOGGER.info("Loaded {} white latex identifiers and {} dark latex identifiers from {}", 
                    whiteLatexIdentifiers.size(), darkLatexIdentifiers.size(), entry.getKey());
            } catch (Exception e) {
                LOGGER.error("Error loading latex team config from {}", entry.getKey(), e);
            }
        }
    }
    
    /**
     * Checks if an entity belongs to the White Latex team based on configuration.
     */
    public static boolean isWhiteLatex(net.minecraft.world.entity.LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        
        EntityType<?> entityType = entity.getType();
        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        String className = entity.getClass().getName();
        String simpleClassName = entity.getClass().getSimpleName();
        
        // Check against configured identifiers
        for (String identifier : whiteLatexIdentifiers) {
            // Check ResourceLocation format (modid:entity_name)
            if (identifier.contains(":") && !identifier.startsWith("net.")) {
                if (entityTypeId.equals(identifier) || entityTypeId.contains(identifier)) {
                    return true;
                }
            }
            // Check full class name
            else if (identifier.contains(".")) {
                if (className.equals(identifier) || className.startsWith(identifier)) {
                    return true;
                }
            }
            // Check partial class name (contains)
            else {
                if (className.toLowerCase().contains(identifier.toLowerCase()) ||
                    simpleClassName.toLowerCase().contains(identifier.toLowerCase())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if an entity belongs to the Dark Latex team based on configuration.
     */
    public static boolean isDarkLatex(net.minecraft.world.entity.LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        
        EntityType<?> entityType = entity.getType();
        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        String className = entity.getClass().getName();
        String simpleClassName = entity.getClass().getSimpleName();
        
        // Check against configured identifiers
        for (String identifier : darkLatexIdentifiers) {
            // Check ResourceLocation format (modid:entity_name)
            if (identifier.contains(":") && !identifier.startsWith("net.")) {
                if (entityTypeId.equals(identifier) || entityTypeId.contains(identifier)) {
                    return true;
                }
            }
            // Check full class name
            else if (identifier.contains(".")) {
                if (className.equals(identifier) || className.startsWith(identifier)) {
                    return true;
                }
            }
            // Check partial class name (contains)
            else {
                if (className.toLowerCase().contains(identifier.toLowerCase()) ||
                    simpleClassName.toLowerCase().contains(identifier.toLowerCase())) {
                    return true;
                }
            }
        }
        
        return false;
    }
}

