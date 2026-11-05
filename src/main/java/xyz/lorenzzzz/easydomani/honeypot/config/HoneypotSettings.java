package xyz.lorenzzzz.easydomani.honeypot.config;

import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public record HoneypotSettings(boolean enabled, int maxY, Material baitStone, Material baitDeepslate, int maxPerPlayer,
                               int minRadius, int maxRadius, long ttlMs, long cooldownMs, int intervalTicks) {

    public static HoneypotSettings disabled() {
        return new HoneypotSettings(false, 40, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, 2, 4, 10, 300_000L, 10_000L, 20);
    }

    public static HoneypotSettings load(ConfigurationSection c, Logger logger) {
        boolean enabled = c.getBoolean("enabled", true);
        int maxY = c.getInt("maxY", 40);
        Material stone = baitBlock(c.getString("baitBlock"), Material.DIAMOND_ORE, logger);
        Material deepslate = baitBlock(c.getString("baitBlockDeepslate"), Material.DEEPSLATE_DIAMOND_ORE, logger);
        int maxPerPlayer = Math.max(1, c.getInt("maxPerPlayer", 2));
        int minRadius = Math.min(32, Math.max(2, c.getInt("minRadius", 4)));
        int maxRadius = Math.min(32, Math.max(minRadius, c.getInt("maxRadius", 10)));
        long ttlMs = Math.max(1, c.getLong("ttlSeconds", 300)) * 1000L;
        long cooldownMs = Math.max(1, c.getLong("placeCooldownSeconds", 10)) * 1000L;
        int intervalTicks = Math.max(5, c.getInt("checkIntervalTicks", 20));
        return new HoneypotSettings(enabled, maxY, stone, deepslate, maxPerPlayer, minRadius, maxRadius, ttlMs, cooldownMs, intervalTicks);
    }

    private static Material baitBlock(String name, Material def, Logger logger) {
        Material m = name == null ? null : Material.matchMaterial(name);
        if (m == null || !m.isBlock()) {
            if (name != null) logger.warning("honeypot: blocco '" + name + "' non valido, uso " + def);
            m = def;
        }
        return m;
    }
}
