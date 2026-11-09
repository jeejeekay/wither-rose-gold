package net.igobyjake.witherrosegold.entity;

import net.igobyjake.witherrosegold.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/** A thrown piezoshard: flies like a snowball and explodes on impact, a bit weaker than TNT. */
public class PiezoshardEntity extends ThrowableItemProjectile {
    /** Size of the blast (which blocks break). TNT is 4. */
    public static final float EXPLOSION_POWER = 3.0F;
    /** Damage to mobs/players: at most 20 at the centre, regardless of the size above. */
    private static final PiezoshardExplosionDamage DAMAGE = new PiezoshardExplosionDamage();

    public PiezoshardEntity(EntityType<? extends PiezoshardEntity> type, Level level) {
        super(type, level);
    }

    public PiezoshardEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.PIEZOSHARD.get(), owner, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PIEZOSHARD.get();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level() instanceof ServerLevel level) {
            level.explode(this, null, DAMAGE, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, false, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }
}
