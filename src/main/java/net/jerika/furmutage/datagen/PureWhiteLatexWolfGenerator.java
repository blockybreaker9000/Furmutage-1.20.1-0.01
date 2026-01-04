package net.jerika.furmutage.datagen;

import net.jerika.furmutage.datagen.EntityGenerator.EntityConfig;
import java.io.IOException;

/**
 * Generator script for PureWhiteLatexWolf entity
 */
public class PureWhiteLatexWolfGenerator {
    
    public static void main(String[] args) throws IOException {
        System.out.println("=== Generating PureWhiteLatexWolf Entity ===\n");
        
        EntityConfig config = new EntityConfigBuilder("PureWhiteLatexWolf")
            .description("A pure white latex wolf entity - 4 blocks tall")
            .monster()
            .size(0.85f, 4.0f)  // 4 blocks tall, width scaled proportionally
            .health(40.0)
            .speed(0.3)
            .damage(6.0)
            .followRange(60.0)
            .hostile()
            .avoidsWater(true)
            .shadowRadius(1.0f)
            .texture("textures/entity/pure_white_latex_wolf.png")
            .addImport("net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal")
            .build();
        
        EntityGenerator.generateEntity(config);
        
        System.out.println("\n=== Generation Complete ===");
        System.out.println("Note: You'll need to create the model and texture manually.");
    }
}

