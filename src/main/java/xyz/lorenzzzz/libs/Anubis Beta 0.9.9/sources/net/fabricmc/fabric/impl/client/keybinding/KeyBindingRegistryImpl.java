package net.fabricmc.fabric.impl.client.keybinding;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/impl/client/keybinding/KeyBindingRegistryImpl.class */
@Environment(EnvType.CLIENT)
public final class KeyBindingRegistryImpl {
    private static final List<class_304> MODDED_KEY_BINDINGS = new ReferenceArrayList();

    private KeyBindingRegistryImpl() {
    }

    public static class_304 registerKeyBinding(class_304 binding) {
        if (class_310.method_1551().field_1690 != null) {
            throw new IllegalStateException("GameOptions has already been initialised");
        }
        for (class_304 existingKeyBindings : MODDED_KEY_BINDINGS) {
            if (existingKeyBindings == binding) {
                throw new IllegalArgumentException("Attempted to register a key binding twice: " + binding.method_1431());
            }
            if (existingKeyBindings.method_1431().equals(binding.method_1431())) {
                throw new IllegalArgumentException("Attempted to register two key bindings with equal ID: " + binding.method_1431() + "!");
            }
        }
        MODDED_KEY_BINDINGS.add(binding);
        return binding;
    }

    public static class_304[] process(class_304[] keysAll) {
        List<class_304> newKeysAll = Lists.newArrayList(keysAll);
        newKeysAll.removeAll(MODDED_KEY_BINDINGS);
        newKeysAll.addAll(MODDED_KEY_BINDINGS);
        return (class_304[]) newKeysAll.toArray(new class_304[0]);
    }
}
