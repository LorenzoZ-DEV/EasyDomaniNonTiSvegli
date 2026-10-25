package net.fabricmc.fabric.mixin.screen;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.client.screen.ButtonList;
import net.fabricmc.fabric.impl.client.screen.ScreenEventFactory;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import net.minecraft.class_6379;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/ScreenMixin.class */
@Mixin({class_437.class})
@Environment(EnvType.CLIENT)
abstract class ScreenMixin implements ScreenExtensions {

    @Shadow
    @Final
    private List<class_6379> field_33815;

    @Shadow
    @Final
    private List<class_364> field_22786;

    @Shadow
    @Final
    private List<class_4068> field_33816;

    @Unique
    private ButtonList fabricButtons;

    @Unique
    private Event<ScreenEvents.Remove> removeEvent;

    @Unique
    private Event<ScreenEvents.BeforeTick> beforeTickEvent;

    @Unique
    private Event<ScreenEvents.AfterTick> afterTickEvent;

    @Unique
    private Event<ScreenEvents.BeforeRender> beforeRenderEvent;

    @Unique
    private Event<ScreenEvents.AfterBackground> afterBackgroundEvent;

    @Unique
    private Event<ScreenEvents.AfterRender> afterRenderEvent;

    @Unique
    private Event<ScreenKeyboardEvents.AllowKeyPress> allowKeyPressEvent;

    @Unique
    private Event<ScreenKeyboardEvents.BeforeKeyPress> beforeKeyPressEvent;

    @Unique
    private Event<ScreenKeyboardEvents.AfterKeyPress> afterKeyPressEvent;

    @Unique
    private Event<ScreenKeyboardEvents.AllowKeyRelease> allowKeyReleaseEvent;

    @Unique
    private Event<ScreenKeyboardEvents.BeforeKeyRelease> beforeKeyReleaseEvent;

    @Unique
    private Event<ScreenKeyboardEvents.AfterKeyRelease> afterKeyReleaseEvent;

    @Unique
    private Event<ScreenMouseEvents.AllowMouseClick> allowMouseClickEvent;

    @Unique
    private Event<ScreenMouseEvents.BeforeMouseClick> beforeMouseClickEvent;

    @Unique
    private Event<ScreenMouseEvents.AfterMouseClick> afterMouseClickEvent;

    @Unique
    private Event<ScreenMouseEvents.AllowMouseRelease> allowMouseReleaseEvent;

    @Unique
    private Event<ScreenMouseEvents.BeforeMouseRelease> beforeMouseReleaseEvent;

    @Unique
    private Event<ScreenMouseEvents.AfterMouseRelease> afterMouseReleaseEvent;

    @Unique
    private Event<ScreenMouseEvents.AllowMouseDrag> allowMouseDragEvent;

    @Unique
    private Event<ScreenMouseEvents.BeforeMouseDrag> beforeMouseDragEvent;

    @Unique
    private Event<ScreenMouseEvents.AfterMouseDrag> afterMouseDragEvent;

    @Unique
    private Event<ScreenMouseEvents.AllowMouseScroll> allowMouseScrollEvent;

    @Unique
    private Event<ScreenMouseEvents.BeforeMouseScroll> beforeMouseScrollEvent;

    @Unique
    private Event<ScreenMouseEvents.AfterMouseScroll> afterMouseScrollEvent;

    ScreenMixin() {
    }

    @Inject(method = {"method_47413(Lnet/minecraft/class_332;IIF)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_332;IIF)V", shift = At.Shift.AFTER)})
    public final void renderWithTooltip(class_332 context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        ScreenEvents.afterBackground((class_437) this).invoker().afterBackground((class_437) this, context, mouseX, mouseY, deltaTicks);
    }

    @Inject(method = {"method_25423(II)V"}, at = {@At("HEAD")})
    private void beforeInitScreen(int width, int height, CallbackInfo ci) {
        beforeInit(width, height);
    }

    @Inject(method = {"method_25423(II)V"}, at = {@At("TAIL")})
    private void afterInitScreen(int width, int height, CallbackInfo ci) {
        afterInit(width, height);
    }

    @Inject(method = {"method_25410(II)V"}, at = {@At("HEAD")})
    private void beforeResizeScreen(int width, int height, CallbackInfo ci) {
        beforeInit(width, height);
    }

    @Inject(method = {"method_25410(II)V"}, at = {@At("TAIL")})
    private void afterResizeScreen(int width, int height, CallbackInfo ci) {
        afterInit(width, height);
    }

    @Unique
    private void beforeInit(int width, int height) {
        this.fabricButtons = null;
        this.removeEvent = ScreenEventFactory.createRemoveEvent();
        this.beforeRenderEvent = ScreenEventFactory.createBeforeRenderEvent();
        this.afterBackgroundEvent = ScreenEventFactory.createAfterBackgroundEvent();
        this.afterRenderEvent = ScreenEventFactory.createAfterRenderEvent();
        this.beforeTickEvent = ScreenEventFactory.createBeforeTickEvent();
        this.afterTickEvent = ScreenEventFactory.createAfterTickEvent();
        this.allowKeyPressEvent = ScreenEventFactory.createAllowKeyPressEvent();
        this.beforeKeyPressEvent = ScreenEventFactory.createBeforeKeyPressEvent();
        this.afterKeyPressEvent = ScreenEventFactory.createAfterKeyPressEvent();
        this.allowKeyReleaseEvent = ScreenEventFactory.createAllowKeyReleaseEvent();
        this.beforeKeyReleaseEvent = ScreenEventFactory.createBeforeKeyReleaseEvent();
        this.afterKeyReleaseEvent = ScreenEventFactory.createAfterKeyReleaseEvent();
        this.allowMouseClickEvent = ScreenEventFactory.createAllowMouseClickEvent();
        this.beforeMouseClickEvent = ScreenEventFactory.createBeforeMouseClickEvent();
        this.afterMouseClickEvent = ScreenEventFactory.createAfterMouseClickEvent();
        this.allowMouseReleaseEvent = ScreenEventFactory.createAllowMouseReleaseEvent();
        this.beforeMouseReleaseEvent = ScreenEventFactory.createBeforeMouseReleaseEvent();
        this.afterMouseReleaseEvent = ScreenEventFactory.createAfterMouseReleaseEvent();
        this.allowMouseDragEvent = ScreenEventFactory.createAllowMouseDragEvent();
        this.beforeMouseDragEvent = ScreenEventFactory.createBeforeMouseDragEvent();
        this.afterMouseDragEvent = ScreenEventFactory.createAfterMouseDragEvent();
        this.allowMouseScrollEvent = ScreenEventFactory.createAllowMouseScrollEvent();
        this.beforeMouseScrollEvent = ScreenEventFactory.createBeforeMouseScrollEvent();
        this.afterMouseScrollEvent = ScreenEventFactory.createAfterMouseScrollEvent();
        ScreenEvents.BEFORE_INIT.invoker().beforeInit(class_310.method_1551(), (class_437) this, width, height);
    }

    @Unique
    private void afterInit(int width, int height) {
        ScreenEvents.AFTER_INIT.invoker().afterInit(class_310.method_1551(), (class_437) this, width, height);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public List<class_339> fabric_getButtons() {
        if (this.fabricButtons == null) {
            this.fabricButtons = new ButtonList(this.field_33816, this.field_33815, this.field_22786);
        }
        return this.fabricButtons;
    }

    @Unique
    private <T> Event<T> ensureEventsAreInitialized(Event<T> event) {
        if (event == null) {
            throw new IllegalStateException(String.format("[fabric-screen-api-v1] The current screen (%s) has not been correctly initialised, please send this crash log to the mod author. This is usually caused by calling setScreen on the wrong thread.", getClass().getName()));
        }
        return event;
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.Remove> fabric_getRemoveEvent() {
        return ensureEventsAreInitialized(this.removeEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.BeforeTick> fabric_getBeforeTickEvent() {
        return ensureEventsAreInitialized(this.beforeTickEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.AfterTick> fabric_getAfterTickEvent() {
        return ensureEventsAreInitialized(this.afterTickEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.BeforeRender> fabric_getBeforeRenderEvent() {
        return ensureEventsAreInitialized(this.beforeRenderEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.AfterBackground> fabric_getAfterBackgroundEvent() {
        return ensureEventsAreInitialized(this.afterBackgroundEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenEvents.AfterRender> fabric_getAfterRenderEvent() {
        return ensureEventsAreInitialized(this.afterRenderEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.AllowKeyPress> fabric_getAllowKeyPressEvent() {
        return ensureEventsAreInitialized(this.allowKeyPressEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.BeforeKeyPress> fabric_getBeforeKeyPressEvent() {
        return ensureEventsAreInitialized(this.beforeKeyPressEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.AfterKeyPress> fabric_getAfterKeyPressEvent() {
        return ensureEventsAreInitialized(this.afterKeyPressEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.AllowKeyRelease> fabric_getAllowKeyReleaseEvent() {
        return ensureEventsAreInitialized(this.allowKeyReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.BeforeKeyRelease> fabric_getBeforeKeyReleaseEvent() {
        return ensureEventsAreInitialized(this.beforeKeyReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenKeyboardEvents.AfterKeyRelease> fabric_getAfterKeyReleaseEvent() {
        return ensureEventsAreInitialized(this.afterKeyReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AllowMouseClick> fabric_getAllowMouseClickEvent() {
        return ensureEventsAreInitialized(this.allowMouseClickEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.BeforeMouseClick> fabric_getBeforeMouseClickEvent() {
        return ensureEventsAreInitialized(this.beforeMouseClickEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AfterMouseClick> fabric_getAfterMouseClickEvent() {
        return ensureEventsAreInitialized(this.afterMouseClickEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AllowMouseRelease> fabric_getAllowMouseReleaseEvent() {
        return ensureEventsAreInitialized(this.allowMouseReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.BeforeMouseRelease> fabric_getBeforeMouseReleaseEvent() {
        return ensureEventsAreInitialized(this.beforeMouseReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AfterMouseRelease> fabric_getAfterMouseReleaseEvent() {
        return ensureEventsAreInitialized(this.afterMouseReleaseEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AllowMouseDrag> fabric_getAllowMouseDragEvent() {
        return ensureEventsAreInitialized(this.allowMouseDragEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.BeforeMouseDrag> fabric_getBeforeMouseDragEvent() {
        return ensureEventsAreInitialized(this.beforeMouseDragEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AfterMouseDrag> fabric_getAfterMouseDragEvent() {
        return ensureEventsAreInitialized(this.afterMouseDragEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AllowMouseScroll> fabric_getAllowMouseScrollEvent() {
        return ensureEventsAreInitialized(this.allowMouseScrollEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.BeforeMouseScroll> fabric_getBeforeMouseScrollEvent() {
        return ensureEventsAreInitialized(this.beforeMouseScrollEvent);
    }

    @Override // net.fabricmc.fabric.impl.client.screen.ScreenExtensions
    public Event<ScreenMouseEvents.AfterMouseScroll> fabric_getAfterMouseScrollEvent() {
        return ensureEventsAreInitialized(this.afterMouseScrollEvent);
    }
}
