package net.igobyjake.witherrosegold.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.igobyjake.witherrosegold.effect.FullSetEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Full-set blast protection. Changes the damage amount as it enters LivingEntity#hurtServer,
 * before armor is applied - the same point NeoForge's LivingIncomingDamageEvent uses.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float witherrosegold$blastProtection(float damage, @Local(argsOnly = true) DamageSource source) {
        return FullSetEffects.modifyIncomingDamage((LivingEntity) (Object) this, source, damage);
    }
}
