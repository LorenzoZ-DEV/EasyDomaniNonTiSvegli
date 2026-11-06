package xyz.lorenzzzz.easydomani.tabcomplete.config;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class TabCompleteRateSettingsTest {

    @Test
    void valoriDefault() {
        assertEquals(new TabCompleteRateSettings(true, 10000L, 40, false), TabCompleteRateSettings.load(new YamlConfiguration()));
    }

    @Test
    void clamp() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("windowMs", 5);
        c.set("maxPackets", 0);
        c.set("experimentalBan", true);
        assertEquals(new TabCompleteRateSettings(true, 1000L, 1, true), TabCompleteRateSettings.load(c));
    }
}
