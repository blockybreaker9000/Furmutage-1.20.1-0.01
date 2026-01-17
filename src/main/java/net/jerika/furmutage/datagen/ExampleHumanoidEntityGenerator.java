package net.jerika.furmutage.datagen;

import java.io.IOException;

/**
 * Example usage of HumanoidLatexEntityGenerator
 * 
 * This class demonstrates how to use the builder to generate humanoid latex entities
 * with full animation support.
 */
public class ExampleHumanoidEntityGenerator {
    
    public static void main(String[] args) throws IOException {
        // Example 1: Dark Latex Wolf - Hostile entity with all animations
        HumanoidLatexEntityGenerator.HumanoidEntityConfig darkWolf = 
            new HumanoidEntityConfigBuilder("DarkLatexWolf")
                .description("A dark latex wolf entity with full animations")
                .size(0.7f, 1.93f)
                .attributes(30.0, 0.3, 5.0, 32.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .hostile()
                .avoidsWater(true)
                .hasTail(true, true)
                .allAnimations()
                .shadowRadius(0.5f)
                .addCustomImport("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal")
                .build();
        
        HumanoidLatexEntityGenerator.generateHumanoidEntity(darkWolf);
        
        System.out.println("\n=== Generated Dark Latex Wolf ===");
        
        // Example 2: Pure White Latex Deer - Peaceful entity
        HumanoidLatexEntityGenerator.HumanoidEntityConfig whiteDeer = 
            new HumanoidEntityConfigBuilder("PureWhiteLatexDeer")
                .description("A pure white latex deer - peaceful and graceful")
                .size(0.6f, 1.8f)
                .attributes(15.0, 0.25, 0.0, 16.0)
                .category(EntityGenerator.MobCategory.CREATURE)
                .peaceful()
                .hasTail(true, false)
                .animations(true, true, false, true, true)  // No attack animation
                .shadowRadius(0.4f)
                .build();
        
        // Uncomment to generate:
        // HumanoidLatexEntityGenerator.generateHumanoidEntity(whiteDeer);
        
        // Example 3: Latex Tiger - Hostile predator with wall climbing
        HumanoidLatexEntityGenerator.HumanoidEntityConfig latexTiger = 
            new HumanoidEntityConfigBuilder("LatexTiger")
                .description("A latex tiger with wall climbing abilities")
                .size(0.8f, 2.0f)
                .attributes(35.0, 0.35, 6.0, 40.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .hostile()
                .hasTail(true, true)
                .allAnimations()
                .shadowRadius(0.6f)
                .addCustomImport("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal")
                .addCustomField("// Custom field for wall climbing logic")
                .build();
        
        // Uncomment to generate:
        // HumanoidLatexEntityGenerator.generateHumanoidEntity(latexTiger);
        
        // Example 4: Custom entity with additional model parts
        HumanoidLatexEntityGenerator.HumanoidEntityConfig latexDragon = 
            new HumanoidEntityConfigBuilder("LatexDragon")
                .description("A latex dragon with wings and horns")
                .size(1.2f, 2.5f)
                .attributes(50.0, 0.3, 8.0, 48.0)
                .category(EntityGenerator.MobCategory.MONSTER)
                .hostile()
                .hasTail(true, true)
                .addCustomModelPart("LeftWing")
                .addCustomModelPart("RightWing")
                .addCustomModelPart("LeftHorn")
                .addCustomModelPart("RightHorn")
                .allAnimations()
                .shadowRadius(0.8f)
                .build();
        
        // Uncomment to generate:
        // HumanoidLatexEntityGenerator.generateHumanoidEntity(latexDragon);
        
        System.out.println("\n=== Usage Instructions ===");
        System.out.println("1. Configure your entity using HumanoidEntityConfigBuilder");
        System.out.println("2. Call HumanoidLatexEntityGenerator.generateHumanoidEntity(config)");
        System.out.println("3. Copy registration snippets to ModEntities.java");
        System.out.println("4. Copy client setup code to furmutage.java");
        System.out.println("5. Add model layer to ModModelLayers.java");
        System.out.println("6. Create or export model from Blockbench");
        System.out.println("7. Create texture file");
    }
}

