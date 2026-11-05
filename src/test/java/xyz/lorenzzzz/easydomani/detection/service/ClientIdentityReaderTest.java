package xyz.lorenzzzz.easydomani.detection.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClientIdentityReaderTest {
    @Test
    void sanitizeBrand() {
        assertEquals("cfabric", ClientIdentityReader.sanitize("§cfab\u0000ric", 64));
        assertEquals(64, ClientIdentityReader.sanitize("x".repeat(500), 64).length());
        assertEquals("", ClientIdentityReader.sanitize(null, 64));
    }
}
