package xyz.lorenzzzz.easydomani.detection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModDetectionTest {
    @Test
    void sanitizeBrand() {
        assertEquals("cfabric", ModDetection.sanitize("§cfab\u0000ric", 64)); // via § e controllo, resto testo puro
        assertEquals(64, ModDetection.sanitize("x".repeat(500), 64).length());
        assertEquals("", ModDetection.sanitize(null, 64));
    }
}
