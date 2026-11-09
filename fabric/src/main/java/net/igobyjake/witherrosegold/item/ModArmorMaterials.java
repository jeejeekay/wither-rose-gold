package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.witherrosegold;
import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.EnumMap;

public class ModArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    // -> assets/witherrosegold/equipment/wither_rose_gold.json (what the worn armor looks like)
    public static final ResourceKey<EquipmentAsset> WITHER_ROSE_GOLD = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(witherrosegold.MOD_ID, "wither_rose_gold"));

    // Netherite's numbers (37 durability multiplier, 3/6/8/3 armor, 3 toughness, enchantability 15)
    // except knockback resistance, which is 0 - the full set gives blast protection + zap instead.
    public static final ArmorMaterial WITHER_ROSE_GOLD_ARMOR_MATERIAL = new ArmorMaterial(37,
            Util.make(new EnumMap<>(ArmorType.class), attribute -> {
                attribute.put(ArmorType.BOOTS, 3);
                attribute.put(ArmorType.LEGGINGS, 6);
                attribute.put(ArmorType.CHESTPLATE, 8);
                attribute.put(ArmorType.HELMET, 3);
                attribute.put(ArmorType.BODY, 19);
            }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F, 0.0F, ModTags.Items.WITHER_ROSE_GOLD_REPAIRABLE, WITHER_ROSE_GOLD);
}
