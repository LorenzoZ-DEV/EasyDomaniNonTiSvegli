package net.fabricmc.fabric.api.resource.v1.pack;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/pack/PackActivationType.class */
public enum PackActivationType {
    NORMAL,
    DEFAULT_ENABLED,
    ALWAYS_ENABLED;

    public boolean isEnabledByDefault() {
        return this == DEFAULT_ENABLED || this == ALWAYS_ENABLED;
    }
}
