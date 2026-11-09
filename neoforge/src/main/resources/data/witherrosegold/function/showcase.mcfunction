# Gives every Wither Rose Gold item and builds an armor stand wearing the full set.
# Run in a creative test world: /function witherrosegold:showcase
give @s witherrosegold:wither_rose_gold_ingot
give @s witherrosegold:wither_rose_smithing_template
give @s witherrosegold:piezoshard 16
give @s witherrosegold:wither_rose_golden_sword
give @s witherrosegold:wither_rose_golden_pickaxe
give @s witherrosegold:wither_rose_golden_axe
give @s witherrosegold:wither_rose_golden_shovel
give @s witherrosegold:wither_rose_golden_hoe
give @s witherrosegold:wither_rose_golden_helmet
give @s witherrosegold:wither_rose_golden_chestplate
give @s witherrosegold:wither_rose_golden_leggings
give @s witherrosegold:wither_rose_golden_boots
summon minecraft:armor_stand ~2 ~ ~ {ShowArms:1b,equipment:{head:{id:"witherrosegold:wither_rose_golden_helmet",count:1},chest:{id:"witherrosegold:wither_rose_golden_chestplate",count:1},legs:{id:"witherrosegold:wither_rose_golden_leggings",count:1},feet:{id:"witherrosegold:wither_rose_golden_boots",count:1},mainhand:{id:"witherrosegold:wither_rose_golden_pickaxe",count:1}}}
give @s rosegoldequipment:rose_gold_ingot 4
give @s minecraft:nether_star
