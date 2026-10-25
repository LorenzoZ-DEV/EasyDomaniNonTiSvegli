package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorElytraFlyMultiPlayerGameMode.class */
@Mixin({class_636.class})
@Environment(EnvType.CLIENT)
public interface AccessorElytraFlyMultiPlayerGameMode {
    @Invoker("method_2911")
    void anubis$elytraFlySyncSelected();
}
