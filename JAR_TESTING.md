# Testing the mod as a JAR (Forge launcher)

## Build the JAR

From project root:

```bash
./gradlew build
```

The mod JAR is in `build/libs/`. Prefer the **plain** JAR (e.g. `furmutage-0.1-1.20.1.jar`); avoid the `-all` and `-sources` JARs unless you need the bundled-deps build.

## Before running

1. **Install Forge 1.20.1** (47.4.0 or the version in `gradle.properties`) for your launcher.
2. **Put the Changed mod in `mods/`** – Furmutage **requires** Changed (v0.15.0+). Without it the game may fail to start or crash. Get Changed 1.20.1 from the mod’s page or build from source.
3. Copy your built `furmutage-*.jar` into the same `mods/` folder as Changed.

## If the game crashes

1. **Find the crash output**
   - **Crash report:** `crash-reports/crash-YYYY-MM-DD_*.txt`
   - **Latest log:** `logs/latest.log`
   - Launchers often open the game directory (e.g. “Open output folder” or “Game Dir”) so you can open these from there.

2. **Common causes**
   - **Changed mod missing or wrong version** – Use Changed for 1.20.1, v0.15.0 or compatible.
   - **Forge version** – Use the Forge version referenced in `gradle.properties` (e.g. 47.4.0 for 1.20.1).
   - **Other mod conflicts** – Try with only Furmutage + Changed (+ Forge) first.

3. **What was fixed for JAR crashes**
   - **Reflection on Minecraft code** – Registration of tainted-log stripping no longer uses the literal field name `"STRIPPABLES"` (which fails in an obfuscated production JAR). It now finds the correct field by type so it works in both dev and the built JAR.

If it still crashes, open the **full** `crash-*.txt` (or the end of `latest.log` around the crash) and share it so the exact error can be fixed.

## “Mixin config couldn’t be applied”

If you see this for `furmutage.mixins.json`:

- The mixin config is now **optional** (`"required": false`), so the game should still start even if one mixin fails (e.g. refmap or Changed class).
- **Use the main JAR** (`furmutage-0.1-1.20.1.jar`), not the `-all` one, so the refmap and resources are correct.
- Ensure **Changed** is in `mods/` and loads (the crystal mixin targets Changed; if Changed is missing or loads later, that mixin can fail).
- Rebuild and replace the JAR in `mods/` after pulling the change.

## Changed + Curios mixin error (CuriosApi.getEntitySlots)

If you get:

```text
InvalidInjectionException: @WrapMethod ... CuriosApiMixin ... getEntitySlots ...
Expected Lnet/minecraft/world/entity/EntityType; but got Lnet/minecraft/world/entity/LivingEntity;
```

**Cause:** Changed’s Curios compatibility mixin was written for an older Curios API that used `getEntitySlots(EntityType)`. Newer Curios (e.g. 5.3.3+1.20.1) uses `getEntitySlots(LivingEntity)`, so the mixin no longer matches and fails. This is a bug in **Changed**, not in Furmutage.

**Workaround:** Do not use Curios when playing with Changed. Remove Curios from your `mods/` folder. Furmutage does not require Curios. For runClient/dev this project no longer adds Curios to the run. For pack/JAR do not put Curios in mods/ if you use Changed.

- ~~Use Curios 5.7.x (does not fix it)~~
- **Remove Curios** from your `mods/` folder when playing with Changed if you don’t need it. Furmutage does not require Curios to run.
- **Report to Changed** – the Curios compatibility mixin needs to be updated for Curios 5.8+ (`getEntitySlots(LivingEntity)`).

