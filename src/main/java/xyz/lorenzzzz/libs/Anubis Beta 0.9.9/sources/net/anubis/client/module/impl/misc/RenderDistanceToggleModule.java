package net.anubis.client.module.impl.misc;

import net.anubis.client.event.EventHandler;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/RenderDistanceToggleModule.class */
@Environment(EnvType.CLIENT)
public final class RenderDistanceToggleModule extends ClientModule {
    private static final int RESTORE_AFTER_TICKS = 10;
    private static final int REARM_MARGIN = 2;
    private final NumberSetting lowDistance;
    private final NumberSetting resetY;
    private boolean armed;
    private boolean restoring;
    private int timer;
    private int savedDistance;

    public RenderDistanceToggleModule() {
        super("render-distance-toggle", "Render Distance Toggle", "Reloads chunks when you go below a Y level.", ModuleCategory.BASE_RAID);
        this.lowDistance = (NumberSetting) addSetting("low-distance", new NumberSetting("Low Distance", 2.0f, 2.0f, 32.0f).step(1.0f));
        this.resetY = (NumberSetting) addSetting("reset-y", new NumberSetting("Reset Y", -1.0f, -64.0f, 320.0f).step(1.0f));
        this.armed = true;
        this.savedDistance = -1;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.armed = true;
        this.restoring = false;
        this.timer = 0;
        this.savedDistance = -1;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        if (this.restoring) {
            restore(class_310.method_1551());
        }
    }

    @EventHandler(stage = Stage.POST)
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null) {
            if (this.restoring) {
                restore(mc);
                return;
            }
            return;
        }
        int y = mc.field_1724.method_31478();
        if (!this.restoring && y > this.resetY.getInt() + 2) {
            this.armed = true;
        }
        if (this.armed && y <= this.resetY.getInt()) {
            this.armed = false;
            this.restoring = true;
            this.timer = 0;
            this.savedDistance = ((Integer) mc.field_1690.method_42503().method_41753()).intValue();
            apply(mc, this.lowDistance.getInt());
            return;
        }
        if (this.restoring) {
            int i = this.timer + 1;
            this.timer = i;
            if (i >= 10) {
                restore(mc);
            }
        }
    }

    private void restore(class_310 mc) {
        if (this.savedDistance >= 0) {
            apply(mc, this.savedDistance);
        }
        this.restoring = false;
    }

    private static void apply(class_310 mc, int chunks) {
        mc.field_1690.method_42503().method_41748(Integer.valueOf(chunks));
        mc.field_1690.method_1643();
    }
}
