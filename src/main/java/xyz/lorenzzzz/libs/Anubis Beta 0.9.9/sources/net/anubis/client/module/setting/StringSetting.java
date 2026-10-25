package net.anubis.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import imgui.type.ImString;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/StringSetting.class */
@Environment(EnvType.CLIENT)
public final class StringSetting extends Setting {
    private final ImString imString;

    public StringSetting(String name, String defaultValue) {
        super(name);
        this.imString = new ImString(defaultValue, 512);
    }

    public String get() {
        return this.imString.get();
    }

    public void set(String newValue) {
        if (this.imString.get().equals(newValue)) {
            return;
        }
        this.imString.set(newValue);
        notifyChanged();
    }

    public String getValue() {
        return get();
    }

    public ImString getImString() {
        return this.imString;
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        return new JsonPrimitive(this.imString.get());
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        try {
            this.imString.set(element.getAsString());
        } catch (RuntimeException e) {
        }
    }
}
