package net.anubis.client.module.impl.render;

import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TimeChangerModule.class */
@Environment(EnvType.CLIENT)
public final class TimeChangerModule extends ClientModule {
    public static volatile TimeChangerModule active;
    private final NumberSetting time;
    private class_638 trackedLevel;
    private long serverDayTime;
    private long appliedDayTime;

    public TimeChangerModule() {
        super("time-changer", "Time Changer", "Makes you able to set a custom time.", ModuleCategory.RENDER);
        this.time = (NumberSetting) addSetting("time", new NumberSetting("Time", 0.0f, -20000.0f, 20000.0f).step(1.0f));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
        class_638 level = class_310.method_1551().field_1687;
        if (level != null) {
            applyForcedTime(level);
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        class_638 level = class_310.method_1551().field_1687;
        if (level != null && level == this.trackedLevel) {
            writeDayTime(level, currentServerDayTime(level));
        }
        this.trackedLevel = null;
    }

    private void onPostTick(TickEvent event) {
        class_638 level = class_310.method_1551().field_1687;
        if (level == null) {
            this.trackedLevel = null;
        } else {
            applyForcedTime(level);
        }
    }

    private void applyForcedTime(class_638 level) {
        this.serverDayTime = level == this.trackedLevel ? currentServerDayTime(level) : level.method_8532();
        this.trackedLevel = level;
        this.appliedDayTime = forcedDayTime();
        writeDayTime(level, this.appliedDayTime);
    }

    private static void writeDayTime(class_638 level, long dayTime) {
        if (level.method_8532() == dayTime) {
            return;
        }
        level.method_28104().method_165(dayTime);
        level.method_75728().method_76405();
    }

    private long currentServerDayTime(class_638 level) {
        return this.serverDayTime + (level.method_8532() - this.appliedDayTime);
    }

    public long interceptServerDayTime(class_638 level, long dayTime) {
        this.trackedLevel = level;
        this.serverDayTime = dayTime;
        this.appliedDayTime = forcedDayTime();
        return this.appliedDayTime;
    }

    private long forcedDayTime() {
        return (long) this.time.get();
    }
}
