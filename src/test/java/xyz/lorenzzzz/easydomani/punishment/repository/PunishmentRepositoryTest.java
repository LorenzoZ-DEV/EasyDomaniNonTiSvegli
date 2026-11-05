package xyz.lorenzzzz.easydomani.punishment.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.UUID;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.punishment.model.PunishmentRecord;

class PunishmentRepositoryTest {
    private HikariDataSource ds;
    private PunishmentRepository repo;

    @BeforeEach
    void open() throws Exception {
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        HikariConfig cfg = new HikariConfig();
        cfg.setDataSource(h2);
        cfg.setMaximumPoolSize(2);
        ds = new HikariDataSource(cfg);
        repo = new PunishmentRepository(ds);
        repo.createTable();
        repo.createTable();
    }

    @AfterEach
    void close() {
        ds.close();
    }

    @Test
    void nessunaSanzione() throws Exception {
        assertNull(repo.find(UUID.randomUUID()));
    }

    @Test
    void contatoreSaleEPersiste() throws Exception {
        UUID id = UUID.randomUUID();
        repo.upsert(new PunishmentRecord(id, "Steve", 1, 1000L, "Meteor Client"));
        repo.upsert(new PunishmentRecord(id, "Steve", 2, 2000L, "Anubis"));
        PunishmentRecord e = repo.find(id);
        assertEquals(2, e.count());
        assertEquals(2000L, e.lastMs());
        assertEquals("Anubis", e.lastMod());
        assertEquals("Steve", e.name());
    }

    @Test
    void reset() throws Exception {
        UUID id = UUID.randomUUID();
        repo.upsert(new PunishmentRecord(id, "Alex", 3, 1L, "Wurst Client"));
        repo.delete(id);
        assertNull(repo.find(id));
        repo.delete(id);
    }

    @Test
    void campiTroncati() throws Exception {
        UUID id = UUID.randomUUID();
        repo.upsert(new PunishmentRecord(id, "n".repeat(100), 1, 1L, "m".repeat(200)));
        PunishmentRecord e = repo.find(id);
        assertEquals(32, e.name().length());
        assertEquals(64, e.lastMod().length());
    }
}
