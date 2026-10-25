package net.fabricmc.fabric.impl.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/screen/ScreenEventFactory.class */
@Environment(EnvType.CLIENT)
public final class ScreenEventFactory {
    public static Event<ScreenEvents.Remove> createRemoveEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.Remove.class, callbacks -> {
            return screen -> {
                for (ScreenEvents.Remove callback : callbacks) {
                    callback.onRemove(screen);
                }
            };
        });
    }

    public static Event<ScreenEvents.BeforeRender> createBeforeRenderEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.BeforeRender.class, callbacks -> {
            return (screen, matrices, mouseX, mouseY, tickDelta) -> {
                for (ScreenEvents.BeforeRender callback : callbacks) {
                    callback.beforeRender(screen, matrices, mouseX, mouseY, tickDelta);
                }
            };
        });
    }

    public static Event<ScreenEvents.AfterBackground> createAfterBackgroundEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.AfterBackground.class, callbacks -> {
            return (screen, matrices, mouseX, mouseY, tickDelta) -> {
                for (ScreenEvents.AfterBackground callback : callbacks) {
                    callback.afterBackground(screen, matrices, mouseX, mouseY, tickDelta);
                }
            };
        });
    }

    public static Event<ScreenEvents.AfterRender> createAfterRenderEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.AfterRender.class, callbacks -> {
            return (screen, matrices, mouseX, mouseY, tickDelta) -> {
                for (ScreenEvents.AfterRender callback : callbacks) {
                    callback.afterRender(screen, matrices, mouseX, mouseY, tickDelta);
                }
            };
        });
    }

    public static Event<ScreenEvents.BeforeTick> createBeforeTickEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.BeforeTick.class, callbacks -> {
            return screen -> {
                for (ScreenEvents.BeforeTick callback : callbacks) {
                    callback.beforeTick(screen);
                }
            };
        });
    }

    public static Event<ScreenEvents.AfterTick> createAfterTickEvent() {
        return EventFactory.createArrayBacked(ScreenEvents.AfterTick.class, callbacks -> {
            return screen -> {
                for (ScreenEvents.AfterTick callback : callbacks) {
                    callback.afterTick(screen);
                }
            };
        });
    }

    public static Event<ScreenKeyboardEvents.AllowKeyPress> createAllowKeyPressEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyPress.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.AllowKeyPress callback : callbacks) {
                    if (!callback.allowKeyPress(screen, context)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenKeyboardEvents.BeforeKeyPress> createBeforeKeyPressEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyPress.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.BeforeKeyPress callback : callbacks) {
                    callback.beforeKeyPress(screen, context);
                }
            };
        });
    }

    public static Event<ScreenKeyboardEvents.AfterKeyPress> createAfterKeyPressEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyPress.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.AfterKeyPress callback : callbacks) {
                    callback.afterKeyPress(screen, context);
                }
            };
        });
    }

    public static Event<ScreenKeyboardEvents.AllowKeyRelease> createAllowKeyReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyRelease.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.AllowKeyRelease callback : callbacks) {
                    if (!callback.allowKeyRelease(screen, context)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenKeyboardEvents.BeforeKeyRelease> createBeforeKeyReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyRelease.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.BeforeKeyRelease callback : callbacks) {
                    callback.beforeKeyRelease(screen, context);
                }
            };
        });
    }

    public static Event<ScreenKeyboardEvents.AfterKeyRelease> createAfterKeyReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyRelease.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenKeyboardEvents.AfterKeyRelease callback : callbacks) {
                    callback.afterKeyRelease(screen, context);
                }
            };
        });
    }

    public static Event<ScreenMouseEvents.AllowMouseClick> createAllowMouseClickEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseClick.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenMouseEvents.AllowMouseClick callback : callbacks) {
                    if (!callback.allowMouseClick(screen, context)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenMouseEvents.BeforeMouseClick> createBeforeMouseClickEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseClick.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenMouseEvents.BeforeMouseClick callback : callbacks) {
                    callback.beforeMouseClick(screen, context);
                }
            };
        });
    }

    public static Event<ScreenMouseEvents.AfterMouseClick> createAfterMouseClickEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseClick.class, callbacks -> {
            return (screen, context, consumed) -> {
                boolean consume = false;
                for (ScreenMouseEvents.AfterMouseClick callback : callbacks) {
                    consume |= callback.afterMouseClick(screen, context, consume | consumed);
                }
                return consume;
            };
        });
    }

    public static Event<ScreenMouseEvents.AllowMouseRelease> createAllowMouseReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseRelease.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenMouseEvents.AllowMouseRelease callback : callbacks) {
                    if (!callback.allowMouseRelease(screen, context)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenMouseEvents.BeforeMouseRelease> createBeforeMouseReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseRelease.class, callbacks -> {
            return (screen, context) -> {
                for (ScreenMouseEvents.BeforeMouseRelease callback : callbacks) {
                    callback.beforeMouseRelease(screen, context);
                }
            };
        });
    }

    public static Event<ScreenMouseEvents.AfterMouseRelease> createAfterMouseReleaseEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseRelease.class, callbacks -> {
            return (screen, context, consumed) -> {
                boolean consume = false;
                for (ScreenMouseEvents.AfterMouseRelease callback : callbacks) {
                    consume |= callback.afterMouseRelease(screen, context, consume | consumed);
                }
                return consume;
            };
        });
    }

    public static Event<ScreenMouseEvents.AllowMouseDrag> createAllowMouseDragEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseDrag.class, callbacks -> {
            return (screen, context, horizontalAmount, verticalAmount) -> {
                for (ScreenMouseEvents.AllowMouseDrag callback : callbacks) {
                    if (!callback.allowMouseDrag(screen, context, horizontalAmount, verticalAmount)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenMouseEvents.BeforeMouseDrag> createBeforeMouseDragEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseDrag.class, callbacks -> {
            return (screen, context, horizontalAmount, verticalAmount) -> {
                for (ScreenMouseEvents.BeforeMouseDrag callback : callbacks) {
                    callback.beforeMouseDrag(screen, context, horizontalAmount, verticalAmount);
                }
            };
        });
    }

    public static Event<ScreenMouseEvents.AfterMouseDrag> createAfterMouseDragEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseDrag.class, callbacks -> {
            return (screen, context, horizontalAmount, verticalAmount, consumed) -> {
                boolean consume = false;
                for (ScreenMouseEvents.AfterMouseDrag callback : callbacks) {
                    consume |= callback.afterMouseDrag(screen, context, horizontalAmount, verticalAmount, consume | consumed);
                }
                return consume;
            };
        });
    }

    public static Event<ScreenMouseEvents.AllowMouseScroll> createAllowMouseScrollEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseScroll.class, callbacks -> {
            return (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                for (ScreenMouseEvents.AllowMouseScroll callback : callbacks) {
                    if (!callback.allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
                        return false;
                    }
                }
                return true;
            };
        });
    }

    public static Event<ScreenMouseEvents.BeforeMouseScroll> createBeforeMouseScrollEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseScroll.class, callbacks -> {
            return (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                for (ScreenMouseEvents.BeforeMouseScroll callback : callbacks) {
                    callback.beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
                }
            };
        });
    }

    public static Event<ScreenMouseEvents.AfterMouseScroll> createAfterMouseScrollEvent() {
        return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseScroll.class, callbacks -> {
            return (screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> {
                boolean consume = false;
                for (ScreenMouseEvents.AfterMouseScroll callback : callbacks) {
                    consume |= callback.afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount, consume | consumed);
                }
                return consume;
            };
        });
    }

    private ScreenEventFactory() {
    }
}
