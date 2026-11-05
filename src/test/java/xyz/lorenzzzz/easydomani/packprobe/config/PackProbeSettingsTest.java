package xyz.lorenzzzz.easydomani.packprobe.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class PackProbeSettingsTest {

    private static final class Capture extends Handler {
        final List<String> lines = new ArrayList<>();

        @Override
        public void publish(LogRecord r) {
            lines.add(r.getMessage());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }

    private static Logger logger(Capture cap) {
        Logger l = Logger.getAnonymousLogger();
        l.setUseParentHandlers(false);
        l.addHandler(cap);
        return l;
    }

    @Test
    void url() {
        assertTrue(PackProbeSettings.isHttpUrl("http://mc.example.net:8164"));
        assertTrue(PackProbeSettings.isHttpUrl("https://cdn.example.net/p.zip"));
        assertFalse(PackProbeSettings.isHttpUrl(""));
        assertFalse(PackProbeSettings.isHttpUrl("ftp://x/p.zip"));
        assertFalse(PackProbeSettings.isHttpUrl("http://"));
        assertFalse(PackProbeSettings.isHttpUrl("non un url"));
    }

    @Test
    void disattivatoDiDefault() {
        Capture cap = new Capture();
        assertFalse(PackProbeSettings.load(new YamlConfiguration(), logger(cap)).enabled());
        assertTrue(cap.lines.isEmpty());
    }

    @Test
    void validazione() {
        Capture cap = new Capture();
        YamlConfiguration c = new YamlConfiguration();
        c.set("enabled", true);
        c.set("externalUrl", "https://cdn.example.net/p.zip");
        assertFalse(PackProbeSettings.load(c, logger(cap)).enabled());
        c.set("value", "x");
        assertFalse(PackProbeSettings.load(c, logger(cap)).enabled());
        c.set("value", "edn-fisso");
        c.set("externalUrl", "ftp://x");
        assertFalse(PackProbeSettings.load(c, logger(cap)).enabled());
        c.set("externalUrl", "");
        assertFalse(PackProbeSettings.load(c, logger(cap)).enabled());
        assertEquals(List.of(
                "Pack probe disattivato: con externalUrl serve un 'value' fisso (il pack caricato non cambia a ogni avvio)",
                "Pack probe disattivato: 'value' non valido (ammessi A-Z a-z 0-9 _ . -, 4-60 caratteri)",
                "Pack probe disattivato: externalUrl non valido (serve http:// o https://)",
                "Pack probe disattivato: publicUrl (http/https) e port (1-65535) obbligatori senza externalUrl"), cap.lines);
    }

    @Test
    void valido() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("enabled", true);
        c.set("publicUrl", "http://mc.example.net:8164/");
        c.set("overrideKeys", List.of("key.anubis.open_gui"));
        PackProbeSettings s = PackProbeSettings.load(c, logger(new Capture()));
        assertTrue(s.enabled());
        assertTrue(s.value().matches("edn-[0-9a-f]+"));
        assertEquals("0.0.0.0", s.bind());
        assertEquals(8164, s.port());
        assertEquals(List.of("key.anubis.open_gui"), s.overrideKeys());
    }
}
