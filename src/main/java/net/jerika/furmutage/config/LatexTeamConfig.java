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
        // Use the full path: data/<namespace>/latex_teams.json
        // The path should be relative to data/ directory
        super(GSON, "latex_teams");
        LOGGER.info("[LatexTeams] LatexTeamConfig reload listener created with path: latex_teams");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        whiteLatexIdentifiers.clear();
        darkLatexIdentifiers.clear();
        
        LOGGER.info("[LatexTeams] Loading config - found {} resource(s)", resources.size());
        
        if (resources.isEmpty()) {
            LOGGER.warn("[LatexTeams] No latex_teams.json files found via reload listener! Trying manual load...");
            // Try to manually load the config file
            try {
                // ResourceLocation for data files: namespace:path (without data/ prefix)
                // This maps to data/<namespace>/<path>.json
                ResourceLocation configLocation = new ResourceLocation(furmutage.MOD_ID, "latex_teams");
                LOGGER.info("[LatexTeams] Attempting manual load from: {} (maps to data/{}/latex_teams.json)", 
                    configLocation, furmutage.MOD_ID);
                
                var resource = manager.getResource(configLocation);
                if (resource.isPresent()) {
                    try (var inputStream = resource.get().open()) {
                        JsonObject json = GSON.fromJson(new java.io.InputStreamReader(inputStream), JsonObject.class);
                        loadFromJson(json, configLocation);
                        LOGGER.info("[LatexTeams] Successfully loaded config manually from {}", configLocation);
                        return;
                    }
                } else {
                    LOGGER.error("[LatexTeams] Config file not found at: {}", configLocation);
                }
            } catch (Exception e) {
                LOGGER.error("[LatexTeams] Error manually loading config", e);
            }
            
            // Try to list available resources for debugging
            try {
                var allResources = manager.listResources("latex_teams", (location) -> 
                    location.getPath().endsWith(".json"));
                LOGGER.warn("[LatexTeams] Available latex_teams resources: {}", allResources.keySet());
            } catch (Exception e) {
                LOGGER.error("[LatexTeams] Error listing resources", e);
            }
            return;
        }
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            try {
                LOGGER.info("[LatexTeams] Processing config from: {}", entry.getKey());
                JsonObject json = entry.getValue().getAsJsonObject();
                loadFromJson(json, entry.getKey());
            } catch (Exception e) {
                LOGGER.error("[LatexTeams] Error loading latex team config from {}", entry.getKey(), e);
            }
        }
        
        if (whiteLatexIdentifiers.isEmpty() && darkLatexIdentifiers.isEmpty()) {
            LOGGER.error("[LatexTeams] Config loaded but no identifiers found! Check JSON format.");
        }
    }
    
    private void loadFromJson(JsonObject json, ResourceLocation source) {
        loadFromJsonInternal(json, source);
    }
    
    /**
     * Public method to force load config from JSON (for server start fallback).
     */
    public static void forceLoadFromJson(JsonObject json) {
        whiteLatexIdentifiers.clear();
        darkLatexIdentifiers.clear();
        loadFromJsonInternal(json, new ResourceLocation(furmutage.MOD_ID, "latex_teams"));
    }
    
    private static void loadFromJsonInternal(JsonObject json, ResourceLocation source) {
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
        
        LOGGER.info("[LatexTeams] Loaded {} white latex identifiers and {} dark latex identifiers from {}", 
            whiteLatexIdentifiers.size(), darkLatexIdentifiers.size(), source);
        if (whiteLatexIdentifiers.size() > 0) {
            LOGGER.info("[LatexTeams] White Latex identifiers (first 10): {}", 
                whiteLatexIdentifiers.stream().limit(10).toList());
        }
        if (darkLatexIdentifiers.size() > 0) {
            LOGGER.info("[LatexTeams] Dark Latex identifiers (first 10): {}", 
                darkLatexIdentifiers.stream().limit(10).toList());
        }
    }
    
    /**
     * Checks if an entity matches any identifier in the config (either team).
     * This is used to quickly check if we should process an entity at all.
     */
    public static boolean isEntityInConfig(net.minecraft.world.entity.LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        
        // If config isn't loaded, don't process any entities
        if (whiteLatexIdentifiers.isEmpty() && darkLatexIdentifiers.isEmpty()) {
            return false;
        }
        
        EntityType<?> entityType = entity.getType();
        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        String className = entity.getClass().getName();
        String simpleClassName = entity.getClass().getSimpleName();
        
        // Check against all identifiers (both teams)
        java.util.Set<String> allIdentifiers = new java.util.HashSet<>(whiteLatexIdentifiers);
        allIdentifiers.addAll(darkLatexIdentifiers);
        
        for (String identifier : allIdentifiers) {
            // Check ResourceLocation format (modid:entity_name)
            if (identifier.contains(":") && !identifier.startsWith("net.")) {
                if (entityTypeId.equals(identifier)) {
                    return true;
                }
            }
            // Check full class name (exact match or starts with)
            else if (identifier.contains(".") && identifier.startsWith("net.")) {
                if (className.equals(identifier) || className.startsWith(identifier + ".")) {
                    return true;
                }
            }
            // Check partial class name (contains)
            else {
                String lowerIdentifier = identifier.toLowerCase();
                if (className.toLowerCase().contains(lowerIdentifier) ||
                    simpleClassName.toLowerCase().contains(lowerIdentifier) ||
                    entityTypeId.toLowerCase().contains(lowerIdentifier)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if an entity belongs to the White Latex team based on configuration.
     * Only uses the config file - no hardcoded checks.
     */
    public static boolean isWhiteLatex(net.minecraft.world.entity.LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        
        // Check if config is loaded
        if (whiteLatexIdentifiers.isEmpty()) {
            return false; // No config loaded, entity is not on team
        }
        
        EntityType<?> entityType = entity.getType();
        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        String className = entity.getClass().getName();
        String simpleClassName = entity.getClass().getSimpleName();
        
        // Check against configured identifiers only
        for (String identifier : whiteLatexIdentifiers) {
            boolean matched = false;
            // Check ResourceLocation format (modid:entity_name)
            if (identifier.contains(":") && !identifier.startsWith("net.")) {
                if (entityTypeId.equals(identifier)) {
                    matched = true;
                }
            }
            // Check full class name (exact match or starts with)
            else if (identifier.contains(".") && identifier.startsWith("net.")) {
                if (className.equals(identifier) || className.startsWith(identifier + ".")) {
                    matched = true;
                }
            }
            // Check partial class name (contains) - for simple names
            else {
                String lowerIdentifier = identifier.toLowerCase();
                if (className.toLowerCase().contains(lowerIdentifier) ||
                    simpleClassName.toLowerCase().contains(lowerIdentifier) ||
                    entityTypeId.toLowerCase().contains(lowerIdentifier)) {
                    matched = true;
                }
            }
            
            if (matched) {
                LOGGER.debug("[LatexTeams] Entity {} matched White Latex identifier: {}", entityTypeId, identifier);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if an entity belongs to the Dark Latex team based on configuration.
     * Only uses the config file - no hardcoded checks.
     */
    public static boolean isDarkLatex(net.minecraft.world.entity.LivingEntity entity) {
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        
        // Check if config is loaded
        if (darkLatexIdentifiers.isEmpty()) {
            return false; // No config loaded, entity is not on team
        }
        
        EntityType<?> entityType = entity.getType();
        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        String className = entity.getClass().getName();
        String simpleClassName = entity.getClass().getSimpleName();
        
        // Check against configured identifiers only
        for (String identifier : darkLatexIdentifiers) {
            boolean matched = false;
            // Check ResourceLocation format (modid:entity_name)
            if (identifier.contains(":") && !identifier.startsWith("net.")) {
                if (entityTypeId.equals(identifier)) {
                    matched = true;
                }
            }
            // Check full class name (exact match or starts with)
            else if (identifier.contains(".") && identifier.startsWith("net.")) {
                if (className.equals(identifier) || className.startsWith(identifier + ".")) {
                    matched = true;
                }
            }
            // Check partial class name (contains) - for simple names
            else {
                String lowerIdentifier = identifier.toLowerCase();
                if (className.toLowerCase().contains(lowerIdentifier) ||
                    simpleClassName.toLowerCase().contains(lowerIdentifier) ||
                    entityTypeId.toLowerCase().contains(lowerIdentifier)) {
                    matched = true;
                }
            }
            
            if (matched) {
                LOGGER.debug("[LatexTeams] Entity {} matched Dark Latex identifier: {}", entityTypeId, identifier);
                return true;
            }
        }
        
        return false;
    }
}

