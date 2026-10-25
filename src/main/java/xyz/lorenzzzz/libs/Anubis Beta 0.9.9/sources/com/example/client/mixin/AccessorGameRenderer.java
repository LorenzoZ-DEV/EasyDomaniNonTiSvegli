package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4184;
import net.minecraft.class_757;
import net.minecraft.class_758;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorGameRenderer.class */
@Mixin({class_757.class})
@Environment(EnvType.CLIENT)
public interface AccessorGameRenderer {
    @Invoker("method_3196")
    float anubis$getFov(class_4184 class_4184Var, float f, boolean z);

    @Accessor("field_60793")
    class_758 anubis$getFogRenderer();

    @Invoker("method_3202")
    boolean anubis$shouldRenderBlockOutline();
}
