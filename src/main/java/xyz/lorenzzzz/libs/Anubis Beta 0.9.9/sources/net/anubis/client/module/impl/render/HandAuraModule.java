package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HandAuraModule.class */
@Environment(EnvType.CLIENT)
public final class HandAuraModule extends ClientModule {
    public static volatile HandAuraModule active;
    private static final String COLOR_THEME = "Theme";
    private static final String COLOR_MANUAL = "Manual";
    private static final int WHITE = -1;
    private static final float SHEEN_LIFT = 0.55f;
    private static final long BREATH_NANOS = 3200000000L;
    private static final long SHEEN_NANOS = 4400000000L;
    private final ModeSetting colorMode;
    private final ColorSetting glowColor;
    private final NumberSetting aura;
    private final NumberSetting reach;
    private final NumberSetting rim;
    private final NumberSetting sheen;
    private final ColorSetting sheenColor;
    private final BooleanSetting breathe;

    public HandAuraModule() {
        super("hand-aura", "Hand Aura", "Adds a glowing outline to your hand and held item.", ModuleCategory.VISUALS);
        this.colorMode = (ModeSetting) addSetting("color", new ModeSetting("Color", "Theme", "Theme", "Manual"));
        this.glowColor = (ColorSetting) addSetting("glow-color", new ColorSetting("Glow Color", -13338378));
        this.aura = (NumberSetting) addSetting("aura", new NumberSetting("Aura", 65.0f, 0.0f, 100.0f).step(5.0f));
        this.reach = (NumberSetting) addSetting("reach", new NumberSetting("Reach", 8.0f, 2.0f, 20.0f).step(1.0f));
        this.rim = (NumberSetting) addSetting("rim", new NumberSetting("Rim", 40.0f, 0.0f, 100.0f).step(5.0f));
        this.sheen = (NumberSetting) addSetting("sheen", new NumberSetting("Sheen", 25.0f, 0.0f, 100.0f).step(5.0f));
        this.sheenColor = (ColorSetting) addSetting("sheen-color", new ColorSetting("Sheen Color", -3153665));
        this.breathe = (BooleanSetting) addSetting("breathe", new BooleanSetting("Breathe", true));
        this.glowColor.visibleWhen(this::manual);
        this.reach.visibleWhen(() -> {
            return this.aura.get() > 0.0f;
        });
        this.sheenColor.visibleWhen(() -> {
            return this.sheen.get() > 0.0f && manual();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        if (RenderSystem.isOnRenderThread()) {
            HandAuraPass.release();
        } else {
            class_310.method_1551().execute(HandAuraModule::releaseIfOff);
        }
    }

    private static void releaseIfOff() {
        if (active == null) {
            HandAuraPass.release();
        }
    }

    public static boolean beginCapture() {
        HandAuraModule module = active;
        return module != null && module.shows() && HandAuraPass.begin();
    }

    public static void endCapture(boolean drawn) {
        if (HandAuraPass.end() && drawn) {
            HandAuraModule module = active;
            float scale = Math.max(1.0f, class_310.method_1551().method_1522().field_1481 / 1080.0f);
            if (module == null) {
                HandAuraPass.compose(-1, 0.0f, 2.0f, 0.0f, 2.0f * scale, -1, 0.0f, 0.0f);
                return;
            }
            long now = System.nanoTime();
            float phase = (float) ((now % SHEEN_NANOS) / 4.4E9d);
            int glow = module.glowArgb();
            HandAuraPass.compose(glow, module.auraStrength(now), module.reach.get() * scale, module.rim.get() / 100.0f, 2.0f * scale, module.sheenArgb(glow), module.sheen.get() / 100.0f, phase);
        }
    }

    private boolean manual() {
        return this.colorMode.is("Manual");
    }

    private int glowArgb() {
        return manual() ? this.glowColor.argb() : CosmeticAccent.argb();
    }

    private int sheenArgb(int glow) {
        return manual() ? this.sheenColor.argb() : class_9848.method_61319(SHEEN_LIFT, glow, -1);
    }

    private boolean shows() {
        return this.aura.get() > 0.0f || this.rim.get() > 0.0f;
    }

    private float auraStrength(long now) {
        float strength = this.aura.get() / 100.0f;
        if (!this.breathe.get()) {
            return strength;
        }
        double cycle = (now % BREATH_NANOS) / 3.2E9d;
        float swell = (float) Math.sin(cycle * 3.141592653589793d);
        return strength * (0.6f + (0.4f * swell * swell));
    }
}
