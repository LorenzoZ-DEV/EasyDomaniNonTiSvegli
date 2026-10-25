package net.fabricmc.fabric.impl.resource;

import java.util.WeakHashMap;
import net.minecraft.class_3262;
import net.minecraft.class_5352;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/PackSourceTracker.class */
public final class PackSourceTracker {
    private static final WeakHashMap<class_3262, class_5352> SOURCES = new WeakHashMap<>();

    public static class_5352 getSource(class_3262 pack) {
        return SOURCES.getOrDefault(pack, class_5352.field_25347);
    }

    public static void setSource(class_3262 pack, class_5352 source) {
        SOURCES.put(pack, source);
    }
}
