#!/usr/bin/env python3
"""NeoForge only: add the template to the wither's drops with a global loot modifier.
usage: neo_wither_loot.py <neoforge/src/main/resources> <version>"""
import json, os, sys
res, ver = sys.argv[1], sys.argv[2]
def w(rel, data):
    p = os.path.join(res, rel); os.makedirs(os.path.dirname(p), exist_ok=True)
    open(p, "w", newline="\n").write(json.dumps(data, indent=2) + "\n")
# The extra drops, as a normal loot table.
w("data/witherrosegold/loot_table/entities/wither_bonus.json", {
    "type": "minecraft:entity",
    "pools": [{"rolls": 1, "entries": [{"type": "minecraft:item", "name": "witherrosegold:wither_rose_smithing_template"}]}]})
# "When minecraft:entities/wither is rolled, also roll witherrosegold:entities/wither_bonus."
target = {"loot_table_id": "minecraft:entities/wither"}
if ver == "mc/26.3":   # 26.3: one "condition" object, keyed by "type"
    mod = {"type": "neoforge:add_table", "condition": {"type": "neoforge:loot_table_id", **target}}
else:
    mod = {"type": "neoforge:add_table", "conditions": [{"condition": "neoforge:loot_table_id", **target}]}
mod["table"] = "witherrosegold:entities/wither_bonus"
w("data/witherrosegold/loot_modifiers/wither_template.json", mod)
if ver == "mc/1.21.11":   # 1.21.11 only loads modifiers listed here (26.1+ loads the whole folder)
    w("data/neoforge/loot_modifiers/global_loot_modifiers.json",
      {"replace": False, "entries": ["witherrosegold:wither_template"]})
