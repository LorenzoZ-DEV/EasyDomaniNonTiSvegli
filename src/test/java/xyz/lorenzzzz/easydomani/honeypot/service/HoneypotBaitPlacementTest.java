package xyz.lorenzzzz.easydomani.honeypot.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.honeypot.model.BlockPositionKey;

class HoneypotBaitPlacementTest {

    @Test
    void candidatoAdiacenteAUnBaitScartato() {
        Map<Long, Object> existing = new HashMap<>();
        existing.put(BlockPositionKey.pack(10, 20, 30), "bait");

        assertTrue(HoneypotBaitService.touchesBait(existing, 10, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(existing, 11, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(existing, 9, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(existing, 10, 21, 30));
        assertTrue(HoneypotBaitService.touchesBait(existing, 10, 19, 30));
        assertTrue(HoneypotBaitService.touchesBait(existing, 10, 20, 31));
        assertTrue(HoneypotBaitService.touchesBait(existing, 10, 20, 29));
    }

    @Test
    void candidatoNonAdiacenteAccettato() {
        Map<Long, Object> existing = new HashMap<>();
        existing.put(BlockPositionKey.pack(10, 20, 30), "bait");

        assertFalse(HoneypotBaitService.touchesBait(existing, 12, 20, 30));
        assertFalse(HoneypotBaitService.touchesBait(existing, 11, 21, 30));
        assertFalse(HoneypotBaitService.touchesBait(existing, 11, 20, 31));
        assertFalse(HoneypotBaitService.touchesBait(new HashMap<>(), 10, 20, 30));
        assertFalse(HoneypotBaitService.touchesBait(null, 10, 20, 30));
    }
}
