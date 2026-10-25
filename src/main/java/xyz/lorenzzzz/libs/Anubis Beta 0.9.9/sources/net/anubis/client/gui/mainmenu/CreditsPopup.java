package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import java.util.List;
import net.anubis.client.gui.EgyptDraw;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/CreditsPopup.class */
@Environment(EnvType.CLIENT)
public final class CreditsPopup {
    private static final List<String> CREDITS = List.of("jeff", "autism", "Meteor Client");
    private static final float ROW_H = 34.0f;
    private static final float DIVIDER_HALF = 56.0f;
    private static final float DIAMOND = 3.0f;
    private final DuskWidgets ui = new DuskWidgets();
    private final MenuFrame frame = new MenuFrame();
    private DuskWidgets.Dialog dialog;
    private long openedAt;

    public void open() {
        this.openedAt = System.nanoTime();
        this.dialog = new DuskWidgets.Dialog("Credits", "").content(CREDITS.size() * ROW_H, this::drawContent).cancel("Close");
    }

    public boolean isOpen() {
        return (this.dialog == null || this.dialog.isClosed()) ? false : true;
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

    public void render(ImDrawList dl, float width, float height, float scale, float dt, float mouseX, float mouseY, boolean clicked) {
        if (isOpen()) {
            float time = (System.nanoTime() - this.openedAt) / 1.0E9f;
            this.frame.begin(dl, dl, width, height, scale, time, dt, mouseX, mouseY, clicked);
            this.ui.begin(this.frame);
            this.ui.dialog(this.dialog);
        }
    }

    private void drawContent(DuskWidgets ui, DuskWidgets.Dialog dialog, float x, float y, float w, float h) {
        MenuFrame f = ui.frame();
        ImDrawList dl = f.bg();
        float s = f.scale();
        float a = ui.alpha();
        ImFont font = f.menuFont();
        float rowH = ROW_H * s;
        float cx = x + (w * 0.5f);
        for (int i = 0; i < CREDITS.size(); i++) {
            String name = CREDITS.get(i);
            float top = y + (i * rowH);
            f.text(dl, font, cx - (f.textWidth(font, name) * 0.5f), top + ((rowH - f.textHeight(font)) * 0.5f), EgyptDraw.GOLD_LIGHT, a, name);
            if (i < CREDITS.size() - 1) {
                divider(dl, cx, top + rowH, s, a);
            }
        }
    }

    private static void divider(ImDrawList dl, float cx, float cy, float s, float a) {
        float half = DIVIDER_HALF * s;
        float d = DIAMOND * s;
        float gap = d * 2.0f;
        float th = Math.max(1.0f, s);
        int line = EgyptDraw.col(EgyptDraw.GOLD, 0.4f * a);
        dl.addLine(cx - half, cy, cx - gap, cy, line, th);
        dl.addLine(cx + gap, cy, cx + half, cy, line, th);
        dl.addQuadFilled(cx, cy - d, cx + d, cy, cx, cy + d, cx - d, cy, EgyptDraw.col(EgyptDraw.GOLD, 0.75f * a));
    }
}
