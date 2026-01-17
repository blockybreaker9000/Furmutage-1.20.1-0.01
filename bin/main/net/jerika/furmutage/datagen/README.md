# Entity Generator Tool

This tool automatically generates entity classes, renderers, and registration code for the Furmutage mod.

## Quick Start

1. **Create an entity configuration** using the `EntityConfigBuilder`:

```java
EntityConfig config = new EntityConfigBuilder("YourEntityName")
    .description("Description of your entity")
    .monster()  // or .animal() or .creature()
    .size(0.7f, 1.93f)
    .health(20.0)
    .speed(0.25)
    .damage(3.0)
    .hostile()
    .build();
```

2. **Generate the entity**:

```java
EntityGenerator.generateEntity(config);
```

3. **The generator will create**:
   - Entity class: `src/main/java/net/jerika/furmutage/entity/custom/YourEntityNameEntity.java`
   - Renderer class: `src/main/java/net/jerika/furmutage/entity/client/renderer/YourEntityNameEntityRenderer.java`
   - Registration snippets: `entity_generator_output/YourEntityName_registration.txt`
   - Client setup snippets: `entity_generator_output/YourEntityName_client_setup.txt`

4. **Copy the snippets**:
   - Add registration code to `ModEntities.java`
   - Add client setup code to `furmutage.java` in the `ClientModEvents.onClientSetup` method

5. **Create resources**:
   - Texture: `src/main/resources/assets/furmutage/textures/entity/your_entity_name.png`
   - Model class (if using custom model): `src/main/java/net/jerika/furmutage/entity/client/model/YourEntityNameModel.java`

## Examples

### Simple Monster

```java
EntityConfig monster = new EntityConfigBuilder("DarkLatexWolf")
    .description("A dark latex wolf that hunts players")
    .monster()
    .size(0.7f, 1.93f)
    .health(30.0)
    .speed(0.3)
    .damage(5.0)
    .hostile()
    .avoidsWater(true)
    .build();

EntityGenerator.generateEntity(monster);
```

### Passive Animal

```java
EntityConfig animal = new EntityConfigBuilder("LatexRabbit")
    .description("A peaceful latex rabbit")
    .animal()
    .size(0.4f, 0.5f)
    .health(5.0)
    .speed(0.25)
    .peaceful()
    .babyScaling(true)
    .build();

EntityGenerator.generateEntity(animal);
```

### Infected Vanilla Mob

```java
EntityConfig infectedCow = new EntityConfigBuilder("DarkLatexCow")
    .description("Dark latex infected cow - now hostile")
    .extendsClass("Cow")
    .category(EntityGenerator.MobCategory.MONSTER)
    .size(0.9f, 1.4f)
    .health(20.0)
    .speed(0.25)
    .damage(3.0)
    .hostile()
    .sounds("ModSounds.DARK_LATEX_COW_AMBIENT", 
            "ModSounds.DARK_LATEX_COW_HURT", 
            "ModSounds.DARK_LATEX_COW_DEATH")
    .texture("textures/entity/dark_latex_cow.png")
    .addImport("net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal")
    .build();

EntityGenerator.generateEntity(infectedCow);
```

## Builder Methods

### Entity Type
- `.monster()` - Creates a Monster entity (hostile by default)
- `.animal()` - Creates an Animal entity (peaceful by default)
- `.creature()` - Creates a PathfinderMob entity
- `.extendsClass(String)` - Extends a specific class (e.g., "Cow", "Pig")

### Properties
- `.description(String)` - Adds a javadoc description
- `.size(float width, float height)` - Sets entity bounding box size
- `.health(double)` - Maximum health
- `.speed(double)` - Movement speed
- `.damage(double)` - Attack damage
- `.followRange(double)` - AI follow range

### Behavior
- `.hostile()` - Makes entity attack players
- `.peaceful()` - Makes entity passive
- `.canSwim(boolean)` - Can swim in water
- `.avoidsWater(boolean)` - Avoids water blocks
- `.looksAtPlayer(boolean)` - Looks at nearby players

### Audio/Visual
- `.sounds(String ambient, String hurt, String death)` - Sound events
  - Use `"ModSounds.SOUND_NAME"` for mod sounds
  - Use `"SoundEvents.SOUND_NAME"` for vanilla sounds
- `.texture(String path)` - Texture path (defaults to `textures/entity/{name}.png`)
- `.babyScaling(boolean)` - Enable baby scaling in renderer
- `.shadowRadius(float)` - Shadow radius

### Advanced
- `.category(MobCategory)` - Mob category (MONSTER, CREATURE, WATER_CREATURE, MISC)
- `.addImport(String)` - Add custom import
- `.addField(String)` - Add custom field to entity class
- `.addMethod(String)` - Add custom method to entity class

## Generated Files Structure

```
src/main/java/net/jerika/furmutage/
├── entity/
│   └── custom/
│       └── YourEntityNameEntity.java          [Generated]
└── entity/client/
    └── renderer/
        └── YourEntityNameEntityRenderer.java  [Generated]

entity_generator_output/
├── YourEntityName_registration.txt            [Generated snippet]
└── YourEntityName_client_setup.txt            [Generated snippet]
```

## Notes

- The generator does NOT create model classes - you'll need to create those manually or use an existing model
- Texture files are not generated - you need to create them manually
- Sound events must be registered in `ModSounds.java` before using
- Model layers must be added to `ModModelLayers.java` if using custom models

## Running

Run `ExampleEntityGenerator.main()` to see examples, or create your own generator class:

```java
public class MyEntityGenerator {
    public static void main(String[] args) throws IOException {
        // Your entity configurations here
        EntityGenerator.generateEntity(yourConfig);
    }
}
```

