package net.anubis.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/ActionSetting.class */
@Environment(EnvType.CLIENT)
public final class ActionSetting extends Setting {
    private final Runnable action;

    public ActionSetting(String name, Runnable action) {
        super(name);
        this.action = action;
    }

    public void run() {
        this.action.run();
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        return JsonNull.INSTANCE;
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
    }
}
