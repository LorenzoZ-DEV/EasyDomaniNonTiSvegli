package net.fabricmc.fabric.impl.client.event.lifecycle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.impl.event.lifecycle.LoadedChunksCache;
import net.minecraft.class_2586;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/event/lifecycle/ClientLifecycleEventsImpl.class */
@Environment(EnvType.CLIENT)
public final class ClientLifecycleEventsImpl implements ClientModInitializer {
    public void onInitializeClient() {
        ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            ((LoadedChunksCache) world).fabric_markLoaded(chunk);
        });
        ClientChunkEvents.CHUNK_UNLOAD.register((world2, chunk2) -> {
            ((LoadedChunksCache) world2).fabric_markUnloaded(chunk2);
        });
        ClientChunkEvents.CHUNK_UNLOAD.register((world3, chunk3) -> {
            for (class_2586 blockEntity : chunk3.method_12214().values()) {
                ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world3);
            }
        });
    }
}
