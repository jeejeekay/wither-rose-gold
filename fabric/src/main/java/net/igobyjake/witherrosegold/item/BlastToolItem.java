package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.util.BlastMining;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * Wither rose gold tools: right-clicking a block blasts a 3x3 (util/BlastMining).
 * Sneak + right-click (or clicking a block the blast can't break) does the tool's normal
 * right-click instead, so axes still strip logs, shovels make paths and hoes till
 * (since 26.3 those come from the item's block_transformer component, which Item#useOn runs).
 */
public class BlastToolItem extends Item {
    public BlastToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult blast = BlastMining.tryBlast(context);
        return blast != null ? blast : super.useOn(context);
    }
}
