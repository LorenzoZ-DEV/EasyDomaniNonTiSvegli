package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.gui.EgyptDraw;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_156;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/CommunityPopup.class */
@Environment(EnvType.CLIENT)
public final class CommunityPopup {
    public static final String INVITE = "discord.gg/anubis";
    public static final String INVITE_URL = "https://discord.gg/anubis";
    private static final float OPEN_DELAY = 0.6f;
    private static final float MARK_RING = 54.0f;
    private static final float MARK_GAP = 14.0f;
    private static final float LINE_GAP = 3.0f;
    private static final float CAPTION_TRACK = 4.0f;
    private static final float CHECK_BOX = 16.0f;
    private static final float CHECK_LABEL_GAP = 9.0f;
    private static final float CHECK_REACH = 5.0f;
    private static final float ROW_GAP = 18.0f;
    private static final float CONTENT_H = 88.0f;
    private static final float[] CLYDE = clydeOutline();
    private static final float CLYDE_EYE_X = 0.332f;
    private static final float CLYDE_EYE_Y = 0.076f;
    private static final float CLYDE_EYE_RX = 0.18f;
    private static final float CLYDE_EYE_RY = 0.2f;
    private static boolean offered;
    private final DuskWidgets ui = new DuskWidgets();
    private final MenuFrame frame = new MenuFrame();
    private DuskWidgets.Dialog dialog;
    private boolean dontShowAgain;
    private long openedAt;

    public void update(float menuTime, boolean live) {
        if (this.dialog != null && this.dialog.isClosed()) {
            this.dialog = null;
        }
        if (this.dialog == null && live && !offered && menuTime >= OPEN_DELAY) {
            offered = true;
            if (ThemeStore.get().communityPopup.get()) {
                open();
            }
        }
    }

    public boolean isOpen() {
        return (this.dialog == null || this.dialog.isClosed()) ? false : true;
    }

    public void render(ImDrawList dl, float width, float height, float scale, float dt, float mouseX, float mouseY, boolean clicked) {
        if (isOpen()) {
            float time = (System.nanoTime() - this.openedAt) / 1.0E9f;
            this.frame.begin(dl, dl, width, height, scale, time, dt, mouseX, mouseY, clicked);
            this.ui.begin(this.frame);
            this.ui.dialog(this.dialog);
        }
    }

    public boolean keyPressed(int key) {
        if (!isOpen()) {
            return false;
        }
        if (key == 256) {
            this.dialog.dismiss();
            return true;
        }
        return true;
    }

    private void open() {
        this.dontShowAgain = false;
        this.openedAt = System.nanoTime();
        this.dialog = new DuskWidgets.Dialog("Join Anubis Community", "Updates, configs and support. Come say hi.").content(CONTENT_H, this::drawContent).action("Join", this::join).cancel("Later", this::answered);
    }

    private void join() {
        class_156.method_668().method_670(INVITE_URL);
        answered();
    }

    private void answered() {
        if (this.dontShowAgain) {
            ThemeStore theme = ThemeStore.get();
            theme.communityPopup.set(false);
            theme.save();
        }
    }

    private void drawContent(DuskWidgets ui, DuskWidgets.Dialog dialog, float x, float y, float w, float h) {
        MenuFrame f = ui.frame();
        ImDrawList dl = f.bg();
        float s = f.scale();
        float a = ui.alpha();
        ImFont captionFont = f.tagFont();
        ImFont inviteFont = f.menuFont();
        float ring = MARK_RING * s;
        float track = CAPTION_TRACK * s;
        float captionW = f.spacedWidth(captionFont, "DISCORD", track);
        float inviteW = f.textWidth(inviteFont, INVITE);
        float groupW = ring + (14.0f * s) + Math.max(captionW, inviteW);
        float gx = x + ((w - groupW) * 0.5f);
        boolean over = ui.hovered(gx, y, groupW, ring);
        if (ui.pressed(gx, y, groupW, ring)) {
            dialog.confirm();
        }
        float k = ui.hover("invite", over);
        float cx = gx + (ring * 0.5f);
        float cy = (y + (ring * 0.5f)) - (2.0f * s);
        float r = ring * 0.44f;
        DuskWidgets.shenRing(dl, cx, cy, r, k, s, a);
        discordMark(dl, cx, cy, r * 1.1f, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD_TEXT, -16777216, k), a), EgyptDraw.col(EgyptDraw.mix(EgyptDraw.LACQUER, EgyptDraw.GOLD, k), a));
        float captionH = f.textHeight(captionFont);
        float inviteH = f.textHeight(inviteFont);
        float tx = gx + ring + (14.0f * s);
        float ty = cy - (((captionH + (LINE_GAP * s)) + inviteH) * 0.5f);
        f.spacedText(dl, captionFont, tx, ty, track, EgyptDraw.MUTED, a, "DISCORD");
        float inviteY = ty + captionH + (LINE_GAP * s);
        float underY = inviteY + inviteH + s;
        f.text(dl, inviteFont, tx, inviteY, EgyptDraw.mix(EgyptDraw.GOLD, EgyptDraw.GOLD_LIGHT, k), a, INVITE);
        if (k > 0.01f) {
            dl.addLine(tx, underY, tx + (inviteW * k), underY, EgyptDraw.col(EgyptDraw.GOLD_LIGHT, k * a), Math.max(1.0f, s));
        }
        ImFont labelFont = f.tagFont();
        float box = 16.0f * s;
        float reach = 5.0f * s;
        float rowW = box + (CHECK_LABEL_GAP * s) + f.textWidth(labelFont, "Don't show again");
        float bx = x + ((w - rowW) * 0.5f);
        float by = (y + h) - box;
        boolean overCheck = ui.hovered(bx, by - reach, rowW, box + (2.0f * reach));
        if (ui.pressed(bx, by - reach, rowW, box + (2.0f * reach))) {
            this.dontShowAgain = !this.dontShowAgain;
        }
        float kc = ui.hover("dont-show-again", overCheck);
        checkBox(dl, bx, by, box, this.dontShowAgain, kc, s, a);
        int labelColor = EgyptDraw.mix(this.dontShowAgain ? EgyptDraw.GOLD_TEXT : EgyptDraw.MUTED, EgyptDraw.GOLD_LIGHT, kc);
        f.text(dl, labelFont, bx + box + (CHECK_LABEL_GAP * s), by + ((box - f.textHeight(labelFont)) * 0.5f) + s, labelColor, a, "Don't show again");
    }

    private static void checkBox(ImDrawList dl, float x, float y, float size, boolean checked, float k, float s, float a) {
        float r = LINE_GAP * s;
        if (!checked) {
            dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(-16777216, 0.55f * a), r);
            dl.addRect(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.GOLD, (0.45f + (0.4f * k)) * a), r, 0, Math.max(1.0f, 1.2f * s));
            return;
        }
        dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD, EgyptDraw.GOLD_LIGHT, k), a), r);
        dl.pathClear();
        dl.pathLineTo(x + (size * 0.24f), y + (size * 0.52f));
        dl.pathLineTo(x + (size * 0.43f), y + (size * 0.71f));
        dl.pathLineTo(x + (size * 0.77f), y + (size * 0.31f));
        dl.pathStroke(EgyptDraw.col(-16777216, a), 0, Math.max(1.4f, 2.0f * s));
    }

    private static void discordMark(ImDrawList dl, float cx, float cy, float w, int ink, int hole) {
        float u = w * 0.5f;
        dl.pathClear();
        for (int i = 0; i < CLYDE.length; i += 2) {
            dl.pathLineTo(cx + (CLYDE[i] * u), cy + (CLYDE[i + 1] * u));
        }
        dl.pathFillConcave(ink);
        for (int side = -1; side <= 1; side += 2) {
            float ex = cx + (side * CLYDE_EYE_X * u);
            float ey = cy + (CLYDE_EYE_Y * u);
            dl.pathClear();
            for (int i2 = 0; i2 < 16; i2++) {
                double angle = ((((double) i2) * 3.141592653589793d) * 2.0d) / ((double) 16);
                dl.pathLineTo(ex + (((float) Math.cos(angle)) * CLYDE_EYE_RX * u), ey + (((float) Math.sin(angle)) * CLYDE_EYE_RY * u));
            }
            dl.pathFillConvex(hole);
        }
    }

    private static float[] clydeOutline() {
        float[] half = new float[2 * (4 + 6 + 3 + 4)];
        int n = put(half, 0, 0.0f, -0.667f);
        int n2 = put(half, put(half, put(half, n, 0.229f, -0.65f), 0.282f, -0.758f), 0.694f, -0.631f);
        for (int i = 1; i <= 6; i++) {
            float t = i / 6;
            float v = 1.0f - t;
            float b0 = v * v * v;
            float b1 = LINE_GAP * v * v * t;
            float b2 = LINE_GAP * v * t * t;
            float b3 = t * t * t;
            n2 = put(half, n2, (b0 * 0.694f) + (b1 * 0.938f) + (b2 * 1.03f) + (b3 * 0.992f), (b0 * (-0.631f)) + (b1 * (-0.272f)) + (b2 * 0.103f) + (b3 * 0.504f));
        }
        int n3 = put(half, put(half, put(half, n2, 0.749f, 0.653f), 0.486f, 0.758f), 0.344f, 0.531f);
        for (int i2 = 1; i2 <= 4; i2++) {
            float cx = 0.344f * (1.0f - (i2 / 4));
            n3 = put(half, n3, cx, (-0.606f) + ((float) Math.sqrt(1.413721f - (cx * cx))));
        }
        int points = n3 / 2;
        float[] outline = new float[2 * ((2 * points) - 2)];
        System.arraycopy(half, 0, outline, 0, n3);
        for (int i3 = points - 2; i3 >= 1; i3--) {
            n3 = put(outline, n3, -half[2 * i3], half[(2 * i3) + 1]);
        }
        return outline;
    }

    private static int put(float[] points, int n, float x, float y) {
        points[n] = x;
        points[n + 1] = y;
        return n + 2;
    }
}
