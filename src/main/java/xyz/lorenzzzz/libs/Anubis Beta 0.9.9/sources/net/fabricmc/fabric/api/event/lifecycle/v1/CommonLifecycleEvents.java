package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_5455;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/CommonLifecycleEvents.class */
public final class CommonLifecycleEvents {
    public static final Event<TagsLoaded> TAGS_LOADED = EventFactory.createArrayBacked(TagsLoaded.class, callbacks -> {
        return (registries, client) -> {
            for (TagsLoaded callback : callbacks) {
                callback.onTagsLoaded(registries, client);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/CommonLifecycleEvents$TagsLoaded.class */
    public interface TagsLoaded {
        void onTagsLoaded(class_5455 class_5455Var, boolean z);
    }

    private CommonLifecycleEvents() {
    }
}
