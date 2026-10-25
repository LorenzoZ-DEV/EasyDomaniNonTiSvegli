package net.anubis.client.module;

import imgui.ImDrawList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/HudOverlayProvider.class */
@Environment(EnvType.CLIENT)
public interface HudOverlayProvider {
    boolean isHudOverlayEnabled();

    void renderHudOverlay(ImDrawList imDrawList, float f, float f2);
}
