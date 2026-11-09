#!/usr/bin/env python3
"""Writes WRG's hand-made resources (shared by both loaders) for one Minecraft branch.
usage: gen_resources.py <rge checkout (on matching branch)> <out resources dir> <version tag>"""
import json, os, re, shutil, sys
rge, out, ver = sys.argv[1], sys.argv[2], sys.argv[3]
NS, RGE = "witherrosegold", "rosegoldequipment"
TOOLS = {"sword": "swords", "pickaxe": "pickaxes", "axe": "axes", "shovel": "shovels", "hoe": "hoes"}
ARMOR = {"helmet": "head_armor", "chestplate": "chest_armor", "leggings": "leg_armor", "boots": "foot_armor"}
rge_res = os.path.join(rge, "neoforge/src/main/resources")
rge_gen = os.path.join(rge, "neoforge/src/generated/resources")

def w(rel, data):
    p = os.path.join(out, rel); os.makedirs(os.path.dirname(p), exist_ok=True)
    with open(p, "w", newline="\n") as f:
        f.write(data if isinstance(data, str) else json.dumps(data, indent=2) + "\n")

def cp(src, rel):
    p = os.path.join(out, rel); os.makedirs(os.path.dirname(p), exist_ok=True); shutil.copyfile(src, p)

def simple_item(name, layer0, parent="minecraft:item/generated"):
    w(f"assets/{NS}/items/{name}.json", {"model": {"type": "minecraft:model", "model": f"{NS}:item/{name}"}})
    w(f"assets/{NS}/models/item/{name}.json", {"parent": parent, "textures": {"layer0": layer0}})

# ---------------------------------------------------------------- textures (placeholders copied from RGE)
tex = os.path.join(rge_res, f"assets/{RGE}/textures")
cp(f"{tex}/item/rose_gold_ingot.png", f"assets/{NS}/textures/item/wither_rose_gold_ingot.png")
for piece in list(TOOLS) + list(ARMOR):
    cp(f"{tex}/item/rose_golden_{piece}.png", f"assets/{NS}/textures/item/wither_rose_golden_{piece}.png")
layers = sorted(os.listdir(f"{tex}/entity/equipment"))
for layer in layers:
    cp(f"{tex}/entity/equipment/{layer}/rose_gold.png", f"assets/{NS}/textures/entity/equipment/{layer}/wither_rose_gold.png")
w(f"assets/{NS}/equipment/wither_rose_gold.json",
  {"layers": {layer: [{"texture": f"{NS}:wither_rose_gold"}] for layer in layers}})

# set-bonus effect icon (18x18 placeholder made from the ingot)
cp(os.path.join(os.path.dirname(os.path.abspath(__file__)), "wither_rosed.png"), f"assets/{NS}/textures/mob_effect/wither_rosed.png")

# ---------------------------------------------------------------- item models
simple_item("wither_rose_gold_ingot", f"{NS}:item/wither_rose_gold_ingot")
simple_item("wither_rose_smithing_template", "minecraft:item/netherite_upgrade_smithing_template")  # placeholder
simple_item("piezoshard", "minecraft:item/echo_shard")                                            # placeholder
for t in TOOLS:
    simple_item(f"wither_rose_golden_{t}", f"{NS}:item/wither_rose_golden_{t}", "minecraft:item/handheld")
# Armor: copy RGE's trim-aware item models (one model per trim material) and rename.
ren = lambda s: s.replace(f"{RGE}:item/rose_golden_", f"{NS}:item/wither_rose_golden_")
for a in ARMOR:
    src = f"{rge_gen}/assets/{RGE}/items/rose_golden_{a}.json"
    w(f"assets/{NS}/items/wither_rose_golden_{a}.json", ren(open(src).read()))
    mdir = f"{rge_gen}/assets/{RGE}/models/item"
    for f in os.listdir(mdir):
        if re.fullmatch(rf"rose_golden_{a}(_.*_trim)?\.json", f):
            w(f"assets/{NS}/models/item/wither_{f}", ren(open(os.path.join(mdir, f)).read()))
# The rose-gold trim on wither armor should be the normal rose gold colour, not RGE's "darker" variant
# (that one exists only so rose gold trim shows up on rose gold armor).
for f in os.listdir(f"{out}/assets/{NS}/models/item"):
    p = f"{out}/assets/{NS}/models/item/{f}"; s = open(p).read()
    if "rose_gold_darker" in s: open(p, "w").write(s.replace("rose_gold_darker", "rose_gold"))
for f in os.listdir(f"{out}/assets/{NS}/items"):
    p = f"{out}/assets/{NS}/items/{f}"; s = open(p).read()
    if "rose_gold_darker" in s: open(p, "w").write(s.replace("rose_gold_darker", "rose_gold"))

# ---------------------------------------------------------------- lang
lang = {
    "itemgroup.witherrosegold.wither_rose_gold_group": "Wither Rose Gold",
    "item.witherrosegold.wither_rose_gold_ingot": "Wither Rose Gold Ingot",
    "item.witherrosegold.wither_rose_smithing_template": "Wither Rose Smithing Template",
    "item.witherrosegold.wither_rose_smithing_template.desc": "Upgrades rose gold equipment to wither rose gold",
    "item.witherrosegold.piezoshard": "Piezoshard",
    "item.witherrosegold.piezoshard.desc": "Unstable crystals that create energy from impact",
    "entity.witherrosegold.piezoshard": "Piezoshard",
    "death.attack.witherrosegold.zap": "%1$s was zapped while trying to hurt %2$s",
    "death.attack.witherrosegold.zap.item": "%1$s was zapped by %3$s while trying to hurt %2$s",
    "effect.witherrosegold.wither_rosed": "Wither Rosed",
    "effect.witherrosegold.wither_rosed.description": "Blast resistant and painful to touch",
    "item.witherrosegold.armor.set_bonus": "Full set: Wither Rosed",
    "item.witherrosegold.armor.set_bonus.desc": "Blast resistant and painful to touch",
}
for t in list(TOOLS) + list(ARMOR):
    lang[f"item.witherrosegold.wither_rose_golden_{t}"] = f"Wither Rose Golden {t.capitalize()}"
w(f"assets/{NS}/lang/en_us.json", lang)

# ---------------------------------------------------------------- recipes
I = lambda n: f"{NS}:{n}"
w(f"data/{NS}/recipe/wither_rose_smithing_template.json", {
    "type": "minecraft:crafting_shaped",
    "key": {"A": "minecraft:amethyst_shard", "C": "minecraft:crying_obsidian", "T": I("wither_rose_smithing_template")},
    "pattern": ["ATA", "ACA", "AAA"],
    "result": {"count": 2, "id": I("wither_rose_smithing_template")}})
w(f"data/{NS}/recipe/piezoshard.json", {
    "type": "minecraft:crafting_shapeless",
    "ingredients": ["minecraft:amethyst_shard"] * 4 + ["minecraft:quartz"] * 4,
    "result": {"count": 1, "id": I("piezoshard")}})
w(f"data/{NS}/recipe/wither_rose_gold_ingot.json", {
    "type": "minecraft:crafting_shapeless",
    "ingredients": ["minecraft:nether_star"] + [I("piezoshard")] * 4 + [f"{RGE}:rose_gold_ingot"] * 4,
    "result": {"count": 4, "id": I("wither_rose_gold_ingot")}})
for t in list(TOOLS) + list(ARMOR):
    w(f"data/{NS}/recipe/wither_rose_golden_{t}_smithing.json", {
        "type": "minecraft:smithing_transform",
        "addition": I("wither_rose_gold_ingot"),
        "base": f"{RGE}:rose_golden_{t}",
        "result": {"id": I(f"wither_rose_golden_{t}")},
        "template": I("wither_rose_smithing_template")})

# The wither's template drop is added by each loader (NeoForge: loot modifier, Fabric: LootTableEvents),
# not here, so vanilla's wither loot table is never replaced.

# ---------------------------------------------------------------- damage type + tags
w(f"data/{NS}/damage_type/zap.json", {"exhaustion": 0.1, "message_id": f"{NS}.zap", "scaling": "when_caused_by_living_non_player"})
w(f"data/{NS}/tags/damage_type/wither_rose_proof.json", {"values": ["#minecraft:is_fire", "#minecraft:is_explosion"]})
w("data/minecraft/tags/damage_type/avoids_guardian_thorns.json", {"values": [I("zap")]})
w("data/minecraft/tags/damage_type/bypasses_armor.json", {"values": [I("zap")]})   # always exactly 3
w(f"data/{NS}/tags/item/wither_rose_gold_repairable.json", {"values": [I("wither_rose_gold_ingot"), I("piezoshard")]})
w(f"data/{NS}/tags/item/wither_rose_gold_armor.json", {"values": [I(f"wither_rose_golden_{a}") for a in ARMOR]})
for t, tag in {**TOOLS, **ARMOR}.items():
    w(f"data/minecraft/tags/item/{tag}.json", {"values": [I(f"wither_rose_golden_{t}")]})

# ---------------------------------------------------------------- /function witherrosegold:showcase
lines = ["# Gives every Wither Rose Gold item and builds an armor stand wearing the full set.",
         "# Run in a creative test world: /function witherrosegold:showcase"]
for n in ["wither_rose_gold_ingot", "wither_rose_smithing_template", "piezoshard"] + [f"wither_rose_golden_{t}" for t in list(TOOLS) + list(ARMOR)]:
    lines.append(f"give @s {NS}:{n}" + (" 16" if n == "piezoshard" else ""))
slots = {"helmet": "head", "chestplate": "chest", "leggings": "legs", "boots": "feet"}
eq = ",".join(f'{s}:{{id:"{NS}:wither_rose_golden_{a}",count:1}}' for a, s in slots.items())
lines.append(f"summon minecraft:armor_stand ~2 ~ ~ {{ShowArms:1b,equipment:{{{eq},mainhand:{{id:\"{NS}:wither_rose_golden_pickaxe\",count:1}}}}}}")
lines.append(f"give @s {RGE}:rose_gold_ingot 4")
lines.append("give @s minecraft:nether_star")
w(f"data/{NS}/function/showcase.mcfunction", "\n".join(lines) + "\n")
print("ok", ver, sum(len(f) for _, _, f in os.walk(out)), "files")
