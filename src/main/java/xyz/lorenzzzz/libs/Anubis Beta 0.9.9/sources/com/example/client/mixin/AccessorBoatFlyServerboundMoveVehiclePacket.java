package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_2833;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorBoatFlyServerboundMoveVehiclePacket.class */
@Mixin({class_2833.class})
@Environment(EnvType.CLIENT)
public interface AccessorBoatFlyServerboundMoveVehiclePacket {
    @Accessor("comp_3350")
    @Mutable
    void anubis$setPosition(class_243 class_243Var);
}
