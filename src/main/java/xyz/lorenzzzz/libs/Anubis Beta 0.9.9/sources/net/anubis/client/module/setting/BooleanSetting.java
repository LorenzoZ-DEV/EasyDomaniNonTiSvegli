package net.anubis.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import imgui.type.ImBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/BooleanSetting.class */
@Environment(EnvType.CLIENT)
public final class BooleanSetting extends Setting {
    private final ImBoolean value;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = new ImBoolean(value);
    }

    public boolean get() {
        return this.value.get();
    }

    public void set(boolean newValue) {
        if (this.value.get() == newValue) {
            return;
        }
        this.value.set(newValue);
        notifyChanged();
    }

    public ImBoolean value() {
        return this.value;
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        return new JsonPrimitive(Boolean.valueOf(this.value.get()));
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        try {
            this.value.set(element.getAsBoolean());
        } catch (RuntimeException e) {
        }
    }
}
