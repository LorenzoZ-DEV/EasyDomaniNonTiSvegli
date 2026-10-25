package com.example.client.mixin;

import net.anubis.client.event.impl.KeyInputEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinKeyboardHandler.class */
@Mixin({class_309.class})
@Environment(EnvType.CLIENT)
public abstract class MixinKeyboardHandler {
    @Inject(method = {"method_1466"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$onKeyPress(long window, int action, class_11908 event, CallbackInfo ci) {
        if (KeyInputEvent.dispatch(event.comp_4795(), event.comp_4796(), action, event.comp_4797())) {
            ci.cancel();
        }
    }
}
