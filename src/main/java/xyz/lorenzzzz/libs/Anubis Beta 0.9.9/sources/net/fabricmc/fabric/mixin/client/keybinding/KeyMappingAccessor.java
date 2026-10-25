package net.fabricmc.fabric.mixin.client.keybinding;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/mixin/client/keybinding/KeyMappingAccessor.class */
@Mixin({class_304.class})
@Environment(EnvType.CLIENT)
public interface KeyMappingAccessor {
    @Accessor("field_1655")
    class_3675.class_306 fabric_getBoundKey();
}
