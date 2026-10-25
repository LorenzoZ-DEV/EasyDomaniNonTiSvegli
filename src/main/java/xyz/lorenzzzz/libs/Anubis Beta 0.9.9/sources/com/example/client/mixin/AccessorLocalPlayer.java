package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10185;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorLocalPlayer.class */
@Mixin({class_746.class})
@Environment(EnvType.CLIENT)
public interface AccessorLocalPlayer {
    @Accessor("field_3923")
    void anubis$setPositionReminder(int i);

    @Accessor("field_3919")
    boolean anubis$wasSprinting();

    @Accessor("field_54156")
    class_10185 anubis$lastSentInput();
}
