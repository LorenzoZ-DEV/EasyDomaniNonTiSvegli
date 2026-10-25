package net.anubis.client.module.impl.render;

import imgui.flag.ImGuiWindowFlags;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/CustomFovModule.class */
@Environment(EnvType.CLIENT)
public final class CustomFovModule extends ClientModule {
    public static volatile CustomFovModule active;
    private static float screenEffectScale = 1.0f;
    private final NumberSetting fov;
    private int culledFov;

    public CustomFovModule() {
        super("custom-fov", "Custom Fov", "Changes your field of view (FOV).", ModuleCategory.VISUALS);
        this.fov = (NumberSetting) addSetting("fov", new NumberSetting("FOV", 110.0f, 30.0f, 160.0f).step(1.0f));
        this.culledFov = ImGuiWindowFlags.NavFlattened;
        this.fov.onChange(this::onFovChanged);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
        this.culledFov = this.fov.getInt();
        scheduleTerrainUpdate();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        this.culledFov = ImGuiWindowFlags.NavFlattened;
        scheduleTerrainUpdate();
    }

    public float fov() {
        return this.fov.getInt();
    }

    public static void hudFovApplied(float vanilla, float applied) {
        screenEffectScale = vanilla == applied ? 1.0f : (float) (Math.tan(Math.toRadians(applied) * 0.5d) / Math.tan(Math.toRadians(vanilla) * 0.5d));
    }

    public static float screenEffectScale() {
        return screenEffectScale;
    }

    private void onFovChanged() {
        int value;
        if (active == this && (value = this.fov.getInt()) != this.culledFov) {
            this.culledFov = value;
            scheduleTerrainUpdate();
        }
    }

    private static void scheduleTerrainUpdate() {
        class_310 mc = class_310.method_1551();
        if (mc == null) {
            return;
        }
        if (!mc.method_18854()) {
            mc.execute(CustomFovModule::scheduleTerrainUpdate);
        } else {
            if (mc.field_1687 == null || mc.field_1769 == null) {
                return;
            }
            mc.field_1769.method_3292();
        }
    }
}
