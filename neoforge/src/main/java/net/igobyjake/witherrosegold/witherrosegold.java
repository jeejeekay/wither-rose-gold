package net.igobyjake.witherrosegold;

import net.igobyjake.witherrosegold.config.ModConfigs;
import com.mojang.logging.LogUtils;
import net.igobyjake.witherrosegold.effect.FullSetEffects;
import net.igobyjake.witherrosegold.effect.ModEffects;
import net.igobyjake.witherrosegold.entity.ModEntities;
import net.igobyjake.witherrosegold.item.ModCreativeModeTabs;
import net.igobyjake.witherrosegold.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

// Wither Rose Gold: a sister mod of Rose Gold Equipment (which must be installed too).
// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(witherrosegold.MOD_ID)
public class witherrosegold {
    public static final String MOD_ID = "witherrosegold";
    public static final Logger LOGGER = LogUtils.getLogger();

    public witherrosegold(IEventBus modEventBus, ModContainer modContainer) {
        ModConfigs.register(modEventBus, modContainer);   // settings (config/witherrosegold-local.toml)
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        // Full-set armor effects (shared code in effect/FullSetEffects).
        NeoForge.EVENT_BUS.addListener(witherrosegold::onIncomingDamage);
        NeoForge.EVENT_BUS.addListener(witherrosegold::afterDamage);
        NeoForge.EVENT_BUS.addListener(witherrosegold::playerTick);
    }

    // Blast protection: runs before armor is applied, like the enchantment would.
    private static void onIncomingDamage(LivingIncomingDamageEvent event) {
        event.setAmount(FullSetEffects.modifyIncomingDamage(event.getEntity(), event.getSource(), event.getAmount()));
    }

    // "Wither Rosed" effect while the full set is worn (checked twice a second).
    private static void playerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity().tickCount % FullSetEffects.SET_BONUS_CHECK_TICKS == 0) {
            FullSetEffects.updateSetBonus(event.getEntity());
        }
    }

    // Zap thorns: after the hit landed (not when a shield blocked all of it).
    private static void afterDamage(LivingDamageEvent.Post event) {
        if (event.getOriginalDamage() > event.getBlockedDamage()) {
            FullSetEffects.afterHurt(event.getEntity(), event.getSource());
        }
    }
}
