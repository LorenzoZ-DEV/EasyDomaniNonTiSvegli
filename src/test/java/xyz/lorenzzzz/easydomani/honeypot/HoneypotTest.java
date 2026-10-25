package xyz.lorenzzzz.easydomani.honeypot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HoneypotTest {
    @Test
    void packPosizioniDistinte() {
        assertNotEquals(Honeypot.pack(1, 2, 3), Honeypot.pack(3, 2, 1));
        assertNotEquals(Honeypot.pack(-1, -64, -1), Honeypot.pack(-1, -63, -1));
        assertEquals(Honeypot.pack(-30000000, -64, 29999999), Honeypot.pack(-30000000, -64, 29999999));
        assertNotEquals(Honeypot.pack(0, 0, 0), Honeypot.pack(0, 0, 1));
    }
}
