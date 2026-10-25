package net.fabricmc.fabric.api.resource.v1;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/DataResourceStore.class */
public interface DataResourceStore {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/DataResourceStore$Key.class */
    public static final class Key<T> {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/DataResourceStore$Mutable.class */
    public interface Mutable extends DataResourceStore {
        <T> void put(Key<T> key, T t);
    }

    <T> T getOrThrow(Key<T> key);
}
