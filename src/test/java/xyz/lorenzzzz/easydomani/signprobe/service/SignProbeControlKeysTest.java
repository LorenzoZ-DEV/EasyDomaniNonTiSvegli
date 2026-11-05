package xyz.lorenzzzz.easydomani.signprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;

class SignProbeControlKeysTest {

    private static List<String> ids(List<ModProfile> defs) {
        List<String> out = new ArrayList<>();
        for (ModProfile d : defs) out.add(d.id());
        return out;
    }

    private static List<String> firstExpected(List<ModProfile> defs) {
        List<String> out = new ArrayList<>();
        for (ModProfile d : defs) out.add(d.probeExpected().get(0));
        return out;
    }

    @Test
    void ctrlPack() {
        List<ModProfile> defs = SignProbeControlKeys.packControls("edn-abc123", "key.anubis.open_gui", "edn-abc123-a");
        assertEquals(List.of("ctrl-pack", "ctrl-ovr-translate", "ctrl-ovr-keybind"), ids(defs));
        for (ModProfile d : defs) assertTrue(SignProbeControlKeys.isControl(d));
        assertEquals(List.of("edn-abc123", "edn-abc123-a", "edn-abc123-a"), firstExpected(defs));
        assertEquals("{\"translate\":\"edn.probe\",\"fallback\":\"FB_TEST\"}", SignProbeNbtWriter.keyJson(defs.get(0)));
        assertEquals("{\"translate\":\"key.anubis.open_gui\",\"fallback\":\"FB_TEST\"}", SignProbeNbtWriter.keyJson(defs.get(1)));
        assertEquals("{\"keybind\":\"key.anubis.open_gui\"}", SignProbeNbtWriter.keyJson(defs.get(2)));
        NBTList<NBTCompound> m = SignProbeNbtWriter.buildSignNbt(defs, "edn-s", true).getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        assertEquals("edn.probe", m.getTag(0).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(0).getStringTagValueOrNull("fallback"));
        assertEquals("key.anubis.open_gui", m.getTag(1).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(1).getStringTagValueOrNull("fallback"));
        assertEquals("key.anubis.open_gui", m.getTag(2).getStringTagValueOrNull("keybind"));
        assertNull(m.getTag(2).getStringTagValueOrNull("fallback"));
        assertEquals("edn-s", m.getTag(3).getStringTagValueOrNull("text"));

        assertEquals(1, SignProbeControlKeys.packControls("edn-abc123", null, null).size());
    }

    @Test
    void controlliEDescrizione() {
        assertTrue(SignProbeControlKeys.allControls(SignProbeControlKeys.CONTROLS));
        assertFalse(SignProbeControlKeys.allControls(List.of(SignProbeControlKeys.CTRL_TRANSLATE, SignProbeResponseClassifierTest.ANUBIS)));
        assertFalse(SignProbeControlKeys.isControl(SignProbeResponseClassifierTest.ANUBIS));
        assertEquals(List.of("ctrl-translate:key.jump", "key.anubis.open_gui"),
                SignProbeControlKeys.describeKeys(List.of(SignProbeControlKeys.CTRL_TRANSLATE, SignProbeResponseClassifierTest.ANUBIS)));
    }
}
