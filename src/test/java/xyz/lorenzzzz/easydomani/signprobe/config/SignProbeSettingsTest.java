package xyz.lorenzzzz.easydomani.signprobe.config;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class SignProbeSettingsTest {

    @Test
    void defaultEClamp() {
        assertEquals(new SignProbeSettings(true, 40, 5, 10, 3, 3, 5), SignProbeSettings.load(new YamlConfiguration()));
        YamlConfiguration c = new YamlConfiguration();
        c.set("enabled", false);
        c.set("timeoutTicks", 1);
        c.set("autoCloseTicks", -4);
        c.set("intervalTicks", 0);
        c.set("keysPerSign", 9);
        c.set("maxPerSession", -1);
        c.set("maxDeferrals", -1);
        assertEquals(new SignProbeSettings(false, 20, 0, 1, 3, 0, 0), SignProbeSettings.load(c));
        c.set("keysPerSign", 0);
        assertEquals(1, SignProbeSettings.load(c).keysPerSign());
    }
}
