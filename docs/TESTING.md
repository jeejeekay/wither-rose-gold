# Testing Wither Rose Gold

## Automated (GameTests)

`src/gametest` runs real game checks on a headless server, with RGE loaded:

- NeoForge: `gradlew runGameTestServer`
- Fabric: `gradlew runGameTest` (also runs as part of `gradlew build`)

CI runs them on every push for every branch and both loaders (GitHub -> Actions). Last line of the log
should say *All N required tests passed*.

| Test | Checks |
|---|---|
| recipes | template copy, piezoshard, ingot, and all 9 smithing upgrades give the right item |
| items | template can't burn/explode; ingot, tools, armor are fireproof (but not explosion proof); piezoshard has its description and stacks to 16 |
| armor | full set = 20 armor, 12 toughness, 0 knockback resistance; trimmable; repairs with the ingot |
| tools | 2031 durability, harvests ancient debris, repairs with the ingot |
| blast_mining | pickaxe on a 3x3 stone wall: 8 blocks gone, obsidian corner stays, block behind untouched, 8 cobblestone, 8 durability, cooldown. Sneak + axe on a log strips it |
| blast_protection | the same explosion hurts a zombie in WRG about half as much as one in netherite |
| zap | hitting a zombie in the full set zaps the attacker |
| piezoshard | throwing uses one and spawns the grenade; a grenade landing on stone blows it up |
| wither_drop | a killed wither drops the template and its nether star |
| showcase_loads | `/function witherrosegold:showcase` loads |

The block/mob tests run inside `witherrosegold:arena`, an empty 13x9x13 structure
(`src/gametest/resources/data/witherrosegold/structure/arena.nbt`).

## By hand (in game)

Creative world with RGE + WRG: `/function witherrosegold:showcase` gives every item, 4 rose gold ingots,
a nether star, and builds an armor stand in the full set.

- [ ] Look at every item in the inventory and the creative tab "Wither Rose Gold"; worn armor looks right (also on a baby zombie)
- [ ] Throw a piezoshard: flies like a snowball, explodes on impact (smaller than TNT)
- [ ] Throw the template and ingot into lava: the template and ingot survive; template survives TNT
- [ ] Smithing table: template + rose gold pickaxe + ingot -> wither rose gold pickaxe (enchantments kept)
- [ ] Trim the armor (e.g. rose gold trim): inventory icon and worn armor show the trim
- [ ] Right-click stone with the pickaxe: 3x3 hole, items drop, one quiet boom; sneak + right-click with the axe strips a log
- [ ] Full set: stand near TNT (takes less damage); let a zombie hit you (it sometimes gets zapped, sparks)
- [ ] Kill a wither: template + nether star drop
