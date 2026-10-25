package net.fabricmc.fabric.impl.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_7225;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/DataResourceLoaderImpl.class */
public final class DataResourceLoaderImpl extends ResourceLoaderImpl implements DataResourceLoader {
    public static final DataResourceLoaderImpl INSTANCE = new DataResourceLoaderImpl();
    private final Map<class_2960, Function<class_7225.class_7874, class_3302>> addedReloaderFactories;

    private DataResourceLoaderImpl() {
        super(class_3264.field_14190);
        this.addedReloaderFactories = new LinkedHashMap();
    }

    @Override // net.fabricmc.fabric.impl.resource.ResourceLoaderImpl
    protected boolean hasResourceReloader(class_2960 id) {
        return super.hasResourceReloader(id) || this.addedReloaderFactories.containsKey(id);
    }

    @Override // net.fabricmc.fabric.api.resource.v1.DataResourceLoader
    public void registerReloader(class_2960 id, Function<class_7225.class_7874, class_3302> factory) {
        Objects.requireNonNull(id, "The reloader identifier should not be null.");
        Objects.requireNonNull(factory, "The reloader factory should not be null.");
        checkUniqueResourceReloader(id);
        for (Map.Entry<class_2960, Function<class_7225.class_7874, class_3302>> entry : this.addedReloaderFactories.entrySet()) {
            if (entry.getValue() == factory) {
                throw new IllegalStateException("Resource reloader factory with ID %s already in resource reloader factory set with ID %s!".formatted(id, entry.getKey()));
            }
        }
        this.addedReloaderFactories.put(id, factory);
    }

    @Override // net.fabricmc.fabric.impl.resource.ResourceLoaderImpl
    protected Set<Map.Entry<class_2960, class_3302>> collectReloadersToAdd(SetupMarkerResourceReloader setupMarker) {
        if (setupMarker == null) {
            throw new IllegalStateException("The setup marker should not be null for data resource loading.");
        }
        class_7225.class_7874 registries = setupMarker.registries();
        Set<Map.Entry<class_2960, class_3302>> reloadersToAdd = super.collectReloadersToAdd(setupMarker);
        for (Map.Entry<class_2960, Function<class_7225.class_7874, class_3302>> entry : this.addedReloaderFactories.entrySet()) {
            class_3302 reloader = entry.getValue().apply(registries);
            reloadersToAdd.add(Map.entry(entry.getKey(), reloader));
        }
        return reloadersToAdd;
    }
}
