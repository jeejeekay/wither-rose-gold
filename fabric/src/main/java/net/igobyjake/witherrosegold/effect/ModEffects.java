package net.igobyjake.witherrosegold.effect;

import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    // Shown while wearing the full set (icon: textures/mob_effect/wither_rosed.png).
    public static final Holder<MobEffect> WITHER_ROSED =
            Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, FullSetEffects.WITHER_ROSED, new WitherRosedEffect());

    public static void registerModEffects() {
        witherrosegold.LOGGER.info("Registering effects for " + witherrosegold.MOD_ID);
    }
}
