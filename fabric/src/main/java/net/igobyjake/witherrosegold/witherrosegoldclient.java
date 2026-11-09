package net.igobyjake.witherrosegold;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.igobyjake.witherrosegold.entity.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

// Client only: a thrown piezoshard is drawn as its item (like a snowball).
public class witherrosegoldclient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.PIEZOSHARD, ThrownItemRenderer::new);
    }
}
