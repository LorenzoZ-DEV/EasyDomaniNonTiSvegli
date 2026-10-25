package xyz.lorenzzzz.easydomani.punishment;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.UUID;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.punishment.PunishmentManager.Entry;

/** Stesso stack di produzione (Hikari + H2), ma in memoria. */
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
        repo.createTable(); // IF NOT EXISTS: idempotente
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
        repo.upsert(new Entry(id, "Steve", 1, 1000L, "Meteor Client"));
        repo.upsert(new Entry(id, "Steve", 2, 2000L, "Anubis"));
        Entry e = repo.find(id);
        assertEquals(2, e.count());
        assertEquals(2000L, e.lastMs());
        assertEquals("Anubis", e.lastMod());
        assertEquals("Steve", e.name());
    }

    @Test
    void reset() throws Exception {
        UUID id = UUID.randomUUID();
        repo.upsert(new Entry(id, "Alex", 3, 1L, "Wurst Client"));
        repo.delete(id);
        assertNull(repo.find(id));
        repo.delete(id); // cancellare due volte non e' un errore
    }

    @Test
    void campiTroncati() throws Exception {
        UUID id = UUID.randomUUID();
        repo.upsert(new Entry(id, "n".repeat(100), 1, 1L, "m".repeat(200)));
        Entry e = repo.find(id);
        assertEquals(32, e.name().length());
        assertEquals(64, e.lastMod().length());
    }
}
