package net.fabricmc.fabric.api.client.screen.v1;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.fabricmc.fabric.mixin.screen.ScreenAccessor;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_339;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/api/client/screen/v1/Screens.class */
@Environment(EnvType.CLIENT)
public final class Screens {
    public static List<class_339> getButtons(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).fabric_getButtons();
    }

    @Deprecated
    public static class_327 getTextRenderer(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return screen.method_64506();
    }

    public static class_310 getClient(class_437 screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ((ScreenAccessor) screen).getClient();
    }

    private Screens() {
    }
}
