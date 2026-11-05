package xyz.lorenzzzz.easydomani.honeypot.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.honeypot.model.BlockPositionKey;

class HoneypotBaitPlacementTest {

    @Test
    void candidatoAdiacenteAUnBaitScartato() {
        Map<Long, Object> existing = new HashMap<>();
        existing.put(BlockPositionKey.pack(10, 20, 30), "bait");

        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 10, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 11, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 9, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 10, 21, 30));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 10, 19, 30));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 10, 20, 31));
        assertTrue(HoneypotBaitService.touchesBait(List.of(existing), 10, 20, 29));
    }

    @Test
    void candidatoNonAdiacenteAccettato() {
        Map<Long, Object> existing = new HashMap<>();
        existing.put(BlockPositionKey.pack(10, 20, 30), "bait");

        assertFalse(HoneypotBaitService.touchesBait(List.of(existing), 12, 20, 30));
        assertFalse(HoneypotBaitService.touchesBait(List.of(existing), 11, 21, 30));
        assertFalse(HoneypotBaitService.touchesBait(List.of(existing), 11, 20, 31));
        assertFalse(HoneypotBaitService.touchesBait(List.of(new HashMap<>()), 10, 20, 30));
        assertFalse(HoneypotBaitService.touchesBait(null, 10, 20, 30));
    }

    @Test
    void ilControlloValeTraGiocatoriDiversi() {
        Map<Long, Object> primoGiocatore = new HashMap<>();
        primoGiocatore.put(BlockPositionKey.pack(10, 20, 30), "bait");
        Map<Long, Object> secondoGiocatore = new HashMap<>();
        secondoGiocatore.put(BlockPositionKey.pack(50, 20, 30), "bait");
        List<Map<Long, Object>> tutti = List.of(secondoGiocatore, primoGiocatore);

        assertTrue(HoneypotBaitService.touchesBait(tutti, 11, 20, 30));
        assertTrue(HoneypotBaitService.touchesBait(tutti, 50, 21, 30));
        assertFalse(HoneypotBaitService.touchesBait(tutti, 30, 20, 30));
    }
}
