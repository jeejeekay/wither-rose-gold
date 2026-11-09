package net.igobyjake.witherrosegold.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * "Wither Rosed": only a label. It shows the full-set bonus in the inventory; the actual
 * blast protection and zap live in FullSetEffects and depend on the armor, not on this effect.
 */
public class WitherRosedEffect extends MobEffect {
    public WitherRosedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xB04A5A);
    }
}
