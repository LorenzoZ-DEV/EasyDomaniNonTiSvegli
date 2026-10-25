package net.fabricmc.fabric.api.resource;

import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/ResourcePackActivationType.class */
@Deprecated
public enum ResourcePackActivationType {
    NORMAL(PackActivationType.NORMAL),
    DEFAULT_ENABLED(PackActivationType.DEFAULT_ENABLED),
    ALWAYS_ENABLED(PackActivationType.ALWAYS_ENABLED);

    final PackActivationType replacement;

    ResourcePackActivationType(PackActivationType replacement) {
        this.replacement = replacement;
    }

    public boolean isEnabledByDefault() {
        return this == DEFAULT_ENABLED || this == ALWAYS_ENABLED;
    }
}
