package net.fabricmc.fabric.api.resource;

import net.fabricmc.fabric.api.resource.v1.pack.ModPackResources;
import net.fabricmc.loader.api.metadata.ModMetadata;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/ModResourcePack.class */
@Deprecated
public interface ModResourcePack extends ModPackResources {
    @Override // net.fabricmc.fabric.api.resource.v1.pack.ModPackResources
    ModMetadata getFabricModMetadata();

    @Override // net.fabricmc.fabric.api.resource.v1.pack.ModPackResources
    ModResourcePack createOverlay(String str);
}
