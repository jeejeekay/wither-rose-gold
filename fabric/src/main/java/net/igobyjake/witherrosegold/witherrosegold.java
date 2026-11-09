package net.igobyjake.witherrosegold;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.igobyjake.witherrosegold.effect.FullSetEffects;
import net.igobyjake.witherrosegold.entity.ModEntities;
import net.igobyjake.witherrosegold.item.ModItemGroups;
import net.igobyjake.witherrosegold.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Wither Rose Gold: a sister mod of Rose Gold Equipment (which must be installed too).
public class witherrosegold implements ModInitializer {
    public static final String MOD_ID = "witherrosegold";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
    }
}
