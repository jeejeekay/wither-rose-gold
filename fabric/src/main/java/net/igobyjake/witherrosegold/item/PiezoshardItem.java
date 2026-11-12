package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.config.WrgConfig;
import net.igobyjake.witherrosegold.entity.PiezoshardEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Right-click to throw (works like a snowball), with a short cooldown so it can't be spammed. */
public class PiezoshardItem extends Item {
    // The cooldown is a setting: see config/WrgConfig.

    public PiezoshardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(PiezoshardEntity::new, serverLevel, stack, player, 0.0F, 1.5F, 1.0F);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (WrgConfig.piezoshardCooldown > 0) player.getCooldowns().addCooldown(stack, WrgConfig.piezoshardCooldown);
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
