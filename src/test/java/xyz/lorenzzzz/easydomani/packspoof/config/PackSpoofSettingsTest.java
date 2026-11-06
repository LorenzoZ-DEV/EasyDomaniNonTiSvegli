package xyz.lorenzzzz.easydomani.packspoof.config;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class PackSpoofSettingsTest {

    @Test
    void valoriDefault() {
        assertEquals(new PackSpoofSettings(true, 50L, false, false), PackSpoofSettings.load(new YamlConfiguration()));
    }

    @Test
    void clamp() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("enabled", false);
        c.set("maxMs", -5);
        c.set("experimentalBan", true);
        c.set("behindVelocity", true);
        assertEquals(new PackSpoofSettings(false, 0L, true, true), PackSpoofSettings.load(c));
        c.set("maxMs", 99999);
        assertEquals(1000L, PackSpoofSettings.load(c).maxMs());
    }
}
