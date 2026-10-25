package net.fabricmc.fabric.impl.resource.pack;

import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/pack/FabricPack.class */
public interface FabricPack {
    default boolean fabric$isHidden() {
        return false;
    }

    default boolean fabric$parentsEnabled(Set<String> enabled) {
        return true;
    }

    default void fabric$setParentsPredicate(Predicate<Set<String>> predicate) {
    }
}
