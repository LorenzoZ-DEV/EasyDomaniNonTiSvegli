package xyz.lorenzzzz.easydomani.detection.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.detection.config.ModProfileLoader;

class ModProfileTest {
    static Map<String, ModProfile> byId;

    @BeforeAll
    static void loadDefaults() throws Exception {
        try (var in = ModProfileTest.class.getClassLoader().getResourceAsStream("mods.yml")) {
            assertNotNull(in, "mods.yml nel classpath");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<ModProfile> list = ModProfileLoader.load(yaml.getConfigurationSection("profiles"), Logger.getLogger("test"));
            byId = new HashMap<>();
            for (ModProfile p : list) byId.put(p.id(), p);
        }
    }

    @Test
    void profiliDiDefault() {
        assertEquals(java.util.Set.of("meteor", "wurst", "liquidbounce", "anubis", "freecam",
                "xaero-minimap", "xaero-worldmap", "journeymap"), byId.keySet());
        assertTrue(byId.get("meteor").ban());
        assertTrue(byId.get("wurst").ban());
        assertTrue(byId.get("liquidbounce").ban());
        assertTrue(byId.get("anubis").ban());
        assertFalse(byId.get("freecam").ban());
        assertFalse(byId.get("xaero-minimap").ban());
        assertFalse(byId.get("journeymap").ban());
    }

    @Test
    void brandSpooferLiquidBounce() {
        ModProfile lb = byId.get("liquidbounce");
        assertTrue(lb.matchesBrand("lunarclient:v2.21.33-2612"));
        assertTrue(lb.matchesBrand(" CB "));
        assertTrue(lb.matchesBrand("cb"));
        assertFalse(lb.matchesBrand("lunarclient:v2.21.33-2613"));
        assertFalse(lb.matchesBrand("vanilla"));
        assertFalse(lb.matchesBrand("geyser"));
        assertFalse(lb.matchesBrand(null));
        assertFalse(lb.hasProbe());

        for (ModProfile p : byId.values()) {
            assertFalse(p.matchesBrand("fabric"), p.id());
            assertFalse(p.matchesBrand("vanilla"), p.id());
        }
    }

    @Test
    void brandContains() {
        ModProfile p = new ModProfile("x", "X", false, List.of(), List.of("badclient"), List.of(), null, List.of(), false, false);
        assertTrue(p.matchesBrand("Fabric BadClient 1.2"));
        assertFalse(p.matchesBrand("fabric"));
    }

    @Test
    void canaliPerPrefisso() {
        assertTrue(byId.get("meteor").matchesChannel("meteor-client:packet"));
        assertTrue(byId.get("meteor").matchesChannel("METEOR-CLIENT:x"));
        assertFalse(byId.get("meteor").matchesChannel("meteorite:x"));
        assertTrue(byId.get("wurst").matchesChannel("wurst:zoom"));
        assertTrue(byId.get("xaero-minimap").matchesChannel("xaerominimap:main"));
        assertTrue(byId.get("journeymap").matchesChannel("journeymap:common"));
        assertFalse(byId.get("anubis").matchesChannel("anything:x"));
        for (ModProfile p : byId.values()) {
            assertFalse(p.matchesChannel("fabric:registry/sync"), p.id());
            assertFalse(p.matchesChannel("minecraft:brand"), p.id());
        }
    }

    @Test
    void probeDaMods() {
        ModProfile anubis = byId.get("anubis");
        assertEquals("key.anubis.open_gui", anubis.probeKey());
        assertTrue(anubis.probeKeybind());
        assertTrue(anubis.hasProbe());
        assertTrue(byId.get("meteor").probeBypass());
        assertFalse(byId.get("journeymap").hasProbe(), "journeymap non verificato");
        assertTrue(byId.get("freecam").hasProbe());
    }

    @Test
    void keybindSenzaExpected() {
        YamlConfiguration y = new YamlConfiguration();
        y.set("k.probe.key", "key.x");
        y.set("k.probe.keybind", true);
        y.set("n.probe.key", "key.y");
        List<ModProfile> l = ModProfileLoader.load(y, Logger.getLogger("test"));
        assertTrue(l.get(0).hasProbe());
        assertFalse(l.get(1).hasProbe());
    }
}
