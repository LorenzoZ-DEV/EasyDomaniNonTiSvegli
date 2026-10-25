package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorElytraFlyBlockBehaviour.class */
@Mixin({class_4970.class})
@Environment(EnvType.CLIENT)
public interface AccessorElytraFlyBlockBehaviour {
    @Accessor("field_23159")
    boolean anubis$elytraFlyHasCollision();
}
