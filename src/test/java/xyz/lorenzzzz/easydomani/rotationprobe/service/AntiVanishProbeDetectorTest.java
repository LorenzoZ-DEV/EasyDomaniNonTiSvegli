package xyz.lorenzzzz.easydomani.rotationprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class AntiVanishProbeDetectorTest {

    @Test
    void antiVanish() {
        assertTrue(AntiVanishProbeDetector.isAntiVanishProbe(30000, "minecraft:msg "));
        assertTrue(AntiVanishProbeDetector.isAntiVanishProbe(5, "minecraft:msg "));
        assertTrue(AntiVanishProbeDetector.isAntiVanishProbe(39999, "/give "));
        assertFalse(AntiVanishProbeDetector.isAntiVanishProbe(0, "/msg "));
        assertFalse(AntiVanishProbeDetector.isAntiVanishProbe(12, "/minecraft:msg "));
        assertFalse(AntiVanishProbeDetector.isAntiVanishProbe(-1, "minecraft:msg"));
    }

    @Test
    void logPulitoETroncato() {
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
        AntiVanishProbeDetector d = new AntiVanishProbeDetector(logger);
        d.inspect("Steve", 3, "/msg ");
        d.inspect("Steve", 3, null);
        d.inspect("Steve", 4, "minecraft:msg ");
        d.inspect("Alex", 30001, "§a\u0007" + "x".repeat(120));
        assertEquals(List.of("[AntiVanish] Steve id=4 text='minecraft:msg '",
                "[AntiVanish] Alex id=30001 text='a" + "x".repeat(97) + "'"), out);
    }
}
