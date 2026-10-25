package net.fabricmc.fabric.api.resource;

import java.util.function.Function;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_7225;
import org.jetbrains.annotations.ApiStatus;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/ResourceManagerHelper.class */
@Deprecated
@ApiStatus.NonExtendable
public interface ResourceManagerHelper {
    @Deprecated
    void registerReloadListener(IdentifiableResourceReloadListener identifiableResourceReloadListener);

    @Deprecated
    void registerReloadListener(class_2960 class_2960Var, Function<class_7225.class_7874, IdentifiableResourceReloadListener> function);

    @Deprecated
    default void addReloadListener(IdentifiableResourceReloadListener listener) {
        registerReloadListener(listener);
    }

    static ResourceManagerHelper get(class_3264 type) {
        return ResourceManagerHelperImpl.get(type);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(class_2960 id, ModContainer container, ResourcePackActivationType activationType) {
        return ResourceLoader.registerBuiltinPack(id, container, activationType.replacement);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(class_2960 id, ModContainer container, class_2561 displayName, ResourcePackActivationType activationType) {
        return ResourceLoader.registerBuiltinPack(id, container, displayName, activationType.replacement);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(class_2960 id, ModContainer container, String displayName, ResourcePackActivationType activationType) {
        return ResourceLoader.registerBuiltinPack(id, container, class_2561.method_43470(displayName), activationType.replacement);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(class_2960 id, String subPath, ModContainer container, boolean enabledByDefault) {
        return ResourceLoaderImpl.registerBuiltinPack(id, subPath, container, class_2561.method_43470(id.method_12836() + "/" + id.method_12832()), enabledByDefault ? PackActivationType.DEFAULT_ENABLED : PackActivationType.NORMAL);
    }
}
