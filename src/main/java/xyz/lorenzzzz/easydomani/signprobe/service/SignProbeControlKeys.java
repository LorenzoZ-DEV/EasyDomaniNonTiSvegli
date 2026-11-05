package xyz.lorenzzzz.easydomani.signprobe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.packprobe.service.ProbePackArchiveBuilder;

final class SignProbeControlKeys {
    static final String CTRL_FALLBACK_TEXT = "FB_TEST";
    static final ModProfile CTRL_TRANSLATE = control("ctrl-translate", "key.jump", false);
    static final ModProfile CTRL_KEYBIND = control("ctrl-keybind", "key.jump", true);
    static final ModProfile CTRL_FALLBACK = control("ctrl-fallback", "key.anubis.open_gui", false);
    static final List<ModProfile> CONTROLS = List.of(CTRL_TRANSLATE, CTRL_KEYBIND, CTRL_FALLBACK);

    static final String CTRL_PACK_ID = "ctrl-pack";
    static final String CTRL_OVR_TRANSLATE_ID = "ctrl-ovr-translate", CTRL_OVR_KEYBIND_ID = "ctrl-ovr-keybind";
    private static final Set<String> PACK_CONTROL_IDS = Set.of(CTRL_PACK_ID, CTRL_OVR_TRANSLATE_ID, CTRL_OVR_KEYBIND_ID);

    private SignProbeControlKeys() {
    }

    static List<ModProfile> packControls(String expected, String overrideKey, String overrideValue) {
        List<ModProfile> out = new ArrayList<>(3);
        out.add(packControl(CTRL_PACK_ID, ProbePackArchiveBuilder.KEY, expected, false));
        if (overrideKey != null) {
            out.add(packControl(CTRL_OVR_TRANSLATE_ID, overrideKey, overrideValue, false));
            out.add(packControl(CTRL_OVR_KEYBIND_ID, overrideKey, overrideValue, true));
        }
        return out;
    }

    static boolean isControl(ModProfile def) {
        return CONTROLS.contains(def) || PACK_CONTROL_IDS.contains(def.id());
    }

    static boolean hasFallback(ModProfile def) {
        return CTRL_FALLBACK.equals(def) || CTRL_PACK_ID.equals(def.id()) || CTRL_OVR_TRANSLATE_ID.equals(def.id());
    }

    static boolean allControls(List<ModProfile> group) {
        for (ModProfile d : group) {
            if (!isControl(d)) return false;
        }
        return true;
    }

    static List<String> describeKeys(List<ModProfile> group) {
        List<String> out = new ArrayList<>();
        for (ModProfile d : group) out.add(isControl(d) ? d.id() + ":" + d.probeKey() : d.probeKey());
        return out;
    }

    private static ModProfile control(String id, String key, boolean keybind) {
        return new ModProfile(id, id, false, List.of(), List.of(), List.of(), key, List.of(), false, keybind);
    }

    private static ModProfile packControl(String id, String key, String expected, boolean keybind) {
        return new ModProfile(id, id, false, List.of(), List.of(), List.of(), key, List.of(expected), false, keybind);
    }
}
