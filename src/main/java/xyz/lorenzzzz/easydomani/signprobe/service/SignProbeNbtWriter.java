package xyz.lorenzzzz.easydomani.signprobe.service;

import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.util.List;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;

public final class SignProbeNbtWriter {

    private SignProbeNbtWriter() {
    }

    public static NBTCompound buildSignNbt(List<ModProfile> defs, String sentinel, boolean modern) {
        NBTCompound root = new NBTCompound();
        NBTCompound front = new NBTCompound();
        if (modern) {
            NBTList<NBTCompound> messages = new NBTList<>(NBTType.COMPOUND);
            for (int i = 0; i < 4; i++) {
                NBTCompound line = i < defs.size() ? keyComponent(defs.get(i)) : textComponent(i == 3 ? sentinel : "");
                messages.addTag(line);
                root.setTag("Text" + (i + 1), line);
            }
            front.setTag("messages", messages);
        } else {
            NBTList<NBTString> messages = new NBTList<>(NBTType.STRING);
            for (int i = 0; i < 4; i++) {
                NBTString line = new NBTString(i < defs.size() ? keyJson(defs.get(i)) : "{\"text\":\"" + (i == 3 ? sentinel : "") + "\"}");
                messages.addTag(line);
                root.setTag("Text" + (i + 1), line);
            }
            front.setTag("messages", messages);
        }
        root.setTag("front_text", front);
        root.setTag("is_waxed", new NBTByte((byte) 0));
        return root;
    }

    static String keyJson(ModProfile def) {
        if (def.probeKeybind()) return "{\"keybind\":\"" + def.probeKey() + "\"}";
        if (SignProbeControlKeys.hasFallback(def)) {
            return "{\"translate\":\"" + def.probeKey() + "\",\"fallback\":\"" + SignProbeControlKeys.CTRL_FALLBACK_TEXT + "\"}";
        }
        return def.probeBypass()
                ? "{\"translate\":\"%s\",\"with\":[{\"translate\":\"" + def.probeKey() + "\"}]}"
                : "{\"translate\":\"" + def.probeKey() + "\"}";
    }

    private static NBTCompound keyComponent(ModProfile def) {
        NBTCompound inner = new NBTCompound();
        inner.setTag(def.probeKeybind() ? "keybind" : "translate", new NBTString(def.probeKey()));
        if (def.probeKeybind()) return inner;
        if (SignProbeControlKeys.hasFallback(def)) inner.setTag("fallback", new NBTString(SignProbeControlKeys.CTRL_FALLBACK_TEXT));
        if (!def.probeBypass()) return inner;
        NBTCompound outer = new NBTCompound();
        outer.setTag("translate", new NBTString("%s"));
        NBTList<NBTCompound> with = new NBTList<>(NBTType.COMPOUND);
        with.addTag(inner);
        outer.setTag("with", with);
        return outer;
    }

    private static NBTCompound textComponent(String text) {
        NBTCompound c = new NBTCompound();
        c.setTag("text", new NBTString(text));
        return c;
    }
}
