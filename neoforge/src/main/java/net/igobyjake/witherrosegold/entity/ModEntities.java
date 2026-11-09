package net.igobyjake.witherrosegold.entity;

import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(witherrosegold.MOD_ID);

    // Same size/tracking as a snowball.
    public static final DeferredHolder<EntityType<?>, EntityType<PiezoshardEntity>> PIEZOSHARD = ENTITY_TYPES.registerEntityType(
            "piezoshard", PiezoshardEntity::new, MobCategory.MISC,
            builder -> builder.noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
