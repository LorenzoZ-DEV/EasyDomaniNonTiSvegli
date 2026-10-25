package com.example.client.mixin;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_374;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorHudToastManager.class */
@Mixin({class_374.class})
@Environment(EnvType.CLIENT)
public interface AccessorHudToastManager {
    @Accessor("field_2239")
    List<?> anubis$hudVisibleToasts();
}
