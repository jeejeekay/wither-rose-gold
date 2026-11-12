package net.igobyjake.witherrosegold.entity;

import net.igobyjake.witherrosegold.config.WrgConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

/**
 * How much a piezoshard explosion hurts, separate from how big it is (power 3, which decides the
 * blocks it breaks). Same falloff as vanilla, but scaled so the very centre does the
 * piezoshard_max_damage setting (default 20; before armor and difficulty) instead of vanilla's 43 for this size.
 *
 * Vanilla-only (same file on Fabric and NeoForge).
 */
public class PiezoshardExplosionDamage extends ExplosionDamageCalculator {
    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity, float exposure) {
        double distance = Math.sqrt(entity.distanceToSqr(explosion.center())) / (explosion.radius() * 2.0F);
        return damageAt(distance, exposure);
    }

    /**
     * @param distance 0 at the centre, 1 at the edge of the damage range
     * @param exposure 0..1, how much of the entity the blast can "see"
     */
    public static float damageAt(double distance, float exposure) {
        double impact = (1.0 - distance) * exposure;
        return (float) ((impact * impact + impact) / 2.0 * (WrgConfig.piezoshardMaxDamage - 1.0) + 1.0);
    }
}
