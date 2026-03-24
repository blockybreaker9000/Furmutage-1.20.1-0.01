# Modpack scripts (KubeJS + CraftTweaker)

These files are **not** loaded by the Furmutage mod JAR. They are meant to be copied into a **modpack instance** that has:

- **KubeJS** (Forge 1.20.1) **or**
- **CraftTweaker** (Forge 1.20.1)

**Do not enable both** `changed_endgame` scripts at once or you will get duplicate recipes.

## Install — KubeJS

1. Install **KubeJS** for Minecraft **1.20.1** Forge.
2. Copy the contents of `kubejs/` into your instance folder (merge with existing `kubejs/`):
   - `server_scripts/001_changed_endgame.js`
   - `server_scripts/000_create_ie_progression.js` (optional examples)
3. `/reload` on a server or restart the game.

## Install — CraftTweaker

1. Install **CraftTweaker** for **1.20.1** Forge.
2. Copy `scripts/changed_endgame.zs` into the instance `scripts/` folder.
3. Run `/ct reload` or restart.

## What these scripts do

- **Removes** the default Changed recipes for:
  - `changed:stasis_chamber`
  - `changed:exoskeleton`
  - `changed:exoskeleton_charger`
- **Adds** new **endgame** shaped recipes using **Create**, **Immersive Engineering**, **Furmutage** parts, and **nether stars** / heavy components.

If any item ID does not match your mod versions, edit the scripts (comments mark the spots).

See `docs/MODPACK_PROGRESSION.md` for progression ideas between Create and IE.

## TaCZ gun progression (Furmutage + Create + IE)

See **`tacz_gunpack/README.md`** — copy `furmutage_tacz_progression` into your instance’s **`tacz`** folder (e.g. `...\Instances\Furmutage Survival Modpack\tacz\`).
