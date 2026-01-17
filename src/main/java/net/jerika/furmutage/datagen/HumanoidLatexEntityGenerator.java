package net.jerika.furmutage.datagen;

import net.jerika.furmutage.datagen.EntityGenerator.MobCategory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for Humanoid Latex Entities with full animation support
 * 
 * This generator creates entities that use HierarchicalModel with animations,
 * matching the style of LatexExoMutant and other animated entities in the mod.
 * 
 * It generates:
 * - Entity class with AnimationState fields and setup logic
 * - Model class with setupAnim() and animation application
 * - Renderer class
 * - Animation class with idle, walk, attack, jump, and swim animations
 * - Registration snippets
 */
public class HumanoidLatexEntityGenerator {
    
    private static final String MOD_ID = "furmutage";
    private static final String BASE_PACKAGE = "net.jerika.furmutage";
    
    public static class HumanoidEntityConfig {
        // Basic Information
        public String entityName;              // e.g., "DarkLatexWolf"
        public String displayName;             // e.g., "Dark Latex Wolf"
        public String description;             // Optional description
        
        // Entity Type
        public String parentClass = "Monster";
        public MobCategory category = MobCategory.MONSTER;
        
        // Size
        public float width = 0.7f;
        public float height = 1.93f;
        
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
        
        // Model Parts (required for humanoid entities)
        public boolean hasTail = true;
        public boolean hasTailPrimary = true;  // Secondary tail segment
        public List<String> customModelParts = new ArrayList<>();  // Additional parts like ears, wings, etc.
        
        // Animation Settings
        public boolean hasIdleAnimation = true;
        public boolean hasWalkAnimation = true;
        public boolean hasAttackAnimation = true;
        public boolean hasJumpAnimation = true;
        public boolean hasSwimAnimation = true;
        
        // Renderer
        public String texturePath = null;  // Will default to "textures/entity/{entity_name}.png"
        public boolean hasBabyScaling = false;
        public float shadowRadius = 0.5f;
        
        // Custom code
        public List<String> customImports = new ArrayList<>();
        public List<String> customFields = new ArrayList<>();
        public List<String> customMethods = new ArrayList<>();
        
        public HumanoidEntityConfig(String entityName) {
            this.entityName = entityName;
            this.displayName = formatDisplayName(entityName);
        }
        
        private String formatDisplayName(String name) {
            return name.replaceAll("([a-z])([A-Z])", "$1 $2");
        }
    }
    
    /**
     * Generate all files for a humanoid latex entity with animations
     */
    public static void generateHumanoidEntity(HumanoidEntityConfig config) throws IOException {
        System.out.println("Generating humanoid latex entity: " + config.entityName);
        
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        String modelClassName = entityClassName + "Model";
        String animationsClassName = config.entityName + "Animations";
        
        // Create directories
        Path entityDir = Paths.get("src/main/java/net/jerika/furmutage/entity/custom");
        Path rendererDir = Paths.get("src/main/java/net/jerika/furmutage/entity/client/renderer");
        Path modelDir = Paths.get("src/main/java/net/jerika/furmutage/entity/client/model");
        Path animationsDir = Paths.get("src/main/java/net/jerika/furmutage/entity/animations");
        
        Files.createDirectories(entityDir);
        Files.createDirectories(rendererDir);
        Files.createDirectories(modelDir);
        Files.createDirectories(animationsDir);
        
        // Generate entity class
        generateHumanoidEntityClass(config, entityDir.resolve(entityClassName + ".java"));
        
        // Generate model class
        generateHumanoidModelClass(config, modelDir.resolve(modelClassName + ".java"));
        
        // Generate renderer class
        generateHumanoidRendererClass(config, rendererDir.resolve(rendererClassName + ".java"));
        
        // Generate animations class
        generateAnimationsClass(config, animationsDir.resolve(animationsClassName + ".java"));
        
        // Generate registration snippets
        generateHumanoidRegistrationSnippets(config);
        
        System.out.println("Humanoid entity generation complete!");
        System.out.println("Next steps:");
        System.out.println("1. Copy registration code to ModEntities.java");
        System.out.println("2. Copy client setup code to furmutage.java");
        System.out.println("3. Add model layer to ModModelLayers.java");
        System.out.println("4. Add layer definition registration to ModEventBusClientEvents.java");
        System.out.println("5. Create texture at: assets/" + MOD_ID + "/textures/entity/" + getSnakeCase(config.entityName) + ".png");
        System.out.println("6. Complete createBodyLayer() method in model class (or export from Blockbench)");
    }
    
    private static void generateHumanoidEntityClass(HumanoidEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String animationsClassName = config.entityName + "Animations";
        
        StringBuilder code = new StringBuilder();
        
        // Package and imports
        code.append("package net.jerika.furmutage.entity.custom;\n\n");
        code.append("import net.jerika.furmutage.entity.ModEntities;\n");
        code.append("import net.minecraft.core.BlockPos;\n");
        code.append("import net.minecraft.world.entity.EntityType;\n");
        code.append("import net.minecraft.world.entity.Pose;\n");
        code.append("import net.minecraft.world.entity.AnimationState;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.AttributeSupplier;\n");
        code.append("import net.minecraft.world.entity.ai.attributes.Attributes;\n");
        code.append("import net.minecraft.world.entity.ai.goal.*;\n");
        code.append("import net.minecraft.world.entity.monster.Monster;\n");
        
        if (config.isHostile || config.canAttack) {
            code.append("import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;\n");
            code.append("import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;\n");
        }
        
        code.append("import net.minecraft.world.entity.player.Player;\n");
        code.append("import net.minecraft.world.level.Level;\n");
        code.append("import net.minecraft.world.level.block.state.BlockState;\n");
        
        if (config.customImports.size() > 0 || config.isHostile) {
            code.append("import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;\n");
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
        code.append("public class ").append(entityClassName).append(" extends ").append(config.parentClass).append(" {\n");
        
        // Animation states
        code.append("    public final AnimationState idleAnimationState = new AnimationState();\n");
        code.append("    public final AnimationState attackAnimationState = new AnimationState();\n");
        code.append("    public int attackAnimationTimeout = 0;\n");
        if (config.hasJumpAnimation) {
            code.append("    public final AnimationState jumpAnimationState = new AnimationState();\n");
            code.append("    public int jumpAnimationTimeout = 0;\n");
        }
        if (config.hasSwimAnimation) {
            code.append("    public final AnimationState swimAnimationState = new AnimationState();\n");
        }
        code.append("\n");
        
        // Constructor
        code.append("    public ").append(entityClassName)
            .append("(EntityType<? extends ").append(config.parentClass).append("> pEntityType, Level pLevel) {\n");
        code.append("        super(pEntityType, pLevel);\n");
        code.append("    }\n\n");
        
        // Tick method
        code.append("    @Override\n");
        code.append("    public void tick() {\n");
        code.append("        super.tick();\n\n");
        code.append("        if (this.level().isClientSide()) {\n");
        code.append("            setupAnimationStates();\n");
        code.append("        }\n");
        
        // Custom tick logic
        if (config.customFields.stream().anyMatch(f -> f.contains("horizontalCollision") || f.contains("climb"))) {
            code.append("\n");
            code.append("        // Custom movement logic\n");
            code.append("        if (!this.level().isClientSide && this.horizontalCollision) {\n");
            code.append("            this.setDeltaMovement(this.getDeltaMovement().x, 0.2D, this.getDeltaMovement().z);\n");
            code.append("        }\n");
        }
        
        code.append("    }\n\n");
        
        // Setup animation states
        code.append("    private void setupAnimationStates() {\n");
        if (config.hasIdleAnimation) {
            code.append("        // Idle animation: play continuously when stationary\n");
            code.append("        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6 && this.onGround()) {\n");
            code.append("            if (!this.idleAnimationState.isStarted()) {\n");
            code.append("                this.idleAnimationState.start(this.tickCount);\n");
            code.append("            }\n");
            code.append("        } else {\n");
            code.append("            this.idleAnimationState.stop();\n");
            code.append("        }\n\n");
        }
        
        if (config.hasAttackAnimation) {
            code.append("        // Attack animation - triggered when attacking\n");
            code.append("        if (this.swinging && attackAnimationTimeout <= 0) {\n");
            code.append("            attackAnimationTimeout = 12; // Animation length (0.6s * 20 ticks)\n");
            code.append("            attackAnimationState.start(this.tickCount);\n");
            code.append("        } else {\n");
            code.append("            --this.attackAnimationTimeout;\n");
            code.append("        }\n\n");
            code.append("        if (!this.swinging && attackAnimationTimeout <= 0) {\n");
            code.append("            attackAnimationState.stop();\n");
            code.append("        }\n\n");
        }
        
        if (config.hasJumpAnimation) {
            code.append("        // Jump animation - only trigger if entity is 2 blocks or higher from ground\n");
            code.append("        if (isAtLeast2BlocksHigh()) {\n");
            code.append("            if (!this.jumpAnimationState.isStarted()) {\n");
            code.append("                jumpAnimationTimeout = 10;\n");
            code.append("                this.jumpAnimationState.start(this.tickCount);\n");
            code.append("            }\n");
            code.append("        } else if (this.onGround() && jumpAnimationTimeout <= 0) {\n");
            code.append("            this.jumpAnimationState.stop();\n");
            code.append("        } else if (jumpAnimationTimeout > 0) {\n");
            code.append("            --this.jumpAnimationTimeout;\n");
            code.append("        }\n\n");
        }
        
        if (config.hasSwimAnimation) {
            code.append("        // Swimming animation - play when in water\n");
            code.append("        if (this.isInWater()) {\n");
            code.append("            if (!this.swimAnimationState.isStarted()) {\n");
            code.append("                this.swimAnimationState.start(this.tickCount);\n");
            code.append("            }\n");
            code.append("        } else {\n");
            code.append("            this.swimAnimationState.stop();\n");
            code.append("        }\n");
        }
        
        code.append("    }\n\n");
        
        // Helper method for jump detection
        if (config.hasJumpAnimation) {
            code.append("    /**\n");
            code.append("     * Checks if the entity is at least 2 blocks high from the ground\n");
            code.append("     */\n");
            code.append("    private boolean isAtLeast2BlocksHigh() {\n");
            code.append("        if (this.onGround()) {\n");
            code.append("            return false;\n");
            code.append("        }\n\n");
            code.append("        BlockPos entityPos = this.blockPosition();\n");
            code.append("        double entityY = this.getY();\n\n");
            code.append("        // Find the first solid block below the entity\n");
            code.append("        for (int y = 0; y <= 10; y++) {\n");
            code.append("            BlockPos checkPos = entityPos.below(y);\n");
            code.append("            BlockState state = this.level().getBlockState(checkPos);\n\n");
            code.append("            // Check if block is solid (not air and has collision shape)\n");
            code.append("            if (!state.isAir() && !state.getCollisionShape(this.level(), checkPos).isEmpty()) {\n");
            code.append("                double groundY = checkPos.getY() + 1.0; // Top of the block\n");
            code.append("                double distanceFromGround = entityY - groundY;\n\n");
            code.append("                // Check if entity is at least 2 blocks (2.0) above the ground\n");
            code.append("                return distanceFromGround >= 2.0;\n");
            code.append("            }\n");
            code.append("        }\n\n");
            code.append("        // If no solid block found within 10 blocks, assume we're high enough\n");
            code.append("        return true;\n");
            code.append("    }\n\n");
        }
        
        // Update walk animation
        code.append("    @Override\n");
        code.append("    protected void updateWalkAnimation(float pPartialTick) {\n");
        code.append("        float f;\n");
        code.append("        if (this.getPose() == Pose.STANDING) {\n");
        code.append("            f = Math.min(pPartialTick * 6f, 1f);\n");
        code.append("        } else {\n");
        code.append("            f = 0f;\n");
        code.append("        }\n");
        code.append("        this.walkAnimation.update(f, 0.2f);\n");
        code.append("    }\n\n");
        
        // Register goals
        code.append("    @Override\n");
        code.append("    protected void registerGoals() {\n");
        
        int priority = 0;
        if (config.canSwim) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new FloatGoal(this));\n");
        }
        
        if (config.canMeleeAttack && config.isHostile) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new MeleeAttackGoal(this, 1.0D, false));\n");
        }
        
        if (config.isHostile) {
            code.append("        this.goalSelector.addGoal(").append(priority++).append(", new ChangedEntityImprovedPathfindingGoal(this));\n");
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
            code.append("\n");
            code.append("        // Hostile targeting\n");
            if (config.canAttack) {
                code.append("        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));\n");
            }
            code.append("        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));\n");
        }
        
        code.append("    }\n\n");
        
        // Create attributes
        code.append("    public static AttributeSupplier.Builder createAttributes() {\n");
        code.append("        return ").append(config.parentClass).append(".createLivingAttributes()\n");
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
    
    private static void generateHumanoidModelClass(HumanoidEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String modelClassName = entityClassName + "Model";
        String animationsClassName = config.entityName + "Animations";
        String layerName = getSnakeCase(config.entityName).toUpperCase() + "_LAYER";
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.client.model;\n\n");
        code.append("// Made with Blockbench or Entity Generator\n");
        code.append("// IMPORTANT: You need to create the model definition (createBodyLayer) manually or export from Blockbench\n\n");
        code.append("import net.jerika.furmutage.entity.animations.").append(animationsClassName).append(";\n");
        code.append("import net.jerika.furmutage.entity.custom.").append(entityClassName).append(";\n");
        code.append("import net.minecraft.client.model.HierarchicalModel;\n");
        code.append("import net.minecraft.client.model.geom.ModelPart;\n");
        code.append("import net.minecraft.client.model.geom.PartPose;\n");
        code.append("import net.minecraft.client.model.geom.builders.*;\n");
        code.append("import net.minecraft.util.Mth;\n\n");
        
        code.append("public class ").append(modelClassName).append("<T extends ").append(entityClassName).append("> extends HierarchicalModel<T> {\n");
        code.append("    private final ModelPart root;\n");
        
        // Standard humanoid parts
        code.append("    private final ModelPart Head;\n");
        code.append("    private final ModelPart Torso;\n");
        code.append("    private final ModelPart LeftArm;\n");
        code.append("    private final ModelPart RightArm;\n");
        code.append("    private final ModelPart LeftLeg;\n");
        code.append("    private final ModelPart RightLeg;\n");
        
        if (config.hasTail) {
            code.append("    private final ModelPart Tail;\n");
        }
        if (config.hasTailPrimary) {
            code.append("    private final ModelPart TailPrimary;\n");
        }
        
        // Custom model parts
        for (String part : config.customModelParts) {
            code.append("    private final ModelPart ").append(part).append(";\n");
        }
        
        code.append("\n");
        
        // Constructor
        code.append("    public ").append(modelClassName).append("(ModelPart root) {\n");
        code.append("        this.root = root;\n");
        code.append("        this.Head = root.getChild(\"Head\");\n");
        code.append("        this.Torso = root.getChild(\"Torso\");\n");
        code.append("        this.LeftArm = root.getChild(\"LeftArm\");\n");
        code.append("        this.RightArm = root.getChild(\"RightArm\");\n");
        code.append("        this.LeftLeg = root.getChild(\"LeftLeg\");\n");
        code.append("        this.RightLeg = root.getChild(\"RightLeg\");\n");
        
        if (config.hasTail) {
            code.append("        this.Tail = Torso.getChild(\"Tail\");\n");
        }
        if (config.hasTailPrimary) {
            code.append("        this.TailPrimary = Tail.getChild(\"TailPrimary\");\n");
        }
        
        for (String part : config.customModelParts) {
            String parent = getPartParent(part);
            code.append("        this.").append(part).append(" = ").append(parent).append(".getChild(\"").append(part).append("\");\n");
        }
        
        code.append("    }\n\n");
        
        // createBodyLayer method - placeholder
        code.append("    public static LayerDefinition createBodyLayer() {\n");
        code.append("        MeshDefinition meshdefinition = new MeshDefinition();\n");
        code.append("        PartDefinition partdefinition = meshdefinition.getRoot();\n\n");
        code.append("        // TODO: Define your model parts here or export from Blockbench\n");
        code.append("        // Example structure:\n");
        code.append("        // PartDefinition Head = partdefinition.addOrReplaceChild(\"Head\", ...);\n");
        code.append("        // PartDefinition Torso = partdefinition.addOrReplaceChild(\"Torso\", ...);\n");
        code.append("        // etc.\n\n");
        code.append("        return LayerDefinition.create(meshdefinition, 128, 128);\n");
        code.append("    }\n\n");
        
        // root() method
        code.append("    @Override\n");
        code.append("    public ModelPart root() {\n");
        code.append("        return this.root;\n");
        code.append("    }\n\n");
        
        // setupAnim method
        code.append("    @Override\n");
        code.append("    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {\n");
        code.append("        this.root().getAllParts().forEach(ModelPart::resetPose);\n");
        code.append("        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);\n\n");
        
        if (config.hasWalkAnimation) {
            code.append("        // Walk animation\n");
            code.append("        this.animateWalk(").append(animationsClassName).append(".").append(getConstantName(config.entityName)).append("_WALK, limbSwing, limbSwingAmount, 2f, 2f);\n\n");
        }
        
        if (config.hasIdleAnimation) {
            code.append("        // Idle animation\n");
            code.append("        this.animate(entity.idleAnimationState, ").append(animationsClassName).append(".").append(getConstantName(config.entityName)).append("_IDLE, ageInTicks, 1.5f);\n\n");
        }
        
        if (config.hasAttackAnimation) {
            code.append("        // Attack animation\n");
            code.append("        this.animate(entity.attackAnimationState, ").append(animationsClassName).append(".").append(getConstantName(config.entityName)).append("_ATTACK, ageInTicks, 2f);\n\n");
        }
        
        if (config.hasJumpAnimation) {
            code.append("        // Jump animation\n");
            code.append("        this.animate(entity.jumpAnimationState, ").append(animationsClassName).append(".").append(getConstantName(config.entityName)).append("_JUMP, ageInTicks, 1.0f);\n\n");
        }
        
        if (config.hasSwimAnimation) {
            code.append("        // Swimming animation - when in water\n");
            code.append("        if (entity.isInWater()) {\n");
            code.append("            this.animate(entity.swimAnimationState, ").append(animationsClassName).append(".").append(getConstantName(config.entityName)).append("_SWIM, ageInTicks, 1.0f);\n");
            code.append("        }\n");
        }
        
        code.append("    }\n\n");
        
        // applyHeadRotation method
        code.append("    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {\n");
        code.append("        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);\n");
        code.append("        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);\n\n");
        code.append("        this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);\n");
        code.append("        this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);\n");
        code.append("    }\n");
        code.append("}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateHumanoidRendererClass(HumanoidEntityConfig config, Path filePath) throws IOException {
        String entityClassName = config.entityName + "Entity";
        String rendererClassName = entityClassName + "Renderer";
        String modelClassName = entityClassName + "Model";
        String layerName = getSnakeCase(config.entityName).toUpperCase() + "_LAYER";
        
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
        code.append("        super(pContext, new ").append(modelClassName).append("<>(pContext.bakeLayer(ModModelLayers.").append(layerName).append(")), ")
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
    
    private static void generateAnimationsClass(HumanoidEntityConfig config, Path filePath) throws IOException {
        String animationsClassName = config.entityName + "Animations";
        String constantPrefix = getConstantName(config.entityName);
        
        StringBuilder code = new StringBuilder();
        
        code.append("package net.jerika.furmutage.entity.animations;\n\n");
        code.append("import net.minecraft.client.animation.AnimationChannel;\n");
        code.append("import net.minecraft.client.animation.AnimationDefinition;\n");
        code.append("import net.minecraft.client.animation.Keyframe;\n");
        code.append("import net.minecraft.client.animation.KeyframeAnimations;\n\n");
        
        code.append("/**\n");
        code.append(" * Animation definitions for ").append(config.displayName).append("\n");
        code.append(" * Based on LatexExoMutantAnimations - smooth and fluid animations\n");
        code.append(" */\n");
        code.append("public class ").append(animationsClassName).append(" {\n");
        
        // IDLE Animation
        if (config.hasIdleAnimation) {
            code.append("    public static final AnimationDefinition ").append(constantPrefix).append("_IDLE = AnimationDefinition.Builder.withLength(2.5F).looping()\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.POSITION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"Head\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(2.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(2.5F, KeyframeAnimations.degreeVec(-2.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            
            if (config.hasTail) {
                code.append("        .addAnimation(\"Tail\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
                code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
                code.append("        ))\n");
                
                if (config.hasTailPrimary) {
                    code.append("        .addAnimation(\"TailPrimary\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
                    code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                    code.append("            new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                    code.append("            new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
                    code.append("        ))\n");
                }
            }
            
            code.append("        .build();\n\n");
        }
        
        // WALK Animation
        if (config.hasWalkAnimation) {
            code.append("    public static final AnimationDefinition ").append(constantPrefix).append("_WALK = AnimationDefinition.Builder.withLength(1.0F).looping()\n");
            code.append("        // Torso rotation - natural side-to-side sway matching leg movement\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        // Torso vertical movement - natural weight shift\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.POSITION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        // Left leg - natural forward/backward swing with proper timing\n");
            code.append("        .addAnimation(\"LeftLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.125F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.375F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.875F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        // Right leg - opposite phase to left leg\n");
            code.append("        .addAnimation(\"RightLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.125F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.375F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.875F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        // Left arm - swings opposite to left leg (natural gait)\n");
            code.append("        .addAnimation(\"LeftArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.125F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.875F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        // Right arm - swings opposite to right leg\n");
            code.append("        .addAnimation(\"RightArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.375F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.625F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.875F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            
            if (config.hasTail) {
                code.append("        // Tail - natural sway following body movement\n");
                code.append("        .addAnimation(\"Tail\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
                code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
                code.append("        ))\n");
                
                if (config.hasTailPrimary) {
                    code.append("        // Tail primary - secondary sway for more natural movement\n");
                    code.append("        .addAnimation(\"TailPrimary\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
                    code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                    code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                    code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
                    code.append("        ))\n");
                }
            }
            
            code.append("        // Head - slight bobbing to match body movement\n");
            code.append("        .addAnimation(\"Head\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .build();\n\n");
        }
        
        // ATTACK Animation
        if (config.hasAttackAnimation) {
            code.append("    public static final AnimationDefinition ").append(constantPrefix).append("_ATTACK = AnimationDefinition.Builder.withLength(0.6F)\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.2F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.4F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"LeftArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.2F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.4F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"RightArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.2F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.4F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"Head\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.2F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.4F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .build();\n\n");
        }
        
        // JUMP Animation
        if (config.hasJumpAnimation) {
            code.append("    public static final AnimationDefinition ").append(constantPrefix).append("_JUMP = AnimationDefinition.Builder.withLength(0.5F)\n");
            code.append("        .addAnimation(\"LeftLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"RightLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)\n");
            code.append("        ))\n");
            code.append("        .build();\n\n");
        }
        
        // SWIM Animation
        if (config.hasSwimAnimation) {
            code.append("    public static final AnimationDefinition ").append(constantPrefix).append("_SWIM = AnimationDefinition.Builder.withLength(1.0F).looping()\n");
            code.append("        // Swimming animation - similar to player swimming with arm and leg movements\n");
            code.append("        .addAnimation(\"Torso\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"LeftArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, -60.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"RightArm\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 60.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"LeftLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(30.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(60.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(30.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            code.append("        .addAnimation(\"RightLeg\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
            code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.25F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(0.75F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
            code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
            code.append("        ))\n");
            
            if (config.hasTail) {
                code.append("        .addAnimation(\"Tail\", new AnimationChannel(AnimationChannel.Targets.ROTATION,\n");
                code.append("            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),\n");
                code.append("            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)\n");
                code.append("        ))\n");
            }
            
            code.append("        .build();\n");
        }
        
        code.append("}\n");
        
        Files.write(filePath, code.toString().getBytes());
        System.out.println("Generated: " + filePath);
    }
    
    private static void generateHumanoidRegistrationSnippets(HumanoidEntityConfig config) throws IOException {
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
        
        // Write snippets to files
        Path snippetsDir = Paths.get("entity_generator_output");
        Files.createDirectories(snippetsDir);
        
        Files.write(snippetsDir.resolve(config.entityName + "_registration.txt"), regCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_client_setup.txt"), clientCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_model_layer.txt"), layerCode.toString().getBytes());
        Files.write(snippetsDir.resolve(config.entityName + "_layer_definition.txt"), layerDefCode.toString().getBytes());
        
        System.out.println("Generated snippets in: entity_generator_output/");
    }
    
    private static String getSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private static String getConstantName(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }
    
    private static String getPartParent(String partName) {
        // Default parent for custom parts - can be overridden
        if (partName.toLowerCase().contains("ear") || partName.toLowerCase().contains("horn")) {
            return "Head";
        } else if (partName.toLowerCase().contains("wing")) {
            return "Torso";
        }
        return "Torso"; // Default
    }
    
    /**
     * Example usage
     */
    public static void main(String[] args) throws IOException {
        // Example: Create a Dark Latex Wolf humanoid entity
        HumanoidEntityConfig darkWolf = new HumanoidEntityConfig("DarkLatexWolf");
        darkWolf.description = "A dark latex wolf entity with full animations";
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
        darkWolf.hasTail = true;
        darkWolf.hasTailPrimary = true;
        darkWolf.customImports.add("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal");
        
        generateHumanoidEntity(darkWolf);
        
        System.out.println("\n=== Example Usage ===");
        System.out.println("HumanoidEntityConfig config = new HumanoidEntityConfig(\"YourEntityName\");");
        System.out.println("// Set properties...");
        System.out.println("HumanoidLatexEntityGenerator.generateHumanoidEntity(config);");
    }
}

