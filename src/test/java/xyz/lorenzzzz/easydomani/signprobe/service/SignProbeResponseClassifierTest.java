package xyz.lorenzzzz.easydomani.signprobe.service;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.lorenzzzz.easydomani.signprobe.model.SignProbeResult.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;

class SignProbeResponseClassifierTest {
    static final String S = "edn-1234abcd";
    static final ModProfile ANUBIS = new ModProfile("anubis", "Anubis", true, List.of(), List.of(), List.of(),
            "key.anubis.open_gui", List.of("open anubis"), false, false);
    static final ModProfile METEOR = new ModProfile("meteor", "Meteor Client", true, List.of(), List.of(), List.of(),
            "key.category.meteor-client.meteor-client", List.of("meteor client"), true, false);
    static final ModProfile XAERO = new ModProfile("xaero-minimap", "Xaero's Minimap", false, List.of(), List.of(), List.of(),
            "gui.xaero_minimap", List.of("xaero's minimap", "minimappa"), false, false);
    static final ModProfile KB = new ModProfile("anubis", "Anubis", true, List.of(), List.of(), List.of(),
            "key.anubis.open_gui", List.of(), false, true);

    @Test
    void keybind() {
        assertEquals(MATCH, SignProbeResponseClassifier.classify(KB, S, "Right Shift", true));
        assertEquals(NO_MATCH, SignProbeResponseClassifier.classify(KB, S, "key.anubis.open_gui", true));
        assertEquals(TAMPERED, SignProbeResponseClassifier.classify(KB, S, "Right Shift", false));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(KB, S, "edn-abc123-a", true));
    }

    @Test
    void keybindRispostaNonPlausibile() {
        assertEquals(MATCH, SignProbeResponseClassifier.classify(KB, S, "Right Shift", true));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(KB, S, "x".repeat(32), true));
        assertEquals(UNKNOWN, SignProbeResponseClassifier.classify(KB, S, "x".repeat(33), true));
        assertEquals(UNKNOWN, SignProbeResponseClassifier.classify(KB, S, "pre key.anubis.open_gui", true));
        assertEquals(TAMPERED, SignProbeResponseClassifier.classify(KB, S, "x".repeat(33), false));
    }

    @Test
    void traduzioneNotaMatch() {
        assertEquals(MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, "Open Anubis", true));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, "§aOpen Anubis ", true));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, "\"Open Anubis\"", false));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(XAERO, S, "Minimappa", true));
        assertEquals(MATCH, SignProbeResponseClassifier.classify(METEOR, S, "Meteor Client", true));
    }

    @Test
    void clientVanillaRimandaLaKey() {
        assertEquals(NO_MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, "key.anubis.open_gui", true));
        assertEquals(NO_MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, "{\"translate\":\"key.anubis.open_gui\"}", true));
        assertEquals(NO_MATCH, SignProbeResponseClassifier.classify(ANUBIS, S, S, true));
    }

    @Test
    void rispostaAnomala() {
        assertEquals(TAMPERED, SignProbeResponseClassifier.classify(ANUBIS, S, "", true));
        assertEquals(TAMPERED, SignProbeResponseClassifier.classify(ANUBIS, S, null, true));
        assertEquals(UNKNOWN, SignProbeResponseClassifier.classify(ANUBIS, S, "qualcosa", true));
        assertEquals(TAMPERED, SignProbeResponseClassifier.classify(ANUBIS, S, "qualcosa", false));
    }

    @Test
    void sentinella() {
        assertTrue(SignProbeResponseClassifier.isIntact(new String[]{"a", "b", "c", S}, S));
        assertTrue(SignProbeResponseClassifier.isIntact(new String[]{"a", "b", "c", "§f" + S}, S));
        assertFalse(SignProbeResponseClassifier.isIntact(new String[]{"a", "b", "c", "altro"}, S));
        assertFalse(SignProbeResponseClassifier.isIntact(new String[]{"a"}, S));
        assertFalse(SignProbeResponseClassifier.isIntact(null, S));
    }
}
