package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class ModToolTiers {
    // Netherite mining level and damage (+4), but faster (11 vs 9) and tougher (2520 vs 2031 uses). Enchantability 15.
    public static final ToolMaterial WITHER_ROSE_GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2520, 11.0F, 4.0F, 15, ModTags.Items.WITHER_ROSE_GOLD_REPAIRABLE);
}
