package xyz.lorenzzzz.easydomani.punishment;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PunishmentManagerTest {

    @Test
    void parseDurataValida() {
        assertEquals(Duration.ofSeconds(30), PunishmentManager.parseDuration("30s"));
        assertEquals(Duration.ofMinutes(10), PunishmentManager.parseDuration("10m"));
        assertEquals(Duration.ofHours(12), PunishmentManager.parseDuration(" 12H "));
        assertEquals(Duration.ofDays(3), PunishmentManager.parseDuration("3d"));
        assertEquals(Duration.ofDays(14), PunishmentManager.parseDuration("2w"));
        assertEquals(Duration.ZERO, PunishmentManager.parseDuration("0"));
    }

    @Test
    void parsePermanente() {
        for (String s : new String[]{"perm", "permanent", "-1", "PERM"}) {
            assertTrue(PunishmentManager.parseDuration(s).isNegative(), s);
        }
    }

    @Test
    void parseDurataNonValida() {
        for (String s : new String[]{null, "", "abc", "10x", "-5m", "0m", "m", "999999999999d"}) {
            assertNull(PunishmentManager.parseDuration(s), String.valueOf(s));
        }
    }

    @Test
    void formatDurata() {
        assertEquals("10m", PunishmentManager.formatDuration(Duration.ofMinutes(10)));
        assertEquals("12h", PunishmentManager.formatDuration(Duration.ofHours(12)));
        assertEquals("3d", PunishmentManager.formatDuration(Duration.ofDays(3)));
        assertEquals("2w", PunishmentManager.formatDuration(Duration.ofDays(14)));
        assertEquals("90s", PunishmentManager.formatDuration(Duration.ofSeconds(90)));
        assertEquals("permanente", PunishmentManager.formatDuration(null));
    }

    static final List<Duration> LADDER = List.of(Duration.ofMinutes(10), Duration.ofHours(12), Duration.ofDays(3));

    @Test
    void ladderAScala() {
        assertEquals(Duration.ofMinutes(10), PunishmentManager.durationFor(LADDER, 1));
        assertEquals(Duration.ofHours(12), PunishmentManager.durationFor(LADDER, 2));
        assertEquals(Duration.ofDays(3), PunishmentManager.durationFor(LADDER, 3));
        assertEquals(Duration.ofDays(3), PunishmentManager.durationFor(LADDER, 7), "oltre l'ultimo si riusa l'ultimo");
        assertEquals(Duration.ofMinutes(10), PunishmentManager.durationFor(LADDER, 0));
        assertNull(PunishmentManager.durationFor(List.of(Duration.ofDays(1), PunishmentManager.parseDuration("perm")), 2), "perm = null");
    }

    @Test
    void resetAfter() {
        UUID id = UUID.randomUUID();
        long now = 1_800_000_000_000L; // timestamp realistico (2027)
        var recent = new PunishmentManager.Entry(id, "x", 2, now - 60_000, "Wurst");
        var old = new PunishmentManager.Entry(id, "x", 2, now - Duration.ofDays(31).toMillis(), "Wurst");
        assertEquals(2, PunishmentManager.effectiveCount(recent, Duration.ofDays(30), now));
        assertEquals(0, PunishmentManager.effectiveCount(old, Duration.ofDays(30), now));
        assertEquals(2, PunishmentManager.effectiveCount(old, Duration.ZERO, now), "0 = mai azzerato");
        assertEquals(0, PunishmentManager.effectiveCount(null, Duration.ZERO, now));
    }
}
