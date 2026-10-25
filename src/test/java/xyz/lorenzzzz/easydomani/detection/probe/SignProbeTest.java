package xyz.lorenzzzz.easydomani.detection.probe;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.lorenzzzz.easydomani.detection.probe.SignProbe.Result.*;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.util.List;
import xyz.lorenzzzz.easydomani.detection.ModProfile;
import org.junit.jupiter.api.Test;

class SignProbeTest {
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
        assertEquals(MATCH, SignProbe.classify(KB, S, "Right Shift", true));
        assertEquals(NO_MATCH, SignProbe.classify(KB, S, "key.anubis.open_gui", true));
        assertEquals(TAMPERED, SignProbe.classify(KB, S, "Right Shift", false));
        // con il pack override caricato un vanilla risponde il valore del pack = MATCH: per questo il pack parte
        // solo a fine coda e i MATCH con pack attivo non sanzionano (PackProbe#isPending)
        assertEquals(MATCH, SignProbe.classify(KB, S, "edn-abc123-a", true));
        assertEquals("{\"keybind\":\"key.anubis.open_gui\"}", SignProbe.keyJson(KB));
        NBTCompound modern = SignProbe.buildSignNbt(List.of(KB), S, true);
        assertEquals("key.anubis.open_gui", modern.getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages").getTag(0).getStringTagValueOrNull("keybind"));
    }

    @Test
    void traduzioneNotaMatch() {
        assertEquals(MATCH, SignProbe.classify(ANUBIS, S, "Open Anubis", true));
        assertEquals(MATCH, SignProbe.classify(ANUBIS, S, "§aOpen Anubis ", true));
        assertEquals(MATCH, SignProbe.classify(ANUBIS, S, "\"Open Anubis\"", false));
        assertEquals(MATCH, SignProbe.classify(XAERO, S, "Minimappa", true));
        assertEquals(MATCH, SignProbe.classify(METEOR, S, "Meteor Client", true));
    }

    @Test
    void clientVanillaRimandaLaKey() {
        assertEquals(NO_MATCH, SignProbe.classify(ANUBIS, S, "key.anubis.open_gui", true));
        assertEquals(NO_MATCH, SignProbe.classify(ANUBIS, S, "{\"translate\":\"key.anubis.open_gui\"}", true));
        assertEquals(NO_MATCH, SignProbe.classify(ANUBIS, S, S, true));
    }

    @Test
    void rispostaAnomala() {
        assertEquals(TAMPERED, SignProbe.classify(ANUBIS, S, "", true));
        assertEquals(TAMPERED, SignProbe.classify(ANUBIS, S, null, true));
        assertEquals(UNKNOWN, SignProbe.classify(ANUBIS, S, "qualcosa", true));
        assertEquals(TAMPERED, SignProbe.classify(ANUBIS, S, "qualcosa", false));
    }

    @Test
    void sentinella() {
        assertTrue(SignProbe.isIntact(new String[]{"a", "b", "c", S}, S));
        assertTrue(SignProbe.isIntact(new String[]{"a", "b", "c", "§f" + S}, S));
        assertFalse(SignProbe.isIntact(new String[]{"a", "b", "c", "altro"}, S));
        assertFalse(SignProbe.isIntact(new String[]{"a"}, S));
        assertFalse(SignProbe.isIntact(null, S));
    }

    @Test
    void bypassMeteor() {
        assertEquals("{\"translate\":\"%s\",\"with\":[{\"translate\":\"key.category.meteor-client.meteor-client\"}]}", SignProbe.keyJson(METEOR));
        assertEquals("{\"translate\":\"key.anubis.open_gui\"}", SignProbe.keyJson(ANUBIS));
    }

    @Test
    void chiaviDiControllo() {
        assertEquals("{\"translate\":\"key.jump\"}", SignProbe.keyJson(SignProbe.CTRL_TRANSLATE));
        assertEquals("{\"keybind\":\"key.jump\"}", SignProbe.keyJson(SignProbe.CTRL_KEYBIND));
        assertEquals("{\"translate\":\"key.anubis.open_gui\",\"fallback\":\"FB_TEST\"}", SignProbe.keyJson(SignProbe.CTRL_FALLBACK));
        NBTList<NBTCompound> m = SignProbe.buildSignNbt(SignProbe.CONTROLS, S, true)
                .getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        assertEquals("key.jump", m.getTag(0).getStringTagValueOrNull("translate"));
        assertNull(m.getTag(0).getStringTagValueOrNull("fallback"));
        assertEquals("key.jump", m.getTag(1).getStringTagValueOrNull("keybind"));
        assertEquals("key.anubis.open_gui", m.getTag(2).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(2).getStringTagValueOrNull("fallback"));
        assertEquals(S, m.getTag(3).getStringTagValueOrNull("text"));
        // i profili veri restano senza fallback
        assertNull(SignProbe.buildSignNbt(List.of(ANUBIS), S, true)
                .getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages").getTag(0).getStringTagValueOrNull("fallback"));
    }

    @Test
    void nbtCartello() {
        NBTCompound legacy = SignProbe.buildSignNbt(List.of(ANUBIS, METEOR), S, false);
        NBTList<NBTString> lines = legacy.getCompoundTagOrNull("front_text").getStringListTagOrNull("messages");
        assertEquals(4, lines.size());
        assertEquals("{\"translate\":\"key.anubis.open_gui\"}", lines.getTag(0).getValue());
        assertEquals("{\"text\":\"" + S + "\"}", lines.getTag(3).getValue());

        NBTCompound modern = SignProbe.buildSignNbt(List.of(METEOR), S, true);
        NBTList<NBTCompound> mLines = modern.getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        NBTCompound first = mLines.getTag(0);
        assertEquals("%s", first.getStringTagValueOrNull("translate"));
        assertEquals(S, mLines.getTag(3).getStringTagValueOrNull("text"));
    }
}
