package net.anubis.client.gui;

import com.example.client.mixin.AccessorLevelLoadingScreen;
import com.example.client.mixin.AccessorLoadingOverlay;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiIO;
import java.util.Locale;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_3928;
import net.minecraft.class_424;
import net.minecraft.class_425;
import net.minecraft.class_435;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/LoadingScreens.class */
@Environment(EnvType.CLIENT)
public final class LoadingScreens {
    private static final String HEAD_RESOURCE = "/assets/anubis/textures/gui/head.png";
    private static final int BACKGROUND_RGB = 722950;
    private static final float FADE_OUT_SECONDS = 0.45f;
    private static float lastProgress;
    private static float lastAlpha;
    private static float shownFill;
    private static final long ORIGIN = System.nanoTime();
    private static long lastActiveAt = -1;
    private static String lastStatus = "";

    private LoadingScreens() {
    }

    public static int backgroundColor(int argb) {
        return !AnubisTitleScreen.enabled() ? argb : (argb & (-16777216)) | BACKGROUND_RGB;
    }

    public static boolean hasWork() {
        if (!AnubisTitleScreen.enabled()) {
            return false;
        }
        if (lastActiveAt >= 0) {
            return true;
        }
        class_310 mc = class_310.method_1551();
        return (mc.method_18506() instanceof class_425) || (mc.field_1755 instanceof class_3928) || (mc.field_1755 instanceof class_424) || (mc.field_1755 instanceof class_435);
    }

    public static void render() {
        float progress;
        String status;
        float alpha;
        if (AnubisTitleScreen.enabled()) {
            class_310 mc = class_310.method_1551();
            long now = System.nanoTime();
            AccessorLoadingOverlay accessorLoadingOverlayMethod_18506 = mc.method_18506();
            if (accessorLoadingOverlayMethod_18506 instanceof class_425) {
                AccessorLoadingOverlay state = (class_425) accessorLoadingOverlayMethod_18506;
                alpha = reloadAlpha(state);
                progress = state.anubis$getProgress();
                status = "Loading";
            } else {
                AccessorLevelLoadingScreen accessorLevelLoadingScreen = mc.field_1755;
                if (accessorLevelLoadingScreen instanceof class_3928) {
                    progress = ((class_3928) accessorLevelLoadingScreen).anubis$getProgress();
                    status = "Entering world";
                } else if ((accessorLevelLoadingScreen instanceof class_424) || (accessorLevelLoadingScreen instanceof class_435)) {
                    progress = -1.0f;
                    String title = accessorLevelLoadingScreen.method_25440().getString();
                    status = title.isBlank() ? "Loading" : title;
                } else {
                    drawFadingOut(now);
                    return;
                }
                alpha = 1.0f;
            }
            lastActiveAt = now;
            lastProgress = progress;
            lastStatus = status;
            lastAlpha = alpha;
            draw(alpha, progress, status, now);
        }
    }

    private static void drawFadingOut(long now) {
        if (lastActiveAt < 0) {
            return;
        }
        float elapsed = seconds(now - lastActiveAt);
        if (elapsed >= FADE_OUT_SECONDS) {
            lastActiveAt = -1L;
        } else {
            draw(lastAlpha * (1.0f - (elapsed / FADE_OUT_SECONDS)), lastProgress < 0.0f ? -1.0f : 1.0f, lastStatus, now);
        }
    }

    private static float reloadAlpha(AccessorLoadingOverlay state) {
        long millis = class_156.method_658();
        long fadeOutStart = state.anubis$getFadeOutStart();
        long fadeInStart = state.anubis$getFadeInStart();
        float fadeOut = fadeOutStart > -1 ? (millis - fadeOutStart) / 1000.0f : -1.0f;
        float fadeIn = fadeInStart > -1 ? (millis - fadeInStart) / 500.0f : -1.0f;
        if (fadeOut >= 1.0f) {
            return 1.0f - clamp01(fadeOut - 1.0f);
        }
        if (state.anubis$isFadeIn()) {
            return clamp01(fadeIn);
        }
        return 1.0f;
    }

    private static void draw(float a, float progress, String status, long now) {
        if (a <= 0.001f) {
            return;
        }
        ImGuiIO io2 = ImGui.getIO();
        float sw = io2.getDisplaySizeX();
        float sh = io2.getDisplaySizeY();
        float s = ImGuiManager.getInstance().uiScale();
        float t = seconds(now - ORIGIN);
        float dt = Math.max(0.001f, io2.getDeltaTime());
        ImDrawList dl = ImGui.getForegroundDrawList();
        dl.addRectFilledMultiColor(0.0f, 0.0f, sw, sh, EgyptDraw.col(-15857145, a), EgyptDraw.col(-15857145, a), EgyptDraw.col(-16514302, a), EgyptDraw.col(-16514302, a));
        float headSize = Math.min(sh * 0.3f, 290.0f * s);
        float sunR = headSize * 0.62f;
        float cx = sw * 0.5f;
        float cy = sh * 0.42f;
        EgyptDraw.sunDisk(dl, cx, cy, sunR, t * 0.12f, s, a);
        float target = progress >= 0.0f ? clamp01(progress) : (float) ((((double) t) * 0.45d) % 1.0d);
        if (progress < 0.0f || target < shownFill) {
            shownFill = target;
        } else {
            shownFill += (target - shownFill) * Math.min(1.0f, dt * 6.0f);
        }
        ThemeImages.Texture head = EgyptDraw.texture("/assets/anubis/textures/gui/head.png");
        float x0 = cx - (headSize * 0.5f);
        float y0 = cy - (headSize * 0.5f);
        float x1 = x0 + headSize;
        float y1 = y0 + headSize;
        if (head != null) {
            EgyptDraw.glow(dl, cx, y1 - (headSize * shownFill), headSize * FADE_OUT_SECONDS, EgyptDraw.GOLD, 0.12f * a);
            dl.addImage(head.id(), x0, y0, x1, y1, 0.0f, 0.0f, 1.0f, 1.0f, EgyptDraw.col(-9743568, 0.35f * a));
            float clipTop = y1 - (headSize * shownFill);
            dl.pushClipRect(x0, clipTop, x1, y1, true);
            dl.addImage(head.id(), x0, y0, x1, y1, 0.0f, 0.0f, 1.0f, 1.0f, EgyptDraw.col(-1, a));
            dl.popClipRect();
        }
        ImFont big = ImGuiManager.getInstance().menuFont();
        ImFont small = ImGuiManager.getInstance().menuTagFont();
        float nameY = cy + (sunR * 1.34f) + (14.0f * s);
        EgyptDraw.drawSpaced(dl, big, cx, nameY, "ANUBIS CLIENT", 5.0f * s, EgyptDraw.GOLD, a, true);
        float bigSize = big != null ? big.getFontSize() : 24.0f * s;
        float barW = 260.0f * s;
        float barY = nameY + bigSize + (16.0f * s);
        float line = Math.max(1.0f, 1.5f * s);
        float bx = cx - (barW * 0.5f);
        dl.addLine(bx, barY, bx + barW, barY, EgyptDraw.col(EgyptDraw.GOLD, 0.18f * a), line);
        float end = progress >= 0.0f ? bx + (barW * shownFill) : bx + (barW * (0.5f + (0.5f * ((float) Math.sin(t * 2.2f)))));
        float start = progress >= 0.0f ? bx : Math.max(bx, end - (barW * 0.25f));
        dl.addLine(start, barY, end, barY, EgyptDraw.col(EgyptDraw.GOLD_LIGHT, a), line);
        float d = 3.5f * s;
        dl.addQuadFilled(end, barY - d, end + d, barY, end, barY + d, end - d, barY, EgyptDraw.col(EgyptDraw.GOLD_LIGHT, a));
        String line2 = progress >= 0.0f ? status + "  ·  " + Math.round(clamp01(progress) * 100.0f) + "%" : status;
        EgyptDraw.drawSpaced(dl, small, cx, barY + (12.0f * s), line2.toUpperCase(Locale.ROOT), 2.0f * s, EgyptDraw.MUTED, a, false);
    }

    private static float seconds(long nanos) {
        return nanos / 1.0E9f;
    }

    private static float clamp01(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }
}
