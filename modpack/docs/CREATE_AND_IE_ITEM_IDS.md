# Create & Immersive Engineering — item IDs (cheat sheet)

**Minecraft 1.20.1 Forge** — typical registry names. **Always verify in JEI** or `/give <namespace>:` + tab; patches can rename items.

---

## Create (`create:`)

### Early game — foundations & first rotation

| Category        | Item ID |
|----------------|---------|
| Core alloy     | `create:andesite_alloy` |
| Rotation parts | `create:shaft` |
| Gears          | `create:cogwheel`, `create:large_cogwheel` |
| Simple SU      | `create:water_wheel`, `create:windmill_bearing`, `create:hand_crank` |
| Basic casing   | `create:andesite_casing` |

### Mid game — brass & automation

| Category        | Item ID |
|----------------|---------|
| Brass line     | `create:brass_ingot`, `create:brass_casing` |
| Zinc (if used) | `create:zinc_ingot` |
| Logistics      | `create:depot`, `create:chute`, `create:smart_chute`, `create:brass_tunnel` |
| Belts          | `create:belt_connector` |
| Processing     | `create:mechanical_press`, `create:mixer`, `create:basin` |
| Deployer       | `create:deployer` |
| Assembly output| `create:precision_mechanism` *(usually via sequenced assembly)* |

### Late game — electronics & heavy parts

| Category     | Item ID |
|-------------|---------|
| Electronics | `create:electron_tube` |
| Sturdy sheet| `create:sturdy_sheet` *(if present in your Create version — check JEI)* |

> **Addons:** Items like refined radiance / shadow steel are often from **other mods**, not base Create — search JEI before using in recipes.

---

## Immersive Engineering (`immersiveengineering:`)

### Early game — coke, steel, plates

| Category   | Item ID |
|-----------|---------|
| Hammer    | `immersiveengineering:hammer` |
| Fuel coke | `immersiveengineering:coal_coke` |
| Steel     | `immersiveengineering:ingot_steel` |
| Plates    | `immersiveengineering:plate_iron`, `immersiveengineering:plate_copper`, `immersiveengineering:plate_steel`, `immersiveengineering:plate_aluminum` |
| Wire coils| `immersiveengineering:wirecoil_copper`, `immersiveengineering:wirecoil_electrum` *(tiers vary)* |
| Capacitor | `immersiveengineering:capacitor_lv` *(optional early storage)* |

### Mid game — power, wiring, machines

| Category        | Item ID |
|----------------|---------|
| Kinetic power  | `immersiveengineering:waterwheel`, `immersiveengineering:windmill`, `immersiveengineering:dynamo` *(item forms; multiblock placement)* |
| Connectors     | `immersiveengineering:connector_lv`, `immersiveengineering:relay_lv` *(MV/HV variants exist)* |
| Engineering    | `immersiveengineering:light_engineering`, `immersiveengineering:heavy_engineering` |
| Electronics    | `immersiveengineering:electron_tube` |
| Processing     | `immersiveengineering:metal_press`, `immersiveengineering:assembler`, `immersiveengineering:arc_furnace` *(multiblocks — check projector/blueprint items in JEI)* |

### Late game — HV & heavy industry

| Category     | Item ID |
|-------------|---------|
| Heavy wiring| `immersiveengineering:wirecoil_steel` *(and HV connector/relay names — check JEI)* |
| Capacitors  | `immersiveengineering:capacitor_mv`, `immersiveengineering:capacitor_hv` *(if present)* |
| Bulk steel  | `immersiveengineering:plate_steel`, `immersiveengineering:ingot_steel` *(still core)* |

---

## Cross-mod progression (how to use this doc)

- **Early:** Furmutage recipes can ask for `andesite_alloy`, basic IE **plates** / **coal coke** path.
- **Mid:** Require `create:brass_ingot`, `create:precision_mechanism`, IE **steel plates** + **electron_tube**.
- **Late:** Require `create:electron_tube`, IE **heavy_engineering**, **capacitor_hv**, extra **plate_steel**.

Related pack files:

- `modpack/kubejs/server_scripts/001_changed_endgame.js` — Changed endgame using Create + IE + Furmutage.
- `modpack/tacz_gunpack/` — TaCZ gun/ammo recipes tiered with Create + IE + Furmutage.
- `modpack/docs/MODPACK_PROGRESSION.md` — chapter-style progression outline.

---

*Last updated for reference; verify IDs in-game before shipping a modpack.*
