package xyz.lorenzzzz.easydomani.packspoof.service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;
import xyz.lorenzzzz.easydomani.packspoof.config.PackSpoofSettings;

public final class PackSpoofService {
    private record Accepted(UUID pack, long at) {
    }

    private final LongSupplier clock;
    private final Map<UUID, Accepted> accepted = new ConcurrentHashMap<>();
    private final Map<UUID, Long> pending = new ConcurrentHashMap<>();
    private final Set<UUID> flagged = ConcurrentHashMap.newKeySet();
    private volatile PackSpoofSettings settings = PackSpoofSettings.disabled();

    public PackSpoofService(LongSupplier clock) {
        this.clock = clock;
    }

    public void reload(PackSpoofSettings settings) {
        this.settings = settings;
    }

    public PackSpoofSettings settings() {
        return settings;
    }

    public void accepted(UUID player, UUID pack) {
        if (settings.behindVelocity()) return;
        if (!flagged.contains(player)) accepted.put(player, new Accepted(pack, clock.getAsLong()));
    }

    public boolean loaded(UUID player, UUID pack) {
        Accepted a = accepted.get(player);
        if (a == null || !Objects.equals(a.pack(), pack) || !accepted.remove(player, a)) return false;
        long ms = clock.getAsLong() - a.at();
        if (ms > settings.maxMs() || !flagged.add(player)) return false;
        pending.put(player, ms);
        return true;
    }

    public long takePending(UUID player) {
        Long ms = pending.remove(player);
        return ms != null ? ms : -1L;
    }

    public void forget(UUID player) {
        accepted.remove(player);
        pending.remove(player);
        flagged.remove(player);
    }
}
