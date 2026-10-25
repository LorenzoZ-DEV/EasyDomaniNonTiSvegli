package net.fabricmc.fabric.mixin.client.keybinding;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.class_304;
import net.minecraft.class_315;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/mixin/client/keybinding/OptionsMixin.class */
@Mixin({class_315.class})
@Environment(EnvType.CLIENT)
public class OptionsMixin {

    @Mutable
    @Shadow
    @Final
    public class_304[] field_1839;

    @Inject(at = {@At("HEAD")}, method = {"method_1636()V"})
    public void loadHook(CallbackInfo info) {
        this.field_1839 = KeyBindingRegistryImpl.process(this.field_1839);
    }
}
