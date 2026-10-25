package net.anubis.client.util.combat;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_8042;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/combat/TotemPops.class */
@Environment(EnvType.CLIENT)
public final class TotemPops {
    private TotemPops() {
    }

    public static void scan(class_2596<?> packet, Consumer<class_1309> sink) {
        if (packet instanceof class_2663) {
            class_2663 event = (class_2663) packet;
            check(event, sink);
        } else if (packet instanceof class_8042) {
            class_8042 bundle = (class_8042) packet;
            for (class_2663 class_2663Var : bundle.method_48324()) {
                if (class_2663Var instanceof class_2663) {
                    class_2663 event2 = class_2663Var;
                    check(event2, sink);
                }
            }
        }
    }

    private static void check(class_2663 event, Consumer<class_1309> sink) {
        class_638 level;
        if (event.method_11470() == 35 && (level = class_310.method_1551().field_1687) != null) {
            class_1297 entity = event.method_11469(level);
            if (entity instanceof class_1309) {
                class_1309 living = (class_1309) entity;
                if (!living.method_31481()) {
                    sink.accept(living);
                }
            }
        }
    }
}
