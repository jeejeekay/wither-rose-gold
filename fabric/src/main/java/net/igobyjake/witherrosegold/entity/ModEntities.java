package net.igobyjake.witherrosegold.entity;

import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    private static final ResourceKey<EntityType<?>> PIEZOSHARD_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(witherrosegold.MOD_ID, "piezoshard"));

    // Same size/tracking as a snowball.
    public static final EntityType<PiezoshardEntity> PIEZOSHARD = Registry.register(BuiltInRegistries.ENTITY_TYPE, PIEZOSHARD_KEY,
            EntityType.Builder.<PiezoshardEntity>of(PiezoshardEntity::new, MobCategory.MISC)
                    .noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(PIEZOSHARD_KEY));

    public static void registerModEntities() {
        witherrosegold.LOGGER.info("Registering entities for " + witherrosegold.MOD_ID);
    }
}
