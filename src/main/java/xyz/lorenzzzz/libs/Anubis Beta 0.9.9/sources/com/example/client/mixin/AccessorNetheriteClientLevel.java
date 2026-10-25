package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_638;
import net.minecraft.class_7202;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorNetheriteClientLevel.class */
@Mixin({class_638.class})
@Environment(EnvType.CLIENT)
public interface AccessorNetheriteClientLevel {
    @Accessor("field_37951")
    class_7202 anubis$blockStatePredictionHandler();
}
