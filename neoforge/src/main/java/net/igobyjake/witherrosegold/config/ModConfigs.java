package net.igobyjake.witherrosegold.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * NeoForge side of the settings: config/witherrosegold-local.toml (26.3 calls this type "local"; it was "common"), also editable in game from
 * Mods -> Wither Rose Gold -> Config (NeoForge draws that screen for us). Whenever the file is
 * loaded or changed, the values are copied into WrgConfig.
 */
public final class ModConfigs {
    public static final ModConfigSpec COMMON_SPEC;
    private static final ModConfigSpec.DoubleValue ZAP_CHANCE;
    private static final ModConfigSpec.DoubleValue ZAP_DAMAGE;
    private static final ModConfigSpec.DoubleValue BLAST_PROTECTION;
    private static final ModConfigSpec.BooleanValue SET_BONUS_EFFECT;
    private static final ModConfigSpec.BooleanValue BLAST_MINING;
    private static final ModConfigSpec.IntValue BLAST_MINING_COOLDOWN;
    private static final ModConfigSpec.DoubleValue PIEZOSHARD_MAX_DAMAGE;
    private static final ModConfigSpec.BooleanValue PIEZOSHARD_BREAKS_BLOCKS;
    private static final ModConfigSpec.IntValue PIEZOSHARD_COOLDOWN;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Full armor set").translation("witherrosegold.configuration.armor").push("armor");
        ZAP_CHANCE = builder.comment("Chance (0-1) that something hitting you in melee gets zapped.")
                .translation("witherrosegold.configuration.zap_chance")
                .defineInRange("zap_chance", WrgConfig.DEFAULT_ZAP_CHANCE, 0.0, 1.0);
        ZAP_DAMAGE = builder.comment("Zap damage (ignores armor).")
                .translation("witherrosegold.configuration.zap_damage")
                .defineInRange("zap_damage", WrgConfig.DEFAULT_ZAP_DAMAGE, 0.0, 100.0);
        BLAST_PROTECTION = builder.comment("Fraction (0-1) of explosion damage the full set removes.")
                .translation("witherrosegold.configuration.blast_protection")
                .defineInRange("blast_protection", WrgConfig.DEFAULT_BLAST_PROTECTION, 0.0, 1.0);
        SET_BONUS_EFFECT = builder.comment("Show the 'Wither Rosed' effect while the full set is worn.")
                .translation("witherrosegold.configuration.set_bonus_effect")
                .define("set_bonus_effect", true);
        builder.pop();

        builder.comment("Tools").translation("witherrosegold.configuration.tools").push("tools");
        BLAST_MINING = builder.comment("Right-clicking a block with a wither rose gold tool blasts a 3x3.")
                .translation("witherrosegold.configuration.blast_mining")
                .define("blast_mining", true);
        BLAST_MINING_COOLDOWN = builder.comment("Ticks between blasts (20 = 1 second).")
                .translation("witherrosegold.configuration.blast_mining_cooldown")
                .defineInRange("blast_mining_cooldown", WrgConfig.DEFAULT_BLAST_MINING_COOLDOWN, 0, 1200);
        builder.pop();

        builder.comment("Piezoshard").translation("witherrosegold.configuration.piezoshard").push("piezoshard");
        PIEZOSHARD_MAX_DAMAGE = builder.comment("Damage at the centre of a piezoshard explosion (before armor). TNT would be 57.")
                .translation("witherrosegold.configuration.piezoshard_max_damage")
                .defineInRange("piezoshard_max_damage", WrgConfig.DEFAULT_PIEZOSHARD_MAX_DAMAGE, 0.0, 200.0);
        PIEZOSHARD_BREAKS_BLOCKS = builder.comment("Piezoshard explosions break blocks.")
                .translation("witherrosegold.configuration.piezoshard_breaks_blocks")
                .define("piezoshard_breaks_blocks", true);
        PIEZOSHARD_COOLDOWN = builder.comment("Ticks between throws (20 = 1 second).")
                .translation("witherrosegold.configuration.piezoshard_cooldown")
                .defineInRange("piezoshard_cooldown", WrgConfig.DEFAULT_PIEZOSHARD_COOLDOWN, 0, 1200);
        builder.pop();

        COMMON_SPEC = builder.build();
    }

    private ModConfigs() {}

    public static void register(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.LOCAL, COMMON_SPEC);   // 26.3 renamed COMMON to LOCAL
        // These only fire for this mod's own config files (WRG has just this one).
        modEventBus.addListener(ModConfigEvent.Loading.class, event -> apply());
        modEventBus.addListener(ModConfigEvent.Reloading.class, event -> apply());
    }

    private static void apply() {
        WrgConfig.zapChance = ZAP_CHANCE.get();
        WrgConfig.zapDamage = ZAP_DAMAGE.get();
        WrgConfig.blastProtection = BLAST_PROTECTION.get();
        WrgConfig.setBonusEffect = SET_BONUS_EFFECT.get();
        WrgConfig.blastMining = BLAST_MINING.get();
        WrgConfig.blastMiningCooldown = BLAST_MINING_COOLDOWN.get();
        WrgConfig.piezoshardMaxDamage = PIEZOSHARD_MAX_DAMAGE.get();
        WrgConfig.piezoshardBreaksBlocks = PIEZOSHARD_BREAKS_BLOCKS.get();
        WrgConfig.piezoshardCooldown = PIEZOSHARD_COOLDOWN.get();
    }
}
