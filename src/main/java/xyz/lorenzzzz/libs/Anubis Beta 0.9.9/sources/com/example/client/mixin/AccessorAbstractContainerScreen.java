package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorAbstractContainerScreen.class */
@Mixin({class_465.class})
@Environment(EnvType.CLIENT)
public interface AccessorAbstractContainerScreen<T extends class_1703> {
    @Accessor("field_2787")
    class_1735 anubis$getHoveredSlot();
}
