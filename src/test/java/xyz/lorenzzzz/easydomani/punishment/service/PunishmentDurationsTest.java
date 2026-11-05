package xyz.lorenzzzz.easydomani.punishment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.punishment.model.PunishmentRecord;

class PunishmentDurationsTest {

    @Test
    void parseDurataValida() {
        assertEquals(Duration.ofSeconds(30), PunishmentDurations.parseDuration("30s"));
        assertEquals(Duration.ofMinutes(10), PunishmentDurations.parseDuration("10m"));
        assertEquals(Duration.ofHours(12), PunishmentDurations.parseDuration(" 12H "));
        assertEquals(Duration.ofDays(3), PunishmentDurations.parseDuration("3d"));
        assertEquals(Duration.ofDays(14), PunishmentDurations.parseDuration("2w"));
        assertEquals(Duration.ZERO, PunishmentDurations.parseDuration("0"));
    }

    @Test
    void parsePermanente() {
        for (String s : new String[]{"perm", "permanent", "-1", "PERM"}) {
            assertTrue(PunishmentDurations.parseDuration(s).isNegative(), s);
        }
    }

    @Test
    void parseDurataNonValida() {
        for (String s : new String[]{null, "", "abc", "10x", "-5m", "0m", "m", "999999999999d"}) {
            assertNull(PunishmentDurations.parseDuration(s), String.valueOf(s));
        }
    }

    @Test
    void formatDurata() {
        assertEquals("10m", PunishmentDurations.formatDuration(Duration.ofMinutes(10)));
        assertEquals("12h", PunishmentDurations.formatDuration(Duration.ofHours(12)));
        assertEquals("3d", PunishmentDurations.formatDuration(Duration.ofDays(3)));
        assertEquals("2w", PunishmentDurations.formatDuration(Duration.ofDays(14)));
        assertEquals("90s", PunishmentDurations.formatDuration(Duration.ofSeconds(90)));
        assertEquals("permanente", PunishmentDurations.formatDuration(null));
    }

    static final List<Duration> LADDER = List.of(Duration.ofMinutes(10), Duration.ofHours(12), Duration.ofDays(3));

    @Test
    void ladderAScala() {
        assertEquals(Duration.ofMinutes(10), PunishmentDurations.durationFor(LADDER, 1));
        assertEquals(Duration.ofHours(12), PunishmentDurations.durationFor(LADDER, 2));
        assertEquals(Duration.ofDays(3), PunishmentDurations.durationFor(LADDER, 3));
        assertEquals(Duration.ofDays(3), PunishmentDurations.durationFor(LADDER, 7), "oltre l'ultimo si riusa l'ultimo");
        assertEquals(Duration.ofMinutes(10), PunishmentDurations.durationFor(LADDER, 0));
        assertNull(PunishmentDurations.durationFor(List.of(Duration.ofDays(1), PunishmentDurations.parseDuration("perm")), 2), "perm = null");
    }

    @Test
    void resetAfter() {
        UUID id = UUID.randomUUID();
        long now = 1_800_000_000_000L;
        var recent = new PunishmentRecord(id, "x", 2, now - 60_000, "Wurst");
        var old = new PunishmentRecord(id, "x", 2, now - Duration.ofDays(31).toMillis(), "Wurst");
        assertEquals(2, PunishmentDurations.effectiveCount(recent, Duration.ofDays(30), now));
        assertEquals(0, PunishmentDurations.effectiveCount(old, Duration.ofDays(30), now));
        assertEquals(2, PunishmentDurations.effectiveCount(old, Duration.ZERO, now), "0 = mai azzerato");
        assertEquals(0, PunishmentDurations.effectiveCount(null, Duration.ZERO, now));
    }
}
