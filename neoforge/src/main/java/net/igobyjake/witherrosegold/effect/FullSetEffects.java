package net.igobyjake.witherrosegold.effect;

import net.igobyjake.witherrosegold.config.WrgConfig;
import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * What a full set of wither rose gold armor does. Built in, so it works on top of (and
 * independently from) any enchantments:
 *  - Blast protection: explosions do half damage.
 *  - Zap: whoever hits you in melee has a 50% chance to take 3 damage (ignores armor, and doesn't
 *    give them the usual half-second of hit immunity, so your own attacks still land).
 *  - While a player wears it, they have the "Wither Rosed" effect so the bonus shows in the inventory.
 *
 * Vanilla-only (same file on Fabric and NeoForge); the loaders just call these methods.
 */
public final class FullSetEffects {
    // Amounts (zap chance/damage, blast protection) are settings: see config/WrgConfig.
    /** How often (in ticks) players are checked for the set-bonus effect. */
    public static final int SET_BONUS_CHECK_TICKS = 10;
    public static final Identifier WITHER_ROSED = Identifier.fromNamespaceAndPath("witherrosegold", "wither_rosed");

    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private FullSetEffects() {}

    public static boolean hasFullSet(LivingEntity entity) {
        for (EquipmentSlot slot : SLOTS) {
            if (!entity.getItemBySlot(slot).is(ModTags.Items.WITHER_ROSE_GOLD_ARMOR)) return false;
        }
        return true;
    }

    /** Called before damage is applied; returns the (possibly reduced) amount. */
    public static float modifyIncomingDamage(LivingEntity victim, DamageSource source, float amount) {
        if (amount > 0 && source.is(DamageTypeTags.IS_EXPLOSION) && hasFullSet(victim)) {
            return amount * (float) (1.0 - WrgConfig.blastProtection);
        }
        return amount;
    }

    /** Called after the victim actually took a hit. */
    public static void afterHurt(LivingEntity victim, DamageSource source) {
        if (!(victim.level() instanceof ServerLevel level)) return;
        // Only direct melee hits from a living attacker; never react to thorns/zaps (no ping-pong between two sets).
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity target) || attacker != source.getDirectEntity() || attacker == victim) return;
        if (source.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) return;
        if (!hasFullSet(victim) || victim.getRandom().nextFloat() >= WrgConfig.zapChance) return;

        DamageSource zap = new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ModTags.DamageTypes.ZAP), victim);
        target.hurtServer(level, zap, (float) WrgConfig.zapDamage);
        clearHitCooldown(target);   // the zap doesn't make the attacker immune to the next real hit

        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY(0.5), target.getZ(),
                14, target.getBbWidth() * 0.4, target.getBbHeight() * 0.3, target.getBbWidth() * 0.4, 0.15);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT,
                SoundSource.PLAYERS, 0.3F, 1.9F);
    }

    // The field holding the "recently hurt" cooldown was renamed in 26.3.
    private static void clearHitCooldown(LivingEntity entity) {
        entity.damageCooldownTime = 0;
    }

    /** Gives the "Wither Rosed" effect while the full set is worn, and takes it away when it isn't. */
    public static void updateSetBonus(LivingEntity entity) {
        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.get(WITHER_ROSED).orElse(null);
        if (effect == null) return;
        boolean wearing = WrgConfig.setBonusEffect && hasFullSet(entity);
        MobEffectInstance current = entity.getEffect(effect);
        if (wearing && current == null) {
            // Infinite, no particles, icon shown in the inventory and HUD.
            entity.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, 0, true, false, true));
        } else if (!wearing && current != null) {
            entity.removeEffect(effect);
        }
    }
}
