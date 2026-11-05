package xyz.lorenzzzz.easydomani.detection.config;

import org.bukkit.configuration.ConfigurationSection;

public record ModDetectionSettings(boolean enabled, int joinDelayTicks, boolean notifyOnJoin, boolean notifyVanilla) {

    public static ModDetectionSettings disabled() {
        return new ModDetectionSettings(false, 1, false, false);
    }

    public static ModDetectionSettings load(ConfigurationSection root) {
        return new ModDetectionSettings(
                root.getBoolean("modDetection.enabled", true),
                Math.max(1, root.getInt("modDetection.joinDelayTicks", 40)),
                root.getBoolean("brand.notifyOnJoin", true),
                root.getBoolean("brand.notifyVanilla", false));
    }
}
