package net.fabricmc.fabric.impl.event.lifecycle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.class_1297;
import net.minecraft.class_2586;
import net.minecraft.class_2818;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/impl/event/lifecycle/LifecycleEventsImpl.class */
public final class LifecycleEventsImpl implements ModInitializer {
    public void onInitialize() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            ((LoadedChunksCache) world).fabric_markLoaded(chunk);
        });
        ServerChunkEvents.CHUNK_UNLOAD.register((world2, chunk2) -> {
            ((LoadedChunksCache) world2).fabric_markUnloaded(chunk2);
        });
        ServerChunkEvents.CHUNK_UNLOAD.register((world3, chunk3) -> {
            for (class_2586 blockEntity : chunk3.method_12214().values()) {
                ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world3);
            }
        });
        ServerWorldEvents.UNLOAD.register((server, world4) -> {
            for (class_2818 chunk4 : ((LoadedChunksCache) world4).fabric_getLoadedChunks()) {
                for (class_2586 blockEntity : chunk4.method_12214().values()) {
                    ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world4);
                }
            }
            for (class_1297 entity : world4.method_27909()) {
                ServerEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, world4);
            }
        });
    }
}
