package net.jerika.furmutage.config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

/**
 * Uses the Changed mod's transfur system to detect which team a player counts as for targeting.
 * Uses Changed's LatexType.getEntityLatexType(Entity) first (same as ChangedEntity targeting and
 * EntityUtil.maybeGetOverlaying), then variant hostility / form-ID fallbacks.
 */
public final class TransfurTeamHelper {

    /** Set to true to log what IDs we get from Changed (for debugging). Logs at most once per 20 seconds. */
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
     * 1) Uses Changed's LatexType.getEntityLatexType(player) — same as ChangedEntity.targetSelectorTest
     *    and EntityUtil.maybeGetOverlaying (overlaying ChangedEntity's getLatexType()).
     * 2) Fallback: variant getLatexType().isHostileTo(WHITE/DARK).
     * 3) Fallback: form-ID list matching.
     */
    public static int getPlayerTransfurTeam(Player player) {
        if (player == null) return TEAM_NONE;
        try {
            // First: use Changed's getEntityLatexType(entity) — canonical source for "what latex type is this entity?"
            int teamFromEntityLatexType = getTeamFromEntityLatexType(player);
            if (teamFromEntityLatexType != TEAM_NONE) return teamFromEntityLatexType;

            Object variant = getVariantFromPlayer(player);
            if (variant == null) return TEAM_NONE;

            // Second: variant's getLatexType().isHostileTo(WHITE/DARK)
            int teamFromLatexType = getTeamFromLatexTypeHostility(variant);
            if (teamFromLatexType != TEAM_NONE) return teamFromLatexType;

            // Fallback: form-ID list matching
            String id = getVariantId(variant);
            if (id == null || id.isEmpty()) return TEAM_NONE;
            String entityTypeId = formIdToEntityTypeId(id);
            if (entityTypeId == null || entityTypeId.isEmpty()) return TEAM_NONE;
            entityTypeId = entityTypeId.toLowerCase().trim();
            if (matchesWhiteForm(entityTypeId)) return TEAM_WHITE;
            if (matchesDarkForm(entityTypeId)) return TEAM_DARK;
            if (DEBUG_TRANSFUR_IDS) debugLogVariantIds(player, variant, id, entityTypeId, TEAM_NONE);
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
     * Uses LatexType.getEntityLatexType(entity) — same as ChangedEntity and EntityUtil.maybeGetOverlaying.
     * For a transfurred player this returns the overlaying ChangedEntity's latex type.
     */
    private static int getTeamFromEntityLatexType(Player player) {
        try {
            Class<?> latexTypeClass = Class.forName("net.ltxprogrammer.changed.entity.latex.LatexType");
            java.lang.reflect.Method getEntityLatexType = latexTypeClass.getMethod("getEntityLatexType", Entity.class);
            Object playerLatexType = getEntityLatexType.invoke(null, player);
            if (playerLatexType == null) return TEAM_NONE;

            Object whiteType = getChangedLatexType("WHITE_LATEX");
            Object darkType = getChangedLatexType("DARK_LATEX");
            if (whiteType == null || darkType == null) return TEAM_NONE;

            if (playerLatexType == whiteType) return TEAM_WHITE;
            if (playerLatexType == darkType) return TEAM_DARK;
            return TEAM_NONE;
        } catch (Throwable t) {
            return TEAM_NONE;
        }
    }

    /** Get the player's current latex variant from LatexVariantInstance (same as addon block detection). */
    private static Object getVariantFromPlayer(Player player) {
        try {
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            if (instance == null) return null;
            java.lang.reflect.Method getVariantMethod = instanceClass.getMethod("getLatexVariant");
            return getVariantMethod.invoke(instance);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Use Changed's getLatexType().isHostileTo() like DarkLatexClumpItem / WhiteLatexClumpItem.
     * If variant.getLatexType().isHostileTo(WHITE_LATEX) → player is dark team. If isHostileTo(DARK_LATEX) → white team.
     */
    private static int getTeamFromLatexTypeHostility(Object variant) {
        if (variant == null) return TEAM_NONE;
        try {
            java.lang.reflect.Method getLatexType = variant.getClass().getMethod("getLatexType");
            Object latexType = getLatexType.invoke(variant);
            if (latexType == null) return TEAM_NONE;

            Object whiteType = getChangedLatexType("WHITE_LATEX");
            Object darkType = getChangedLatexType("DARK_LATEX");
            if (whiteType == null || darkType == null) return TEAM_NONE;

            java.lang.reflect.Method isHostileTo = latexType.getClass().getMethod("isHostileTo", Object.class);
            Boolean hostileToWhite = (Boolean) isHostileTo.invoke(latexType, whiteType);
            Boolean hostileToDark = (Boolean) isHostileTo.invoke(latexType, darkType);
            if (Boolean.TRUE.equals(hostileToWhite)) return TEAM_DARK;  // player form is hostile to white → dark team
            if (Boolean.TRUE.equals(hostileToDark)) return TEAM_WHITE;   // player form is hostile to dark → white team
            return TEAM_NONE;
        } catch (Throwable t) {
            return TEAM_NONE;
        }
    }

    /** Get ChangedLatexTypes.WHITE_LATEX.get() or DARK_LATEX.get() via reflection. */
    private static Object getChangedLatexType(String fieldName) {
        try {
            Class<?> typesClass = Class.forName("net.ltxprogrammer.changed.init.ChangedLatexTypes");
            java.lang.reflect.Field field = typesClass.getField(fieldName);
            Object registryObject = field.get(null);
            if (registryObject == null) return null;
            java.lang.reflect.Method getMethod = registryObject.getClass().getMethod("get");
            return getMethod.invoke(registryObject);
        } catch (Throwable t) {
            return null;
        }
    }

    /** Log what we got from Changed so we can match it. Throttled to once per DEBUG_LOG_INTERVAL_MS. */
    private static void debugLogVariantIds(Player player, Object variant, String rawId, String entityTypeId, int resultTeam) {
        long now = System.currentTimeMillis();
        if (now - lastDebugLogTime < DEBUG_LOG_INTERVAL_MS) return;
        lastDebugLogTime = now;
        StringBuilder sb = new StringBuilder();
        sb.append("[TransfurTeamHelper] Transfur ID debug for ").append(player.getName().getString()).append(":\n");
        sb.append("  variant.getClass() = ").append(variant == null ? "null" : variant.getClass().getName()).append("\n");
        sb.append("  raw id from getVariantId() = ").append(rawId).append("\n");
        sb.append("  entityTypeId after formIdToEntityTypeId = ").append(entityTypeId).append("\n");
        sb.append("  result team = ").append(resultTeam == TEAM_WHITE ? "WHITE" : resultTeam == TEAM_DARK ? "DARK" : "NONE").append("\n");
        if (variant != null) {
            Class<?> c = variant.getClass();
            for (String methodName : new String[]{"getEntityType", "getFormId", "getRegistryName", "getId", "getKey", "getParent", "getForm"}) {
                try {
                    java.lang.reflect.Method m = c.getMethod(methodName);
                    Object r = m.invoke(variant);
                    sb.append("  ").append(methodName).append("() = ").append(r == null ? "null" : r + " [" + r.getClass().getSimpleName() + "]").append("\n");
                } catch (NoSuchMethodException e) {
                    sb.append("  ").append(methodName).append("() = (no such method)\n");
                } catch (Throwable t) {
                    sb.append("  ").append(methodName).append("() = ERROR: ").append(t.getMessage()).append("\n");
                }
            }
            sb.append("  variant.toString() = ").append(variant.toString()).append("\n");
        }
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

    /**
     * Convert Changed form ID to entity type ID so we can look up in LatexTeamConfig.
     * e.g. "changed:form_pure_white_latex_wolf" -> "changed:pure_white_latex_wolf"
     *      "changed:form_dark_latex_wolf/male" -> "changed:dark_latex_wolf_male"
     */
    private static String formIdToEntityTypeId(String id) {
        if (id == null) return id;
        String s = id;
        if (s.contains("form_")) s = s.replace("form_", "");
        if (s.contains("/")) s = s.replace("/", "_");
        return s;
    }

    private static String getVariantId(Object variant) {
        if (variant == null) return null;
        Class<?> c = variant.getClass();
        // Prefer getEntityType() -> getKey().location() for direct entity type ID
        try {
            java.lang.reflect.Method m = c.getMethod("getEntityType");
            Object et = m.invoke(variant);
            if (et != null) {
                java.lang.reflect.Method getKey = et.getClass().getMethod("getKey");
                Object key = getKey.invoke(et);
                if (key != null) {
                    java.lang.reflect.Method loc = key.getClass().getMethod("location");
                    Object rl = loc.invoke(key);
                    if (rl != null) return rl.toString();
                }
            }
        } catch (Throwable ignored) { }
        String s = invokeStringOrResourceLocation(c, variant, "getFormId");
        if (s != null) return s;
        s = invokeStringOrResourceLocation(c, variant, "getRegistryName");
        if (s != null) return s;
        s = invokeStringOrResourceLocation(c, variant, "getId");
        if (s != null) return s;
        try {
            java.lang.reflect.Method m = c.getMethod("getKey");
            Object key = m.invoke(variant);
            if (key != null) {
                java.lang.reflect.Method loc = key.getClass().getMethod("location");
                Object rl = loc.invoke(key);
                if (rl != null) return rl.toString();
            }
        } catch (Throwable ignored) { }
        // Fallback: toString() might be "FormId [changed:form_dark_latex_wolf]" or similar
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
            return ts;
        }
        return null;
    }

    private static String invokeStringOrResourceLocation(Class<?> c, Object variant, String methodName) {
        try {
            java.lang.reflect.Method m = c.getMethod(methodName);
            Object r = m.invoke(variant);
            if (r == null) return null;
            if (r instanceof String) return (String) r;
            return r.toString();
        } catch (Throwable ignored) { }
        return null;
    }
}
