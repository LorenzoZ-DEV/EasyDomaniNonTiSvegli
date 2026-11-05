package xyz.lorenzzzz.easydomani.punishment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import xyz.lorenzzzz.easydomani.punishment.model.PunishmentRecord;

public final class PunishmentRepository {
    private static final int MAX_NAME = 32;
    private static final int MAX_MOD = 64;

    private final DataSource dataSource;

    public PunishmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createTable() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS punishments (
                         uuid     UUID        PRIMARY KEY,
                         name     VARCHAR(32) NOT NULL,
                         count    INT         NOT NULL,
                         last_ms  BIGINT      NOT NULL,
                         last_mod VARCHAR(64) NOT NULL
                     )""")) {
            ps.executeUpdate();
        }
    }

    public PunishmentRecord find(UUID uuid) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT name, count, last_ms, last_mod FROM punishments WHERE uuid = ?")) {
            ps.setObject(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new PunishmentRecord(uuid, rs.getString(1), rs.getInt(2), rs.getLong(3), rs.getString(4)) : null;
            }
        }
    }

    public void upsert(PunishmentRecord e) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "MERGE INTO punishments (uuid, name, count, last_ms, last_mod) KEY (uuid) VALUES (?, ?, ?, ?, ?)")) {
            ps.setObject(1, e.uuid());
            ps.setString(2, truncate(e.name(), MAX_NAME));
            ps.setInt(3, e.count());
            ps.setLong(4, e.lastMs());
            ps.setString(5, truncate(e.lastMod(), MAX_MOD));
            ps.executeUpdate();
        }
    }

    public void delete(UUID uuid) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM punishments WHERE uuid = ?")) {
            ps.setObject(1, uuid);
            ps.executeUpdate();
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "-";
        return s.length() <= max ? s : s.substring(0, max);
    }
}
