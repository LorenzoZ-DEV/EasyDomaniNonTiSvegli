package net.fabricmc.fabric.impl.resource.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_7225;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/impl/resource/loader/ResourceManagerHelperImpl.class */
public class ResourceManagerHelperImpl implements ResourceManagerHelper {
    private static final Map<class_3264, ResourceManagerHelperImpl> registryMap = new HashMap();
    private final ResourceLoader resourceLoader;

    private ResourceManagerHelperImpl(class_3264 type) {
        this.resourceLoader = ResourceLoader.get(type);
    }

    public static ResourceManagerHelperImpl get(class_3264 type) {
        return registryMap.computeIfAbsent(type, ResourceManagerHelperImpl::new);
    }

    @Override // net.fabricmc.fabric.api.resource.ResourceManagerHelper
    public void registerReloadListener(IdentifiableResourceReloadListener listener) {
        this.resourceLoader.registerReloader(listener.getFabricId(), listener);
        listener.getFabricDependencies().forEach(dependency -> {
            this.resourceLoader.addReloaderOrdering(dependency, listener.getFabricId());
        });
    }

    @Override // net.fabricmc.fabric.api.resource.ResourceManagerHelper
    public void registerReloadListener(class_2960 identifier, final Function<class_7225.class_7874, IdentifiableResourceReloadListener> listenerFactory) {
        this.resourceLoader.registerReloader(identifier, new class_3302(this) { // from class: net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl.1
            public CompletableFuture<Void> method_25931(class_3302.class_11558 store, Executor prepareExecutor, class_3302.class_4045 reloadSynchronizer, Executor applyExecutor) {
                class_7225.class_7874 registries = (class_7225.class_7874) store.method_72362(ResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY);
                class_3302 resourceReloader = (class_3302) listenerFactory.apply(registries);
                return resourceReloader.method_25931(store, prepareExecutor, reloadSynchronizer, applyExecutor);
            }
        });
    }
}
