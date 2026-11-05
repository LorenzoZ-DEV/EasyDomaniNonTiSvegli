package xyz.lorenzzzz.easydomani.honeypot.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class HoneypotSettingsTest {

    @Test
    void defaultEClamp() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        List<String> out = new ArrayList<>();
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord r) {
                out.add(r.getMessage());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        });
        HoneypotSettings d = HoneypotSettings.load(new YamlConfiguration(), logger);
        assertEquals(new HoneypotSettings(true, 40, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, 2, 4, 10, 300_000L, 10_000L, 20), d);
        assertTrue(out.isEmpty());

        YamlConfiguration c = new YamlConfiguration();
        c.set("maxPerPlayer", 0);
        c.set("minRadius", 40);
        c.set("maxRadius", 1);
        c.set("ttlSeconds", 0);
        c.set("placeCooldownSeconds", -3);
        c.set("checkIntervalTicks", 1);
        HoneypotSettings s = HoneypotSettings.load(c, logger);
        assertEquals(new HoneypotSettings(true, 40, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, 1, 32, 32, 1000L, 1000L, 5), s);
        assertTrue(out.isEmpty());
    }
}
