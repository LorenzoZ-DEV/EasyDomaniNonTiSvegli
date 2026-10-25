package net.fabricmc.fabric.api.client.screen.v1;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.class_11909;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents.class */
@Environment(EnvType.CLIENT)
public final class ScreenMouseEvents {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AfterMouseClick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterMouseClick {
        boolean afterMouseClick(class_437 class_437Var, class_11909 class_11909Var, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AfterMouseDrag.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterMouseDrag {
        boolean afterMouseDrag(class_437 class_437Var, class_11909 class_11909Var, double d, double d2, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AfterMouseRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterMouseRelease {
        boolean afterMouseRelease(class_437 class_437Var, class_11909 class_11909Var, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AfterMouseScroll.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterMouseScroll {
        boolean afterMouseScroll(class_437 class_437Var, double d, double d2, double d3, double d4, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AllowMouseClick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowMouseClick {
        boolean allowMouseClick(class_437 class_437Var, class_11909 class_11909Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AllowMouseDrag.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowMouseDrag {
        boolean allowMouseDrag(class_437 class_437Var, class_11909 class_11909Var, double d, double d2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AllowMouseRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowMouseRelease {
        boolean allowMouseRelease(class_437 class_437Var, class_11909 class_11909Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$AllowMouseScroll.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AllowMouseScroll {
        boolean allowMouseScroll(class_437 class_437Var, double d, double d2, double d3, double d4);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$BeforeMouseClick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeMouseClick {
        void beforeMouseClick(class_437 class_437Var, class_11909 class_11909Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$BeforeMouseDrag.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeMouseDrag {
        void beforeMouseDrag(class_437 class_437Var, class_11909 class_11909Var, double d, double d2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$BeforeMouseRelease.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeMouseRelease {
        void beforeMouseRelease(class_437 class_437Var, class_11909 class_11909Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenMouseEvents$BeforeMouseScroll.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeMouseScroll {
        void beforeMouseScroll(class_437 class_437Var, double d, double d2, double d3, double d4);
    }

    public static Event<AllowMouseClick> allowMouseClick(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowMouseClickEvent();
    }

    public static Event<BeforeMouseClick> beforeMouseClick(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseClickEvent();
    }

    public static Event<AfterMouseClick> afterMouseClick(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterMouseClickEvent();
    }

    public static Event<AllowMouseRelease> allowMouseRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowMouseReleaseEvent();
    }

    public static Event<BeforeMouseRelease> beforeMouseRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseReleaseEvent();
    }

    public static Event<AfterMouseRelease> afterMouseRelease(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterMouseReleaseEvent();
    }

    public static Event<AllowMouseDrag> allowMouseDrag(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowMouseDragEvent();
    }

    public static Event<BeforeMouseDrag> beforeMouseDrag(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseDragEvent();
    }

    public static Event<AfterMouseDrag> afterMouseDrag(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterMouseDragEvent();
    }

    public static Event<AllowMouseScroll> allowMouseScroll(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAllowMouseScrollEvent();
    }

    public static Event<BeforeMouseScroll> beforeMouseScroll(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseScrollEvent();
    }

    public static Event<AfterMouseScroll> afterMouseScroll(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterMouseScrollEvent();
    }

    private ScreenMouseEvents() {
    }
}
