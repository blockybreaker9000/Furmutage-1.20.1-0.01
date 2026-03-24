# Furmutage TACZ — gun & ammo recipe progression

This is a **TaCZ gun pack** that only adds **recipes** (no new gun models). It retunes the **default `tacz:` guns** so crafting them needs **Furmutage**, **Create**, and **Immersive Engineering** items in order.

## Install (your CurseForge instance)

1. Copy the folder **`furmutage_tacz_progression`** into:
   ```
   C:\Users\Jeremiah Nolan\curseforge\minecraft\Instances\Furmutage Survival Modpack\tacz\
   ```
   You should end up with:
   ```
   ...\Furmutage Survival Modpack\tacz\furmutage_tacz_progression\tacz\pack.json
   ```
2. **Or** zip the **`furmutage_tacz_progression`** folder (so the zip contains `tacz\pack.json` at the root of the archive — same layout as the folder) and put the `.zip` in the same `tacz` folder, if your TaCZ build only loads zips.
3. Run **`/tacz reload`** or restart the game.

## If a gun does not exist in your pack

TaCZ default gun IDs vary by version. Open the gun workbench / JEI and check the gun’s ID.

- Rename or duplicate the matching JSON in `tacz/recipes/gun/` to the **file name** that gun uses (e.g. `hk_mp5a5.json`).
- Set `"id": "tacz:that_gun_id"` in the `result` block.

## Progression summary

| Tier | Example file   | Theme |
|------|----------------|--------|
| 1    | `m1911.json`   | Vanilla metals + **Furmutage** TSC parts |
| 2    | `uzi.json`     | + **Create** (brass, andesite alloy, cogwheel) |
| 3    | `ak47.json`    | + **Immersive Engineering** steel + circuits |
| 4    | `ai_awp.json`  | Endgame: **IE** heavy parts + **Furmutage** thunderium / advanced tech |

**Ammo** `9mm.json` is a simple stepped cost using the same mods.

Remove any JSON you don’t want or that errors on load.
