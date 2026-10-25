package net.fabricmc.fabric.api.client.screen.v1;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents.class */
@Environment(EnvType.CLIENT)
public final class ScreenEvents {
    public static final Event<BeforeInit> BEFORE_INIT = EventFactory.createArrayBacked(BeforeInit.class, callbacks -> {
        return (client, screen, scaledWidth, scaledHeight) -> {
            for (BeforeInit callback : callbacks) {
                callback.beforeInit(client, screen, scaledWidth, scaledHeight);
            }
        };
    });
    public static final Event<AfterInit> AFTER_INIT = EventFactory.createArrayBacked(AfterInit.class, callbacks -> {
        return (client, screen, scaledWidth, scaledHeight) -> {
            for (AfterInit callback : callbacks) {
                callback.afterInit(client, screen, scaledWidth, scaledHeight);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$AfterBackground.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterBackground {
        void afterBackground(class_437 class_437Var, class_332 class_332Var, int i, int i2, float f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$AfterInit.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterInit {
        void afterInit(class_310 class_310Var, class_437 class_437Var, int i, int i2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$AfterRender.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterRender {
        void afterRender(class_437 class_437Var, class_332 class_332Var, int i, int i2, float f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$AfterTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterTick {
        void afterTick(class_437 class_437Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$BeforeInit.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeInit {
        void beforeInit(class_310 class_310Var, class_437 class_437Var, int i, int i2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$BeforeRender.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeRender {
        void beforeRender(class_437 class_437Var, class_332 class_332Var, int i, int i2, float f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$BeforeTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BeforeTick {
        void beforeTick(class_437 class_437Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/ScreenEvents$Remove.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Remove {
        void onRemove(class_437 class_437Var);
    }

    public static Event<Remove> remove(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getRemoveEvent();
    }

    public static Event<BeforeRender> beforeRender(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeRenderEvent();
    }

    public static Event<AfterBackground> afterBackground(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterBackgroundEvent();
    }

    public static Event<AfterRender> afterRender(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterRenderEvent();
    }

    public static Event<BeforeTick> beforeTick(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getBeforeTickEvent();
    }

    public static Event<AfterTick> afterTick(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getAfterTickEvent();
    }

    private ScreenEvents() {
    }
}
