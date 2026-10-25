package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_3218;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerEntityEvents.class */
public final class ServerEntityEvents {
    public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (entity, world) -> {
            for (Load callback : callbacks) {
                callback.onLoad(entity, world);
            }
        };
    });
    public static final Event<Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (entity, world) -> {
            for (Unload callback : callbacks) {
                callback.onUnload(entity, world);
            }
        };
    });
    public static final Event<EquipmentChange> EQUIPMENT_CHANGE = EventFactory.createArrayBacked(EquipmentChange.class, callbacks -> {
        return (livingEntity, equipmentSlot, previous, next) -> {
            for (EquipmentChange callback : callbacks) {
                callback.onChange(livingEntity, equipmentSlot, previous, next);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerEntityEvents$EquipmentChange.class */
    @FunctionalInterface
    public interface EquipmentChange {
        void onChange(class_1309 class_1309Var, class_1304 class_1304Var, class_1799 class_1799Var, class_1799 class_1799Var2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerEntityEvents$Load.class */
    @FunctionalInterface
    public interface Load {
        void onLoad(class_1297 class_1297Var, class_3218 class_3218Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerEntityEvents$Unload.class */
    @FunctionalInterface
    public interface Unload {
        void onUnload(class_1297 class_1297Var, class_3218 class_3218Var);
    }

    private ServerEntityEvents() {
    }
}
