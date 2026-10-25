package net.anubis.client.module.impl.misc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.util.LevelUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_238;
import net.minecraft.class_310;
import net.minecraft.class_634;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/RenderUtilModule.class */
@Environment(EnvType.CLIENT)
public final class RenderUtilModule extends ClientModule {
    private static final double MARKER_Y = 63.0d;
    private static final float LINE_WIDTH = 1.5f;
    private static final int CYAN = 65535;
    private static final int SOURCE_FILL_ALPHA = 40;
    private final BooleanSetting clearOnToggle;
    private final BooleanSetting highlightSources;
    private final ColorSetting fillColor;
    private final ColorSetting lineColor;
    private final Set<class_1923> flaggedChunks;
    private final Set<class_1923> sourceChunks;
    private class_634 session;

    public RenderUtilModule() {
        super("render-util", "Render Util", "Renders the chunks flagged by Quantum.", ModuleCategory.BASE_RAID);
        this.clearOnToggle = (BooleanSetting) addSetting("clear-chunks-on-toggle", new BooleanSetting("Clear On Toggle", true));
        this.highlightSources = (BooleanSetting) addSetting("render-flagged-chunk", new BooleanSetting("Highlight Source Chunks", true));
        this.fillColor = (ColorSetting) addSetting("side-color", new ColorSetting("Fill Color", 687810600));
        this.lineColor = (ColorSetting) addSetting("line-color", new ColorSetting("Line Color", -55256));
        this.flaggedChunks = new HashSet();
        this.sourceChunks = new HashSet();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        if (this.clearOnToggle.get()) {
            this.flaggedChunks.clear();
            this.sourceChunks.clear();
        }
    }

    private void ensureSession() {
        class_634 connection = class_310.method_1551().method_1562();
        if (connection == this.session) {
            return;
        }
        this.session = connection;
        this.flaggedChunks.clear();
        this.sourceChunks.clear();
    }

    public boolean hasFlaggedChunks() {
        ensureSession();
        return !this.flaggedChunks.isEmpty();
    }

    public boolean isFlagged(class_1923 pos) {
        ensureSession();
        return this.flaggedChunks.contains(pos);
    }

    public void addFlaggedChunks(Collection<class_1923> chunks) {
        ensureSession();
        this.flaggedChunks.addAll(chunks);
    }

    public void clearFlaggedChunks() {
        ensureSession();
        this.flaggedChunks.clear();
    }

    public void addSourceChunk(class_1923 pos) {
        ensureSession();
        this.sourceChunks.add(pos);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        ensureSession();
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || !LevelUtil.isOverworld(mc.field_1687)) {
            return;
        }
        class_12179 flagStyle = class_12179.method_75537(this.lineColor.argb(), LINE_WIDTH, this.fillColor.argb());
        boolean sources = this.highlightSources.get();
        for (class_1923 pos : this.flaggedChunks) {
            if (!sources || !this.sourceChunks.contains(pos)) {
                class_12180.method_75541(marker(pos), flagStyle).method_75533();
            }
        }
        if (sources) {
            class_12179 sourceStyle = class_12179.method_75537(-16711681, LINE_WIDTH, 671154175);
            Iterator<class_1923> it = this.sourceChunks.iterator();
            while (it.hasNext()) {
                class_12180.method_75541(marker(it.next()), sourceStyle).method_75533();
            }
        }
    }

    private static class_238 marker(class_1923 pos) {
        double x = ((double) pos.field_9181) * 16.0d;
        double z = ((double) pos.field_9180) * 16.0d;
        return new class_238(x, 63.0d, z, x + 16.0d, 63.0d, z + 16.0d);
    }
}
