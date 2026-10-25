package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorMovementClientInput.class */
@Mixin({class_744.class})
@Environment(EnvType.CLIENT)
public interface AccessorMovementClientInput {
    @Accessor("field_55868")
    void anubis$setMoveVector(class_241 class_241Var);
}
