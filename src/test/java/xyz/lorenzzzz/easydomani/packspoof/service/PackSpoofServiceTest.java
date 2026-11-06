package xyz.lorenzzzz.easydomani.packspoof.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.packspoof.config.PackSpoofSettings;

class PackSpoofServiceTest {
    private long now;
    private PackSpoofService service;
    private final UUID a = UUID.randomUUID();
    private final UUID b = UUID.randomUUID();
    private final UUID pack1 = UUID.randomUUID();
    private final UUID pack2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        now = 1000;
        service = new PackSpoofService(() -> now);
        service.reload(new PackSpoofSettings(true, 50L, false));
    }

    @Test
    void caricamentoIstantaneoScatta() {
        service.accepted(a, pack1);
        now += 1;
        assertTrue(service.loaded(a, pack1));
        assertEquals(1L, service.takePending(a));
        assertEquals(-1L, service.takePending(a));
    }

    @Test
    void caricamentoRealeNonScatta() {
        service.accepted(a, pack1);
        now += 400;
        assertFalse(service.loaded(a, pack1));
        assertEquals(-1L, service.takePending(a));
    }

    @Test
    void sogliaInclusa() {
        service.accepted(a, pack1);
        now += 50;
        assertTrue(service.loaded(a, pack1));
    }

    @Test
    void loadedSenzaAcceptedNonScatta() {
        assertFalse(service.loaded(a, pack1));
    }

    @Test
    void packDiversoNonScatta() {
        service.accepted(a, pack1);
        service.accepted(a, pack2);
        assertFalse(service.loaded(a, pack1));
    }

    @Test
    void piuPackInSequenzaVanillaNonScatta() {
        service.accepted(a, pack1);
        service.accepted(a, pack2);
        now += 300;
        assertFalse(service.loaded(a, pack1));
        assertFalse(service.loaded(a, pack2));
    }

    @Test
    void unaSolaVoltaPerSessione() {
        service.accepted(a, pack1);
        assertTrue(service.loaded(a, pack1));
        service.accepted(a, pack2);
        assertFalse(service.loaded(a, pack2));
    }

    @Test
    void forgetResetta() {
        service.accepted(a, pack1);
        assertTrue(service.loaded(a, pack1));
        service.forget(a);
        assertEquals(-1L, service.takePending(a));
        service.accepted(a, pack1);
        assertTrue(service.loaded(a, pack1));
    }

    @Test
    void playerIndipendenti() {
        service.accepted(a, pack1);
        assertFalse(service.loaded(b, pack1));
        assertTrue(service.loaded(a, pack1));
    }

    @Test
    void packIdNulloGestito() {
        service.accepted(a, null);
        assertTrue(service.loaded(a, null));
    }
}
