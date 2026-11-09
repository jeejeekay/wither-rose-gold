# Wither Rose Gold

A sister mod to [Rose Gold Equipment](https://github.com/jeejeekay/rose-gold-equipment) (RGE).
**RGE must be installed too.** Wither Rose Gold upgrades RGE's rose gold tools and armor in the
smithing table, the way netherite upgrades diamond.

Mod id `witherrosegold`, package `net.igobyjake.witherrosegold`. Textures are placeholders copied from
RGE for now (the template uses vanilla's netherite upgrade template picture, the piezoshard uses the echo shard).

## What's in it

| Item | How you get it | What it does |
|---|---|---|
| Wither Rose Smithing Template | Every wither drops one (on top of its nether star; added to the drops, vanilla's loot table isn't replaced). Copy it: template top-middle, crying obsidian below it, 7 amethyst shards around (makes 2) | Can't burn or be blown up, like a nether star |
| Piezoshard | 4 amethyst shards + 4 quartz, any shape | Right-click to throw; explodes on impact (blast size 3, TNT is 4; at most 20 damage at the centre). Stacks to 16, 1 s cooldown. Also repairs wither rose gold gear |
| Wither Rose Gold Ingot | Nether star + 4 piezoshards + 4 rose gold ingots, any shape (makes 4) | Fireproof, like netherite |
| Wither rose gold sword / pickaxe / shovel / axe / hoe | Smithing table: template + rose gold tool + ingot | Netherite mining level and damage, but mining speed 11 and 2520 durability; fireproof. **Right-click a block = 3x3 blast** |
| Wither rose gold helmet / chestplate / leggings / boots | Smithing table: template + rose gold armor + ingot | Netherite armor and toughness, fireproof, **no** knockback resistance. **Full set = blast protection + zap** |

**3x3 blast** (right-click a block with any wither rose gold tool): breaks the 3x3 square flat against the
face you clicked. Blocks drop as if mined with the tool (Fortune / Silk Touch apply), with one small puff of
particles and one quiet sound. 1 durability per block, half-second cooldown. It won't touch obsidian-tough
blocks, chests/furnaces/other block entities, liquids or bedrock, or blocks the tool couldn't harvest.
**Sneak + right-click** does the tool's normal action instead (strip logs, make paths, till).

**Full set** (built in, works alongside enchantments):
- Blast protection: explosions do half damage.
- Zap: when something hits you in melee, 50% chance it gets shocked for exactly 3 damage (ignores armor, and doesn't give the attacker hit immunity, so your own hits still land).
- Players wearing the full set get the **Wither Rosed** effect (shown in the inventory, no particles) as a reminder; the armor tooltip explains the bonus.

## Layout

Same as RGE: one branch per Minecraft version, each with a `neoforge/` and a `fabric/` folder.

| Branch | Minecraft | NeoForge | Fabric API | Java |
|---|---|---|---|---|
| `mc/1.21.11` | 1.21.11 | 21.11.48 | 0.141.6 | 21 |
| `mc/26.1` | 26.1.2 | 26.1.2.125 | 0.155.3 | 25 |
| `mc/26.2` | 26.2 | 26.2.0.66 | 0.161.0 | 25 |
| `mc/26.3` | 26.3 | 26.3.0.100 | 0.165.2 | 25 |

```
neoforge/ or fabric/
  src/main/java/net/igobyjake/witherrosegold/
    witherrosegold.java          mod entry point (+ NeoForge damage events / Fabric AFTER_DAMAGE)
    witherrosegoldclient.java    renders the thrown piezoshard
    item/        ModItems, ModArmorMaterials, ModToolTiers, BlastToolItem, PiezoshardItem, creative tab
    entity/      ModEntities, PiezoshardEntity (the grenade)
    effect/      FullSetEffects   - blast protection + zap      (same file on both loaders)
    util/        BlastMining      - the 3x3 blast                (same file on both loaders)
                 ModTags
    (Fabric) witherrosegold.java also adds the template to the wither's drops with LootTableEvents.MODIFY
    mixin/       (Fabric only) LivingEntityMixin - hooks blast protection; Fabric API has no "change damage" event
  src/main/resources/            recipes, tags, models, lang... plain JSON, same on both loaders
                                 (NeoForge also has data/witherrosegold/loot_modifiers/wither_template.json:
                                 "when the wither's loot is rolled, also roll entities/wither_bonus")
  src/gametest/                  automated in-game tests (docs/TESTING.md)
  libs/                          put the RGE jar here to run the game (not committed)
```

Unlike RGE there is no datagen: the resources are plain hand-written JSON (identical on both loaders),
which made porting much simpler.

## Running it from the source

WRG's code doesn't use RGE's classes (only its item names, in recipes), so it compiles without RGE.
To **run** it (`runClient`, `runServer`, GameTests) the RGE jar for the same loader + Minecraft version has
to be in `<loader>/libs/`. `build-all.ps1` does that for you from RGE's `jars` folder; CI builds RGE from
the same-named branch of the RGE repo.
