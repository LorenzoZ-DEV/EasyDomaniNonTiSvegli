package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_368;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorHudToastInstance.class */
@Mixin(targets = {"net/minecraft/class_374$class_375"})
@Environment(EnvType.CLIENT)
public interface AccessorHudToastInstance {
    @Accessor("field_2241")
    class_368 anubis$hudToast();

    @Accessor("field_52787")
    int anubis$hudFirstSlot();

    @Accessor("field_2244")
    class_368.class_369 anubis$hudVisibility();

    @Accessor("field_52789")
    float anubis$hudVisiblePortion();
}
