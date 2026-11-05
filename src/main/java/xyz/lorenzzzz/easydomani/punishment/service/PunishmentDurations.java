package xyz.lorenzzzz.easydomani.punishment.service;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import xyz.lorenzzzz.easydomani.punishment.model.PunishmentRecord;

public final class PunishmentDurations {
    public static final Duration PERMANENT = Duration.ofSeconds(-1);
    private static final long MINUTE = 60, HOUR = 60 * MINUTE, DAY = 24 * HOUR, WEEK = 7 * DAY;

    private PunishmentDurations() {
    }

    public static int effectiveCount(PunishmentRecord e, Duration resetAfter, long now) {
        if (e == null || e.count() <= 0) return 0;
        if (!resetAfter.isZero() && e.lastMs() > 0 && now - e.lastMs() > resetAfter.toMillis()) return 0;
        return e.count();
    }

    public static Duration durationFor(List<Duration> ladder, int offense) {
        Duration d = ladder.get(Math.min(Math.max(offense, 1), ladder.size()) - 1);
        return d.isNegative() ? null : d;
    }

    public static String formatDuration(Duration d) {
        if (d == null) return "permanente";
        long s = d.getSeconds();
        if (s >= WEEK && s % WEEK == 0) return s / WEEK + "w";
        if (s >= DAY && s % DAY == 0) return s / DAY + "d";
        if (s >= HOUR && s % HOUR == 0) return s / HOUR + "h";
        if (s >= MINUTE && s % MINUTE == 0) return s / MINUTE + "m";
        return s + "s";
    }

    public static Duration parseDuration(String raw) {
        if (raw == null) return null;
        String t = raw.trim().toLowerCase(Locale.ROOT);
        if (t.isEmpty()) return null;
        if (t.equals("perm") || t.equals("permanent") || t.equals("-1")) return PERMANENT;
        if (t.equals("0")) return Duration.ZERO;
        try {
            long n = Long.parseLong(t.substring(0, t.length() - 1).trim());
            if (n <= 0 || n > 100_000) return null;
            return switch (t.charAt(t.length() - 1)) {
                case 's' -> Duration.ofSeconds(n);
                case 'm' -> Duration.ofMinutes(n);
                case 'h' -> Duration.ofHours(n);
                case 'd' -> Duration.ofDays(n);
                case 'w' -> Duration.ofDays(n * 7);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
