package net.anubis.client.module.impl.render;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ZoomModule.class */
@Environment(EnvType.CLIENT)
public final class ZoomModule extends ClientModule {
    private static final float MIN_ZOOM = 1.1f;
    private static final float MAX_ZOOM = 50.0f;
    private static final float SCROLL_STEP = 1.2f;
    private static final float FAST_RATE = 30.0f;
    private static final float SLOW_RATE = 5.0f;
    private static final float MAX_FRAME_SECONDS = 0.1f;
    private static volatile ZoomModule instance;
    private static float target = 1.0f;
    private static float current = 1.0f;
    private static long lastNanos;
    private final NumberSetting zoom;
    private final NumberSetting smoothness;
    private final BooleanSetting scroll;
    private final BooleanSetting lowerSensitivity;
    private final BooleanSetting cinematic;
    private boolean smoothBefore;
    private boolean smoothPushed;

    public ZoomModule() {
        super("zoom", "Zoom", "Hold the key to zoom in.", ModuleCategory.VISUALS);
        this.zoom = (NumberSetting) addSetting("zoom", new NumberSetting("Zoom", 4.0f, 1.5f, 20.0f).step(0.5f));
        this.smoothness = (NumberSetting) addSetting("smoothness", new NumberSetting("Smoothness", 60.0f, 0.0f, 100.0f).step(5.0f));
        this.scroll = (BooleanSetting) addSetting("scroll", new BooleanSetting("Scroll", true));
        this.lowerSensitivity = (BooleanSetting) addSetting("lower-sensitivity", new BooleanSetting("Lower Sensitivity", true));
        this.cinematic = (BooleanSetting) addSetting("cinematic", new BooleanSetting("Cinematic Camera", false));
        setKeybind(67);
        setHoldMode(true);
        instance = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        target = this.zoom.get();
        if (this.cinematic.get()) {
            class_310 mc = class_310.method_1551();
            this.smoothBefore = mc.field_1690.field_1914;
            mc.field_1690.field_1914 = true;
            this.smoothPushed = true;
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        target = 1.0f;
        if (this.smoothPushed) {
            class_310.method_1551().field_1690.field_1914 = this.smoothBefore;
            this.smoothPushed = false;
        }
    }

    public static float fov(float fov) {
        float factor = advance();
        return factor > 1.0001f ? fov / factor : fov;
    }

    public static double turnScale() {
        ZoomModule module = instance;
        if (module == null || !module.lowerSensitivity.get() || current <= 1.0001f) {
            return 1.0d;
        }
        return 1.0d / ((double) current);
    }

    public static boolean scroll(double amount) {
        ZoomModule module = instance;
        if (module == null || !module.isEnabled() || !module.scroll.get() || amount == 0.0d) {
            return false;
        }
        float next = target * (amount > 0.0d ? SCROLL_STEP : 0.8333333f);
        target = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, next));
        return true;
    }

    private static float advance() {
        long now = System.nanoTime();
        float seconds = lastNanos == 0 ? 0.0f : Math.min(MAX_FRAME_SECONDS, (now - lastNanos) / 1.0E9f);
        lastNanos = now;
        ZoomModule module = instance;
        float smooth = module == null ? 0.0f : module.smoothness.get() / 100.0f;
        if (smooth <= 0.0f || current == target) {
            current = target;
            return current;
        }
        float rate = FAST_RATE + ((-25.0f) * smooth);
        float blend = 1.0f - ((float) Math.exp((-seconds) * rate));
        double from = Math.log(current);
        double to = Math.log(target);
        current = (float) Math.exp(from + ((to - from) * ((double) blend)));
        if (Math.abs(current - target) < 0.002f) {
            current = target;
        }
        return current;
    }
}
