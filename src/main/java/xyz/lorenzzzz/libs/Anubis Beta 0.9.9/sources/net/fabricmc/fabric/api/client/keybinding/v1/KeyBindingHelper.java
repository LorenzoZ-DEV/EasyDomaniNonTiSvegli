package net.fabricmc.fabric.api.client.keybinding.v1;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.fabricmc.fabric.mixin.client.keybinding.KeyMappingAccessor;
import net.minecraft.class_304;
import net.minecraft.class_3675;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/api/client/keybinding/v1/KeyBindingHelper.class */
@Environment(EnvType.CLIENT)
public final class KeyBindingHelper {
    private KeyBindingHelper() {
    }

    public static class_304 registerKeyBinding(class_304 keyBinding) {
        Objects.requireNonNull(keyBinding, "key binding cannot be null");
        return KeyBindingRegistryImpl.registerKeyBinding(keyBinding);
    }

    public static class_3675.class_306 getBoundKeyOf(class_304 keyBinding) {
        return ((KeyMappingAccessor) keyBinding).fabric_getBoundKey();
    }
}
