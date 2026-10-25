package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_3222;
import net.minecraft.class_6860;
import net.minecraft.server.MinecraftServer;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents.class */
public final class ServerLifecycleEvents {
    public static final Event<ServerStarting> SERVER_STARTING = EventFactory.createArrayBacked(ServerStarting.class, callbacks -> {
        return server -> {
            for (ServerStarting callback : callbacks) {
                callback.onServerStarting(server);
            }
        };
    });
    public static final Event<ServerStarted> SERVER_STARTED = EventFactory.createArrayBacked(ServerStarted.class, callbacks -> {
        return server -> {
            for (ServerStarted callback : callbacks) {
                callback.onServerStarted(server);
            }
        };
    });
    public static final Event<ServerStopping> SERVER_STOPPING = EventFactory.createArrayBacked(ServerStopping.class, callbacks -> {
        return server -> {
            for (ServerStopping callback : callbacks) {
                callback.onServerStopping(server);
            }
        };
    });
    public static final Event<ServerStopped> SERVER_STOPPED = EventFactory.createArrayBacked(ServerStopped.class, callbacks -> {
        return server -> {
            for (ServerStopped callback : callbacks) {
                callback.onServerStopped(server);
            }
        };
    });
    public static final Event<SyncDataPackContents> SYNC_DATA_PACK_CONTENTS = EventFactory.createArrayBacked(SyncDataPackContents.class, callbacks -> {
        return (player, joined) -> {
            for (SyncDataPackContents callback : callbacks) {
                callback.onSyncDataPackContents(player, joined);
            }
        };
    });
    public static final Event<StartDataPackReload> START_DATA_PACK_RELOAD = EventFactory.createArrayBacked(StartDataPackReload.class, callbacks -> {
        return (server, serverResourceManager) -> {
            for (StartDataPackReload callback : callbacks) {
                callback.startDataPackReload(server, serverResourceManager);
            }
        };
    });
    public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = EventFactory.createArrayBacked(EndDataPackReload.class, callbacks -> {
        return (server, serverResourceManager, success) -> {
            for (EndDataPackReload callback : callbacks) {
                callback.endDataPackReload(server, serverResourceManager, success);
            }
        };
    });
    public static final Event<BeforeSave> BEFORE_SAVE = EventFactory.createArrayBacked(BeforeSave.class, callbacks -> {
        return (server, flush, force) -> {
            for (BeforeSave callback : callbacks) {
                callback.onBeforeSave(server, flush, force);
            }
        };
    });
    public static final Event<AfterSave> AFTER_SAVE = EventFactory.createArrayBacked(AfterSave.class, callbacks -> {
        return (server, flush, force) -> {
            for (AfterSave callback : callbacks) {
                callback.onAfterSave(server, flush, force);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$AfterSave.class */
    @FunctionalInterface
    public interface AfterSave {
        void onAfterSave(MinecraftServer minecraftServer, boolean z, boolean z2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$BeforeSave.class */
    @FunctionalInterface
    public interface BeforeSave {
        void onBeforeSave(MinecraftServer minecraftServer, boolean z, boolean z2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$EndDataPackReload.class */
    @FunctionalInterface
    public interface EndDataPackReload {
        void endDataPackReload(MinecraftServer minecraftServer, class_6860 class_6860Var, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$ServerStarted.class */
    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$ServerStarting.class */
    @FunctionalInterface
    public interface ServerStarting {
        void onServerStarting(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$ServerStopped.class */
    @FunctionalInterface
    public interface ServerStopped {
        void onServerStopped(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$ServerStopping.class */
    @FunctionalInterface
    public interface ServerStopping {
        void onServerStopping(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$StartDataPackReload.class */
    @FunctionalInterface
    public interface StartDataPackReload {
        void startDataPackReload(MinecraftServer minecraftServer, class_6860 class_6860Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerLifecycleEvents$SyncDataPackContents.class */
    @FunctionalInterface
    public interface SyncDataPackContents {
        void onSyncDataPackContents(class_3222 class_3222Var, boolean z);
    }

    private ServerLifecycleEvents() {
    }
}
