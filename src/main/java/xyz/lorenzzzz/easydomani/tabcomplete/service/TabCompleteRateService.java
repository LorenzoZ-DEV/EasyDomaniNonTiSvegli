package xyz.lorenzzzz.easydomani.tabcomplete.service;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;
import xyz.lorenzzzz.easydomani.tabcomplete.config.TabCompleteRateSettings;

public final class TabCompleteRateService {
    private final LongSupplier clock;
    private final Map<UUID, ArrayDeque<Long>> hits = new ConcurrentHashMap<>();
    private final Set<UUID> flagged = ConcurrentHashMap.newKeySet();
    private volatile TabCompleteRateSettings settings = TabCompleteRateSettings.disabled();

    public TabCompleteRateService(LongSupplier clock) {
        this.clock = clock;
    }

    public void reload(TabCompleteRateSettings settings) {
        this.settings = settings;
    }

    public TabCompleteRateSettings settings() {
        return settings;
    }

    public int record(UUID uuid) {
        TabCompleteRateSettings s = settings;
        long now = clock.getAsLong();
        int[] count = new int[1];
        hits.compute(uuid, (k, q) -> {
            ArrayDeque<Long> d = q != null ? q : new ArrayDeque<>();
            while (!d.isEmpty() && now - d.peekFirst() >= s.windowMs()) d.pollFirst();
            d.addLast(now);
            count[0] = d.size();
            return d;
        });
        return count[0] > s.maxPackets() && flagged.add(uuid) ? count[0] : 0;
    }

    public void forget(UUID uuid) {
        hits.remove(uuid);
        flagged.remove(uuid);
    }
}
