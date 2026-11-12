package net.igobyjake.witherrosegold.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** The in-game settings screen on Fabric, drawn by Cloth Config. Only loaded when Cloth Config is installed. */
public final class ClothConfigScreen {
    private ClothConfigScreen() {}

    private static Component name(String key) {
        return Component.translatable("witherrosegold.configuration." + key);
    }

    private static Component tip(String key) {
        return Component.translatable("witherrosegold.configuration." + key + ".tooltip");
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("witherrosegold.configuration.title"))
                .setSavingRunnable(ConfigFile::save);
        ConfigEntryBuilder e = builder.entryBuilder();

        ConfigCategory armor = builder.getOrCreateCategory(name("armor"));
        armor.addEntry(e.startDoubleField(name("zap_chance"), WrgConfig.zapChance).setDefaultValue(WrgConfig.DEFAULT_ZAP_CHANCE)
                .setMin(0).setMax(1).setTooltip(tip("zap_chance")).setSaveConsumer(v -> WrgConfig.zapChance = v).build());
        armor.addEntry(e.startDoubleField(name("zap_damage"), WrgConfig.zapDamage).setDefaultValue(WrgConfig.DEFAULT_ZAP_DAMAGE)
                .setMin(0).setMax(100).setTooltip(tip("zap_damage")).setSaveConsumer(v -> WrgConfig.zapDamage = v).build());
        armor.addEntry(e.startDoubleField(name("blast_protection"), WrgConfig.blastProtection).setDefaultValue(WrgConfig.DEFAULT_BLAST_PROTECTION)
                .setMin(0).setMax(1).setTooltip(tip("blast_protection")).setSaveConsumer(v -> WrgConfig.blastProtection = v).build());
        armor.addEntry(e.startBooleanToggle(name("set_bonus_effect"), WrgConfig.setBonusEffect).setDefaultValue(true)
                .setTooltip(tip("set_bonus_effect")).setSaveConsumer(v -> WrgConfig.setBonusEffect = v).build());

        ConfigCategory tools = builder.getOrCreateCategory(name("tools"));
        tools.addEntry(e.startBooleanToggle(name("blast_mining"), WrgConfig.blastMining).setDefaultValue(true)
                .setTooltip(tip("blast_mining")).setSaveConsumer(v -> WrgConfig.blastMining = v).build());
        tools.addEntry(e.startIntField(name("blast_mining_cooldown"), WrgConfig.blastMiningCooldown).setDefaultValue(WrgConfig.DEFAULT_BLAST_MINING_COOLDOWN)
                .setMin(0).setMax(1200).setTooltip(tip("blast_mining_cooldown")).setSaveConsumer(v -> WrgConfig.blastMiningCooldown = v).build());

        ConfigCategory shard = builder.getOrCreateCategory(name("piezoshard"));
        shard.addEntry(e.startDoubleField(name("piezoshard_max_damage"), WrgConfig.piezoshardMaxDamage).setDefaultValue(WrgConfig.DEFAULT_PIEZOSHARD_MAX_DAMAGE)
                .setMin(0).setMax(200).setTooltip(tip("piezoshard_max_damage")).setSaveConsumer(v -> WrgConfig.piezoshardMaxDamage = v).build());
        shard.addEntry(e.startBooleanToggle(name("piezoshard_breaks_blocks"), WrgConfig.piezoshardBreaksBlocks).setDefaultValue(true)
                .setTooltip(tip("piezoshard_breaks_blocks")).setSaveConsumer(v -> WrgConfig.piezoshardBreaksBlocks = v).build());
        shard.addEntry(e.startIntField(name("piezoshard_cooldown"), WrgConfig.piezoshardCooldown).setDefaultValue(WrgConfig.DEFAULT_PIEZOSHARD_COOLDOWN)
                .setMin(0).setMax(1200).setTooltip(tip("piezoshard_cooldown")).setSaveConsumer(v -> WrgConfig.piezoshardCooldown = v).build());

        return builder.build();
    }
}
