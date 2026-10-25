package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorJesusServerboundMovePlayerPacket.class */
@Mixin({class_2828.class})
@Environment(EnvType.CLIENT)
public interface AccessorJesusServerboundMovePlayerPacket {
    @Accessor("field_29179")
    @Mutable
    void anubis$jesusSetOnGround(boolean z);
}
