package xyz.lorenzzzz.easydomani.signprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import com.github.retrooper.packetevents.util.Vector3i;
import java.util.List;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.signprobe.model.ActiveSignProbe;

class SignProbeSessionTest {
    static final ModProfile QUEUED = profile("queued");
    static final ModProfile FIRST_PENDING = profile("first-pending");
    static final ModProfile SECOND_PENDING = profile("second-pending");

    @Test
    void cambioMondoChiudeLaProbeERimetteInRetryLePendenti() {
        SignProbeService.Session session = new SignProbeService.Session(List.of(QUEUED));
        ActiveSignProbe probe = probe(FIRST_PENDING, SECOND_PENDING);
        session.active = probe;

        assertTrue(session.abandonActive());

        assertNull(session.active);
        assertSame(probe, session.late);
        assertEquals(List.of(QUEUED, FIRST_PENDING, SECOND_PENDING), List.copyOf(session.queue));
        assertFalse(session.close(probe));
    }

    @Test
    void senzaProbeAttivaNonCambiaNulla() {
        SignProbeService.Session session = new SignProbeService.Session(List.of(QUEUED));

        assertFalse(session.abandonActive());

        assertNull(session.late);
        assertEquals(List.of(QUEUED), List.copyOf(session.queue));
    }

    @Test
    void secondoCambioMondoNonDuplicaIRetry() {
        SignProbeService.Session session = new SignProbeService.Session(List.of());
        session.active = probe(FIRST_PENDING);
        assertTrue(session.abandonActive());
        session.queue.clear();

        session.active = probe(FIRST_PENDING);
        assertTrue(session.abandonActive());

        assertTrue(session.queue.isEmpty());
    }

    private static ActiveSignProbe probe(ModProfile... defs) {
        return new ActiveSignProbe(1L, new Vector3i(0, 64, 0), 0, "edn-test", List.of(defs), Long.MAX_VALUE);
    }

    private static ModProfile profile(String id) {
        return new ModProfile(id, id, true, List.of(), List.of(), List.of(), "key." + id, List.of(id), false, false);
    }
}
