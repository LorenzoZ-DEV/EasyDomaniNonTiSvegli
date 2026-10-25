package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.impl.event.lifecycle.LoadedChunksCache;
import net.minecraft.class_1937;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/LevelMixin.class */
@Mixin({class_1937.class})
public abstract class LevelMixin implements LoadedChunksCache {

    @Unique
    private final Set<class_2818> loadedChunks = new HashSet();

    @Override // net.fabricmc.fabric.impl.event.lifecycle.LoadedChunksCache
    public Set<class_2818> fabric_getLoadedChunks() {
        return this.loadedChunks;
    }

    @Override // net.fabricmc.fabric.impl.event.lifecycle.LoadedChunksCache
    public void fabric_markLoaded(class_2818 chunk) {
        this.loadedChunks.add(chunk);
    }

    @Override // net.fabricmc.fabric.impl.event.lifecycle.LoadedChunksCache
    public void fabric_markUnloaded(class_2818 chunk) {
        this.loadedChunks.remove(chunk);
    }
}
