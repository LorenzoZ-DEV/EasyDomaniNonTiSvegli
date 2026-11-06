package xyz.lorenzzzz.easydomani.tabcomplete.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.tabcomplete.config.TabCompleteRateSettings;

class TabCompleteRateServiceTest {
    private long now;
    private TabCompleteRateService service;
    private final UUID a = UUID.randomUUID();
    private final UUID b = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        now = 0;
        service = new TabCompleteRateService(() -> now);
        service.reload(new TabCompleteRateSettings(true, 1000L, 3, false));
    }

    private int burst(UUID u, int n) {
        int flagged = 0;
        for (int i = 0; i < n; i++) flagged = Math.max(flagged, service.record(u));
        return flagged;
    }

    @Test
    void sottoSogliaNonScatta() {
        assertEquals(0, burst(a, 3));
    }

    @Test
    void oltreSogliaScattaUnaSolaVolta() {
        assertEquals(0, burst(a, 3));
        assertEquals(4, service.record(a));
        assertEquals(0, service.record(a));
        assertEquals(0, service.record(a));
    }

    @Test
    void contaIPacchettiRealiNellaFinestra() {
        assertEquals(0, burst(a, 3));
        service.reload(new TabCompleteRateSettings(true, 1000L, 1, false));
        assertEquals(4, service.record(a));
    }

    @Test
    void finestraScadutaResetta() {
        burst(a, 3);
        now = 1000;
        assertEquals(0, burst(a, 3));
    }

    @Test
    void forgetResetta() {
        assertEquals(4, burst(a, 4));
        service.forget(a);
        assertEquals(0, burst(a, 3));
        assertEquals(4, service.record(a));
    }

    @Test
    void playerIndipendenti() {
        assertEquals(4, burst(a, 4));
        assertEquals(0, burst(b, 3));
    }
}
