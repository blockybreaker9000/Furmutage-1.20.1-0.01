package net.jerika.furmutage.datagen;

import java.io.IOException;

/**
 * Example usage of ChangedStyleHumanoidGenerator
 * 
 * This generator creates entities that use Changed mod's HumanoidAnimator and AnimatorPresets
 * for professional, high-quality animations.
 * 
 * Requirements:
 * - Changed mod must be a dependency in build.gradle
 * - Entities extend ChangedEntity (from Changed mod)
 * - Models use AdvancedHumanoidModel and HumanoidAnimator
 * - Renderers use AdvancedHumanoidRenderer
 */
public class ExampleChangedStyleGenerator {
    
    public static void main(String[] args) throws IOException {
        // Example 1: Latex Wolf - Using wolfLike preset
        ChangedStyleHumanoidGenerator.ChangedEntityConfig Wolf =
            new ChangedEntityConfigBuilder("LatexWolf")
                .description("A latex wolf using Changed mod's animation system")
                .size(0.7f, 1.93f)
                .attributes(30.0, 0.3, 5.0, 32.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .wolfLike()  // Automatically sets up wolf-like animations with ears, tail, digitigrade legs
                .hipOffset(-1.5f)
                .shadowRadius(0.5f)
                .build();
        
        ChangedStyleHumanoidGenerator.generateChangedStyleEntity(Wolf);
        
        System.out.println("\n=== Generated Latex Wolf ===");
        
        // Example 2: Pure White Latex Cat - Using catLike preset meow
        ChangedStyleHumanoidGenerator.ChangedEntityConfig whiteCat = 
            new ChangedEntityConfigBuilder("PureWhiteLatexCat")
                .description("A pure white latex cat - graceful and agile")
                .size(0.6f, 1.8f)
                .attributes(25.0, 0.35, 4.0, 28.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .catLike()  // Cat-like animations
                .hipOffset(-1.5f)
                .build();
        
        // Uncomment to generate:
        // ChangedStyleHumanoidGenerator.generateChangedStyleEntity(whiteCat);
        
        // Example 3: Latex Dragon - Using dragonLike preset rawr
        ChangedStyleHumanoidGenerator.ChangedEntityConfig latexDragon = 
            new ChangedEntityConfigBuilder("LatexDragon")
                .description("A powerful latex dragon")
                .size(1.0f, 2.2f)
                .attributes(50.0, 0.3, 8.0, 40.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .dragonLike()  // Dragon-like animations
                .hasTail(true, 4)  // Extra long tail with 4 joints
                .hipOffset(-1.5f)
                .build();
        
        // Uncomment to generate:
        // ChangedStyleHumanoidGenerator.generateChangedStyleEntity(latexDragon);
        
        // Example 4: Latex Human - Using humanLike preset (no tail, no ears, plantigrade legs) human sounds
        ChangedStyleHumanoidGenerator.ChangedEntityConfig latexHuman = 
            new ChangedEntityConfigBuilder("LatexHuman")
                .description("A latex entity with human-like proportions")
                .size(0.6f, 1.8f)
                .attributes(20.0, 0.25, 2.0, 24.0)
                .category(EntityGenerator.MobCategory.CREATURE)
                .humanLike()  // Human-like animations (no tail, plantigrade)
                .build();
        
        // Uncomment to generate:
        // ChangedStyleHumanoidGenerator.generateChangedStyleEntity(latexHuman);
        
        // Example 5: Custom preset configuration
        ChangedStyleHumanoidGenerator.ChangedEntityConfig customEntity = 
            new ChangedEntityConfigBuilder("CustomLatexEntity")
                .description("Custom entity with manual preset configuration")
                .size(0.7f, 1.9f)
                .attributes(30.0, 0.3, 5.0, 32.0)
                .preset(ChangedStyleHumanoidGenerator.AnimatorPresetType.WOLF_LIKE_ARMOR)
                .hasEars(true)
                .hasTail(true, 2)  // Only 2 tail joints
                .digitigradeLegs(true)
                .hipOffset(-1.5f)
                .hasArmorSupport(true)  // Enable armor model support
                .build();
        
        // Uncomment to generate:
        // ChangedStyleHumanoidGenerator.generateChangedStyleEntity(customEntity);
        
        System.out.println("\n=== Usage Instructions ===");
        System.out.println("1. Ensure Changed mod is a dependency in build.gradle:");
        System.out.println("   dependencies {");
        System.out.println("       implementation fg.deobf(\"net.ltxprogrammer.changed:changed:\" + changed_version)");
        System.out.println("   }");
        System.out.println("2. Configure your entity using ChangedEntityConfigBuilder");
        System.out.println("3. Call ChangedStyleHumanoidGenerator.generateChangedStyleEntity(config)");
        System.out.println("4. Copy registration snippets to appropriate files");
        System.out.println("5. Create or export model from Blockbench");
        System.out.println("6. Create texture file");
    }
}

