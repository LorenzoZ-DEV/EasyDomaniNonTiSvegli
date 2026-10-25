package net.anubis.client.gui.mainmenu;

import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_332;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskBackdrop.class */
@Environment(EnvType.CLIENT)
public final class DuskBackdrop {
    public static final class_2960 TEXTURE = class_2960.method_60655(AddonManager.ENTRYPOINT, "textures/gui/dusk_backdrop.png");
    private static final float ASPECT = 1.7777778f;

    private DuskBackdrop() {
    }

    public static void draw(class_332 graphics, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        float screen = width / height;
        float u = screen < ASPECT ? (1.0f - (screen / ASPECT)) * 0.5f : 0.0f;
        float v = screen > ASPECT ? (1.0f - (ASPECT / screen)) * 0.5f : 0.0f;
        graphics.method_70845(TEXTURE, 0, 0, width, height, u, 1.0f - u, v, 1.0f - v);
    }
}
