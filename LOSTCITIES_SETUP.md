# LostCities Dependency Setup

## How to Add LostCities as a Dependency

LostCities is configured to use CurseMaven, but you need to find the correct file ID for a version compatible with Forge 47.4.0.

### Steps:

1. **Go to LostCities CurseForge page for 1.20.1:**
   - Visit: https://www.curseforge.com/minecraft/mc-mods/lost-cities/files/all?filter-game-version=1738749986%3A6756

2. **Find a compatible version:**
   - Look for versions that support Forge 47.4.0 (or close to it)
   - Check the version description for compatibility info

3. **Get the File ID:**
   - Click on a compatible version
   - Look at the URL - it will be something like: `.../files/5123456`
   - The number at the end (e.g., `5123456`) is the file ID

4. **Update build.gradle:**
   - Open `build.gradle`
   - Find the line: `runtimeOnly fg.deobf("curse.maven:lost-cities-269024:5123456")`
   - Replace `5123456` with your actual file ID
   - Do the same for the `compileOnly` line

5. **Sync Gradle:**
   - Run `./gradlew build` or sync your IDE

### Alternative: Local JAR Approach

If CurseMaven doesn't work, you can use the local JAR approach:

1. Download LostCities JAR from CurseForge
2. Place it in `libs/lostcities/` folder
3. Update `build.gradle` to use:
   ```gradle
   implementation fg.deobf("lostcities:lostcities:VERSION") {
       exclude group: "com.github.mcjty", module: "mcjtylib"
   }
   ```
   Replace `VERSION` with the actual version from the JAR filename

### Note on Mapping Compatibility

If you get mapping errors (like `NoSuchFieldError: f_256833_`), the version you chose has incompatible mappings. Try a different version that was compiled with mappings compatible with Forge 47.4.0 and parchment 2023.06.26-1.20.1.
