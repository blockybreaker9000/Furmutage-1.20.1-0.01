package net.jerika.furmutage.datagen;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern for creating ChangedEntityConfig objects easily
 * 
 * Usage:
 * ChangedEntityConfig config = new ChangedEntityConfigBuilder("LatexWolf")
 *     .description("A latex wolf using Changed animations")
 *     .size(0.7f, 1.93f)
 *     .attributes(30.0, 0.3, 5.0, 32.0)
 *     .wolfLike()  // Uses wolfLike animator preset
 *     .build();
 */
public class ChangedEntityConfigBuilder {
    private final ChangedStyleHumanoidGenerator.ChangedEntityConfig config;
    
    public ChangedEntityConfigBuilder(String entityName) {
        this.config = new ChangedStyleHumanoidGenerator.ChangedEntityConfig(entityName);
    }
    
    public ChangedEntityConfigBuilder description(String description) {
        config.description = description;
        return this;
    }
    
    public ChangedEntityConfigBuilder parentClass(String parentClass) {
        config.parentClass = parentClass;
        return this;
    }
    
    public ChangedEntityConfigBuilder category(EntityGenerator.MobCategory category) {
        config.category = category;
        return this;
    }
    
    public ChangedEntityConfigBuilder size(float width, float height) {
        config.width = width;
        config.height = height;
        return this;
    }
    
    public ChangedEntityConfigBuilder attributes(double maxHealth, double movementSpeed, double attackDamage, double followRange) {
        config.maxHealth = maxHealth;
        config.movementSpeed = movementSpeed;
        config.attackDamage = attackDamage;
        config.followRange = followRange;
        return this;
    }
    
    public ChangedEntityConfigBuilder maxHealth(double maxHealth) {
        config.maxHealth = maxHealth;
        return this;
    }
    
    public ChangedEntityConfigBuilder movementSpeed(double speed) {
        config.movementSpeed = speed;
        return this;
    }
    
    public ChangedEntityConfigBuilder attackDamage(double damage) {
        config.attackDamage = damage;
        return this;
    }
    
    public ChangedEntityConfigBuilder followRange(double range) {
        config.followRange = range;
        return this;
    }
    
    // Animator preset shortcuts
    public ChangedEntityConfigBuilder wolfLike() {
        config.presetType = ChangedStyleHumanoidGenerator.AnimatorPresetType.WOLF_LIKE;
        config.hasEars = true;
        config.hasTail = true;
        config.tailJoints = 3;
        config.hasDigitigradeLegs = true;
        return this;
    }
    
    public ChangedEntityConfigBuilder catLike() {
        config.presetType = ChangedStyleHumanoidGenerator.AnimatorPresetType.CAT_LIKE;
        config.hasEars = true;
        config.hasTail = true;
        config.tailJoints = 3;
        config.hasDigitigradeLegs = true;
        return this;
    }
    
    public ChangedEntityConfigBuilder humanLike() {
        config.presetType = ChangedStyleHumanoidGenerator.AnimatorPresetType.HUMAN_LIKE;
        config.hasEars = false;
        config.hasTail = false;
        config.hasDigitigradeLegs = false;
        return this;
    }
    
    public ChangedEntityConfigBuilder dragonLike() {
        config.presetType = ChangedStyleHumanoidGenerator.AnimatorPresetType.DRAGON_LIKE;
        config.hasEars = false;
        config.hasTail = true;
        config.tailJoints = 3;
        config.hasDigitigradeLegs = true;
        return this;
    }
    
    public ChangedEntityConfigBuilder sharkLike() {
        config.presetType = ChangedStyleHumanoidGenerator.AnimatorPresetType.SHARK_LIKE;
        config.hasEars = false;
        config.hasTail = true;
        config.tailJoints = 3;
        config.hasDigitigradeLegs = true;
        return this;
    }
    
    public ChangedEntityConfigBuilder preset(ChangedStyleHumanoidGenerator.AnimatorPresetType presetType) {
        config.presetType = presetType;
        return this;
    }
    
    public ChangedEntityConfigBuilder hasEars(boolean hasEars) {
        config.hasEars = hasEars;
        return this;
    }
    
    public ChangedEntityConfigBuilder hasTail(boolean hasTail, int tailJoints) {
        config.hasTail = hasTail;
        config.tailJoints = tailJoints;
        return this;
    }
    
    public ChangedEntityConfigBuilder hasTail(boolean hasTail) {
        config.hasTail = hasTail;
        if (hasTail) {
            config.tailJoints = 3;
        }
        return this;
    }
    
    public ChangedEntityConfigBuilder digitigradeLegs(boolean digitigrade) {
        config.hasDigitigradeLegs = digitigrade;
        return this;
    }
    
    public ChangedEntityConfigBuilder hipOffset(float offset) {
        config.hipOffset = offset;
        return this;
    }
    
    public ChangedEntityConfigBuilder texturePath(String path) {
        config.texturePath = path;
        return this;
    }
    
    public ChangedEntityConfigBuilder shadowRadius(float radius) {
        config.shadowRadius = radius;
        return this;
    }
    
    public ChangedEntityConfigBuilder hasArmorSupport(boolean hasArmor) {
        config.hasArmorSupport = hasArmor;
        return this;
    }
    
    public ChangedEntityConfigBuilder addCustomImport(String importStatement) {
        if (config.customImports == null) {
            config.customImports = new ArrayList<>();
        }
        config.customImports.add(importStatement);
        return this;
    }
    
    public ChangedEntityConfigBuilder addCustomField(String field) {
        if (config.customFields == null) {
            config.customFields = new ArrayList<>();
        }
        config.customFields.add(field);
        return this;
    }
    
    public ChangedEntityConfigBuilder addCustomMethod(String method) {
        if (config.customMethods == null) {
            config.customMethods = new ArrayList<>();
        }
        config.customMethods.add(method);
        return this;
    }
    
    public ChangedStyleHumanoidGenerator.ChangedEntityConfig build() {
        return config;
    }
}

