package net.fabricmc.fabric.impl.client.screen;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.class_339;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/screen/ScreenExtensions.class */
@Environment(EnvType.CLIENT)
public interface ScreenExtensions {
    List<class_339> fabric_getButtons();

    Event<ScreenEvents.Remove> fabric_getRemoveEvent();

    Event<ScreenEvents.BeforeTick> fabric_getBeforeTickEvent();

    Event<ScreenEvents.AfterTick> fabric_getAfterTickEvent();

    Event<ScreenEvents.BeforeRender> fabric_getBeforeRenderEvent();

    Event<ScreenEvents.AfterBackground> fabric_getAfterBackgroundEvent();

    Event<ScreenEvents.AfterRender> fabric_getAfterRenderEvent();

    Event<ScreenKeyboardEvents.AllowKeyPress> fabric_getAllowKeyPressEvent();

    Event<ScreenKeyboardEvents.BeforeKeyPress> fabric_getBeforeKeyPressEvent();

    Event<ScreenKeyboardEvents.AfterKeyPress> fabric_getAfterKeyPressEvent();

    Event<ScreenKeyboardEvents.AllowKeyRelease> fabric_getAllowKeyReleaseEvent();

    Event<ScreenKeyboardEvents.BeforeKeyRelease> fabric_getBeforeKeyReleaseEvent();

    Event<ScreenKeyboardEvents.AfterKeyRelease> fabric_getAfterKeyReleaseEvent();

    Event<ScreenMouseEvents.AllowMouseClick> fabric_getAllowMouseClickEvent();

    Event<ScreenMouseEvents.BeforeMouseClick> fabric_getBeforeMouseClickEvent();

    Event<ScreenMouseEvents.AfterMouseClick> fabric_getAfterMouseClickEvent();

    Event<ScreenMouseEvents.AllowMouseRelease> fabric_getAllowMouseReleaseEvent();

    Event<ScreenMouseEvents.BeforeMouseRelease> fabric_getBeforeMouseReleaseEvent();

    Event<ScreenMouseEvents.AfterMouseRelease> fabric_getAfterMouseReleaseEvent();

    Event<ScreenMouseEvents.AllowMouseDrag> fabric_getAllowMouseDragEvent();

    Event<ScreenMouseEvents.BeforeMouseDrag> fabric_getBeforeMouseDragEvent();

    Event<ScreenMouseEvents.AfterMouseDrag> fabric_getAfterMouseDragEvent();

    Event<ScreenMouseEvents.AllowMouseScroll> fabric_getAllowMouseScrollEvent();

    Event<ScreenMouseEvents.BeforeMouseScroll> fabric_getBeforeMouseScrollEvent();

    Event<ScreenMouseEvents.AfterMouseScroll> fabric_getAfterMouseScrollEvent();

    static ScreenExtensions getExtensions(class_437 screen) {
        return (ScreenExtensions) screen;
    }
}
