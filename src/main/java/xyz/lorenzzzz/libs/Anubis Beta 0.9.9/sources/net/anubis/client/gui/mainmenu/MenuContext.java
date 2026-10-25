package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import java.util.Objects;
import java.util.function.Supplier;
import net.anubis.client.gui.ImGuiScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_4189;
import net.minecraft.class_426;
import net.minecraft.class_429;
import net.minecraft.class_4325;
import net.minecraft.class_437;
import net.minecraft.class_4749;
import net.minecraft.class_500;
import net.minecraft.class_526;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/MenuContext.class */
@Environment(EnvType.CLIENT)
public final class MenuContext extends MenuFrame {
    private static final float LOOK_EASE = 2.5f;
    private final class_437 screen;
    private final Runnable openAccounts;
    private final Runnable openCredits;
    private final float[] hover = new float[MenuAction.values().length];
    private float lookX;
    private float lookY;

    public MenuContext(class_437 screen, Runnable openAccounts, Runnable openCredits) {
        this.screen = screen;
        this.openAccounts = openAccounts;
        this.openCredits = openCredits;
    }

    @Override // net.anubis.client.gui.mainmenu.MenuFrame
    public void begin(ImDrawList bg, ImDrawList fg, float width, float height, float scale, float time, float dt, float mouseX, float mouseY, boolean clicked) {
        super.begin(bg, fg, width, height, scale, time, dt, mouseX, mouseY, clicked);
        boolean onScreen = mouseX >= 0.0f && mouseY >= 0.0f && mouseX <= width && mouseY <= height;
        float targetX = onScreen ? ((mouseX / width) * 2.0f) - 1.0f : 0.0f;
        float targetY = onScreen ? ((mouseY / height) * 2.0f) - 1.0f : 0.0f;
        float k = Math.min(1.0f, dt * LOOK_EASE);
        this.lookX += (targetX - this.lookX) * k;
        this.lookY += (targetY - this.lookY) * k;
    }

    public float lookX() {
        return this.lookX;
    }

    public float lookY() {
        return this.lookY;
    }

    public boolean enabled(MenuAction action) {
        return !(action == MenuAction.MULTIPLAYER || action == MenuAction.REALMS) || class_310.method_1551().method_29043();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public float control(MenuAction action, float x, float y, float w, float h) throws MatchException {
        boolean over = inside(x, y, w, h);
        if (!enabled(action)) {
            if (over) {
                tooltip("Multiplayer is disabled", mouseX(), y + h + px(6.0f));
            }
            return hover(action, false);
        }
        if (over && clicked()) {
            activate(action);
        }
        return hover(action, over);
    }

    public float hover(MenuAction action, boolean target) {
        float k = this.hover[action.ordinal()];
        float k2 = k + (((target ? 1.0f : 0.0f) - k) * Math.min(1.0f, dt() * 12.0f));
        this.hover[action.ordinal()] = k2;
        return k2;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public void activate(MenuAction action) throws MatchException {
        Supplier<class_437> supplier;
        if (enabled(action)) {
            class_310 mc = class_310.method_1551();
            switch (action) {
                case SINGLEPLAYER:
                    supplier = () -> {
                        return new class_526(this.screen);
                    };
                    break;
                case MULTIPLAYER:
                    supplier = () -> {
                        return mc.field_1690.field_21840 ? new class_500(this.screen) : new class_4749(this.screen);
                    };
                    break;
                case REALMS:
                    supplier = () -> {
                        return new class_4325(this.screen);
                    };
                    break;
                case OPTIONS:
                    supplier = () -> {
                        return new class_429(this.screen, mc.field_1690);
                    };
                    break;
                case LANGUAGE:
                    supplier = () -> {
                        return new class_426(this.screen, mc.field_1690, mc.method_1526());
                    };
                    break;
                case ACCESSIBILITY:
                    supplier = () -> {
                        return new class_4189(this.screen, mc.field_1690);
                    };
                    break;
                case ANUBIS:
                    supplier = ImGuiScreen::new;
                    break;
                case QUIT:
                case MODS:
                case ACCOUNTS:
                case CREDITS:
                    supplier = null;
                    break;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
            Supplier<class_437> next = supplier;
            if (action == MenuAction.QUIT) {
                Objects.requireNonNull(mc);
                mc.execute(mc::method_1592);
                return;
            }
            if (action == MenuAction.ACCOUNTS) {
                this.openAccounts.run();
                return;
            }
            if (action == MenuAction.CREDITS) {
                this.openCredits.run();
            } else if (action == MenuAction.MODS) {
                mc.execute(() -> {
                    class_156.method_668().method_60932(FabricLoader.getInstance().getGameDir().resolve("mods"));
                });
            } else if (next != null) {
                mc.method_63588(() -> {
                    mc.method_1507((class_437) next.get());
                });
            }
        }
    }
}
