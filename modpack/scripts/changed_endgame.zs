// Furmutage modpack — Changed endgame crafts (CraftTweaker)
// Copy into your .minecraft/scripts/
// Requires: changed, create, immersiveengineering, furmutage
// Use EITHER this file OR kubejs 001_changed_endgame.js — not both.
//
// If removal fails, run /ct dump recipes or check EMI/JEI for exact recipe IDs and use removeByName.

// Remove default recipes (Changed uses these IDs in datapack)
craftingTable.removeByName("changed:stasis_chamber");
craftingTable.removeByName("changed:exoskeleton");
craftingTable.removeByName("changed:exoskeleton_charger");

// Stasis chamber
craftingTable.addShaped("furmutage_changed_stasis_chamber_endgame", <item:changed:stasis_chamber>, [
    [<item:immersiveengineering:heavy_engineering>, <item:furmutage:thunderium_block>, <item:immersiveengineering:heavy_engineering>],
    [<item:furmutage:tsc_advanced_tech>, <item:minecraft:nether_star>, <item:furmutage:tsc_gears>],
    [<item:immersiveengineering:heavy_engineering>, <item:furmutage:thunderium_block>, <item:immersiveengineering:heavy_engineering>]
]);

// Exoskeleton
craftingTable.addShaped("furmutage_changed_exoskeleton_endgame", <item:changed:exoskeleton>, [
    [<item:immersiveengineering:plate_steel>, <item:create:precision_mechanism>, <item:immersiveengineering:plate_steel>],
    [<item:changed:laser_emitter>, <item:changed:computer>, <item:changed:laser_emitter>],
    [<item:immersiveengineering:heavy_engineering>, <item:minecraft:netherite_block>, <item:immersiveengineering:heavy_engineering>]
]);

// Exoskeleton charger
craftingTable.addShaped("furmutage_changed_exoskeleton_charger_endgame", <item:changed:exoskeleton_charger>, [
    [<item:create:electron_tube>, <item:immersiveengineering:plate_steel>, <item:create:electron_tube>],
    [<item:immersiveengineering:heavy_engineering>, <item:minecraft:armor_stand>, <item:immersiveengineering:heavy_engineering>],
    [<item:immersiveengineering:plate_aluminum>, <item:immersiveengineering:capacitor_hv>, <item:immersiveengineering:plate_aluminum>]
]);
