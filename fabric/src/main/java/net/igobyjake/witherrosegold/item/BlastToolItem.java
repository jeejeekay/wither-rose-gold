package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.util.BlastMining;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;

/**
 * Wither rose gold tools: right-clicking a block blasts a 3x3 (util/BlastMining).
 * Sneak + right-click (or clicking a block the blast can't break) does the tool's normal
 * right-click instead, so axes still strip logs, shovels make paths and hoes till.
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

    public static class Axe extends AxeItem {
        public Axe(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
            super(material, attackDamage, attackSpeed, properties);
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            InteractionResult blast = BlastMining.tryBlast(context);
            return blast != null ? blast : super.useOn(context);
        }
    }

    public static class Shovel extends ShovelItem {
        public Shovel(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
            super(material, attackDamage, attackSpeed, properties);
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            InteractionResult blast = BlastMining.tryBlast(context);
            return blast != null ? blast : super.useOn(context);
        }
    }

    public static class Hoe extends HoeItem {
        public Hoe(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
            super(material, attackDamage, attackSpeed, properties);
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            InteractionResult blast = BlastMining.tryBlast(context);
            return blast != null ? blast : super.useOn(context);
        }
    }
}
