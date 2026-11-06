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

    private boolean burst(UUID u, int n) {
        boolean any = false;
        for (int i = 0; i < n; i++) any |= service.record(u);
        return any;
    }

    @Test
    void sottoSogliaNonScatta() {
        assertFalse(burst(a, 3));
    }

    @Test
    void oltreSogliaScattaUnaSolaVolta() {
        assertFalse(burst(a, 3));
        assertTrue(service.record(a));
        assertFalse(service.record(a));
        assertFalse(service.record(a));
    }

    @Test
    void finestraScadutaResetta() {
        burst(a, 3);
        now = 1000;
        assertFalse(burst(a, 3));
    }

    @Test
    void forgetResetta() {
        assertTrue(burst(a, 4));
        service.forget(a);
        assertFalse(burst(a, 3));
        assertTrue(service.record(a));
    }

    @Test
    void playerIndipendenti() {
        assertTrue(burst(a, 4));
        assertFalse(burst(b, 3));
    }
}
