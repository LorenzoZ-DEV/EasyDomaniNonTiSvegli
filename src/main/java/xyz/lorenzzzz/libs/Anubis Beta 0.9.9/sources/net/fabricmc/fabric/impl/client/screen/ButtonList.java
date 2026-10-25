package net.fabricmc.fabric.impl.client.screen;

import java.util.AbstractList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_6379;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/screen/ButtonList.class */
@Environment(EnvType.CLIENT)
public final class ButtonList extends AbstractList<class_339> {
    private final List<class_4068> drawables;
    private final List<class_6379> selectables;
    private final List<class_364> children;

    public ButtonList(List<class_4068> drawables, List<class_6379> selectables, List<class_364> children) {
        this.drawables = drawables;
        this.selectables = selectables;
        this.children = children;
    }

    @Override // java.util.AbstractList, java.util.List
    public class_339 get(int index) {
        int drawableIndex = translateIndex(this.drawables, index, false);
        return this.drawables.get(drawableIndex);
    }

    @Override // java.util.AbstractList, java.util.List
    public class_339 set(int index, class_339 element) {
        int drawableIndex = translateIndex(this.drawables, index, false);
        this.drawables.set(drawableIndex, element);
        int selectableIndex = translateIndex(this.selectables, index, false);
        this.selectables.set(selectableIndex, element);
        int childIndex = translateIndex(this.children, index, false);
        return this.children.set(childIndex, element);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, class_339 element) {
        int duplicateIndex = this.drawables.indexOf(element);
        if (duplicateIndex >= 0) {
            this.drawables.remove(element);
            this.selectables.remove(element);
            this.children.remove(element);
            if (duplicateIndex <= translateIndex(this.drawables, index, true)) {
                index--;
            }
        }
        int drawableIndex = translateIndex(this.drawables, index, true);
        this.drawables.add(drawableIndex, element);
        int selectableIndex = translateIndex(this.selectables, index, true);
        this.selectables.add(selectableIndex, element);
        int childIndex = translateIndex(this.children, index, true);
        this.children.add(childIndex, element);
    }

    @Override // java.util.AbstractList, java.util.List
    public class_339 remove(int index) {
        class_339 removedButton = this.drawables.remove(translateIndex(this.drawables, index, false));
        this.selectables.remove(removedButton);
        this.children.remove(removedButton);
        return removedButton;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        int ret = 0;
        for (class_4068 drawable : this.drawables) {
            if (drawable instanceof class_339) {
                ret++;
            }
        }
        return ret;
    }

    private int translateIndex(List<?> list, int index, boolean allowAfter) {
        int remaining = index;
        int max = list.size();
        for (int i = 0; i < max; i++) {
            if (list.get(i) instanceof class_339) {
                if (remaining == 0) {
                    return i;
                }
                remaining--;
            }
        }
        if (allowAfter && remaining == 0) {
            return list.size();
        }
        throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", Integer.valueOf(index), Integer.valueOf(index - remaining)));
    }
}
