package net.anubis.client.module.impl.misc;

import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/HomeMetaModule.class */
@Environment(EnvType.CLIENT)
public final class HomeMetaModule extends ClientModule {
    private static final double TELEPORT_DISTANCE_SQ = 25.0d;
    private final NumberSetting homeNumber;
    private final NumberSetting delay;
    private final NumberSetting rtpTimeout;
    private Step step;
    private long nextAtMs;
    private long rtpDeadlineMs;
    private class_243 rtpStart;
    private int home;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/HomeMetaModule$Step.class */
    @Environment(EnvType.CLIENT)
    private enum Step {
        DELHOME,
        SETHOME,
        RTP,
        WAIT_TELEPORT,
        HOME
    }

    public HomeMetaModule() {
        super("home-meta", "Home Meta", "Re-sets a home, RTPs away, then teleports back home.", ModuleCategory.BASE_RAID);
        this.homeNumber = (NumberSetting) addSetting("home-number", new NumberSetting("Home Number", 3.0f, 1.0f, 10.0f).step(1.0f));
        this.delay = (NumberSetting) addSetting("delay", new NumberSetting("Delay (s)", 0.5f, 0.0f, 10.0f).step(0.5f));
        this.rtpTimeout = (NumberSetting) addSetting("rtp-max-timeout", new NumberSetting("RTP Timeout (s)", 10.0f, 1.0f, 30.0f).step(1.0f));
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean inConfigs() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        if (class_310.method_1551().field_1724 == null) {
            requestDisable();
            return;
        }
        this.home = this.homeNumber.getInt();
        this.step = Step.DELHOME;
        this.nextAtMs = 0L;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.method_1562() == null) {
            requestDisable();
            return;
        }
        long now = System.currentTimeMillis();
        if (now < this.nextAtMs) {
        }
        long delayMs = (long) (((double) this.delay.get()) * 1000.0d);
        switch (this.step) {
            case DELHOME:
                command(mc, "delhome " + this.home);
                this.step = Step.SETHOME;
                this.nextAtMs = now + delayMs;
                break;
            case SETHOME:
                command(mc, "sethome " + this.home);
                this.step = Step.RTP;
                this.nextAtMs = now + delayMs;
                break;
            case RTP:
                this.rtpStart = mc.field_1724.method_73189();
                command(mc, "rtp");
                this.rtpDeadlineMs = now + ((long) (((double) this.rtpTimeout.get()) * 1000.0d));
                this.step = Step.WAIT_TELEPORT;
                break;
            case WAIT_TELEPORT:
                boolean moved = mc.field_1724.method_73189().method_1025(this.rtpStart) > TELEPORT_DISTANCE_SQ;
                if (moved || now >= this.rtpDeadlineMs) {
                    this.step = Step.HOME;
                    this.nextAtMs = now + delayMs;
                }
                break;
            case HOME:
                command(mc, "home " + this.home);
                requestDisable();
                break;
        }
    }

    private static void command(class_310 mc, String command) {
        mc.method_1562().method_45730(command);
    }
}
