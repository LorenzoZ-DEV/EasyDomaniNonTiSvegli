package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3928;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorLevelLoadingScreen.class */
@Mixin({class_3928.class})
@Environment(EnvType.CLIENT)
public interface AccessorLevelLoadingScreen {
    @Accessor("field_61632")
    float anubis$getProgress();
}
