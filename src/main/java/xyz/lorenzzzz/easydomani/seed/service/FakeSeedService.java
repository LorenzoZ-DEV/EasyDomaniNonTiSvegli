package xyz.lorenzzzz.easydomani.seed.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;

public final class FakeSeedService {
    private final Logger logger;
    private final BooleanSupplier debug;
    private final Random random;
    private final Map<String, Long> fakeSeeds = new ConcurrentHashMap<>();
    private volatile boolean enabled;

    public FakeSeedService(Logger logger, BooleanSupplier debug, Random random) {
        this.logger = logger;
        this.debug = debug;
        this.random = random;
    }

    public void reload(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long fakeSeed(String world) {
        return fakeSeeds.computeIfAbsent(world == null ? "" : world, k -> {
            long seed = random.nextLong();
            if (debug.getAsBoolean()) logger.info("[Seed] hashed seed finto per " + k);
            return seed;
        });
    }
}
