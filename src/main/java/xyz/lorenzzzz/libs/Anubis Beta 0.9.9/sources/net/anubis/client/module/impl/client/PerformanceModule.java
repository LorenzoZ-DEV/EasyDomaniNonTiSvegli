package net.anubis.client.module.impl.client;

import imgui.ImDrawList;
import imgui.ImFont;
import java.util.Locale;
import net.anubis.client.esp.ChunkScanner;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.gui.AnubisWidgets;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/PerformanceModule.class */
@Environment(EnvType.CLIENT)
public final class PerformanceModule extends ClientModule implements HudOverlayProvider {
    private final BooleanSetting frustumCulling;
    private final NumberSetting scanBudget;
    private long smoothedNanos;

    public PerformanceModule() {
        super("performance", "Performance", "Render performance settings.", ModuleCategory.CLIENT);
        this.frustumCulling = (BooleanSetting) addSetting("frustum-culling", new BooleanSetting("Frustum Culling", true));
        this.scanBudget = (NumberSetting) addSetting("scan-budget-ms", new NumberSetting("Scan Budget ms", 2.0f, 0.5f, 8.0f).step(0.5f));
    }

    public static boolean frustumCulling() {
        PerformanceModule module = (PerformanceModule) ModuleRegistry.get(PerformanceModule.class);
        return module == null || module.frustumCulling.get();
    }

    public static long scanBudgetNanos() {
        PerformanceModule module = (PerformanceModule) ModuleRegistry.get(PerformanceModule.class);
        if (module == null) {
            return 2000000L;
        }
        return (long) (module.scanBudget.get() * 1000000.0f);
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        this.smoothedNanos += (Render3DEvent.lastDurationNanos() - this.smoothedNanos) / 8;
        String text = String.format(Locale.ROOT, "3D listeners %.2f ms  ·  scan queue %d", Double.valueOf(this.smoothedNanos / 1000000.0d), Integer.valueOf(ChunkScanner.pending()));
        ImFont font = ImGuiManager.getInstance().hudLabelFont();
        float x = (screenW - AnubisWidgets.textWidth(font, text)) - 8.0f;
        float y = (screenH - AnubisWidgets.lineHeight(font)) - 6.0f;
        AnubisWidgets.text(dl, font, x + 1.0f, y + 1.0f, -1610612736, text);
        AnubisWidgets.text(dl, font, x, y, -3618616, text);
    }
}
