package net.igobyjake.witherrosegold;

import net.igobyjake.witherrosegold.entity.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

// Client only: a thrown piezoshard is drawn as its item (like a snowball).
@EventBusSubscriber(modid = witherrosegold.MOD_ID, value = Dist.CLIENT)
public class witherrosegoldclient {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PIEZOSHARD.get(), ThrownItemRenderer::new);
    }
}
