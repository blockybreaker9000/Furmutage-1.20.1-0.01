package net.jerika.furmutage.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;

import java.util.Set;

/**
 * Uses the Changed mod's transfur system to detect which team a player counts as for targeting.
 * Uses direct API: TransfurVariant.getEntityVariant(player), variant.getLatexType(), and
 * form-ID fallbacks so team targeting works reliably.
 */
public final class TransfurTeamHelper {

    /** Set to true to log unknown form IDs (for debugging). Logs at most once per 20 seconds. */
    public static final boolean DEBUG_TRANSFUR_IDS = true;

    /** Not transfurred or form not in lists → neither team targets the player. */
    public static final int TEAM_NONE = 0;
    /** Player transfurred as a form in WHITE_FORMS → dark team will target. */
    public static final int TEAM_WHITE = 1;
    /** Player transfurred as a form in DARK_FORMS → white team will target. */
    public static final int TEAM_DARK = 2;

    private static long lastDebugLogTime = 0;
    private static final long DEBUG_LOG_INTERVAL_MS = 20_000;

    /** Player transfurred as any of these (or variant, e.g. .../male) → dark team is aggressive to them. */
    private static final Set<String> WHITE_FORMS = Set.of(
            "changed:pure_white_latex_wolf",
            "changed:latex_mutant_bloodcell_wolf",
            "changed:latex_snake",
            "changed:headless_knight",
            "changed:white_latex_knight_fusion",
            "changed:white_latex_wolf_female",
            "changed:white_latex_wolf_male",
            "changed:white_latex_wolf",
            "changed:white_latex_knight",
            "changed:white_latex_centaur",
            "changed:behemoth_head",
            "changed:behemoth_hand_left",
            "changed:behemoth_hand_right",
            "changed:milk_pudding",
            "changed:pure_white_latex_wolf_pup",
            "changed:custom_latex"
    );

    /** Player transfurred as any of these (or variant) → white team is aggressive to them. */
    private static final Set<String> DARK_FORMS = Set.of(
            "changed:dark_latex_wolf_male",
            "changed:dark_latex_wolf_female",
            "changed:dark_latex_wolf_partial",
            "changed:dark_latex_wolf_pup",
            "changed:dark_latex_wolf",
            "changed:dark_latex_yufeng",
            "changed:latex_benign_wolf",
            "changed:phage_latex_wolf_female",
            "changed:phage_latex_wolf_male",
            "changed:phage_latex_wolf",
            "changed:dark_latex_double_yufeng",
            "changed:latex_crow"
    );

    /**
     * Returns the player's effective team for targeting based on their current transfur.
     * 1) Tries LatexType.getEntityLatexType(player) when available (canonical).
     * 2) Uses TransfurVariant.getEntityVariant(player).getLatexType() and compares to WHITE_LATEX/DARK_LATEX.
     * 3) Fallback: variant.getLatexType().isHostileTo(WHITE/DARK).
     * 4) Fallback: form-ID list matching.
     */
    public static int getPlayerTransfurTeam(Player player) {
        if (player == null) return TEAM_NONE;
        try {
            // First: use Changed's getEntityLatexType(entity) when available (same as ChangedEntity targeting)
            int teamFromEntityLatexType = getTeamFromEntityLatexType(player);
            if (teamFromEntityLatexType != TEAM_NONE) return teamFromEntityLatexType;

            // Second: get variant and use its latex type (variant may return a different type than LatexType)
            TransfurVariant<?> variant = TransfurVariant.getEntityVariant(player);
            if (variant == null) return TEAM_NONE;

            Object latexType = variant.getLatexType();
            if (latexType != null) {
                // Use hostility for team detection (works across LatexType / LatexTypeOld)
                if (isHostileTo(latexType, ChangedLatexTypes.WHITE_LATEX.get())) return TEAM_DARK;
                if (isHostileTo(latexType, ChangedLatexTypes.DARK_LATEX.get())) return TEAM_WHITE;
            }

            // Fallback: form-ID list matching (for forms not in WHITE_LATEX/DARK_LATEX or custom)
            String entityTypeId = getVariantEntityTypeId(variant);
            if (entityTypeId != null && !entityTypeId.isEmpty()) {
                entityTypeId = entityTypeId.toLowerCase().trim();
                if (matchesWhiteForm(entityTypeId)) return TEAM_WHITE;
                if (matchesDarkForm(entityTypeId)) return TEAM_DARK;
                if (DEBUG_TRANSFUR_IDS) debugLogVariantIds(player, variant, entityTypeId, TEAM_NONE);
            }
            return TEAM_NONE;
        } catch (Throwable t) {
            if (DEBUG_TRANSFUR_IDS && player != null) {
                long now = System.currentTimeMillis();
                if (now - lastDebugLogTime > DEBUG_LOG_INTERVAL_MS) {
                    lastDebugLogTime = now;
                    net.jerika.furmutage.furmutage.LOGGER.warn("[TransfurTeamHelper] Error getting transfur team for {}: {} - {}", player.getName().getString(), t.getClass().getSimpleName(), t.getMessage());
                }
            }
            return TEAM_NONE;
        }
    }

    /**
     * Uses LatexType.getEntityLatexType(entity) when available — same as ChangedEntity targeting.
     */
    private static int getTeamFromEntityLatexType(Player player) {
        try {
            LatexType playerLatexType = LatexType.getEntityLatexType(player);
            if (playerLatexType == null) return TEAM_NONE;
            if (playerLatexType == ChangedLatexTypes.WHITE_LATEX.get()) return TEAM_WHITE;
            if (playerLatexType == ChangedLatexTypes.DARK_LATEX.get()) return TEAM_DARK;
            return TEAM_NONE;
        } catch (Throwable t) {
            return TEAM_NONE;
        }
    }

    /** Call isHostileTo(other) on the latex type (works for both LatexType and LatexTypeOld). */
    private static boolean isHostileTo(Object latexType, Object other) {
        if (latexType == null || other == null) return false;
        try {
            java.lang.reflect.Method m = latexType.getClass().getMethod("isHostileTo", Object.class);
            Object result = m.invoke(latexType, other);
            return Boolean.TRUE.equals(result);
        } catch (Throwable t) {
            return false;
        }
    }

    /** Get entity type ID from variant for form list matching (e.g. "changed:dark_latex_wolf_male"). */
    private static String getVariantEntityTypeId(TransfurVariant<?> variant) {
        if (variant == null) return null;
        try {
            var entityType = variant.getEntityType();
            if (entityType != null) {
                ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
                if (key != null) return key.toString();
            }
        } catch (Throwable ignored) { }
        try {
            ResourceLocation formId = variant.getFormId();
            if (formId != null) return formIdToEntityTypeId(formId.toString());
        } catch (Throwable ignored) { }
        String ts = variant.toString();
        if (ts != null && (ts.contains("form_") || ts.contains("changed:"))) {
            int start = ts.indexOf("changed:");
            if (start >= 0) {
                int end = ts.length();
                for (int i = start; i < ts.length(); i++) {
                    char ch = ts.charAt(i);
                    if (ch == ' ' || ch == ']' || ch == ')') { end = i; break; }
                }
                return ts.substring(start, end);
            }
        }
        return null;
    }

    /** Convert form ID to entity-type style ID for list matching. */
    private static String formIdToEntityTypeId(String id) {
        if (id == null) return id;
        String s = id;
        if (s.contains("form_")) s = s.replace("form_", "");
        if (s.contains("/")) s = s.replace("/", "_");
        return s;
    }

    /** Log unknown form for debugging. Throttled. */
    private static void debugLogVariantIds(Player player, TransfurVariant<?> variant, String entityTypeId, int resultTeam) {
        long now = System.currentTimeMillis();
        if (now - lastDebugLogTime < DEBUG_LOG_INTERVAL_MS) return;
        lastDebugLogTime = now;
        StringBuilder sb = new StringBuilder();
        sb.append("[TransfurTeamHelper] Transfur ID debug for ").append(player.getName().getString()).append(":\n");
        sb.append("  entityTypeId = ").append(entityTypeId).append("\n");
        sb.append("  result team = ").append(resultTeam == TEAM_WHITE ? "WHITE" : resultTeam == TEAM_DARK ? "DARK" : "NONE").append("\n");
        if (variant != null) sb.append("  variant = ").append(variant.toString()).append("\n");
        net.jerika.furmutage.furmutage.LOGGER.info(sb.toString());
    }

    /** Exact match or prefix match (e.g. "changed:dark_latex_wolf" matches "changed:dark_latex_wolf_male"). */
    private static boolean matchesWhiteForm(String entityTypeId) {
        for (String form : WHITE_FORMS) {
            if (entityTypeId.equals(form)) return true;
            if (form.startsWith(entityTypeId + "_") || entityTypeId.startsWith(form + "_")) return true;
        }
        return false;
    }

    private static boolean matchesDarkForm(String entityTypeId) {
        for (String form : DARK_FORMS) {
            if (entityTypeId.equals(form)) return true;
            if (form.startsWith(entityTypeId + "_") || entityTypeId.startsWith(form + "_")) return true;
        }
        return false;
    }
}
