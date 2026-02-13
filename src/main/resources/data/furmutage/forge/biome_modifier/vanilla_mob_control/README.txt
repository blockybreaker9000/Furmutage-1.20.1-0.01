Vanilla Mob Spawn Control (Data-Driven)
======================================

These JSON files use Forge's "remove_spawns" biome modifier to prevent vanilla
mobs from spawning. This replaces the old spawn-then-discard approach, which
was inefficient and caused performance issues.

HOW TO ADJUST SPAWNING:
-----------------------
- To DISABLE a mob: Keep the corresponding no_<mob>.json file (mob won't spawn)
- To ENABLE a mob: DELETE the no_<mob>.json file for that mob
- To REDUCE spawn rate: You would need to override biome JSONs in 
  data/minecraft/worldgen/biome/ - these files only support on/off

FILES:
------
no_zombie.json          - Overworld zombies
no_husk.json            - Desert husks
no_skeleton.json        - Overworld skeletons
no_stray.json           - Snowy strays
no_creeper.json         - Creepers
no_spider.json          - Spiders
no_pillager.json        - Pillagers (patrols, outposts)
no_enderman.json        - Endermen
no_witch.json           - Witches
no_drowned.json         - Drowned (rivers, oceans)
no_vindicator.json      - Vindicators (woodland mansions)
no_evoker.json          - Evokers (woodland mansions)
no_ravager.json         - Ravagers (raids)
no_zombified_piglin.json - Nether zombified piglins
no_piglin.json          - Nether piglins and piglin brutes
no_guardian.json        - Ocean guardians
no_elder_guardian.json  - Elder guardians (ocean monuments)
no_piglin_brute.json    - Nether piglin brutes (piglin is in no_piglin.json)

NOTE: Deleting a file requires rebuilding the mod. For quick testing without
rebuild, you can override these in a datapack with type "forge:none" to disable
the removal.
