package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_500;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorMenusJoinMultiplayerScreen.class */
@Mixin({class_500.class})
@Environment(EnvType.CLIENT)
public interface AccessorMenusJoinMultiplayerScreen {
    @Invoker("method_2534")
    void anubis$refreshServerList();
}
