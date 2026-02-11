# Optional Enhanced Hordes datapacks (Furmutage)

The **Furmutage** mod already adds **Minecraft**, **Changed**, and **Furmutage** entities to the `forge:hordes` tag (Enhanced Hordes). Those are always on.

**Auto-include (no datapack needed):** The mod’s built-in tag also lists entities from Spore, The Flesh That Hates, The Ravenous, Undead Revamp 2, Zoniex, and Zombie Extreme with `"required": false`. Forge only adds an entry if that entity exists in the registry (i.e. the mod is installed). So if you have e.g. Spore installed, its entities are automatically in the hordes tag; if you don’t, they’re skipped. No separate datapacks needed for that.

These optional datapacks are for players who prefer **manual control**: add only the zip(s) for the mods you want, enable them in the world’s datapack list, and leave the rest out. Use them if you don’t want the mod to reference optional mod IDs at all, or if you want to enable/disable by world.

## How to use (players)

1. **Zip** the folder of the datapack you want (e.g. `furmutage_hordes_spore` → `furmutage_hordes_spore.zip`). The zip must contain `pack.mcmeta` and `data/` at the root (no extra parent folder).
2. Put the zip in your world’s `datapacks` folder:  
   `saves/<WorldName>/datapacks/`
3. (Optional) For a new world: put the zip in the `datapacks` folder **before** creating the world, or use **Edit** on an existing world and add it.
4. In-game: **Esc → Open LAN (or use existing world) → Datapacks**, or when creating a world, open **Datapacks** and move the pack from “Available” to “Selected”.
5. Reload (or restart the world). Only enable a datapack if you have that mod installed.

## Packs included

| Folder | For mod |
|--------|--------|
| `furmutage_hordes_spore` | Spore |
| `furmutage_hordes_the_flesh_that_hates` | The Flesh That Hates |
| `furmutage_hordes_the_ravenous` | The Ravenous |
| `furmutage_hordes_undead_revamp2` | Undead Revamp 2 |
| `furmutage_hordes_zoniex` | Zoniex |
| `furmutage_hordes_zombie_extreme` | Zombie Extreme |

## For modpack / release

- Zip each folder so the **contents** of the folder are at the root of the zip (e.g. `furmutage_hordes_spore.zip` contains `pack.mcmeta` and `data/`, not a single folder containing them).
- Ship these zips with the mod or on the mod’s download page so players can drop the ones they need into `saves/<WorldName>/datapacks/`.
