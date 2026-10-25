package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorLoadingOverlay.class */
@Mixin({class_425.class})
@Environment(EnvType.CLIENT)
public interface AccessorLoadingOverlay {
    @Accessor("field_17770")
    float anubis$getProgress();

    @Accessor("field_17771")
    long anubis$getFadeOutStart();

    @Accessor("field_18220")
    long anubis$getFadeInStart();

    @Accessor("field_18219")
    boolean anubis$isFadeIn();
}
