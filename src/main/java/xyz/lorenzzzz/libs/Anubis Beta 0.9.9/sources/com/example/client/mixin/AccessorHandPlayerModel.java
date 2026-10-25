package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorHandPlayerModel.class */
@Mixin({class_591.class})
@Environment(EnvType.CLIENT)
public interface AccessorHandPlayerModel {
    @Accessor("field_3480")
    boolean anubis$handSlim();
}
