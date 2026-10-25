package net.fabricmc.fabric.api.resource.v1;

import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_7225;
import net.minecraft.class_7699;
import org.jetbrains.annotations.ApiStatus;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/ResourceLoader.class */
@ApiStatus.NonExtendable
public interface ResourceLoader {
    public static final class_3302.class_11559<class_7225.class_7874> RELOADER_REGISTRY_LOOKUP_KEY = new class_3302.class_11559<>();
    public static final class_3302.class_11559<class_7699> RELOADER_FEATURE_SET_KEY = new class_3302.class_11559<>();

    void registerReloader(class_2960 class_2960Var, class_3302 class_3302Var);

    void addReloaderOrdering(class_2960 class_2960Var, class_2960 class_2960Var2);

    static ResourceLoader get(class_3264 type) {
        return ResourceLoaderImpl.get(type);
    }

    static boolean registerBuiltinPack(class_2960 id, ModContainer container, PackActivationType activationType) {
        return ResourceLoaderImpl.registerBuiltinPack(id, "resourcepacks/" + id.method_12832(), container, activationType);
    }

    static boolean registerBuiltinPack(class_2960 id, ModContainer container, class_2561 displayName, PackActivationType activationType) {
        return ResourceLoaderImpl.registerBuiltinPack(id, "resourcepacks/" + id.method_12832(), container, displayName, activationType);
    }
}
