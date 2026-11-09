package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.witherrosegold;
import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(witherrosegold.MOD_ID);

    // ---- Materials ----
    // fireResistant(): floats in lava and can't burn, like netherite.
    public static final DeferredItem<Item> WITHER_ROSE_GOLD_INGOT = ITEMS.registerItem("wither_rose_gold_ingot",
            properties -> new Item(properties.fireResistant()));
    // Dropped by the wither. Survives fire AND explosions (#witherrosegold:wither_rose_proof), so the wither's
    // own blasts can't destroy it.
    public static final DeferredItem<Item> WITHER_ROSE_SMITHING_TEMPLATE = ITEMS.registerItem("wither_rose_smithing_template",
            properties -> new Item(properties.rarity(Rarity.RARE)
                    .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(ModTags.DamageTypes.WITHER_ROSE_PROOF))
                    .component(DataComponents.LORE, lore("item.witherrosegold.wither_rose_smithing_template.desc"))));
    // A throwable grenade (see PiezoshardItem / PiezoshardEntity).
    public static final DeferredItem<PiezoshardItem> PIEZOSHARD = ITEMS.registerItem("piezoshard",
            properties -> new PiezoshardItem(properties.stacksTo(16)
                    .component(DataComponents.LORE, lore("item.witherrosegold.piezoshard.desc"))));

    // ---- Tools ---- (netherite stats; right-click a block = 3x3 blast, sneak + right-click = the normal action)
    public static final DeferredItem<BlastToolItem> WITHER_ROSE_GOLDEN_SWORD = ITEMS.registerItem("wither_rose_golden_sword",
            properties -> new BlastToolItem(properties.sword(ModToolTiers.WITHER_ROSE_GOLD, 3.0F, -2.4F).fireResistant()));
    public static final DeferredItem<BlastToolItem> WITHER_ROSE_GOLDEN_PICKAXE = ITEMS.registerItem("wither_rose_golden_pickaxe",
            properties -> new BlastToolItem(properties.pickaxe(ModToolTiers.WITHER_ROSE_GOLD, 1.0F, -2.8F).fireResistant()));
    public static final DeferredItem<BlastToolItem.Shovel> WITHER_ROSE_GOLDEN_SHOVEL = ITEMS.registerItem("wither_rose_golden_shovel",
            properties -> new BlastToolItem.Shovel(ModToolTiers.WITHER_ROSE_GOLD, 1.5F, -3.0F, properties.fireResistant()));
    public static final DeferredItem<BlastToolItem.Axe> WITHER_ROSE_GOLDEN_AXE = ITEMS.registerItem("wither_rose_golden_axe",
            properties -> new BlastToolItem.Axe(ModToolTiers.WITHER_ROSE_GOLD, 5.0F, -3.0F, properties.fireResistant()));
    public static final DeferredItem<BlastToolItem.Hoe> WITHER_ROSE_GOLDEN_HOE = ITEMS.registerItem("wither_rose_golden_hoe",
            properties -> new BlastToolItem.Hoe(ModToolTiers.WITHER_ROSE_GOLD, -4.0F, 0.0F, properties.fireResistant()));

    // Armor tooltip explaining the full-set bonus.
    private static final ItemLore SET_BONUS = new ItemLore(List.of(
            Component.translatable("item.witherrosegold.armor.set_bonus").withStyle(style -> style.withColor(ChatFormatting.LIGHT_PURPLE).withItalic(false)),
            Component.translatable("item.witherrosegold.armor.set_bonus.desc").withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));

    // ---- Armor ---- (netherite armor + toughness, no knockback resistance; full set = blast protection + zap)
    public static final DeferredItem<Item> WITHER_ROSE_GOLDEN_HELMET = ITEMS.registerItem("wither_rose_golden_helmet",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WITHER_ROSE_GOLD_ARMOR_MATERIAL, ArmorType.HELMET).fireResistant()
                    .component(DataComponents.LORE, SET_BONUS)));
    public static final DeferredItem<Item> WITHER_ROSE_GOLDEN_CHESTPLATE = ITEMS.registerItem("wither_rose_golden_chestplate",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WITHER_ROSE_GOLD_ARMOR_MATERIAL, ArmorType.CHESTPLATE).fireResistant()
                    .component(DataComponents.LORE, SET_BONUS)));
    public static final DeferredItem<Item> WITHER_ROSE_GOLDEN_LEGGINGS = ITEMS.registerItem("wither_rose_golden_leggings",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WITHER_ROSE_GOLD_ARMOR_MATERIAL, ArmorType.LEGGINGS).fireResistant()
                    .component(DataComponents.LORE, SET_BONUS)));
    public static final DeferredItem<Item> WITHER_ROSE_GOLDEN_BOOTS = ITEMS.registerItem("wither_rose_golden_boots",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.WITHER_ROSE_GOLD_ARMOR_MATERIAL, ArmorType.BOOTS).fireResistant()
                    .component(DataComponents.LORE, SET_BONUS)));

    // A grey, non-italic tooltip line.
    private static ItemLore lore(String key) {
        return new ItemLore(List.of(Component.translatable(key).withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
