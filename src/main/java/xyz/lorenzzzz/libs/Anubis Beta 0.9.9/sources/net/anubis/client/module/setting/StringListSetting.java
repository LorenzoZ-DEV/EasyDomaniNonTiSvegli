package net.anubis.client.module.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import imgui.type.ImString;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/StringListSetting.class */
@Environment(EnvType.CLIENT)
public final class StringListSetting extends Setting {
    public static final int MAX_ENTRIES = 32;
    private static final int ENTRY_CAPACITY = 256;
    private static final char LEGACY_SEPARATOR = '|';
    private final List<ImString> entries;
    private List<String> values;

    public StringListSetting(String name, String... defaults) {
        super(name);
        this.entries = new ArrayList();
        for (String value : defaults) {
            this.entries.add(new ImString(value, 256));
        }
    }

    public List<String> get() {
        if (this.values == null) {
            List<String> out = new ArrayList<>(this.entries.size());
            for (ImString entry : this.entries) {
                String value = entry.get().trim();
                if (!value.isEmpty()) {
                    out.add(value);
                }
            }
            this.values = List.copyOf(out);
        }
        return this.values;
    }

    public int size() {
        return this.entries.size();
    }

    public ImString entry(int index) {
        return this.entries.get(index);
    }

    public boolean canAdd() {
        return this.entries.size() < 32;
    }

    public void add() {
        if (canAdd()) {
            this.entries.add(new ImString("", 256));
            changed();
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= this.entries.size()) {
            return;
        }
        this.entries.remove(index);
        changed();
    }

    public void edited() {
        changed();
    }

    private void changed() {
        this.values = null;
        notifyChanged();
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        JsonArray out = new JsonArray();
        for (ImString entry : this.entries) {
            out.add(entry.get());
        }
        return out;
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        List<String> loaded;
        if (element == null) {
            return;
        }
        if (element.isJsonArray()) {
            loaded = new ArrayList();
            for (JsonElement item : element.getAsJsonArray()) {
                if (item != null && item.isJsonPrimitive()) {
                    loaded.add(item.getAsString());
                }
            }
        } else if (element.isJsonPrimitive()) {
            loaded = splitLegacy(element.getAsString());
        } else {
            return;
        }
        this.entries.clear();
        for (int i = 0; i < loaded.size() && i < 32; i++) {
            this.entries.add(new ImString(loaded.get(i), 256));
        }
        this.values = null;
    }

    private static List<String> splitLegacy(String raw) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int i = 0;
        while (i < raw.length()) {
            char c = raw.charAt(i);
            if (c == '\\' && i + 1 < raw.length() && raw.charAt(i + 1) == LEGACY_SEPARATOR) {
                current.append('|');
                i++;
            } else if (c == LEGACY_SEPARATOR) {
                addTrimmed(out, current);
                current.setLength(0);
            } else {
                current.append(c);
            }
            i++;
        }
        addTrimmed(out, current);
        return out;
    }

    private static void addTrimmed(List<String> out, CharSequence entry) {
        String value = entry.toString().trim();
        if (!value.isEmpty()) {
            out.add(value);
        }
    }
}
