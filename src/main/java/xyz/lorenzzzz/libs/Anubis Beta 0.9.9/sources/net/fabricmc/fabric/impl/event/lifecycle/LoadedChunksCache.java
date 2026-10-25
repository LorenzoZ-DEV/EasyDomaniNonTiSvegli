package net.fabricmc.fabric.impl.event.lifecycle;

import java.util.Set;
import net.minecraft.class_2818;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/impl/event/lifecycle/LoadedChunksCache.class */
public interface LoadedChunksCache {
    Set<class_2818> fabric_getLoadedChunks();

    void fabric_markLoaded(class_2818 class_2818Var);

    void fabric_markUnloaded(class_2818 class_2818Var);
}
