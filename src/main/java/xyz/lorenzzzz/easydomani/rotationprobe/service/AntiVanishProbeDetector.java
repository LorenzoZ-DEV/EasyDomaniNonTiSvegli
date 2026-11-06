package xyz.lorenzzzz.easydomani.rotationprobe.service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class AntiVanishProbeDetector {
    static final String ANTIVANISH_TEXT = "minecraft:msg ";
    static final int ANTIVANISH_MIN_ID = 30000;

    private final Set<UUID> alerted = ConcurrentHashMap.newKeySet();

    public boolean inspect(UUID uuid, int id, String text) {
        return text != null && isAntiVanishProbe(id, text) && alerted.add(uuid);
    }

    public void forget(UUID uuid) {
        alerted.remove(uuid);
    }

    static boolean isAntiVanishProbe(int id, String text) {
        return ANTIVANISH_TEXT.equals(text) || id >= ANTIVANISH_MIN_ID;
    }
}
