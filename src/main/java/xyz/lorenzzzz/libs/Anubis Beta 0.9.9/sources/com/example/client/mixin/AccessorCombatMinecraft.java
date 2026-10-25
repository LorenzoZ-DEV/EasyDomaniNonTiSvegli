package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorCombatMinecraft.class */
@Mixin({class_310.class})
@Environment(EnvType.CLIENT)
public interface AccessorCombatMinecraft {
    @Accessor("field_1752")
    int anubis$rightClickDelay();

    @Accessor("field_1752")
    void anubis$setRightClickDelay(int i);
}
