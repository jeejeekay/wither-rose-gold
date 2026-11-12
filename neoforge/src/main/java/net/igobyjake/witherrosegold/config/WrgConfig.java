package net.igobyjake.witherrosegold.config;

/**
 * Wither Rose Gold's settings, as plain values the rest of the mod reads every time it uses them,
 * so changes apply straight away (no restart).
 *
 * The loader keeps these up to date: NeoForge from config/witherrosegold-common.toml (and its
 * built-in config screen), Fabric from config/witherrosegold.json (and the Mod Menu + Cloth Config
 * screen). They're only used by the server side of the game.
 *
 * Vanilla-only (same file on Fabric and NeoForge).
 */
public final class WrgConfig {
    // Defaults (also what the GameTests expect)
    public static final double DEFAULT_ZAP_CHANCE = 0.5;
    public static final double DEFAULT_ZAP_DAMAGE = 3.0;
    public static final double DEFAULT_BLAST_PROTECTION = 0.5;
    public static final int DEFAULT_BLAST_MINING_COOLDOWN = 10;
    public static final double DEFAULT_PIEZOSHARD_MAX_DAMAGE = 20.0;
    public static final int DEFAULT_PIEZOSHARD_COOLDOWN = 20;

    /** Full set: chance (0-1) that a melee attacker gets zapped. */
    public static double zapChance = DEFAULT_ZAP_CHANCE;
    /** Full set: zap damage (ignores armor). */
    public static double zapDamage = DEFAULT_ZAP_DAMAGE;
    /** Full set: fraction (0-1) of explosion damage removed. */
    public static double blastProtection = DEFAULT_BLAST_PROTECTION;
    /** Tools: right-click a block for the 3x3 blast. */
    public static boolean blastMining = true;
    /** Tools: ticks (20 = 1 second) between blasts. */
    public static int blastMiningCooldown = DEFAULT_BLAST_MINING_COOLDOWN;
    /** Piezoshard: damage at the very centre of its explosion (before armor). */
    public static double piezoshardMaxDamage = DEFAULT_PIEZOSHARD_MAX_DAMAGE;
    /** Piezoshard: its explosion breaks blocks. */
    public static boolean piezoshardBreaksBlocks = true;
    /** Piezoshard: ticks between throws. */
    public static int piezoshardCooldown = DEFAULT_PIEZOSHARD_COOLDOWN;
    /** Full set: show the "Wither Rosed" effect while it's worn. */
    public static boolean setBonusEffect = true;

    private WrgConfig() {}
}
