package xyz.lorenzzzz.easydomani.rotationprobe.config;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class RotationProbeSettingsTest {

    @Test
    void defaultEClamp() {
        assertEquals(new RotationProbeSettings(false, 200L), RotationProbeSettings.load(new YamlConfiguration()));
        assertEquals(RotationProbeSettings.disabled(), RotationProbeSettings.load(new YamlConfiguration()));
        YamlConfiguration c = new YamlConfiguration();
        c.set("enabled", true);
        c.set("delayTicks", -5);
        assertEquals(new RotationProbeSettings(true, 1L), RotationProbeSettings.load(c));
    }
}
