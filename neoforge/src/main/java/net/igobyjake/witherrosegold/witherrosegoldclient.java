package net.igobyjake.witherrosegold;

import net.igobyjake.witherrosegold.entity.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

// Client only: a thrown piezoshard is drawn as its item (like a snowball), and the "Config" button in
// the Mods list opens NeoForge's built-in settings screen (config/ModConfigs + lang witherrosegold.configuration.*).
@Mod(value = witherrosegold.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = witherrosegold.MOD_ID, value = Dist.CLIENT)
public class witherrosegoldclient {
    public witherrosegoldclient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PIEZOSHARD.get(), ThrownItemRenderer::new);
    }
}
