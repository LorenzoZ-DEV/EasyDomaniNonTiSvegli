package xyz.lorenzzzz.easydomani.tabcomplete.config;

import org.bukkit.configuration.ConfigurationSection;

public record TabCompleteRateSettings(boolean enabled, long windowMs, int maxPackets, boolean experimentalBan) {

    public static TabCompleteRateSettings disabled() {
        return new TabCompleteRateSettings(false, 10000L, 40, false);
    }

    public static TabCompleteRateSettings load(ConfigurationSection c) {
        return new TabCompleteRateSettings(c.getBoolean("enabled", true), Math.max(1000L, c.getLong("windowMs", 10000)),
                Math.max(1, c.getInt("maxPackets", 40)), c.getBoolean("experimentalBan", false));
    }
}
