# Humanoid Latex Entity Generator

A comprehensive data generator for creating humanoid latex entities with full animation support in the Furmutage mod.

## Features

- ✅ Generates entity classes with `AnimationState` fields and setup logic
- ✅ Generates model classes with `HierarchicalModel` and `setupAnim()` method
- ✅ Generates renderer classes extending `MobRenderer`
- ✅ Generates animation classes with pre-configured animations:
  - **Idle Animation**: Smooth breathing and subtle movement
  - **Walk Animation**: Natural bipedal walking with arm and leg coordination
  - **Attack Animation**: Dual-arm attack animation
  - **Jump Animation**: Jumping pose animation
  - **Swim Animation**: Swimming motion with arm and leg movements
- ✅ Generates registration code snippets for easy integration
- ✅ Supports custom model parts (wings, horns, additional tails, etc.)
- ✅ Builder pattern for easy configuration

## Animations Source

All animations are based on `LatexExoMutantAnimations`, which provides smooth, fluid animations similar to the Changed mod's latex entities. The animations use `CATMULLROM` interpolation for natural movement.

## Quick Start

### Basic Usage

```java
import net.jerika.furmutage.datagen.*;

// Create configuration using builder
HumanoidLatexEntityGenerator.HumanoidEntityConfig config = 
    new HumanoidEntityConfigBuilder("DarkLatexWolf")
        .description("A dark latex wolf with animations")
        .size(0.7f, 1.93f)
        .attributes(30.0, 0.3, 5.0, 32.0)
        .category(EntityGenerator.MobCategory.MONSTER)
        .hostile()
        .hasTail(true, true)
        .allAnimations()
        .build();

// Generate all files
HumanoidLatexEntityGenerator.generateHumanoidEntity(config);
```

### What Gets Generated

1. **Entity Class** (`DarkLatexWolfEntity.java`)
   - Animation state fields (`idleAnimationState`, `attackAnimationState`, etc.)
   - `setupAnimationStates()` method for client-side animation logic
   - `registerGoals()` for AI behavior
   - `createAttributes()` for entity stats

2. **Model Class** (`DarkLatexWolfEntityModel.java`)
   - Extends `HierarchicalModel`
   - `setupAnim()` method that applies animations
   - Placeholder `createBodyLayer()` (you need to fill this in or export from Blockbench)

3. **Renderer Class** (`DarkLatexWolfEntityRenderer.java`)
   - Extends `MobRenderer`
   - Texture location method
   - Optional baby scaling support

4. **Animations Class** (`DarkLatexWolfAnimations.java`)
   - Pre-configured animations copied from `LatexExoMutantAnimations`
   - All keyframes and interpolation settings included

5. **Registration Snippets** (in `entity_generator_output/`)
   - Code to add to `ModEntities.java`
   - Code to add to `furmutage.java` client setup
   - Code to add to `ModModelLayers.java`

## Configuration Options

### Basic Properties

```java
.description("Entity description")              // Optional javadoc description
.size(width, height)                            // Entity bounding box
.parentClass("Monster")                         // Parent class (Monster, Animal, etc.)
.category(EntityGenerator.MobCategory.MONSTER) // Mob category for spawning
```

### Attributes

```java
.attributes(maxHealth, movementSpeed, attackDamage, followRange)
// Or individually:
.maxHealth(30.0)
.movementSpeed(0.3)
.attackDamage(5.0)
.followRange(32.0)
```

### AI Behavior

```java
.hostile()                    // Makes entity hostile (enables attack goals)
.peaceful()                   // Makes entity peaceful
.canSwim(true)                // Can swim
.canAttack(true)              // Can attack entities
.canMeleeAttack(true)         // Uses melee attack goal
.avoidsWater(true)            // Uses WaterAvoidingRandomStrollGoal
.looksAtPlayer(true)          // Looks at nearby players
```

### Model Features

```java
.hasTail(true, true)          // Has tail, and secondary tail segment
.addCustomModelPart("Wings")  // Add custom model parts (wings, horns, etc.)
```

### Animations

```java
.allAnimations()              // Enable all animations (idle, walk, attack, jump, swim)
.noAnimations()               // Disable all animations
.animations(idle, walk, attack, jump, swim)  // Enable specific animations
```

### Rendering

```java
.texturePath("custom/path/texture.png")  // Custom texture path
.shadowRadius(0.5f)                      // Shadow size
.hasBabyScaling(true)                    // Enable baby scaling in renderer
```

### Custom Code

```java
.addCustomImport("package.Class")        // Add import statements
.addCustomField("private int customField;")  // Add custom fields
.addCustomMethod("public void customMethod() { ... }")  // Add custom methods
```

## Complete Example

```java
HumanoidLatexEntityGenerator.HumanoidEntityConfig latexTiger = 
    new HumanoidEntityConfigBuilder("LatexTiger")
        .description("A powerful latex tiger with wall climbing")
        .size(0.8f, 2.0f)
        .attributes(35.0, 0.35, 6.0, 40.0)
        .category(EntityGenerator.MobCategory.MONSTER)
        .hostile()
        .avoidsWater(true)
        .hasTail(true, true)
        .addCustomModelPart("LeftEar")
        .addCustomModelPart("RightEar")
        .allAnimations()
        .shadowRadius(0.6f)
        .addCustomImport("net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal")
        .build();

HumanoidLatexEntityGenerator.generateHumanoidEntity(latexTiger);
```

## After Generation

After generating your entity, you need to:

1. **Copy Registration Code**
   - Copy from `entity_generator_output/{EntityName}_registration.txt`
   - Paste into `ModEntities.java`

2. **Copy Client Setup Code**
   - Copy from `entity_generator_output/{EntityName}_client_setup.txt`
   - Paste into `furmutage.java` in the `onClientSetup` method

3. **Add Model Layer**
   - Copy from `entity_generator_output/{EntityName}_model_layer.txt`
   - Paste into `ModModelLayers.java`

4. **Register Layer Definition**
   - Copy from `entity_generator_output/{EntityName}_layer_definition.txt`
   - Paste into `ModEventBusClientEvents.java` in the `registerLayer` method

5. **Create Model Definition**
   - The model class has a placeholder `createBodyLayer()` method
   - Either:
     - Manually define using `MeshDefinition` and `CubeListBuilder`
     - Export from Blockbench (recommended)
     - Copy from an existing similar model

6. **Create Texture**
   - Create texture at: `assets/furmutage/textures/entity/{entity_name}.png`
   - Default size: 128x128 pixels (or larger)

7. **Register Attributes**
   - In your mod's attribute registration event, call:
   ```java
   event.put(ModEntities.YOUR_ENTITY.get(), YourEntityEntity.createAttributes().build());
   ```

## Animation Details

All animations are pre-configured with keyframes copied from `LatexExoMutantAnimations`:

- **Idle**: 2.5 second loop with subtle torso movement, head rotation, and tail sway
- **Walk**: 1.0 second loop with coordinated leg and arm movement, torso rotation
- **Attack**: 0.6 second one-time animation with dual-arm strike
- **Jump**: 0.5 second animation triggered when entity is 2+ blocks high
- **Swim**: 1.0 second loop for swimming motion

Animations automatically handle:
- Head rotation based on entity looking direction
- Walk animation blending based on movement speed
- Animation state management (start/stop)
- Client-side only animation updates

## Troubleshooting

### Model parts not animating
- Make sure model part names in `createBodyLayer()` match animation part names exactly
- Check that parts are properly parented (Tail under Torso, etc.)

### Animations not playing
- Verify `setupAnimationStates()` is called in `tick()` method
- Check that animations are properly referenced in `setupAnim()` method
- Ensure entity is on client side when checking animation states

### Compilation errors
- Make sure all required imports are present
- Check that `ModModelLayers` has the layer defined
- Verify entity is registered in `ModEntities`

## See Also

- `ExampleHumanoidEntityGenerator.java` - More examples
- `LatexExoMutantAnimations.java` - Source of animation definitions
- `LatexExoMutantEntity.java` - Example of entity with animations
- `LatexExoMutantModel.java` - Example of model with animations

