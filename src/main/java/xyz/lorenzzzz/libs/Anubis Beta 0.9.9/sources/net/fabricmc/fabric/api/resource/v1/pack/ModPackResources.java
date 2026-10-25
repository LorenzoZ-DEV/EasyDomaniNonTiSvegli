package net.fabricmc.fabric.api.resource.v1.pack;

import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.class_3262;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/pack/ModPackResources.class */
public interface ModPackResources extends class_3262 {
    ModMetadata getFabricModMetadata();

    ModPackResources createOverlay(String str);
}
