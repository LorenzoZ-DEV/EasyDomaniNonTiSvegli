package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.List;
import net.anubis.client.gui.EgyptDraw;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskMenu.class */
@Environment(EnvType.CLIENT)
public final class DuskMenu {
    private static final float SUN_R = 150.0f;
    private static final float HEAD_SIZE = 1.7f;
    private static final float NAME_GAP = 26.0f;
    private static final float ITEMS_GAP = 34.0f;
    private static final float ROW_H = 56.0f;
    private static final float QUIT_GAP = 12.0f;
    private static final float BUTTON_W = 320.0f;
    private static final float BUTTON_H = 46.0f;
    private static final float ITEM_TRACK = 2.0f;
    private static final List<MenuAction> ICONS = List.of(MenuAction.ACCOUNTS, MenuAction.LANGUAGE, MenuAction.ACCESSIBILITY, MenuAction.ANUBIS, MenuAction.MODS, MenuAction.CREDITS);
    private static final float ICON_R = 9.0f;
    private static final float ICON_STEP = 34.0f;
    private static final float ICON_MARGIN = 26.0f;
    private static final int ITEM = -923950;
    private static final float ITEM_ALPHA = 0.85f;
    private static final int ICON = -4676216;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskMenu$Layout.class */
    @Environment(EnvType.CLIENT)
    public static final class Layout extends Record {
        private final float cx;
        private final float sunY;
        private final float sunR;
        private final float nameY;
        private final float itemsY;

        public Layout(float cx, float sunY, float sunR, float nameY, float itemsY) {
            this.cx = cx;
            this.sunY = sunY;
            this.sunR = sunR;
            this.nameY = nameY;
            this.itemsY = itemsY;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Layout.class), Layout.class, "cx;sunY;sunR;nameY;itemsY", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->cx:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunR:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->nameY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->itemsY:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Layout.class), Layout.class, "cx;sunY;sunR;nameY;itemsY", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->cx:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunR:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->nameY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->itemsY:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Layout.class, Object.class), Layout.class, "cx;sunY;sunR;nameY;itemsY", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->cx:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->sunR:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->nameY:F", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskMenu$Layout;->itemsY:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float cx() {
            return this.cx;
        }

        public float sunY() {
            return this.sunY;
        }

        public float sunR() {
            return this.sunR;
        }

        public float nameY() {
            return this.nameY;
        }

        public float itemsY() {
            return this.itemsY;
        }
    }

    private DuskMenu() {
    }

    public static Layout layout(float width, float height, float s) {
        float sunR = SUN_R * s;
        float nameH = 20.0f * s;
        float items = (MenuAction.MAIN.size() * ROW_H * s) + (QUIT_GAP * s);
        float column = (ITEM_TRACK * sunR) + (26.0f * s) + nameH + (34.0f * s) + items;
        float top = Math.max(48.0f * s, DuskScene.horizon(height) - column);
        float sunY = top + sunR;
        float nameY = sunY + sunR + (26.0f * s);
        return new Layout(width * 0.5f, sunY, sunR, nameY, nameY + nameH + (34.0f * s));
    }

    public static void render(MenuContext c) {
        ImDrawList dl = c.bg();
        float sw = c.width();
        float sh = c.height();
        float s = c.scale();
        Layout l = layout(sw, sh, s);
        DuskScene.draw(dl, sw, sh, s, c.time(), DuskScene.horizon(sh), l.cx(), l.sunY(), l.sunR(), c.lookX(), c.lookY());
        DuskEmblem.draw(c, l.cx(), l.sunY(), l.sunR() * HEAD_SIZE, l.nameY());
        float y = l.itemsY();
        for (int i = 0; i < MenuAction.MAIN.size(); i++) {
            MenuAction action = MenuAction.MAIN.get(i);
            if (action == MenuAction.QUIT) {
                y += QUIT_GAP * s;
            }
            item(c, action, i, l.cx(), y, c.intro(0.4f + (i * 0.05f)));
            y += ROW_H * s;
        }
        float a = c.intro(0.7f);
        float iconY = sh - (26.0f * s);
        float iconX = (sw - (26.0f * s)) - (((ICONS.size() - 1) * 34.0f) * s);
        for (int i2 = 0; i2 < ICONS.size(); i2++) {
            icon(c, ICONS.get(i2), iconX + (i2 * 34.0f * s), iconY, a);
        }
    }

    private static void item(MenuContext c, MenuAction action, int index, float cx, float y, float a) {
        ImDrawList dl = c.bg();
        ImFont font = c.itemFont();
        float s = c.scale();
        float w = BUTTON_W * s;
        float h = BUTTON_H * s;
        float x = cx - (w * 0.5f);
        boolean enabled = c.enabled(action);
        float k = a > 0.5f ? c.control(action, x, y, w, h) : c.hover(action, false);
        DuskPill.frame(c, x, y, w, h, k, DuskEmblem.buttonPass(c, index), a);
        String label = action.label();
        float track = ITEM_TRACK * s;
        float ty = y + ((h - c.textHeight(font)) * 0.5f) + ((1.0f - a) * 8.0f * s);
        float tx = cx - (c.spacedWidth(font, label, track) * 0.5f);
        int color = enabled ? EgyptDraw.mix(-923950, EgyptDraw.GOLD_LIGHT, k) : EgyptDraw.MUTED;
        float alpha = (action == MenuAction.QUIT ? 0.75f + (0.25f * k) : ITEM_ALPHA + (0.14999998f * k)) * a;
        if (enabled) {
            DuskPill.halo(c, font, tx, ty, track, k * a, label);
        }
        c.spacedText(dl, font, tx + s, ty + (1.5f * s), track, -16777216, 0.55f * a, label);
        c.spacedText(dl, font, tx, ty, track, color, alpha, label);
    }

    private static void icon(MenuContext c, MenuAction action, float cx, float cy, float a) {
        float s = c.scale();
        float r = ICON_R * s;
        float reach = 34.0f * s * 0.5f;
        float k = a > 0.5f ? c.control(action, cx - reach, cy - reach, reach * ITEM_TRACK, reach * ITEM_TRACK) : c.hover(action, false);
        MenuIcons.draw(c.bg(), action, cx, cy, r, EgyptDraw.col(EgyptDraw.mix(ICON, EgyptDraw.GOLD_LIGHT, k), a), Math.max(1.2f, 1.5f * s));
        if (k > 0.5f) {
            String name = action == MenuAction.ACCOUNTS ? action.label() + "  ·  " + c.displayName() : action.label();
            c.tooltip(name, Math.min(cx, (c.width() - (c.textWidth(c.tagFont(), name) * 0.5f)) - (18.0f * s)), ((cy - reach) - c.textHeight(c.tagFont())) - (14.0f * s));
        }
    }
}
