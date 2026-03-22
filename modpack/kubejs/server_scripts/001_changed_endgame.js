// Furmutage modpack — Changed endgame crafts (KubeJS)
// Copy into your .minecraft/kubejs/server_scripts/
// Requires: changed, create, immersiveengineering, furmutage (for Furmutage items)

ServerEvents.recipes(event => {
  // Remove default Changed recipes (by output so pack IDs still match)
  event.remove({ output: 'changed:stasis_chamber' })
  event.remove({ output: 'changed:exoskeleton' })
  event.remove({ output: 'changed:exoskeleton_charger' })

  // --- Stasis chamber — endgame (IE structure + Create precision + Furmutage + nether star)
  event.shaped('changed:stasis_chamber', ['HGH', 'MTC', 'HGH'], {
    H: 'immersiveengineering:heavy_engineering',
    G: 'furmutage:thunderium_block',
    M: 'furmutage:tsc_advanced_tech',
    T: 'minecraft:nether_star',
    C: 'furmutage:tsc_gears'
  })

  // --- Exoskeleton — endgame
  event.shaped('changed:exoskeleton', ['PSP', 'LEL', 'BNB'], {
    P: 'immersiveengineering:plate_steel',
    S: 'create:precision_mechanism',
    L: 'changed:laser_emitter',
    E: 'changed:computer',
    B: 'immersiveengineering:heavy_engineering',
    N: 'minecraft:netherite_block'
  })

  // --- Exoskeleton charger — endgame
  event.shaped('changed:exoskeleton_charger', ['ETE', 'BAB', 'PCP'], {
    E: 'create:electron_tube',
    T: 'immersiveengineering:plate_steel',
    B: 'immersiveengineering:heavy_engineering',
    A: 'minecraft:armor_stand',
    P: 'immersiveengineering:plate_aluminum',
    C: 'immersiveengineering:capacitor_hv'
  })
})
