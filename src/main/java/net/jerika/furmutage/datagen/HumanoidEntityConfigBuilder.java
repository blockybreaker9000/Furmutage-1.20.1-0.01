package net.jerika.furmutage.datagen;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern for creating HumanoidEntityConfig objects easily
 * 
 * Usage:
 * HumanoidEntityConfig config = new HumanoidEntityConfigBuilder("DarkLatexWolf")
 *     .description("A dark latex wolf with animations")
 *     .size(0.7f, 1.93f)
 *     .attributes(30.0, 0.3, 5.0, 32.0)
 *     .hostile()
 *     .hasTail(true, true)
 *     .build();
 */
public class HumanoidEntityConfigBuilder {
    private final HumanoidLatexEntityGenerator.HumanoidEntityConfig config;
    
    public HumanoidEntityConfigBuilder(String entityName) {
        this.config = new HumanoidLatexEntityGenerator.HumanoidEntityConfig(entityName);
    }
    
    public HumanoidEntityConfigBuilder description(String description) {
        config.description = description;
        return this;
    }
    
    public HumanoidEntityConfigBuilder parentClass(String parentClass) {
        config.parentClass = parentClass;
        return this;
    }
    
    public HumanoidEntityConfigBuilder category(EntityGenerator.MobCategory category) {
        config.category = category;
        return this;
    }
    
    public HumanoidEntityConfigBuilder size(float width, float height) {
        config.width = width;
        config.height = height;
        return this;
    }
    
    public HumanoidEntityConfigBuilder attributes(double maxHealth, double movementSpeed, double attackDamage, double followRange) {
        config.maxHealth = maxHealth;
        config.movementSpeed = movementSpeed;
        config.attackDamage = attackDamage;
        config.followRange = followRange;
        return this;
    }
    
    public HumanoidEntityConfigBuilder maxHealth(double maxHealth) {
        config.maxHealth = maxHealth;
        return this;
    }
    
    public HumanoidEntityConfigBuilder movementSpeed(double speed) {
        config.movementSpeed = speed;
        return this;
    }
    
    public HumanoidEntityConfigBuilder attackDamage(double damage) {
        config.attackDamage = damage;
        return this;
    }
    
    public HumanoidEntityConfigBuilder followRange(double range) {
        config.followRange = range;
        return this;
    }
    
    public HumanoidEntityConfigBuilder canSwim(boolean canSwim) {
        config.canSwim = canSwim;
        return this;
    }
    
    public HumanoidEntityConfigBuilder hostile() {
        config.isHostile = true;
        config.canAttack = true;
        config.canMeleeAttack = true;
        return this;
    }
    
    public HumanoidEntityConfigBuilder peaceful() {
        config.isHostile = false;
        config.canAttack = false;
        config.canMeleeAttack = false;
        return this;
    }
    
    public HumanoidEntityConfigBuilder canAttack(boolean canAttack) {
        config.canAttack = canAttack;
        return this;
    }
    
    public HumanoidEntityConfigBuilder canMeleeAttack(boolean canMeleeAttack) {
        config.canMeleeAttack = canMeleeAttack;
        return this;
    }
    
    public HumanoidEntityConfigBuilder avoidsWater(boolean avoidsWater) {
        config.avoidsWater = avoidsWater;
        return this;
    }
    
    public HumanoidEntityConfigBuilder looksAtPlayer(boolean looksAtPlayer) {
        config.looksAtPlayer = looksAtPlayer;
        return this;
    }
    
    public HumanoidEntityConfigBuilder hasTail(boolean hasTail, boolean hasTailPrimary) {
        config.hasTail = hasTail;
        config.hasTailPrimary = hasTailPrimary;
        return this;
    }
    
    public HumanoidEntityConfigBuilder hasTail(boolean hasTail) {
        config.hasTail = hasTail;
        config.hasTailPrimary = false;
        return this;
    }
    
    public HumanoidEntityConfigBuilder addCustomModelPart(String partName) {
        if (config.customModelParts == null) {
            config.customModelParts = new ArrayList<>();
        }
        config.customModelParts.add(partName);
        return this;
    }
    
    public HumanoidEntityConfigBuilder animations(boolean idle, boolean walk, boolean attack, boolean jump, boolean swim) {
        config.hasIdleAnimation = idle;
        config.hasWalkAnimation = walk;
        config.hasAttackAnimation = attack;
        config.hasJumpAnimation = jump;
        config.hasSwimAnimation = swim;
        return this;
    }
    
    public HumanoidEntityConfigBuilder allAnimations() {
        config.hasIdleAnimation = true;
        config.hasWalkAnimation = true;
        config.hasAttackAnimation = true;
        config.hasJumpAnimation = true;
        config.hasSwimAnimation = true;
        return this;
    }
    
    public HumanoidEntityConfigBuilder noAnimations() {
        config.hasIdleAnimation = false;
        config.hasWalkAnimation = false;
        config.hasAttackAnimation = false;
        config.hasJumpAnimation = false;
        config.hasSwimAnimation = false;
        return this;
    }
    
    public HumanoidEntityConfigBuilder texturePath(String path) {
        config.texturePath = path;
        return this;
    }
    
    public HumanoidEntityConfigBuilder hasBabyScaling(boolean hasBabyScaling) {
        config.hasBabyScaling = hasBabyScaling;
        return this;
    }
    
    public HumanoidEntityConfigBuilder shadowRadius(float radius) {
        config.shadowRadius = radius;
        return this;
    }
    
    public HumanoidEntityConfigBuilder addCustomImport(String importStatement) {
        if (config.customImports == null) {
            config.customImports = new ArrayList<>();
        }
        config.customImports.add(importStatement);
        return this;
    }
    
    public HumanoidEntityConfigBuilder addCustomField(String field) {
        if (config.customFields == null) {
            config.customFields = new ArrayList<>();
        }
        config.customFields.add(field);
        return this;
    }
    
    public HumanoidEntityConfigBuilder addCustomMethod(String method) {
        if (config.customMethods == null) {
            config.customMethods = new ArrayList<>();
        }
        config.customMethods.add(method);
        return this;
    }
    
    public HumanoidLatexEntityGenerator.HumanoidEntityConfig build() {
        return config;
    }
}

