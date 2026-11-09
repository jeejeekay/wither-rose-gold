package net.igobyjake.witherrosegold;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.igobyjake.witherrosegold.effect.FullSetEffects;
import net.igobyjake.witherrosegold.entity.ModEntities;
import net.igobyjake.witherrosegold.item.ModItemGroups;
import net.igobyjake.witherrosegold.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Wither Rose Gold: a sister mod of Rose Gold Equipment (which must be installed too).
public class witherrosegold implements ModInitializer {
    public static final String MOD_ID = "witherrosegold";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final ResourceKey<LootTable> WITHER_LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/wither"));

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModEntities.registerModEntities();
        ModItemGroups.registerItemGroups();

        // Zap thorns (shared code in effect/FullSetEffects). Blast protection is in mixin/LivingEntityMixin,
        // because Fabric API has no event that can change the amount of damage.
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damageTaken, blocked) -> {
            if (!blocked) FullSetEffects.afterHurt(entity, source);
        });

        // The wither also drops a smithing template. Adds a pool to its loot table as it loads instead of
        // replacing the file, so other mods' changes to the wither's drops still work.
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(WITHER_LOOT)) {
                tableBuilder.withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.WITHER_ROSE_SMITHING_TEMPLATE)));
            }
        });
    }
}
