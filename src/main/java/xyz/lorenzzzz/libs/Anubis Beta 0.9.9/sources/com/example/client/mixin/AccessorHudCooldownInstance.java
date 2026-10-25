package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorHudCooldownInstance.class */
@Mixin(targets = {"net/minecraft/class_1796$class_1797"})
@Environment(EnvType.CLIENT)
public interface AccessorHudCooldownInstance {
    @Accessor("comp_3083")
    int anubis$hudStartTime();

    @Accessor("comp_3084")
    int anubis$hudEndTime();
}
