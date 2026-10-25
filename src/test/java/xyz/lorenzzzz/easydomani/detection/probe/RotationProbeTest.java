package xyz.lorenzzzz.easydomani.detection.probe;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class RotationProbeTest {
    /** Valori della simulazione: big = yaw iniziale + 3600, vanilla = wrapDegrees, Anubis = big. */
    @Test
    void simulationValues() {
        assertEquals(true, RotationProbe.unwrapped(3600f, 3600f));
        assertEquals(false, RotationProbe.unwrapped(3600f, 0f));
        assertEquals(true, RotationProbe.unwrapped(3637.5f, 3637.5f));
        assertEquals(false, RotationProbe.unwrapped(3637.5f, 37.5f));
        assertEquals(true, RotationProbe.unwrapped(-3770f, -3770f));
        assertEquals(false, RotationProbe.unwrapped(-3770f, -170f));
        assertEquals(true, RotationProbe.unwrapped(3779.9f, 3779.9f));
        assertEquals(false, RotationProbe.unwrapped(3779.9f, 179.9f));
        assertEquals(true, RotationProbe.unwrapped(-4325.3f, -4325.3f));
        assertEquals(false, RotationProbe.unwrapped(-4325.3f, -5.3f));
        assertEquals(true, RotationProbe.unwrapped(15945.6f, 15945.6f));
        assertEquals(false, RotationProbe.unwrapped(15945.6f, 105.6f));
    }

    @Test
    void mouseMovementBetweenTicks() {
        // il mouse si muove tra LookAt ed eco: delta piccolo, verdetto invariato
        assertEquals(true, RotationProbe.unwrapped(3637.5f, 3637.5f + 25f));
        assertEquals(false, RotationProbe.unwrapped(3637.5f, 37.5f - 25f));
        assertEquals(false, RotationProbe.unwrapped(3637.5f, 179.9f + 300f)); // flick estremo, ancora wrap
    }

    @Test
    void uncertain() {
        assertNull(RotationProbe.unwrapped(3600f, 1800f));
        assertNull(RotationProbe.unwrapped(3600f, -3600f));
        assertNull(RotationProbe.unwrapped(3600f, Float.NaN));
    }

    @Test
    void verdictOnAllSamples() {
        float big = RotationProbe.wrap(-89.133f) + RotationProbe.PUSH_DEGREES; // 3510.867
        assertEquals(true, RotationProbe.verdict(big, List.of(3510.867f, 3512f)));
        assertEquals(false, RotationProbe.verdict(big, List.of(-89.133f, -80f)));
        // casi reali con la race: un campione a big+3600 o campioni discordanti non devono dare un verdetto
        assertNull(RotationProbe.verdict(3472.168f, List.of(7072.242f)));
        assertNull(RotationProbe.verdict(big, List.of(3510.867f, -89.133f)));
        assertNull(RotationProbe.verdict(big, List.of()));
    }

    @Test
    void wrapLikeVanilla() {
        assertEquals(-89.133f, RotationProbe.wrap(3510.867f), 1e-3f);
        assertEquals(-5.3f, RotationProbe.wrap(-725.3f), 1e-3f);
        assertEquals(-180f, RotationProbe.wrap(180f), 0f);
        assertEquals(179.9f, RotationProbe.wrap(179.9f), 1e-4f);
        // il push assoluto parte sempre da uno yaw nel range: niente accumulo tra tentativi
        assertTrue(Math.abs(RotationProbe.wrap(7072.242f) + RotationProbe.PUSH_DEGREES) < 3780f);
    }

    @Test
    void antiVanish() {
        assertTrue(RotationProbe.isAntiVanishProbe(30000, "minecraft:msg "));
        assertTrue(RotationProbe.isAntiVanishProbe(5, "minecraft:msg "));
        assertTrue(RotationProbe.isAntiVanishProbe(39999, "/give "));
        assertFalse(RotationProbe.isAntiVanishProbe(0, "/msg "));
        assertFalse(RotationProbe.isAntiVanishProbe(12, "/minecraft:msg "));
        assertFalse(RotationProbe.isAntiVanishProbe(-1, "minecraft:msg"));
    }
}
