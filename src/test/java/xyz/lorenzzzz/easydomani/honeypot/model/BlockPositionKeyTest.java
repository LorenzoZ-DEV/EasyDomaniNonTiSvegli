package xyz.lorenzzzz.easydomani.honeypot.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BlockPositionKeyTest {
    @Test
    void packPosizioniDistinte() {
        assertNotEquals(BlockPositionKey.pack(1, 2, 3), BlockPositionKey.pack(3, 2, 1));
        assertNotEquals(BlockPositionKey.pack(-1, -64, -1), BlockPositionKey.pack(-1, -63, -1));
        assertEquals(BlockPositionKey.pack(-30000000, -64, 29999999), BlockPositionKey.pack(-30000000, -64, 29999999));
        assertNotEquals(BlockPositionKey.pack(0, 0, 0), BlockPositionKey.pack(0, 0, 1));
    }
}
