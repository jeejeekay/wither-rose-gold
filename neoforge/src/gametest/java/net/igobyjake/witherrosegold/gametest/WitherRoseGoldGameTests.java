package net.igobyjake.witherrosegold.gametest;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registers each check in WitherRoseGoldChecks as a GameTest function. Each one also has a
 * data/witherrosegold/test_instance/<name>.json saying how to run it.
 * Run them all with: gradlew runGameTestServer
 */
@EventBusSubscriber(modid = "witherrosegold")
public final class WitherRoseGoldGameTests {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, helper -> {
            helper.register(id("recipes"), WitherRoseGoldChecks::recipes);
            helper.register(id("items"), WitherRoseGoldChecks::items);
            helper.register(id("armor"), WitherRoseGoldChecks::armor);
            helper.register(id("tools"), WitherRoseGoldChecks::tools);
            helper.register(id("blast_mining"), WitherRoseGoldChecks::blastMining);
            helper.register(id("blast_protection"), WitherRoseGoldChecks::blastProtection);
            helper.register(id("zap"), WitherRoseGoldChecks::zap);
            helper.register(id("set_bonus"), WitherRoseGoldChecks::setBonus);
            helper.register(id("piezoshard"), WitherRoseGoldChecks::piezoshard);
            helper.register(id("wither_drop"), WitherRoseGoldChecks::witherDrop);
            helper.register(id("showcase_loads"), WitherRoseGoldChecks::showcaseLoads);
        });
    }

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath("witherrosegold", name);
    }
}
