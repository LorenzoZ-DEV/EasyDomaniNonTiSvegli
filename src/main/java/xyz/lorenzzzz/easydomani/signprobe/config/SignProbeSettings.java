package xyz.lorenzzzz.easydomani.signprobe.config;

import org.bukkit.configuration.ConfigurationSection;

public record SignProbeSettings(boolean enabled, int timeoutTicks, int autoCloseTicks, int intervalTicks,
                                int keysPerSign, int maxPerSession, int maxDeferrals) {

    public static SignProbeSettings disabled() {
        return new SignProbeSettings(false, 40, 5, 10, 3, 3, 5);
    }

    public static SignProbeSettings load(ConfigurationSection c) {
        return new SignProbeSettings(
                c.getBoolean("enabled", true),
                Math.max(20, c.getInt("timeoutTicks", 40)),
                Math.max(0, c.getInt("autoCloseTicks", 5)),
                Math.max(1, c.getInt("intervalTicks", 10)),
                Math.max(1, Math.min(3, c.getInt("keysPerSign", 3))),
                Math.max(0, c.getInt("maxPerSession", 3)),
                Math.max(0, c.getInt("maxDeferrals", 5)));
    }
}
