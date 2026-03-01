# How Changed's Latex Layers (Cover) Work

Summary of how white/dark latex blocks produce and spread their "layers" (latex cover) in the Changed mod, based on `Changed-Minecraft-Mod` source.

---

## 1. What the "layer" is

- **Latex cover** is a **per-block state** stored **separately from the block** in each chunk section.
- It’s a `LatexCoverState`: which `LatexType` (NONE, WHITE_LATEX, DARK_LATEX) is on the block and **which faces** (UP, DOWN, NORTH, SOUTH, EAST, WEST) have latex, plus **saturation** (0–15).
- So one block can still be "dirt" or "stone"; the **cover** is an overlay that can be present on some or all faces and can spread.

**Storage:** Each `LevelChunkSection` has a `PalettedContainer<LatexCoverState>` (see `LevelChunkSectionMixin`). When you read "cover at position X", the game either:
- Uses that stored cover, or
- If the **block** at X is a **LatexCoveringSource** (e.g. white/dark latex block or fluid), it **doesn’t** use stored state there; it **returns the cover that block says it provides** (e.g. `WHITE_LATEX.get().sourceCoverState()`).

So: **latex blocks/fluids don’t store cover in the palette for their own position**; they *are* the source and report their cover via `getLatexCoverState()`. Stored cover is for **other** blocks that have been *covered* by spreading.

---

## 2. How blocks "produce" the layer (source)

- **White latex block** and **Dark latex block** extend `AbstractLatexBlock` and implement **`LatexCoveringSource`**.
- They return their cover from **`getLatexCoverState(blockState, blockPos)`**:
  - **WhiteLatexBlock:** `ChangedLatexTypes.WHITE_LATEX.get().sourceCoverState()`
  - **DarkLatexBlock:** `ChangedLatexTypes.DARK_LATEX.get().sourceCoverState()`
- **sourceCoverState()** (in `LatexType` / `SpreadingLatexType`) is the default cover state for that type (all faces false, saturation 0 unless overridden).
- **Fluid blocks** (e.g. `WhiteLatexFluidBlock`, `DarkLatexFluidBlock`) also implement `LatexCoveringSource`: when **GROUNDED** (sitting on a full block), they return the same type’s `sourceCoverState()`; otherwise they return NONE.

So: **latex blocks and grounded latex fluids** are the **sources** of cover. They don’t need stored cover at their position; the block/fluid *is* the source.

---

## 3. How the layer spreads

Spreading is driven by:

- **Random ticks** on **latex cover** (not on the block’s random tick).
- **Neighbor updates** when adjacent block or cover changes.

### 3.1 Random tick (main spread)

- **ServerLevelMixin** injects into **tickChunk**: for each chunk section that `isRandomlyTicking()`, it picks random positions and gets **LatexCoverState** at that position (`LatexCoverState.getAt(section, ...)`).
- If that cover **isRandomlyTicking()** (i.e. not air: `!isAir()`), it calls **`coverState.randomTick(serverLevel, blockPos, random)`**.
- So **any block that has non-air latex cover** (either from storage or from a LatexCoveringSource block) can be randomly ticked. In practice, sections are considered randomly ticking if they have **any** ticking cover (or ticking blocks); the mixin also makes **tickingLatexCoverCount** affect `isRandomlyTicking()`.

**SpreadingLatexType.randomTick():**

1. **Decay:** If `shouldDecay(state, level, blockPos)` → set cover at that pos to NONE and return.
2. **Can’t spread:** If `!canSpread(state)` (saturation >= 15) or game rule `RULE_LATEX_GROWTH_RATE == 0` or random skip → return.
3. Pick a **random direction** and a neighbor position `checkPos`.
4. **Target check:** Neighbor must:
   - Not be in `DENY_LATEX_COVER` and not be a full collision block.
   - Have **no cover** or **same-type cover with higher saturation** (so spread goes into empty or "weaker" cover).
5. **Support check:** Neighbor must have at least one face that can hold cover (`canExistOnSurface(...)` — sturdy face, not in DENY_LATEX_COVER).
6. **Upward spread** is rarer (1/3 chance when spreading upward).
7. Build **new cover** for the neighbor with **`spreadState(level, checkPos, state)`** (current state with saturation +1 and faces set from neighbors), run **defaultCoverBehavior** (e.g. turn grass → dead bush), then:
   - **level.setBlockAndUpdate(checkPos, event.getPlannedState())** (might replace the block)
   - **LatexCoverState.setAtAndUpdate(level, checkPos, event.plannedCoverState)** (set the cover there)

So: **cover spreads one block at a time** into adjacent air/non-full blocks (or same-type cover with lower saturation), and can replace blocks (e.g. grass → dead bush) when covering.

### 3.2 spreadState(level, blockPos, state)

- **SpreadingLatexType.spreadState()**:
  - Increases **saturation** by 1 (capped by state definition).
  - For each **direction**, sets the face (UP/DOWN/NORTH/SOUTH/EAST/WEST) to **true** if that neighbor has a **sturdy face** toward this block (`canExistOnSurface`).
  - If **no face** is true and the block is air → return NONE (no cover).
  - Otherwise return the new state (more saturation, faces set from neighbors).

So spread state is "current state + 1 saturation + which faces are supported by neighbors."

### 3.3 Neighbor-driven updates

- When cover is **set** somewhere, **updateNeighbourShapes** / **updateIndirectNeighbourShapes** run (see `LatexCoverState.markAndNotifyAt` and `setAt`).
- **executeShapeUpdate** loads the **current** cover at the position and calls **updateShape** for the given direction and neighbor (block state or neighbor cover state).
- **SpreadingLatexType.updateShape(direction, neighborState)** (block neighbor): sets the face in that direction to whether the neighbor block can support cover.
- **SpreadingLatexType.updateShape(direction, neighborCoverState)** (latex neighbor): if neighbor is **same type** and **lower saturation**, this position’s cover is updated to **spreadState(level, blockPos, neighborState)** (pull from neighbor); otherwise unchanged.

So when a **block** is placed/broken next to cover, the cover’s faces are updated. When **latex cover** next to it changes, this block’s cover can be updated from the neighbor (e.g. same-type spread).

---

## 4. How the block gets covered in the first place (from a source block)

- **AbstractLatexBlock.tryCover(level, blockPos, type)** is used when something (e.g. **LatexContainerBlock** when it "spills") wants to place cover at a position.
- It:
  - Requires a **SpreadingLatexType**.
  - Requires current cover at that pos to be **air**.
  - Requires block not in DENY_LATEX_COVER and not full collision.
  - Builds **CoveringBlockEvent** with **spreadState(level, pos, type.sourceCoverState())**, runs **defaultCoverBehavior**, then **setBlockAndUpdate** and **LatexCoverState.setAtAndUpdate**.
- So the **first** cover at a position often comes from **tryCover** (e.g. container) or from **randomTick** spreading from an adjacent position that already has cover (including positions that are **LatexCoveringSource** blocks).

- For positions that **are** a white/dark latex **block**: when you read cover there, you don’t read from the palette; you get **sourceCoverState()** from the block. So the "layer" on the block itself is **virtual** — the block is the source. Spreading then goes **from** that block to neighbors: a random tick can run on a position **next to** the latex block (that position might already have been given cover by a previous spread or by tryCover). So in practice, the **source block** doesn’t need stored cover; spreading is from cover that was either placed by tryCover or by a previous spread step, and those positions are the ones that get randomTick and spread further.

- **LevelChunkSectionMixin.getLatexCoverState()**: if the block at (x,y,z) is a **LatexCoveringSource**, it returns **source.getLatexCoverState(blockState, pos)** and **does not** read from `coverStates`. So the chunk’s stored cover at that position is irrelevant for rendering/collision. When **randomTick** runs, it uses **getAt(level, blockPos)** which goes through the chunk section and thus, for a latex block, gets that block’s reported cover. So when the game picks a random position and it lands on a **latex block**, the cover there is the block’s source state; **SpreadingLatexType.randomTick** then runs and can spread **from** that position to a neighbor (the neighbor gets new or updated cover in the palette). So the **source block** does drive spreading by being the "current state" when that position is randomly ticked.

---

## 5. Summary flow

| Concept | Where | What happens |
|--------|--------|---------------|
| **Cover storage** | `LevelChunkSection` (mixin) | `PalettedContainer<LatexCoverState>` per section; serialized with chunk. |
| **Source of cover** | Blocks implementing `LatexCoveringSource` | White/Dark latex **block** and **grounded** latex **fluid** return `type.sourceCoverState()` for their position (no palette read there). |
| **Random tick** | `ServerLevelMixin.tickChunk` | For random positions, get cover; if `coverState.isRandomlyTicking()`, call `coverState.randomTick()`. |
| **Spreading** | `SpreadingLatexType.randomTick` | Pick random neighbor; if it can be covered, compute `spreadState(level, neighborPos, currentState)`, apply defaultCoverBehavior, then set block + `LatexCoverState.setAtAndUpdate`. |
| **spreadState** | `SpreadingLatexType.spreadState` | state + 1 saturation; set each face from `canExistOnSurface(neighbor)`. |
| **Neighbor updates** | `LatexCoverState.setAt` → markAndNotifyAt | updateNeighbourShapes / updateIndirectNeighbourShapes → executeShapeUpdate → updateShape (block or cover neighbor). |
| **Decay** | `SpreadingLatexType.randomTick` | If shouldDecay (e.g. no support, or no same-type neighbor with lower saturation), set to NONE. |

So: **latex blocks and grounded latex fluids** are the **producers** of the layer (they report their cover and are the source when that position is ticked). The layer **spreads** via **random ticks** on cover (and neighbor updates) using **SpreadingLatexType**: saturation increases as it spreads, faces are set from sturdy neighbors, and it can replace blocks (e.g. grass → dead bush) and decay when unsupported.
