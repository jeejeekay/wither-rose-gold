package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class ModToolTiers {
    // Same as netherite: mines everything netherite can, 2031 uses, speed 9, +4 damage, enchantability 15.
    public static final ToolMaterial WITHER_ROSE_GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2031, 9.0F, 4.0F, 15, ModTags.Items.WITHER_ROSE_GOLD_REPAIRABLE);
}
