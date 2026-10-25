package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/MenuIcons.class */
@Environment(EnvType.CLIENT)
public final class MenuIcons {
    private MenuIcons() {
    }

    public static void draw(ImDrawList dl, MenuAction action, float cx, float cy, float r, int color, float th) {
        switch (action) {
            case LANGUAGE:
                globe(dl, cx, cy, r, color, th);
                break;
            case ACCESSIBILITY:
                figure(dl, cx, cy, r, color, th);
                break;
            case ANUBIS:
                ankh(dl, cx, cy, r, color, th);
                break;
            case MODS:
                folder(dl, cx, cy, r, color, th);
                break;
            case ACCOUNTS:
                person(dl, cx, cy, r, color, th);
                break;
            case CREDITS:
                scroll(dl, cx, cy, r, color, th);
                break;
            default:
                dl.addCircleFilled(cx, cy, r * 0.25f, color, 12);
                break;
        }
    }

    public static void person(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        dl.addCircle(cx, cy - (r * 0.38f), r * 0.42f, color, 24, th);
        dl.pathClear();
        dl.pathArcTo(cx, cy + (r * 0.95f), r * 0.82f, 3.1415927f, 6.2831855f, 20);
        dl.pathStroke(color, 0, th);
    }

    public static void ankh(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        ellipse(dl, cx, cy - (r * 0.55f), r * 0.36f, r * 0.45f, color, th);
        dl.addLine(cx - (r * 0.72f), cy - (r * 0.05f), cx + (r * 0.72f), cy - (r * 0.05f), color, th * 1.2f);
        dl.addLine(cx, cy - (r * 0.1f), cx, cy + (r * 1.05f), color, th * 1.2f);
    }

    public static void globe(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        dl.addCircle(cx, cy, r, color, 32, th);
        ellipse(dl, cx, cy, r * 0.45f, r, color, th);
        dl.addLine(cx - r, cy, cx + r, cy, color, th);
    }

    public static void figure(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        dl.addCircleFilled(cx, cy - (r * 0.8f), r * 0.22f, color, 16);
        dl.addLine(cx - (r * 0.85f), cy - (r * 0.35f), cx + (r * 0.85f), cy - (r * 0.35f), color, th);
        dl.addLine(cx, cy - (r * 0.35f), cx, cy + (r * 0.3f), color, th);
        dl.addLine(cx, cy + (r * 0.3f), cx - (r * 0.5f), cy + r, color, th);
        dl.addLine(cx, cy + (r * 0.3f), cx + (r * 0.5f), cy + r, color, th);
    }

    public static void folder(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        float l = cx - r;
        float rt = cx + r;
        float tp = cy - (r * 0.7f);
        float bt = cy + (r * 0.75f);
        dl.pathClear();
        dl.pathLineTo(l, bt);
        dl.pathLineTo(l, tp);
        dl.pathLineTo(cx - (r * 0.2f), tp);
        dl.pathLineTo(cx, tp + (r * 0.3f));
        dl.pathLineTo(rt, tp + (r * 0.3f));
        dl.pathLineTo(rt, bt);
        dl.pathStroke(color, 1, th);
    }

    public static void scroll(ImDrawList dl, float cx, float cy, float r, int color, float th) {
        float l = cx - (r * 0.62f);
        float rt = cx + (r * 0.62f);
        float tp = cy - (r * 0.78f);
        float bt = cy + (r * 0.78f);
        dl.addRect(l, tp, rt, bt, color, 0.0f, 0, th);
        dl.addLine(l - (r * 0.28f), tp, rt + (r * 0.28f), tp, color, th * 1.3f);
        dl.addLine(l - (r * 0.28f), bt, rt + (r * 0.28f), bt, color, th * 1.3f);
        dl.addLine(l + (r * 0.3f), cy - (r * 0.22f), rt - (r * 0.3f), cy - (r * 0.22f), color, th);
        dl.addLine(l + (r * 0.3f), cy + (r * 0.22f), rt - (r * 0.3f), cy + (r * 0.22f), color, th);
    }

    public static void ellipse(ImDrawList dl, float cx, float cy, float rx, float ry, int color, float th) {
        dl.pathClear();
        for (int i = 0; i < 28; i++) {
            float ang = (float) (((((double) i) * 3.141592653589793d) * 2.0d) / ((double) 28));
            dl.pathLineTo(cx + (((float) Math.cos(ang)) * rx), cy + (((float) Math.sin(ang)) * ry));
        }
        dl.pathStroke(color, 1, th);
    }
}
