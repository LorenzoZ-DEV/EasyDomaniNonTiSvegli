package xyz.lorenzzzz.easydomani.config;

import org.bukkit.configuration.ConfigurationSection;

public record PluginSettings(boolean debug, boolean antiSeedCrackerEnabled) {

    public static PluginSettings load(ConfigurationSection root) {
        return new PluginSettings(root.getBoolean("debug", false), root.getBoolean("antiSeedCracker.enabled", true));
    }
}
