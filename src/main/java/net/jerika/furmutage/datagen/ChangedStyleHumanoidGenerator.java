package net.jerika.furmutage.datagen;

import net.jerika.furmutage.datagen.EntityGenerator.MobCategory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for Humanoid Latex Entities using Changed mod's animation system
 * 
 * This generator creates entities that use Changed mod's HumanoidAnimator and AnimatorPresets
 * for animations, matching the style of Changed mod's latex entities.
 * 
 * Requirements:
 * - Changed mod must be a dependency
 * - Entity must extend ChangedEntity (from Changed mod)
 * - Model must extend AdvancedHumanoidModel and use HumanoidAnimator
 * - Renderer must extend AdvancedHumanoidRenderer
 */
public class ChangedStyleHumanoidGenerator {
    
    private static final String MOD_ID = "furmutage";
    private static final String BASE_PACKAGE = "net.jerika.furmutage";
    private static final String CHANGED_PACKAGE = "net.ltxprogrammer.changed";
    
    public static class ChangedEntityConfig {
        // Basic Information
        public String entityName;              // e.g., "DarkLatexWolf"
        public String displayName;             // e.g., "Dark Latex Wolf"
        public String description;             // Optional description
        
        // Entity Type (must extend ChangedEntity)
        public String parentClass = "ChangedEntity";  // Usually ChangedEntity or a subclass
        public MobCategory category = MobCategory.MONSTER;
        
        // Size
        public float width = 0.7f;
        public float height = 1.93f;
        
        // Attributes
        public double maxHealth = 20.0;
        public double movementSpeed = 0.25;
        public double attackDamage = 2.0;
        public double followRange = 32.0;
        
        // Animator Preset Type
        public AnimatorPresetType presetType = AnimatorPresetType.WOLF_LIKE;
        
        // Model parts (for preset configuration)
        public boolean hasEars = true;
        public boolean hasTail = true;
        public int tailJoints = 3;  // Number of tail segments (TailPrimary, TailSecondary, etc.)
        public boolean hasDigitigradeLegs = true;  // Has lower leg, foot, pad structure
        
        // Hip offset for animator
        public float hipOffset = -1.5f;
        
        // Renderer
        public String texturePath = null;  // Will default to "textures/entity/{entity_name}.png"
        public float shadowRadius = 0.5f;
        public boolean hasArmorSupport = false;  // If true, generates armor model support
        
        // Custom code
        public List<String> customImports = new ArrayList<>();
        public List<String> customFields = new ArrayList<>();
        public List<String> customMethods = new ArrayList<>();
        
        public ChangedEntityConfig(String entityName) {
            this.entityName = entityName;
            this.displayName = formatDisplayName(entityName);
        }
        
        private String formatDisplayName(String name) {
            return name.replaceAll("([a-z])([A-Z])", "$1 $2");
        }
    }
    
    public enum AnimatorPresetType {
        WOLF_LIKE("wolfLike"),
        CAT_LIKE("catLike"),
        HUMAN_LIKE("humanLike"),
        DRAGON_LIKE("dragonLike"),
        SHARK_LIKE("sharkLike"),
        BIPEDAL("bipedal"),
        WOLF_LIKE_ARMOR("wolfLikeArmor"),
        CAT_LIKE_ARMOR("catLikeArmor");
        
        private final String methodName;
        
        AnimatorPresetType(String methodName) {
            this.methodName = methodName;
        }
        
        public String getMethodName() {
            return methodName;
        }
    }
    
    /**
     * Generate all files for a Changed-style humanoid entity
     */
    public static void generateChangedStyleEntity(ChangedEntityConfig config) throws IOException {
        System.out.println("Generating Changed-style humanoid entity: " + config.entityName);
        
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        String modelClassName = entityClassName + "Model";
        
        // Create directories
        Path entityDir = Paths.get("src/main/java/net/jerika/furmutage/entity/custom");
        Path rendererDir = Paths.get("src/main/java/net/jerika/furmutage/entity/client/renderer");
        Path modelDir = Paths.get("src/main/java/net/jerika/furmutage/entity/client/model");
        
        Files.createDirectories(entityDir);
        Files.createDirectories(rendererDir);
        Files.createDirectories(modelDir);
        
        // Generate entity class
        generateChangedEntityClass(config, entityDir.resolve(entityClassName + ".java"));
        
        // Generate model class
        generateChangedModelClass(config, modelDir.resolve(modelClassName + ".java"));
        
        // Generate renderer class
        generateChangedRendererClass(config, rendererDir.resolve(rendererClassName + ".java"));
        
        // Generate registration snippets
        generateChangedRegistrationSnippets(config);
        
        System.out.println("Changed-style entity generation complete!");
        System.out.println("Next steps:");
        System.out.println("1. Copy registration code to ModEntities.java");
        System.out.println("2. Copy client setup code to furmutage.java");
        System.out.println("3. Add model layer to ModModelLayers.java");
        System.out.println("4. Add layer definition registration to ModEventBusClientEvents.java");
        System.out.println("5. Create texture at: assets/" + MOD_ID + "/textures/entity/" + getSnakeCase(config.entityName) + ".png");
        System.out.println("6. Complete createBodyLayer() method in model class (export from Blockbench)");
        System.out.println("7. Ensure Changed mod is a dependency in build.gradle");
    }
    
    private static void generateChangedEntityClass(ChangedEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.custom;\n\n");
        code.append("import net.ltxprogrammer.changed.entity.ChangedEntity;\n");
        code.append("import net.minecraft.world.entity.EntityType;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.AttributeSupplier;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.Attributes;\n");
        code.append("import net.minecraft.world.level.Level;\n");
        
        // Custom imports
        for (String imp : config.customImports) {
            code.append("import ").append(imp).append(";\n");
        }
        
        code.append("\n");
        
        if (config.description != null && !config.description.isEmpty()) {
            code.append("/**\n");
            code.append(" * ").append(config.description).append("\n");
            code.append(" */\n");
        }
        
        code.append("public class ").append(entityClassName).append(" extends ").append(config.parentClass).append(" {\n\n");
        
        code.append("    public ").append(entityClassName)
            .append("(EntityType<? extends ").append(config.parentClass).append("> pEntityType, Level pLevel) {\n");
        code.append("        super(pEntityType, pLevel);\n");
        code.append("    }\n\n");
        
        code.append("    public static AttributeSupplier.Builder createAttributes() {\n");
        code.append("        return ChangedEntity.createLatexAttributes()\n");
        code.append("                .add(Attributes.MAX_HEALTH, ").append(config.maxHealth).append("D)\n");
        code.append("                .add(Attributes.MOVEMENT_SPEED, ").append(config.movementSpeed).append("D)\n");
        
        if (config.attackDamage > 0) {
            code.append("                .add(Attributes.ATTACK_DAMAGE, ").append(config.attackDamage).append("D)\n");
        }
        
        code.append("                .add(Attributes.FOLLOW_RANGE, ").append(config.followRange).append("D);\n");
        code.append("    }\n");
        
        // Custom fields
        for (String field : config.customFields) {
            code.append("\n    ").append(field);
        }
        
        // Custom methods
        for (String method : config.customMethods) {
            code.append("\n    ").append(method);
        }
        
        code.append("\n}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateChangedModelClass(ChangedEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String modelClassName = entityClassName + "Model";
        String layerName = getSnakeCase(config.entityName).toUpperCase() + "_LAYER";
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.client.model;\n\n");
        code.append("import com.mojang.blaze3d.vertex.PoseStack;\n");
        code.append("import com.mojang.blaze3d.vertex.VertexConsumer;\n");
        code.append("import net.jerika.furmutage.entity.custom.").append(entityClassName).append(";\n");
        code.append("import net.jerika.furmutage.furmutage;\n");
        code.append("import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;\n");
        code.append("import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;\n");
        code.append("import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;\n");
        code.append("import net.minecraft.client.model.geom.ModelLayerLocation;\n");
        code.append("import net.minecraft.client.model.geom.ModelPart;\n");
        code.append("import net.minecraft.client.model.geom.PartPose;\n");
        code.append("import net.minecraft.client.model.geom.builders.*;\n");
        code.append("import net.minecraft.world.entity.HumanoidArm;\n\n");
        code.append("import java.util.List;\n\n");
        
        code.append("public class ").append(modelClassName)
            .append(" extends AdvancedHumanoidModel<").append(entityClassName).append("> {\n");
        
        code.append("    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(\n");
        code.append("            new net.minecraft.resources.ResourceLocation(furmutage.MOD_ID, \"").append(getSnakeCase(config.entityName)).append("\"), \"main\");\n\n");
        
        // Model parts
        code.append("    private final ModelPart RightLeg;\n");
        code.append("    private final ModelPart LeftLeg;\n");
        code.append("    private final ModelPart RightArm;\n");
        code.append("    private final ModelPart LeftArm;\n");
        code.append("    private final ModelPart Head;\n");
        code.append("    private final ModelPart Torso;\n");
        if (config.hasTail) {
            code.append("    private final ModelPart Tail;\n");
        }
        code.append("    private final HumanoidAnimator<").append(entityClassName).append(", ").append(modelClassName).append("> animator;\n\n");
        
        // Constructor
        code.append("    public ").append(modelClassName).append("(ModelPart root) {\n");
        code.append("        super(root);\n");
        code.append("        this.RightLeg = root.getChild(\"RightLeg\");\n");
        code.append("        this.LeftLeg = root.getChild(\"LeftLeg\");\n");
        code.append("        this.Head = root.getChild(\"Head\");\n");
        code.append("        this.Torso = root.getChild(\"Torso\");\n");
        if (config.hasTail) {
            code.append("        this.Tail = Torso.getChild(\"Tail\");\n");
        }
        code.append("        this.RightArm = root.getChild(\"RightArm\");\n");
        code.append("        this.LeftArm = root.getChild(\"LeftArm\");\n\n");
        
        // Build tail joints list
        if (config.hasTail && config.tailJoints > 0) {
            code.append("        // Tail joints\n");
            code.append("        var tailPrimary = Tail.getChild(\"TailPrimary\");\n");
            if (config.tailJoints >= 2) {
                code.append("        var tailSecondary = tailPrimary.getChild(\"TailSecondary\");\n");
            }
            if (config.tailJoints >= 3) {
                code.append("        var tailTertiary = tailSecondary.getChild(\"TailTertiary\");\n");
            }
            code.append("        List<ModelPart> tailJoints = List.of(");
            List<String> tailList = new ArrayList<>();
            tailList.add("tailPrimary");
            if (config.tailJoints >= 2) tailList.add("tailSecondary");
            if (config.tailJoints >= 3) tailList.add("tailTertiary");
            code.append(String.join(", ", tailList));
            code.append(");\n\n");
        }
        
        // Build leg structure
        if (config.hasDigitigradeLegs) {
            code.append("        // Leg structure for digitigrade legs\n");
            code.append("        var leftLowerLeg = LeftLeg.getChild(\"LeftLowerLeg\");\n");
            code.append("        var leftFoot = leftLowerLeg.getChild(\"LeftFoot\");\n");
            code.append("        var rightLowerLeg = RightLeg.getChild(\"RightLowerLeg\");\n");
            code.append("        var rightFoot = rightLowerLeg.getChild(\"RightFoot\");\n\n");
        }
        
        // Animator setup
        code.append("        // Setup HumanoidAnimator with preset\n");
        code.append("        animator = HumanoidAnimator.of(this).hipOffset(").append(config.hipOffset).append("f)\n");
        
        // Generate preset call based on type
        code.append("                .addPreset(AnimatorPresets.").append(config.presetType.getMethodName()).append("(\n");
        
        if (config.presetType == AnimatorPresetType.WOLF_LIKE || config.presetType == AnimatorPresetType.CAT_LIKE) {
            // wolfLike/catLike: head, leftEar, rightEar, torso, leftArm, rightArm, tail, tailJoints, leftLeg, leftLowerLeg, leftFoot, leftPad, rightLeg, rightLowerLeg, rightFoot, rightPad
            code.append("                        Head");
            if (config.hasEars) {
                code.append(", Head.getChild(\"LeftEar\"), Head.getChild(\"RightEar\")");
            } else {
                code.append(", null, null");
            }
            code.append(",\n                        Torso, LeftArm, RightArm,\n");
            if (config.hasTail) {
                code.append("                        Tail, tailJoints,\n");
            } else {
                code.append("                        null, List.of(),\n");
            }
            if (config.hasDigitigradeLegs) {
                code.append("                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild(\"LeftPad\"),\n");
                code.append("                        RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild(\"RightPad\")));\n");
            } else {
                code.append("                        LeftLeg, null, null, null,\n");
                code.append("                        RightLeg, null, null, null));\n");
            }
        } else if (config.presetType == AnimatorPresetType.HUMAN_LIKE) {
            // humanLike: head, torso, leftArm, rightArm, leftLeg, rightLeg
            code.append("                        Head,\n                        Torso, LeftArm, RightArm,\n");
            code.append("                        LeftLeg, RightLeg));\n");
        } else if (config.presetType == AnimatorPresetType.DRAGON_LIKE || config.presetType == AnimatorPresetType.SHARK_LIKE) {
            // dragonLike/sharkLike: head, torso, leftArm, rightArm, tail, tailJoints, leftLeg, leftLowerLeg, leftFoot, leftPad, rightLeg, rightLowerLeg, rightFoot, rightPad
            code.append("                        Head,\n                        Torso, LeftArm, RightArm,\n");
            if (config.hasTail) {
                code.append("                        Tail, tailJoints,\n");
            } else {
                code.append("                        null, List.of(),\n");
            }
            if (config.hasDigitigradeLegs) {
                code.append("                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild(\"LeftPad\"),\n");
                code.append("                        RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild(\"RightPad\")));\n");
            } else {
                code.append("                        LeftLeg, null, null, null,\n");
                code.append("                        RightLeg, null, null, null));\n");
            }
        } else {
            // bipedal: leftLeg, rightLeg
            code.append("                        LeftLeg, RightLeg));\n");
        }
        
        code.append("    }\n\n");
        
        // createBodyLayer method - placeholder
        code.append("    public static LayerDefinition createBodyLayer() {\n");
        code.append("        MeshDefinition meshdefinition = new MeshDefinition();\n");
        code.append("        PartDefinition partdefinition = meshdefinition.getRoot();\n\n");
        code.append("        // TODO: Define your model parts here or export from Blockbench\n");
        code.append("        // IMPORTANT: Model part names must match what the animator expects:\n");
        code.append("        // - Head, Torso, LeftArm, RightArm, LeftLeg, RightLeg\n");
        if (config.hasTail) {
            code.append("        // - Tail, TailPrimary");
            if (config.tailJoints >= 2) code.append(", TailSecondary");
            if (config.tailJoints >= 3) code.append(", TailTertiary");
            code.append("\n");
        }
        if (config.hasEars) {
            code.append("        // - LeftEar, RightEar (children of Head)\n");
        }
        if (config.hasDigitigradeLegs) {
            code.append("        // - LeftLowerLeg, LeftFoot, LeftPad (children of LeftLeg)\n");
            code.append("        // - RightLowerLeg, RightFoot, RightPad (children of RightLeg)\n");
        }
        code.append("\n        return LayerDefinition.create(meshdefinition, 128, 128);\n");
        code.append("    }\n\n");
        
        // setupHand
        code.append("    public void setupHand(").append(entityClassName).append(" entity) {\n");
        code.append("        animator.setupHand();\n");
        code.append("    }\n\n");
        
        // getArm
        code.append("    @Override\n");
        code.append("    public ModelPart getArm(HumanoidArm p_102852_) {\n");
        code.append("        return p_102852_ == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;\n");
        code.append("    }\n\n");
        
        // getLeg
        code.append("    @Override\n");
        code.append("    public ModelPart getLeg(HumanoidArm p_102852_) {\n");
        code.append("        return p_102852_ == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;\n");
        code.append("    }\n\n");
        
        // getHead
        code.append("    @Override\n");
        code.append("    public ModelPart getHead() {\n");
        code.append("        return this.Head;\n");
        code.append("    }\n\n");
        
        // getTorso
        code.append("    @Override\n");
        code.append("    public ModelPart getTorso() {\n");
        code.append("        return Torso;\n");
        code.append("    }\n\n");
        
        // renderToBuffer
        code.append("    @Override\n");
        code.append("    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {\n");
        code.append("        RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("        LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("        Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("        Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("        RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("        LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);\n");
        code.append("    }\n\n");
        
        // getAnimator
        code.append("    @Override\n");
        code.append("    public HumanoidAnimator<").append(entityClassName).append(", ").append(modelClassName).append("> getAnimator(").append(entityClassName).append(" entity) {\n");
        code.append("        return animator;\n");
        code.append("    }\n");
        code.append("}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateChangedRendererClass(ChangedEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        String modelClassName = entityClassName + "Model";
        String layerName = getSnakeCase(config.entityName).toUpperCase() + "_LAYER";
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.client.renderer;\n\n");
        code.append("import net.jerika.furmutage.entity.client.model.").append(modelClassName).append(";\n");
        code.append("import net.jerika.furmutage.entity.client.model.ModModelLayers;\n");
        code.append("import net.jerika.furmutage.entity.custom.").append(entityClassName).append(";\n");
        code.append("import net.jerika.furmutage.furmutage;\n");
        code.append("import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;\n");
        code.append("import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelPicker;\n");
        code.append("import net.minecraft.client.renderer.entity.EntityRendererProvider;\n");
        code.append("import net.minecraft.resources.ResourceLocation;\n\n");
        
        code.append("public class ").append(rendererClassName)
            .append(" extends AdvancedHumanoidRenderer<").append(entityClassName).append(", ")
            .append(modelClassName).append(", ")
            .append("net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel<").append(entityClassName).append(">> {\n");
        
        code.append("    public ").append(rendererClassName).append("(EntityRendererProvider.Context context) {\n");
        code.append("        super(context, new ").append(modelClassName).append("(context.bakeLayer(ModModelLayers.").append(layerName).append(")),\n");
        
        if (config.hasArmorSupport) {
            code.append("                ArmorModelPicker.basic(context.getModelSet(),\n");
            code.append("                        net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel.MODEL_SET),\n");
        } else {
            code.append("                net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel.MODEL_SET,\n");
        }
        
        code.append("                ").append(config.shadowRadius).append("f);\n");
        code.append("    }\n\n");
        
        code.append("    @Override\n");
        code.append("    public ResourceLocation getTextureLocation(").append(entityClassName).append(" p_114482_) {\n");
        
        String texturePath = config.texturePath != null ? config.texturePath : 
            "textures/entity/" + getSnakeCase(config.entityName) + ".png";
        code.append("        return new ResourceLocation(furmutage.MOD_ID, \"").append(texturePath).append("\");\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateChangedRegistrationSnippets(ChangedEntityConfig config) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String constantName = getSnakeCase(config.entityName).toUpperCase();
        String layerName = constantName + "_LAYER";
        
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
        
        // Generate model layer snippet
        String modelClassName = entityClassName + "Model";
        StringBuilder layerCode = new StringBuilder();
        layerCode.append("    public static final ModelLayerLocation ").append(layerName).append(" = new ModelLayerLocation(\n");
        layerCode.append("            new ResourceLocation(furmutage.MOD_ID, \"").append(getSnakeCase(config.entityName)).append("_layer\"), \"main\");\n");
        
        // Generate layer definition registration snippet
        StringBuilder layerDefCode = new StringBuilder();
        layerDefCode.append("        event.registerLayerDefinition(ModModelLayers.").append(layerName)
            .append(", ").append(modelClassName).append("::createBodyLayer);\n");
        
        // Generate attribute registration snippet
        StringBuilder attrCode = new StringBuilder();
        attrCode.append("        event.put(ModEntities.").append(constantName).append(".get(), ")
            .append(entityClassName).append(".createAttributes().build());\n");
        
        // Write snippets to files
        Path snippetsDir = Paths.get("entity_generator_output");
        Files.createDirectories(snippetsDir);
        
        Files.write(snippetsDir.resolve(config.entityName + "_registration.txt"), regCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_client_setup.txt"), clientCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_model_layer.txt"), layerCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_layer_definition.txt"), layerDefCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_attributes.txt"), attrCode.toString().getBytes());
        
        System.out.println("Generated snippets in: entity_generator_output/");
    }
    
    private static String getSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    /**
     * Example usage
     */
    public static void main(String[] args) throws IOException {
        // Example: Create a Dark Latex Wolf using Changed's animation system
        ChangedEntityConfig darkWolf = new ChangedEntityConfig("DarkLatexWolf");
        darkWolf.description = "A dark latex wolf entity using Changed mod's animation system";
        darkWolf.width = 0.7f;
        darkWolf.height = 1.93f;
        darkWolf.maxHealth = 30.0;
        darkWolf.movementSpeed = 0.3;
        darkWolf.attackDamage = 5.0;
        darkWolf.category = MobCategory.MONSTER;
        darkWolf.presetType = AnimatorPresetType.WOLF_LIKE;
        darkWolf.hasEars = true;
        darkWolf.hasTail = true;
        darkWolf.tailJoints = 3;
        darkWolf.hasDigitigradeLegs = true;
        darkWolf.hipOffset = -1.5f;
        darkWolf.shadowRadius = 0.5f;
        
        generateChangedStyleEntity(darkWolf);
        
        System.out.println("\n=== Example Usage ===");
        System.out.println("ChangedEntityConfig config = new ChangedEntityConfig(\"YourEntityName\");");
        System.out.println("// Set properties...");
        System.out.println("ChangedStyleHumanoidGenerator.generateChangedStyleEntity(config);");
    }
}

