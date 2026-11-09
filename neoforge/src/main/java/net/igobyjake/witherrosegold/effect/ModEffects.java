package net.igobyjake.witherrosegold.effect;

import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, witherrosegold.MOD_ID);

    // Shown while wearing the full set (icon: textures/mob_effect/wither_rosed.png).
    public static final DeferredHolder<MobEffect, MobEffect> WITHER_ROSED = MOB_EFFECTS.register("wither_rosed", WitherRosedEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
