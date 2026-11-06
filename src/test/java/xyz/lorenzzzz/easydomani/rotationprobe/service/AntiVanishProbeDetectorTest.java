package xyz.lorenzzzz.easydomani.rotationprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
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
    void unAlertPerSessione() {
        AntiVanishProbeDetector d = new AntiVanishProbeDetector();
        UUID a = UUID.randomUUID(), b = UUID.randomUUID();
        assertFalse(d.inspect(a, 3, "/msg "));
        assertFalse(d.inspect(a, 3, null));
        assertTrue(d.inspect(a, 4, "minecraft:msg "));
        assertFalse(d.inspect(a, 30001, "x"));
        assertTrue(d.inspect(b, 30001, "x"));
    }

    @Test
    void forgetRiabilita() {
        AntiVanishProbeDetector d = new AntiVanishProbeDetector();
        UUID a = UUID.randomUUID();
        assertTrue(d.inspect(a, 4, "minecraft:msg "));
        d.forget(a);
        assertTrue(d.inspect(a, 4, "minecraft:msg "));
    }
}
