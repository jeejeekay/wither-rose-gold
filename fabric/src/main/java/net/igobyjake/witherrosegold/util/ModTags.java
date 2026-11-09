package net.igobyjake.witherrosegold.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

// Vanilla-only (same file on Fabric and NeoForge).
public class ModTags {
    private static final String MOD_ID = "witherrosegold";

    public static class Items {
        /** What repairs wither rose gold gear in an anvil (the ingot). */
        public static final TagKey<Item> WITHER_ROSE_GOLD_REPAIRABLE = TagKey.create(Registries.ITEM, id("wither_rose_gold_repairable"));
        /** The four armor pieces; wearing all of them turns on the full-set effects. */
        public static final TagKey<Item> WITHER_ROSE_GOLD_ARMOR = TagKey.create(Registries.ITEM, id("wither_rose_gold_armor"));
    }

    public static class DamageTypes {
        /** Fire + explosions: what the smithing template can't be destroyed by. */
        public static final TagKey<DamageType> WITHER_ROSE_PROOF = TagKey.create(Registries.DAMAGE_TYPE, id("wither_rose_proof"));
        /** data/witherrosegold/damage_type/zap.json - the full set's thorns-style shock. */
        public static final ResourceKey<DamageType> ZAP = ResourceKey.create(Registries.DAMAGE_TYPE, id("zap"));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
