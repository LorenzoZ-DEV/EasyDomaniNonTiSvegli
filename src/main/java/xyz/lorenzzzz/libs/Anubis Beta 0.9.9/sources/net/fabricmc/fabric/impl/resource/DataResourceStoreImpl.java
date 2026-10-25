package net.fabricmc.fabric.impl.resource;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.fabric.api.resource.v1.DataResourceStore;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/DataResourceStoreImpl.class */
public final class DataResourceStoreImpl implements DataResourceStore.Mutable {
    private final Map<DataResourceStore.Key<?>, Object> store = new IdentityHashMap();

    @Override // net.fabricmc.fabric.api.resource.v1.DataResourceStore.Mutable
    public <T> void put(DataResourceStore.Key<T> key, T data) {
        this.store.put(key, data);
    }

    @Override // net.fabricmc.fabric.api.resource.v1.DataResourceStore
    public <T> T getOrThrow(DataResourceStore.Key<T> key) {
        return (T) Objects.requireNonNull(this.store.get(key));
    }
}
