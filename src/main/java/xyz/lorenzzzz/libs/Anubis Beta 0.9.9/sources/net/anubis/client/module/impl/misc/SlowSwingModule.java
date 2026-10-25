package net.anubis.client.module.impl.misc;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SlowSwingModule.class */
@Environment(EnvType.CLIENT)
public final class SlowSwingModule extends ClientModule {
    private final NumberSetting speed;

    public SlowSwingModule() {
        super("slow-swing", "Slow Swing", "Control hand swing animation speed.", ModuleCategory.MISC);
        this.speed = (NumberSetting) addSetting("speed", new NumberSetting("Speed", 0.25f, 0.1f, 2.0f).step(0.05f));
    }

    public int getModifiedSwingDuration(int vanilla) {
        float s = this.speed.get();
        if (s <= 0.0f) {
            s = 0.1f;
        }
        int modified = Math.round(vanilla / s);
        return Math.max(1, modified);
    }
}
