package net.anubis.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.anubis.client.input.ModuleKeybinds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/KeySetting.class */
@Environment(EnvType.CLIENT)
public final class KeySetting extends Setting {
    private int value;

    public KeySetting(String name, int defaultKey) {
        super(name);
        this.value = defaultKey;
    }

    public int value() {
        return this.value;
    }

    public void setValue(int glfwKey) {
        if (this.value == glfwKey) {
            return;
        }
        this.value = glfwKey;
        notifyChanged();
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        return new JsonPrimitive(Integer.valueOf(this.value));
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        try {
            int key = element.getAsInt();
            if (key == -1 || ModuleKeybinds.isBindable(key)) {
                this.value = key;
            }
        } catch (RuntimeException e) {
        }
    }
}
