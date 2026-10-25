package net.fabricmc.fabric.impl.resource;

import net.minecraft.class_2960;
import net.minecraft.class_3302;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/FabricResourceReloader.class */
public interface FabricResourceReloader extends class_3302 {
    class_2960 fabric$getId();

    default String method_22322() {
        return String.valueOf(fabric$getId()) + " (" + getClass().getSimpleName() + ")";
    }
}
