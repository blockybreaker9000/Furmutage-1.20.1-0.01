LATEX TEAMS CONFIGURATION
==========================

This file explains how to configure which mobs belong to which team.

CONFIG FILE LOCATION:
---------------------
data/furmutage/latex_teams.json

HOW TO ADD ENTITIES TO TEAMS:
-----------------------------
Edit latex_teams.json and add entity identifiers to either:
- "white_latex_team" array
- "dark_latex_team" array

SUPPORTED IDENTIFIER FORMATS:
-----------------------------
1. ResourceLocation format (modid:entity_name):
   Example: "changed:pure_white_latex_wolf"
   Example: "furmutage:latex_mutant_family"

2. Full class name:
   Example: "net.ltxprogrammer.changed.entity.beast.WhiteLatex"
   Example: "net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity"

3. Partial class name (matches if entity class name contains this string):
   Example: "WhiteLatex"
   Example: "DarkLatex"

HOW TO FIND ENTITY IDENTIFIERS:
-------------------------------
1. ResourceLocation: Check the entity's registry name in-game or in code
2. Class name: Check the entity's Java class file
3. Partial name: Use a substring that appears in the class name

RELOADING CONFIG:
-----------------
The config is automatically reloaded when you:
- Restart the server
- Use the /reload command (if available)

TEAM BEHAVIOR:
--------------
- White Latex and Dark Latex are enemies (will attack each other)
- Both teams are enemies of players
- Entities on the same team will NOT attack each other

