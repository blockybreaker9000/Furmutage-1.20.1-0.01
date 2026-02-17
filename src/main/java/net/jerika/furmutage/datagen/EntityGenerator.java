package net.jerika.furmutage.datagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Entity Generator Tool for Furmutage Mod
 * 
 * This tool generates entity classes, renderers, and registration code based on simple configurations.
 * 
 * Usage:
 * 1. Create an EntityConfig object with your entity details
 * 2. Call EntityGenerator.generateEntity(config)
 * 3. The tool will generate:
 *    - Entity class file
 *    - Renderer class file
 *    - Registration code snippet for ModEntities.java
 *    - Client setup code snippet for furmutage.java
 */
public class EntityGenerator {
    
    private static final String MOD_ID = "furmutage";
    private static final String BASE_PACKAGE = "net.jerika.furmutage";
    
    public static class EntityConfig {
        // Basic Information
        public String entityName;              // e.g., "LatexWolf" -> "LatexWolfEntity"
        public String displayName;             // e.g., "Latex Wolf"
        public String description;             // Optional description for javadoc
        
        // Entity Type
        public EntityBaseType baseType = EntityBaseType.MONSTER;  // MONSTER, ANIMAL, CREATURE
        public String parentClass = "Monster";  // Monster, Animal, Cow, etc.
        
        // Size
        public float width = 0.7f;
        public float height = 1.93f;
        
        // Mob Category
        public MobCategory category = MobCategory.MONSTER;
        
        // Attributes
        public double maxHealth = 20.0;
        public double movementSpeed = 0.25;
        public double attackDamage = 2.0;
        public double followRange = 32.0;
        
        // Goals (AI Behavior)
        public boolean canSwim = true;
        public boolean isHostile = true;
        public boolean canAttack = true;
        public boolean canMeleeAttack = true;
        public boolean avoidsWater = false;
        public boolean looksAtPlayer = true;
        
        // Sounds (optional - can be null)
        public String ambientSound = null;  // Registry name or vanilla sound
        public String hurtSound = null;
        public String deathSound = null;
        
        // Renderer
        public String texturePath = null;  // Will default to "textures/entity/{entity_name}.png"
        public boolean hasBabyScaling = false;
        public float shadowRadius = 0.5f;
        
        // Additional custom code
        public List<String> customImports = new ArrayList<>();
        public List<String> customFields = new ArrayList<>();
        public List<String> customMethods = new ArrayList<>();
        
        public EntityConfig(String entityName) {
            this.entityName = entityName;
            this.displayName = formatDisplayName(entityName);
        }
        
        private String formatDisplayName(String name) {
            // Convert "DarkLatexWolf" -> "Dark Latex Wolf"
            return name.replaceAll("([a-z])([A-Z])", "$1 $2");
        }
    }
    
    public enum EntityBaseType {
        MONSTER("Monster"),
        ANIMAL("Animal"),
        CREATURE("PathfinderMob");
        
        private final String className;
        
        EntityBaseType(String className) {
            this.className = className;
        }
        
        public String getClassName() {
            return className;
        }
    }
    
    public enum MobCategory {
        MONSTER("MobCategory.MONSTER"),
        CREATURE("MobCategory.CREATURE"),
        WATER_CREATURE("MobCategory.WATER_CREATURE"),
        MISC("MobCategory.MISC");
        
        private final String code;
        
        MobCategory(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
    }
    
    /**
     * Generate all files for an entity
     */
    public static void generateEntity(EntityConfig config) throws IOException {
        System.out.println("Generating entity: " + config.entityName);
        
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        
        // Create directories if needed
        Path entityDir = Paths.get("src/main/java/net/jerika/furmutage/entity/custom");
        Path rendererDir = Paths.get("src/main/java/net/jerika/furmutage/entity/client/renderer");
        
        Files.createDirectories(entityDir);
        Files.createDirectories(rendererDir);
        
        // Generate entity class
        generateEntityClass(config, entityDir.resolve(entityClassName + ".java"));
        
        // Generate renderer class
        generateRendererClass(config, rendererDir.resolve(rendererClassName + ".java"));
        
        // Generate registration snippets
        generateRegistrationSnippets(config);
        
        System.out.println("Entity generation complete!");
        System.out.println("Next steps:");
        System.out.println("1. Copy the registration code to ModEntities.java");
        System.out.println("2. Copy the client setup code to furmutage.java");
        System.out.println("3. Create texture at: assets/" + MOD_ID + "/textures/entity/" + getSnakeCase(config.entityName) + ".png");
    }
    
    private static void generateEntityClass(EntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String className = getSnakeCase(config.entityName);
        
        StringBuilder code = new StringBuilder();
        
        // Package and imports
        code.append("package net.jerika.furmutage.entity.custom;\n\n");
        code.append("import net.jerika.furmutage.entity.ModEntities;\n");
        code.append("import net.minecraft.world.entity.EntityType;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.AttributeSupplier;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.Attributes;\n");
        code.append("import net.minecraft.world.entity.ai.goal.*;\n");
        // Import parent class - handle common cases
        if (config.parentClass.equals("Monster")) {
            code.append("import net.minecraft.world.entity.monster.Monster;\n");
        } else if (config.parentClass.equals("Animal")) {
            code.append("import net.minecraft.world.entity.animal.Animal;\n");
        } else if (config.parentClass.equals("PathfinderMob")) {
            code.append("import net.minecraft.world.entity.PathfinderMob;\n");
        } else if (config.parentClass.equals("Cow")) {
            code.append("import net.minecraft.world.entity.animal.Cow;\n");
        } else if (config.parentClass.equals("Pig")) {
            code.append("import net.minecraft.world.entity.animal.Pig;\n");
        } else if (config.parentClass.equals("Chicken")) {
            code.append("import net.minecraft.world.entity.animal.Chicken;\n");
        } else if (config.parentClass.equals("Sheep")) {
            code.append("import net.minecraft.world.entity.animal.Sheep;\n");
        } else if (config.parentClass.equals("Rabbit")) {
            code.append("import net.minecraft.world.entity.animal.Rabbit;\n");
        } else if (config.parentClass.equals("Horse")) {
            code.append("import net.minecraft.world.entity.animal.horse.Horse;\n");
        } else {
            code.append("import net.minecraft.world.entity.").append(config.parentClass).append(";\n");
        }
        
        if (config.isHostile || config.canAttack) {
            code.append("import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;\n");
            code.append("import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;\n");
        }
        
        code.append("import net.minecraft.world.entity.player.Player;\n");
        code.append("import net.minecraft.world.level.Level;\n");
        
        if (config.ambientSound != null || config.hurtSound != null || config.deathSound != null) {
            code.append("import net.minecraft.sounds.SoundEvent;\n");
            code.append("import net.minecraft.world.damagesource.DamageSource;\n");
            code.append("import org.jetbrains.annotations.Nullable;\n");
            if (config.ambientSound != null && config.ambientSound.contains("ModSounds")) {
                code.append("import net.jerika.furmutage.sound.ModSounds;\n");
            }
        }
        
        // Custom imports
        for (String imp : config.customImports) {
            code.append("import ").append(imp).append(";\n");
        }
        
        code.append("\n");
        
        // Class javadoc
        if (config.description != null && !config.description.isEmpty()) {
            code.append("/**\n");
            code.append(" * ").append(config.description).append("\n");
            code.append(" */\n");
        }
        
        // Class declaration
        code.append("public class ").append(entityClassName)
            .append(" extends ").append(config.parentClass).append(" {\n");
        code.append("    public ").append(entityClassName)
            .append("(EntityType<? extends ").append(config.parentClass).append("> pEntityType, Level pLevel) {\n");
        code.append("        super(pEntityType, pLevel);\n");
        code.append("    }\n\n");
        
        // Register goals
        code.append("    @Override\n");
        code.append("    protected void registerGoals() {\n");
        
        if ("Cow".equals(config.parentClass) || "Pig".equals(config.parentClass) || 
            "Chicken".equals(config.parentClass) || "Sheep".equals(config.parentClass) ||
            "Rabbit".equals(config.parentClass) || "Horse".equals(config.parentClass)) {
            code.append("        // Call super to initialize parent class fields\n");
            code.append("        super.registerGoals();\n");
            code.append("        \n");
            code.append("        // Remove all goals\n");
            code.append("        this.goalSelector.removeAllGoals(goal -> true);\n");
            code.append("        this.targetSelector.removeAllGoals(goal -> true);\n");
            code.append("        \n");
        }
        
        int priority = 0;
        if (config.canSwim) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new FloatGoal(this));\n");
        }
        
        if (config.canMeleeAttack && config.isHostile) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new MeleeAttackGoal(this, 1.0D, false));\n");
        }
        
        if (config.avoidsWater) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new WaterAvoidingRandomStrollGoal(this, 0.8D));\n");
        } else {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new RandomStrollGoal(this, 0.8D));\n");
        }
        
        if (config.looksAtPlayer) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new LookAtPlayerGoal(this, Player.class, 6.0F));\n");
        }
        
        code.append("        this.goalSelector.addGoal(").append(priority++).append(", new RandomLookAroundGoal(this));\n");
        
        if (config.isHostile) {
            code.append("        \n");
            code.append("        // Hostile targeting\n");
            if (config.canAttack) {
                code.append("        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));\n");
            }
            code.append("        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));\n");
        }
        
        code.append("    }\n\n");
        
        // Create attributes
        code.append("    public static AttributeSupplier.Builder createAttributes() {\n");
        code.append("        return ").append(config.parentClass).append(".createAttributes()\n");
        code.append("                .add(Attributes.MAX_HEALTH, ").append(config.maxHealth).append("D)\n");
        code.append("                .add(Attributes.MOVEMENT_SPEED, ").append(config.movementSpeed).append("D)\n");
        
        if (config.attackDamage > 0) {
            code.append("                .add(Attributes.ATTACK_DAMAGE, ").append(config.attackDamage).append("D)\n");
        }
        
        code.append("                .add(Attributes.FOLLOW_RANGE, ").append(config.followRange).append("D);\n");
        code.append("    }\n\n");
        
        // Custom fields
        for (String field : config.customFields) {
            code.append("    ").append(field).append("\n\n");
        }
        
        // Sound methods
        if (config.ambientSound != null || config.hurtSound != null || config.deathSound != null) {
            if (config.ambientSound != null) {
                code.append("    @Override\n");
                code.append("    protected SoundEvent getAmbientSound() {\n");
                code.append("        return ").append(getSoundCode(config.ambientSound)).append(";\n");
                code.append("    }\n\n");
            }
            
            if (config.hurtSound != null) {
                code.append("    @Nullable\n");
                code.append("    @Override\n");
                code.append("    protected SoundEvent getHurtSound(DamageSource pDamageSource) {\n");
                code.append("        return ").append(getSoundCode(config.hurtSound)).append(";\n");
                code.append("    }\n\n");
            }
            
            if (config.deathSound != null) {
                code.append("    @Nullable\n");
                code.append("    @Override\n");
                code.append("    protected SoundEvent getDeathSound() {\n");
                code.append("        return ").append(getSoundCode(config.deathSound)).append(";\n");
                code.append("    }\n\n");
            }
        }
        
        // Custom methods
        for (String method : config.customMethods) {
            code.append("    ").append(method).append("\n\n");
        }
        
        code.append("}\n");
        
        // Write file
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateRendererClass(EntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        String modelClassName = config.entityName + "Model";
        String modelLayerName = getSnakeCase(config.entityName).toUpperCase() + "_LAYER";
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.client.renderer;\n\n");
        code.append("import com.mojang.blaze3d.vertex.PoseStack;\n");
        code.append("import net.jerika.furmutage.entity.client.model.").append(modelClassName).append(";\n");
        code.append("import net.jerika.furmutage.entity.client.model.ModModelLayers;\n");
        code.append("import net.jerika.furmutage.entity.custom.").append(entityClassName).append(";\n");
        code.append("import net.jerika.furmutage.furmutage;\n");
        code.append("import net.minecraft.client.renderer.MultiBufferSource;\n");
        code.append("import net.minecraft.client.renderer.entity.EntityRendererProvider;\n");
        code.append("import net.minecraft.client.renderer.entity.MobRenderer;\n");
        code.append("import net.minecraft.resources.ResourceLocation;\n\n");
        
        code.append("public class ").append(rendererClassName)
            .append(" extends MobRenderer<").append(entityClassName).append(", ")
            .append(modelClassName).append("<").append(entityClassName).append(">> {\n");
        
        code.append("    public ").append(rendererClassName).append("(EntityRendererProvider.Context pContext) {\n");
        code.append("        super(pContext, new ").append(modelClassName).append("<>(pContext.bakeLayer(ModModelLayers.").append(modelLayerName).append(")), ")
            .append(config.shadowRadius).append("f);\n");
        code.append("    }\n\n");
        
        code.append("    @Override\n");
        code.append("    public ResourceLocation getTextureLocation(").append(entityClassName).append(" pEntity) {\n");
        
        String texturePath = config.texturePath != null ? config.texturePath : 
            "textures/entity/" + getSnakeCase(config.entityName) + ".png";
        code.append("        return new ResourceLocation(furmutage.MOD_ID, \"").append(texturePath).append("\");\n");
        
        code.append("    }\n\n");
        
        if (config.hasBabyScaling) {
            code.append("    @Override\n");
            code.append("    public void render(").append(entityClassName).append(" pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,\n");
            code.append("                       MultiBufferSource pBuffer, int pPackedLight) {\n");
            code.append("        if(pEntity.isBaby()) {\n");
            code.append("            pMatrixStack.scale(0.5f, 0.5f, 0.5f);\n");
            code.append("        }\n");
            code.append("        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);\n");
            code.append("    }\n");
        }
        
        code.append("}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateRegistrationSnippets(EntityConfig config) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String constantName = getSnakeCase(config.entityName).toUpperCase();
        
        // Generate registration code snippet
        StringBuilder regCode = new StringBuilder();
        regCode.append("    // ").append(config.displayName).append("\n");
        regCode.append("    public static final RegistryObject<EntityType<").append(entityClassName).append(">> ")
            .append(constantName).append(" =\n");
        regCode.append("            ENTITY_TYPES.register(\"").append(getSnakeCase(config.entityName)).append("\", () -> ")
            .append("EntityType.Builder.of(").append(entityClassName).append("::new, ")
            .append(config.category.getCode()).append(")\n");
        regCode.append("                    .sized(").append(config.width).append("f, ").append(config.height).append("f)")
            .append(".build(\"").append(getSnakeCase(config.entityName)).append("\"));\n\n");
        
        // Generate client setup code
        String rendererClassName = entityClassName + "Renderer";
        StringBuilder clientCode = new StringBuilder();
        clientCode.append("            EntityRenderers.register(ModEntities.").append(constantName).append(".get(), ")
            .append(rendererClassName).append("::new);\n");
        
        // Write snippets to files
        Path snippetsDir = Paths.get("entity_generator_output");
        Files.createDirectories(snippetsDir);
        
        Files.write(snippetsDir.resolve(config.entityName + "_registration.txt"), regCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_client_setup.txt"), clientCode.toString().getBytes());
        
        System.out.println("Generated snippets in: entity_generator_output/");
    }
    
    private static String getSoundCode(String sound) {
        if (sound.startsWith("ModSounds.")) {
            return sound + ".get()";
        } else if (sound.startsWith("SoundEvents.")) {
            return sound;
        } else {
            return "SoundEvents." + sound.toUpperCase();
        }
    }
    
    private static String getSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    /**
     * Example usage - creates a sample entity configuration
     */
    public static void main(String[] args) throws IOException {
        // Example: Create a Dark Latex Wolf entity
        EntityConfig darkWolf = new EntityConfig("DarkLatexWolf");
        darkWolf.description = "A dark latex wolf entity that hunts players";
        darkWolf.width = 0.7f;
        darkWolf.height = 1.93f;
        darkWolf.maxHealth = 30.0;
        darkWolf.movementSpeed = 0.3;
        darkWolf.attackDamage = 5.0;
        darkWolf.category = MobCategory.MONSTER;
        darkWolf.isHostile = true;
        darkWolf.canAttack = true;
        darkWolf.canMeleeAttack = true;
        darkWolf.avoidsWater = true;
        darkWolf.shadowRadius = 0.5f;
        darkWolf.customImports.add("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal");
        
        generateEntity(darkWolf);
        
        System.out.println("\n=== Example Usage ===");
        System.out.println("EntityConfig config = new EntityConfig(\"YourEntityName\");");
        System.out.println("// Set properties...");
        System.out.println("EntityGenerator.generateEntity(config);");
    }
}

