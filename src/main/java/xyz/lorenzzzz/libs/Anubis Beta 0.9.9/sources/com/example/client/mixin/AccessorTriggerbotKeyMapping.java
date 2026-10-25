package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorTriggerbotKeyMapping.class */
@Mixin({class_304.class})
@Environment(EnvType.CLIENT)
public interface AccessorTriggerbotKeyMapping {
    @Accessor("field_1661")
    int anubis$triggerbotClickCount();
}
