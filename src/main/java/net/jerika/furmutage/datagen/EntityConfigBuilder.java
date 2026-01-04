package net.jerika.furmutage.datagen;

import net.jerika.furmutage.datagen.EntityGenerator.EntityConfig;
import net.jerika.furmutage.datagen.EntityGenerator.MobCategory;

/**
 * Builder class for creating EntityConfig easily
 * 
 * Example:
 * EntityConfig config = new EntityConfigBuilder("DarkLatexWolf")
 *     .monster()
 *     .size(0.7f, 1.93f)
 *     .health(30.0)
 *     .speed(0.3)
 *     .damage(5.0)
 *     .hostile()
 *     .build();
 */
public class EntityConfigBuilder {
    private final EntityConfig config;
    
    public EntityConfigBuilder(String entityName) {
        this.config = new EntityConfig(entityName);
    }
    
    public EntityConfigBuilder description(String desc) {
        config.description = desc;
        return this;
    }
    
    public EntityConfigBuilder monster() {
        config.baseType = EntityGenerator.EntityBaseType.MONSTER;
        config.parentClass = "Monster";
        config.category = MobCategory.MONSTER;
        config.isHostile = true;
        return this;
    }
    
    public EntityConfigBuilder animal() {
        config.baseType = EntityGenerator.EntityBaseType.ANIMAL;
        config.parentClass = "Animal";
        config.category = MobCategory.CREATURE;
        config.isHostile = false;
        return this;
    }
    
    public EntityConfigBuilder creature() {
        config.baseType = EntityGenerator.EntityBaseType.CREATURE;
        config.parentClass = "PathfinderMob";
        config.category = MobCategory.CREATURE;
        return this;
    }
    
    public EntityConfigBuilder extendsClass(String parentClass) {
        config.parentClass = parentClass;
        return this;
    }
    
    public EntityConfigBuilder size(float width, float height) {
        config.width = width;
        config.height = height;
        return this;
    }
    
    public EntityConfigBuilder health(double health) {
        config.maxHealth = health;
        return this;
    }
    
    public EntityConfigBuilder speed(double speed) {
        config.movementSpeed = speed;
        return this;
    }
    
    public EntityConfigBuilder damage(double damage) {
        config.attackDamage = damage;
        return this;
    }
    
    public EntityConfigBuilder followRange(double range) {
        config.followRange = range;
        return this;
    }
    
    public EntityConfigBuilder category(MobCategory category) {
        config.category = category;
        return this;
    }
    
    public EntityConfigBuilder hostile() {
        config.isHostile = true;
        config.canAttack = true;
        config.canMeleeAttack = true;
        return this;
    }
    
    public EntityConfigBuilder peaceful() {
        config.isHostile = false;
        config.canAttack = false;
        config.canMeleeAttack = false;
        return this;
    }
    
    public EntityConfigBuilder canSwim(boolean canSwim) {
        config.canSwim = canSwim;
        return this;
    }
    
    public EntityConfigBuilder avoidsWater(boolean avoids) {
        config.avoidsWater = avoids;
        return this;
    }
    
    public EntityConfigBuilder looksAtPlayer(boolean looks) {
        config.looksAtPlayer = looks;
        return this;
    }
    
    public EntityConfigBuilder sounds(String ambient, String hurt, String death) {
        config.ambientSound = ambient;
        config.hurtSound = hurt;
        config.deathSound = death;
        return this;
    }
    
    public EntityConfigBuilder texture(String path) {
        config.texturePath = path;
        return this;
    }
    
    public EntityConfigBuilder babyScaling(boolean hasBaby) {
        config.hasBabyScaling = hasBaby;
        return this;
    }
    
    public EntityConfigBuilder shadowRadius(float radius) {
        config.shadowRadius = radius;
        return this;
    }
    
    public EntityConfigBuilder addImport(String importPath) {
        config.customImports.add(importPath);
        return this;
    }
    
    public EntityConfigBuilder addField(String field) {
        config.customFields.add(field);
        return this;
    }
    
    public EntityConfigBuilder addMethod(String method) {
        config.customMethods.add(method);
        return this;
    }
    
    public EntityConfig build() {
        return config;
    }
}

