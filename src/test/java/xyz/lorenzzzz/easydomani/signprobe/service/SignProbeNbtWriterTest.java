package xyz.lorenzzzz.easydomani.signprobe.service;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.lorenzzzz.easydomani.signprobe.service.SignProbeResponseClassifierTest.*;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.util.List;
import org.junit.jupiter.api.Test;

class SignProbeNbtWriterTest {

    @Test
    void keybind() {
        assertEquals("{\"keybind\":\"key.anubis.open_gui\"}", SignProbeNbtWriter.keyJson(KB));
        NBTCompound modern = SignProbeNbtWriter.buildSignNbt(List.of(KB), S, true);
        assertEquals("key.anubis.open_gui", modern.getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages").getTag(0).getStringTagValueOrNull("keybind"));
    }

    @Test
    void bypassMeteor() {
        assertEquals("{\"translate\":\"%s\",\"with\":[{\"translate\":\"key.category.meteor-client.meteor-client\"}]}", SignProbeNbtWriter.keyJson(METEOR));
        assertEquals("{\"translate\":\"key.anubis.open_gui\"}", SignProbeNbtWriter.keyJson(ANUBIS));
    }

    @Test
    void chiaviDiControllo() {
        assertEquals("{\"translate\":\"key.jump\"}", SignProbeNbtWriter.keyJson(SignProbeControlKeys.CTRL_TRANSLATE));
        assertEquals("{\"keybind\":\"key.jump\"}", SignProbeNbtWriter.keyJson(SignProbeControlKeys.CTRL_KEYBIND));
        assertEquals("{\"translate\":\"key.anubis.open_gui\",\"fallback\":\"FB_TEST\"}", SignProbeNbtWriter.keyJson(SignProbeControlKeys.CTRL_FALLBACK));
        NBTList<NBTCompound> m = SignProbeNbtWriter.buildSignNbt(SignProbeControlKeys.CONTROLS, S, true)
                .getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        assertEquals("key.jump", m.getTag(0).getStringTagValueOrNull("translate"));
        assertNull(m.getTag(0).getStringTagValueOrNull("fallback"));
        assertEquals("key.jump", m.getTag(1).getStringTagValueOrNull("keybind"));
        assertEquals("key.anubis.open_gui", m.getTag(2).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(2).getStringTagValueOrNull("fallback"));
        assertEquals(S, m.getTag(3).getStringTagValueOrNull("text"));

        assertNull(SignProbeNbtWriter.buildSignNbt(List.of(ANUBIS), S, true)
                .getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages").getTag(0).getStringTagValueOrNull("fallback"));
    }

    @Test
    void nbtCartello() {
        NBTCompound legacy = SignProbeNbtWriter.buildSignNbt(List.of(ANUBIS, METEOR), S, false);
        NBTList<NBTString> lines = legacy.getCompoundTagOrNull("front_text").getStringListTagOrNull("messages");
        assertEquals(4, lines.size());
        assertEquals("{\"translate\":\"key.anubis.open_gui\"}", lines.getTag(0).getValue());
        assertEquals("{\"text\":\"" + S + "\"}", lines.getTag(3).getValue());

        NBTCompound modern = SignProbeNbtWriter.buildSignNbt(List.of(METEOR), S, true);
        NBTList<NBTCompound> mLines = modern.getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        NBTCompound first = mLines.getTag(0);
        assertEquals("%s", first.getStringTagValueOrNull("translate"));
        assertEquals(S, mLines.getTag(3).getStringTagValueOrNull("text"));
    }
}
