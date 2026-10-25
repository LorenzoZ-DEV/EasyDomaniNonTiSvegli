package net.fabricmc.fabric.api.client.screen.v1;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.class_11908;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents.class */
@Environment(EnvType.CLIENT)
public final class ScreenKeyboardEvents {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$AfterKeyPress.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterKeyPress {
        void afterKeyPress(class_437 class_437Var, class_11908 class_11908Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$AfterKeyRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterKeyRelease {
        void afterKeyRelease(class_437 class_437Var, class_11908 class_11908Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$AllowKeyPress.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowKeyPress {
        boolean allowKeyPress(class_437 class_437Var, class_11908 class_11908Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$AllowKeyRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowKeyRelease {
        boolean allowKeyRelease(class_437 class_437Var, class_11908 class_11908Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$BeforeKeyPress.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeKeyPress {
        void beforeKeyPress(class_437 class_437Var, class_11908 class_11908Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenKeyboardEvents$BeforeKeyRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeKeyRelease {
        void beforeKeyRelease(class_437 class_437Var, class_11908 class_11908Var);
    }

    public static Event<AllowKeyPress> allowKeyPress(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowKeyPressEvent();
    }

    public static Event<BeforeKeyPress> beforeKeyPress(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeKeyPressEvent();
    }

    public static Event<AfterKeyPress> afterKeyPress(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterKeyPressEvent();
    }

    public static Event<AllowKeyRelease> allowKeyRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowKeyReleaseEvent();
    }

    public static Event<BeforeKeyRelease> beforeKeyRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeKeyReleaseEvent();
    }

    public static Event<AfterKeyRelease> afterKeyRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterKeyReleaseEvent();
    }

    private ScreenKeyboardEvents() {
    }
}
