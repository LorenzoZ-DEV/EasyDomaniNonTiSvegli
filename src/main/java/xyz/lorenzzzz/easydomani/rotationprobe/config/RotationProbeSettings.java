package xyz.lorenzzzz.easydomani.rotationprobe.config;

import org.bukkit.configuration.ConfigurationSection;

public record RotationProbeSettings(boolean enabled, long delayTicks) {

    public static RotationProbeSettings disabled() {
        return new RotationProbeSettings(false, 200L);
    }

    public static RotationProbeSettings load(ConfigurationSection c) {
        return new RotationProbeSettings(c.getBoolean("enabled", false), Math.max(1, c.getLong("delayTicks", 200)));
    }
}
