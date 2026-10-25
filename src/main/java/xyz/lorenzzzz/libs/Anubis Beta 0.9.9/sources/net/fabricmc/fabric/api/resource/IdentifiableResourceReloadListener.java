package net.fabricmc.fabric.api.resource;

import java.util.Collection;
import java.util.Collections;
import net.minecraft.class_2960;
import net.minecraft.class_3302;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/IdentifiableResourceReloadListener.class */
@Deprecated
public interface IdentifiableResourceReloadListener extends class_3302 {
    class_2960 getFabricId();

    default Collection<class_2960> getFabricDependencies() {
        return Collections.emptyList();
    }
}
