package net.igobyjake.witherrosegold.effect;

import net.igobyjake.witherrosegold.util.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * What a full set of wither rose gold armor does. Built in, so it works on top of (and
 * independently from) any enchantments:
 *  - Blast protection: explosions do half damage.
 *  - Zap: whoever hits you in melee has a chance to get shocked (like Thorns, but electric).
 *
 * Vanilla-only (same file on Fabric and NeoForge); the loaders just call these two methods.
 */
public final class FullSetEffects {
    /** Fraction of explosion damage removed (vanilla Blast Protection IV on every piece caps at 80%). */
    public static final float BLAST_REDUCTION = 0.5F;
    public static final float ZAP_CHANCE = 0.3F;
    public static final int ZAP_MIN_DAMAGE = 2;
    public static final int ZAP_MAX_DAMAGE = 4;

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
            return amount * (1.0F - BLAST_REDUCTION);
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
        if (!hasFullSet(victim) || victim.getRandom().nextFloat() >= ZAP_CHANCE) return;

        float damage = ZAP_MIN_DAMAGE + victim.getRandom().nextInt(ZAP_MAX_DAMAGE - ZAP_MIN_DAMAGE + 1);
        DamageSource zap = new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ModTags.DamageTypes.ZAP), victim);
        target.hurtServer(level, zap, damage);

        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY(0.5), target.getZ(),
                14, target.getBbWidth() * 0.4, target.getBbHeight() * 0.3, target.getBbWidth() * 0.4, 0.15);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT,
                SoundSource.PLAYERS, 0.3F, 1.9F);
    }
}
