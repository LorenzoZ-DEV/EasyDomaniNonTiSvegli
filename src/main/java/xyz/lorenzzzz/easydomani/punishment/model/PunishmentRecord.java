package xyz.lorenzzzz.easydomani.punishment.model;

import java.util.UUID;

public record PunishmentRecord(UUID uuid, String name, int count, long lastMs, String lastMod) {
    public static PunishmentRecord empty(UUID uuid, String name) {
        return new PunishmentRecord(uuid, name, 0, 0L, "-");
    }
}
