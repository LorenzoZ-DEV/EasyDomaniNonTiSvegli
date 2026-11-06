package xyz.lorenzzzz.easydomani.packspoof.config;

import org.bukkit.configuration.ConfigurationSection;

public record PackSpoofSettings(boolean enabled, long maxMs, boolean experimentalBan) {

    public static PackSpoofSettings disabled() {
        return new PackSpoofSettings(false, 50L, false);
    }

    public static PackSpoofSettings load(ConfigurationSection c) {
        return new PackSpoofSettings(c.getBoolean("enabled", true), Math.clamp(c.getLong("maxMs", 50), 0L, 1000L),
                c.getBoolean("experimentalBan", false));
    }
}
