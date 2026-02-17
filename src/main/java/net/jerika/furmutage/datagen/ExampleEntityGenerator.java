package net.jerika.furmutage.datagen;

import net.jerika.furmutage.datagen.EntityGenerator.EntityConfig;
import java.io.IOException;

/**
 * Example file showing how to use the Entity Generator
 * 
 * Run this main method to generate example entities.
 * Modify the configurations to create your own entities.
 */
public class ExampleEntityGenerator {
    
    public static void main(String[] args) throws IOException {
        System.out.println("=== Furmutage Entity Generator ===\n");
        
        // Example 1: Simple Monster Entity
        EntityConfig Wolf = new EntityConfigBuilder("LatexWolf")
            .description("A latex wolf that hunts players")
            .monster()
            .size(0.7f, 1.93f)
            .health(30.0)
            .speed(0.3)
            .damage(5.0)
            .followRange(40.0)
            .hostile()
            .avoidsWater(true)
            .shadowRadius(0.5f)
            .addImport("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal")
            .build();
        
        EntityGenerator.generateEntity(Wolf);
        
        // Example 2: Passive Latex Animal Entity
        EntityConfig peacefulCreature = new EntityConfigBuilder("LatexRabbit")
            .description("A peaceful latex rabbit")
            .animal()
            .size(0.4f, 0.5f)
            .health(5.0)
            .speed(0.25)
            .peaceful()
            .canSwim(true)
            .babyScaling(true)
            .shadowRadius(0.2f)
            .build();
        
        // Uncomment to generate:
        // EntityGenerator.generateEntity(peacefulCreature);
        
        // Example 3: Hostile Variant of Vanilla Mob aka the Dark Latex Cow
        EntityConfig infectedCow = new EntityConfigBuilder("DarkLatexCow")
            .description("Dark latex infected cow - now hostile")
            .extendsClass("Cow")
            .category(EntityGenerator.MobCategory.MONSTER)
            .size(0.9f, 1.4f)
            .health(20.0)
            .speed(0.25)
            .damage(3.0)
            .hostile()
            .avoidsWater(true)
            .sounds("ModSounds.DARK_LATEX_COW_AMBIENT", 
                    "ModSounds.DARK_LATEX_COW_HURT", 
                    "ModSounds.DARK_LATEX_COW_DEATH")
            .texture("textures/entity/dark_latex_cow.png")
            .shadowRadius(0.6f)
            .addImport("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal")
            .build();
        
        // Uncomment to generate:
        // EntityGenerator.generateEntity(infectedCow);
        
        // Example 4: Aquatic Creature for Aquatic things
        EntityConfig aquaticEntity = new EntityConfigBuilder("LatexSquid")
            .description("A latex squid that lives underwater")
            .creature()
            .category(EntityGenerator.MobCategory.WATER_CREATURE)
            .size(0.8f, 0.8f)
            .health(15.0)
            .speed(0.2)
            .hostile()
            .canSwim(true)
            .shadowRadius(0.4f)
            .build();
        
        // Uncomment to generate:
        // EntityGenerator.generateEntity(aquaticEntity);
        
        System.out.println("\n=== Generation Complete ===");
        System.out.println("Check the 'entity_generator_output' folder for registration snippets!");
        System.out.println("Don't forget to:");
        System.out.println("1. Add registration code to ModEntities.java");
        System.out.println("2. Add client setup code to furmutage.java");
        System.out.println("3. Create texture files");
        System.out.println("4. Create model classes (if needed)");
    }
}

