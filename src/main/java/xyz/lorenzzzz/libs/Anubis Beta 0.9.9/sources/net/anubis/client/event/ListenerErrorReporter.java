package net.anubis.client.event;

import java.util.IdentityHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/ListenerErrorReporter.class */
@Environment(EnvType.CLIENT)
final class ListenerErrorReporter {
    private static final int FULL_TRACE_LIMIT = 4;
    private static final int SUMMARY_INTERVAL = 256;
    private static final Map<ListenerHandle<?>, Integer> failureCounts = new IdentityHashMap();

    private ListenerErrorReporter() {
    }

    static synchronized void report(ListenerHandle<?> handle, Event event, Exception ex) {
        String name;
        int count = failureCounts.merge(handle, 1, (v0, v1) -> {
            return Integer.sum(v0, v1);
        }).intValue();
        if (handle.owner() == null) {
            name = "<static>";
        } else {
            name = handle.owner().getClass().getName();
        }
        String owner = name;
        if (count <= 4) {
            System.err.println("[AnubisEventBus] Listener threw (failure #" + count + "): owner=" + owner + " event=" + event.getClass().getSimpleName() + " stage=" + String.valueOf(handle.stage()) + " priority=" + String.valueOf(handle.priority()));
            ex.printStackTrace();
        } else if (count % 256 == 0) {
            System.err.println("[AnubisEventBus] Listener still failing (" + count + " total): owner=" + owner + " event=" + event.getClass().getSimpleName() + ": " + String.valueOf(ex));
        }
    }
}
