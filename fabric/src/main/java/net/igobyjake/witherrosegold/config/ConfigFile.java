package net.igobyjake.witherrosegold.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.igobyjake.witherrosegold.witherrosegold;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Fabric side of the settings: config/witherrosegold.json. Loaded at startup; edited in game through
 * Mod Menu + Cloth Config if they're installed (see ClothConfigScreen). Changes apply straight away.
 */
public final class ConfigFile {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("witherrosegold.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ConfigFile() {}

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                JsonObject json = JsonParser.parseString(Files.readString(PATH)).getAsJsonObject();
                WrgConfig.zapChance = clamp(json, "zap_chance", WrgConfig.zapChance, 0, 1);
                WrgConfig.zapDamage = clamp(json, "zap_damage", WrgConfig.zapDamage, 0, 100);
                WrgConfig.blastProtection = clamp(json, "blast_protection", WrgConfig.blastProtection, 0, 1);
                WrgConfig.setBonusEffect = bool(json, "set_bonus_effect", WrgConfig.setBonusEffect);
                WrgConfig.blastMining = bool(json, "blast_mining", WrgConfig.blastMining);
                WrgConfig.blastMiningCooldown = (int) clamp(json, "blast_mining_cooldown", WrgConfig.blastMiningCooldown, 0, 1200);
                WrgConfig.piezoshardMaxDamage = clamp(json, "piezoshard_max_damage", WrgConfig.piezoshardMaxDamage, 0, 200);
                WrgConfig.piezoshardBreaksBlocks = bool(json, "piezoshard_breaks_blocks", WrgConfig.piezoshardBreaksBlocks);
                WrgConfig.piezoshardCooldown = (int) clamp(json, "piezoshard_cooldown", WrgConfig.piezoshardCooldown, 0, 1200);
            }
        } catch (Exception e) {
            witherrosegold.LOGGER.warn("Couldn't read " + PATH + ", using defaults", e);
        }
        save();   // writes the file the first time, and adds any settings that are missing
    }

    public static void save() {
        JsonObject json = new JsonObject();
        json.addProperty("_comment", "Wither Rose Gold settings. Chances/protection are 0-1, cooldowns are in ticks (20 = 1 second).");
        json.addProperty("zap_chance", WrgConfig.zapChance);
        json.addProperty("zap_damage", WrgConfig.zapDamage);
        json.addProperty("blast_protection", WrgConfig.blastProtection);
        json.addProperty("set_bonus_effect", WrgConfig.setBonusEffect);
        json.addProperty("blast_mining", WrgConfig.blastMining);
        json.addProperty("blast_mining_cooldown", WrgConfig.blastMiningCooldown);
        json.addProperty("piezoshard_max_damage", WrgConfig.piezoshardMaxDamage);
        json.addProperty("piezoshard_breaks_blocks", WrgConfig.piezoshardBreaksBlocks);
        json.addProperty("piezoshard_cooldown", WrgConfig.piezoshardCooldown);
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(json));
        } catch (Exception e) {
            witherrosegold.LOGGER.warn("Couldn't write " + PATH, e);
        }
    }

    private static double clamp(JsonObject json, String key, double fallback, double min, double max) {
        return json.has(key) ? Math.max(min, Math.min(max, json.get(key).getAsDouble())) : fallback;
    }

    private static boolean bool(JsonObject json, String key, boolean fallback) {
        return json.has(key) ? json.get(key).getAsBoolean() : fallback;
    }
}
