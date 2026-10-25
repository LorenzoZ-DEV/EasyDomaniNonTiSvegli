package net.anubis.client.chat;

import net.anubis.client.config.ThemeStore;
import net.anubis.client.gui.ToggleToasts;
import net.anubis.client.module.ClientModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5250;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/chat/ModuleChat.class */
@Environment(EnvType.CLIENT)
public final class ModuleChat {
    private static final String HEADER = "Anubis Client";
    private static final int DARK_GREY = 5592405;
    private static final int GREY = 11184810;
    private static final int WHITE = 16777215;
    private static final int DANGER = 16733525;
    private static final int GOOD = 5635925;
    private static final int WARN = 16755200;
    private static final int VALUE = 5636095;

    private ModuleChat() {
    }

    public static Body body() {
        return new Body();
    }

    public static void send(ClientModule module, class_2561 body) {
        onClientThread(() -> {
            if (module.notificationsOn()) {
                addLine(module, body);
            }
        });
    }

    public static void send(ClientModule module, Body body) {
        send(module, body.build());
    }

    public static void send(ClientModule module, String text) {
        send(module, (class_2561) class_2561.method_43470(text));
    }

    public static void actionBar(ClientModule module, Body body) {
        class_2561 built = body.build();
        onClientThread(() -> {
            class_310 mc = class_310.method_1551();
            if (!module.notificationsOn() || mc.field_1705 == null) {
                return;
            }
            mc.field_1705.method_1758(line(module, built), false);
        });
    }

    public static void toggleLine(ClientModule module, Body state) {
        ToggleToasts.show(module, module.isEnabled());
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/chat/ModuleChat$Body.class */
    @Environment(EnvType.CLIENT)
    public static final class Body {
        private final class_5250 root = class_2561.method_43473().method_54663(ModuleChat.GREY);

        private Body() {
        }

        public Body text(String text) {
            this.root.method_10852(class_2561.method_43470(text));
            return this;
        }

        public Body name(String name) {
            return part(name, ModuleChat.WHITE);
        }

        public Body danger(String text) {
            return part(text, ModuleChat.DANGER);
        }

        public Body good(String text) {
            return part(text, ModuleChat.GOOD);
        }

        public Body warn(String text) {
            return part(text, ModuleChat.WARN);
        }

        public Body value(Object value) {
            return part(String.valueOf(value), ModuleChat.VALUE);
        }

        public Body coords(int x, int y, int z) {
            return part(x + ", " + y + ", " + z, ModuleChat.VALUE);
        }

        public Body append(class_2561 component) {
            this.root.method_10852(component);
            return this;
        }

        public class_2561 build() {
            return this.root.method_27661();
        }

        private Body part(String text, int rgb) {
            this.root.method_10852(class_2561.method_43470(text).method_54663(rgb));
            return this;
        }
    }

    private static void addLine(ClientModule module, class_2561 body) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1705 != null) {
            mc.field_1705.method_1743().method_1812(line(module, body));
        }
    }

    private static class_5250 line(ClientModule module, class_2561 body) {
        return class_2561.method_43473().method_10852(class_2561.method_43470("[").method_54663(DARK_GREY)).method_10852(class_2561.method_43470("Anubis Client").method_54663(ThemeStore.get().accent.rgb())).method_10852(class_2561.method_43470("] ").method_54663(DARK_GREY)).method_10852(class_2561.method_43470(module.name()).method_54663(WHITE)).method_10852(class_2561.method_43470(" : ").method_54663(DARK_GREY)).method_10852(class_2561.method_43473().method_54663(GREY).method_10852(body));
    }

    private static void onClientThread(Runnable task) {
        class_310 mc = class_310.method_1551();
        if (!mc.method_18854()) {
            mc.execute(task);
        } else {
            task.run();
        }
    }
}
