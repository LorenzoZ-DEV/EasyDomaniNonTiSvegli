package net.fabricmc.fabric.impl.client.keybinding;

import java.util.Comparator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/impl/client/keybinding/CategoryComparator.class */
@Environment(EnvType.CLIENT)
public class CategoryComparator implements Comparator<class_304.class_11900> {
    public static final CategoryComparator INSTANCE = new CategoryComparator();

    @Override // java.util.Comparator
    public int compare(class_304.class_11900 o1, class_304.class_11900 o2) {
        boolean o1Vanilla = o1.comp_4879().method_12836().equals("minecraft");
        boolean o2Vanilla = o2.comp_4879().method_12836().equals("minecraft");
        if (o1Vanilla && o2Vanilla) {
            return 0;
        }
        if (o1Vanilla) {
            return -1;
        }
        if (o2Vanilla) {
            return 1;
        }
        int c = o1.comp_4879().method_12836().compareTo(o2.comp_4879().method_12836());
        if (c != 0) {
            return c;
        }
        return o1.comp_4879().method_12832().compareTo(o2.comp_4879().method_12832());
    }
}
