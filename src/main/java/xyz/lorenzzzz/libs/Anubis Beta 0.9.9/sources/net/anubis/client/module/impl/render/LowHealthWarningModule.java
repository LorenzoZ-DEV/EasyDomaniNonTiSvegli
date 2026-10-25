package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import imgui.ImGui;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.gui.GuiPalette;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/LowHealthWarningModule.class */
@Environment(EnvType.CLIENT)
public final class LowHealthWarningModule extends ClientModule implements HudOverlayProvider {
    private static final String PULSE_HEARTBEAT = "Heartbeat";
    private static final String PULSE_BREATH = "Breath";
    private static final String PULSE_STEADY = "Steady";
    private static final String COLOR_THEME = "Theme";
    private static final String COLOR_MANUAL = "Manual";
    private static final int OPAQUE = -16777216;
    private static final int WHITE = -1;
    private static final float EDGE_LIFT = 0.5f;
    private static final float RISE_RATE = 5.0f;
    private static final float FALL_RATE = 2.6f;
    private static final float DEATH_RATE = 12.0f;
    private static final float CALM_BPM = 64.0f;
    private static final float PANIC_BPM = 150.0f;
    private static final float SECOND_BEAT = 0.2f;
    private static final float BEAT_WIDTH = 0.065f;
    private static final long GAP_NANOS = 250000000;
    private static final float TAU = 6.2831855f;
    private static final float[] BAND_REACH = {1.0f, 0.55f, 0.22f};
    private static final float[] BAND_WEIGHT = {0.5f, 0.34f, 0.3f};
    private final NumberSetting warnBelow;
    private final BooleanSetting countAbsorption;
    private final ModeSetting pulse;
    private final NumberSetting intensity;
    private final NumberSetting reach;
    private final BooleanSetting edgeLine;
    private final ModeSetting colorMode;
    private final ColorSetting glowColor;
    private final ColorSetting edgeColor;
    private float level;
    private float danger;
    private float phase;
    private long lastFrame;

    public LowHealthWarningModule() {
        super("low-health-warning", "Low Health Warning", "Flashes the screen edges when your health is low.", ModuleCategory.RENDER);
        this.warnBelow = (NumberSetting) addSetting("warn-below", new NumberSetting("Warn Below", 7.0f, 2.0f, 20.0f).step(1.0f));
        this.countAbsorption = (BooleanSetting) addSetting("count-absorption", new BooleanSetting("Count Absorption", true));
        this.pulse = (ModeSetting) addSetting("pulse", new ModeSetting("Pulse", PULSE_HEARTBEAT, PULSE_HEARTBEAT, PULSE_BREATH, PULSE_STEADY));
        this.intensity = (NumberSetting) addSetting("intensity", new NumberSetting("Intensity", 0.7f, SECOND_BEAT, 1.0f).step(0.05f));
        this.reach = (NumberSetting) addSetting("reach", new NumberSetting("Reach", 0.16f, 0.06f, 0.35f).step(0.01f));
        this.edgeLine = (BooleanSetting) addSetting("edge-line", new BooleanSetting("Edge Line", true));
        this.colorMode = (ModeSetting) addSetting("color", new ModeSetting("Color", "Theme", "Theme", "Manual"));
        this.glowColor = (ColorSetting) addSetting("glow-color", new ColorSetting("Glow Color", -13338378));
        this.edgeColor = (ColorSetting) addSetting("edge-color", new ColorSetting("Edge Color", -5650177));
        this.glowColor.visibleWhen(this::manual);
        this.edgeColor.visibleWhen(() -> {
            return this.edgeLine.get() && manual();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.level = 0.0f;
        this.danger = 0.0f;
        this.phase = 0.0f;
        this.lastFrame = 0L;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.level = 0.0f;
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        class_310 mc = class_310.method_1551();
        float dt = Math.min(Math.max(ImGui.getIO().getDeltaTime(), 0.0f), 0.1f);
        long now = System.nanoTime();
        if (now - this.lastFrame > GAP_NANOS) {
            this.level = 0.0f;
            this.phase = 0.0f;
        }
        this.lastFrame = now;
        class_746 player = mc.field_1724;
        boolean dead = player == null || player.method_29504();
        float want = 0.0f;
        if (!dead && !mc.field_1690.field_1842 && !player.method_7325() && !player.method_31549().field_7480) {
            float health = player.method_6032() + (this.countAbsorption.get() ? player.method_6067() : 0.0f);
            float limit = this.warnBelow.get();
            if (health < limit) {
                want = 1.0f;
                this.danger = Math.max(0.0f, Math.min(1.0f, (limit - health) / limit));
            }
        }
        float rate = dead ? DEATH_RATE : want > this.level ? 5.0f : FALL_RATE;
        this.level += (want - this.level) * (1.0f - ((float) Math.exp((-rate) * dt)));
        if (this.level < 0.003f && want == 0.0f) {
            this.level = 0.0f;
            this.phase = 0.0f;
            return;
        }
        float bpm = CALM_BPM + (86.0f * this.danger);
        if (this.pulse.is(PULSE_BREATH)) {
            bpm *= 0.5f;
        }
        this.phase = (this.phase + ((dt * bpm) / 60.0f)) % 1.0f;
        float beat = beat();
        float shown = this.level * this.level * (3.0f - (2.0f * this.level));
        float strength = this.intensity.get() * shown * (0.45f + (0.55f * this.danger));
        float depth = this.reach.get() * Math.min(screenW, screenH) * (1.0f + (0.12f * beat));
        boolean manual = manual();
        int accent = manual ? 0 : CosmeticAccent.argb();
        int base = GuiPalette.abgr(manual ? this.glowColor.argb() | (-16777216) : accent);
        for (int i = 0; i < BAND_REACH.length; i++) {
            float alpha = strength * BAND_WEIGHT[i] * (0.6f + (0.4f * beat));
            frame(dl, screenW, screenH, depth * BAND_REACH[i], withAlpha(base, alpha));
        }
        if (this.edgeLine.get()) {
            float line = Math.max(2.0f, depth * 0.07f);
            float alpha2 = this.intensity.get() * shown * (SECOND_BEAT + (0.55f * beat));
            int edge = manual ? this.edgeColor.argb() | (-16777216) : class_9848.method_61319(0.5f, accent, -1);
            frame(dl, screenW, screenH, line, EgyptDraw.col(edge, alpha2));
        }
    }

    private float beat() {
        if (this.pulse.is(PULSE_STEADY)) {
            return 0.35f;
        }
        return this.pulse.is(PULSE_BREATH) ? 0.5f - (0.5f * ((float) Math.cos(TAU * this.phase))) : Math.min(1.0f, bump(this.phase, 0.0f) + (0.62f * bump(this.phase, SECOND_BEAT)));
    }

    private static float bump(float at, float center) {
        float d = Math.abs(at - center);
        float d2 = Math.min(d, 1.0f - d) / BEAT_WIDTH;
        return (float) Math.exp((-d2) * d2);
    }

    private boolean manual() {
        return this.colorMode.is("Manual");
    }

    private static int withAlpha(int abgr, float alpha) {
        int a = Math.round(255.0f * Math.max(0.0f, Math.min(1.0f, alpha)));
        return (abgr & 16777215) | (a << 24);
    }

    private static void frame(ImDrawList dl, float w, float h, float depth, int edge) {
        if (depth < 1.0f || (edge >>> 24) == 0) {
            return;
        }
        int clear = edge & 16777215;
        dl.addRectFilledMultiColor(0.0f, 0.0f, w, depth, edge, edge, clear, clear);
        dl.addRectFilledMultiColor(0.0f, h - depth, w, h, clear, clear, edge, edge);
        dl.addRectFilledMultiColor(0.0f, 0.0f, depth, h, edge, clear, clear, edge);
        dl.addRectFilledMultiColor(w - depth, 0.0f, w, h, clear, edge, edge, clear);
    }
}
