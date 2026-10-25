package net.anubis.client.input;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleRevision;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/input/ModuleKeybinds.class */
@Environment(EnvType.CLIENT)
public final class ModuleKeybinds {
    public static final int MOUSE_OFFSET = 1000;
    private static final int KEY_COUNT = 1008;
    private int sampledCount;
    private int previousCount;
    private List<ClientModule> boundFrom;
    private long boundRevision;
    private final boolean[] previous = new boolean[1008];
    private final boolean[] current = new boolean[1008];
    private final boolean[] sampled = new boolean[1008];
    private int[] sampledKeys = new int[1008];
    private int[] previousKeys = new int[1008];
    private final Map<ClientModule, Integer> held = new IdentityHashMap();
    private boolean wasAcceptingInput = true;
    private final List<ClientModule> bound = new ArrayList();

    public void update(List<ClientModule> modules, IntPredicate isDown, boolean acceptInput) {
        List<ClientModule> candidates = boundModules(modules);
        for (int i = 0; i < this.previousCount; i++) {
            int key = this.previousKeys[i];
            this.current[key] = false;
            this.sampled[key] = false;
        }
        this.sampledCount = 0;
        Iterator<ClientModule> it = candidates.iterator();
        while (it.hasNext()) {
            int key2 = it.next().keybind();
            if (!this.sampled[key2]) {
                this.current[key2] = isDown.test(key2);
                this.sampled[key2] = true;
                int[] iArr = this.sampledKeys;
                int i2 = this.sampledCount;
                this.sampledCount = i2 + 1;
                iArr[i2] = key2;
            }
        }
        if (!this.held.isEmpty()) {
            Iterator<Map.Entry<ClientModule, Integer>> iterator = this.held.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ClientModule, Integer> entry = iterator.next();
                ClientModule module = entry.getKey();
                int key3 = entry.getValue().intValue();
                if (!acceptInput || !module.isHoldMode() || module.keybind() != key3 || !this.current[key3]) {
                    iterator.remove();
                    module.setEnabled(false);
                }
            }
        }
        if (acceptInput && this.wasAcceptingInput) {
            for (ClientModule module2 : candidates) {
                int key4 = module2.keybind();
                if (isBindable(key4) && this.current[key4] && !this.previous[key4]) {
                    if (module2.isAction()) {
                        module2.onClick();
                    } else if (module2.isHoldMode()) {
                        module2.setEnabled(true);
                        if (module2.isEnabled()) {
                            this.held.put(module2, Integer.valueOf(key4));
                        }
                    } else {
                        module2.toggle();
                    }
                }
            }
        }
        for (int i3 = 0; i3 < this.previousCount; i3++) {
            this.previous[this.previousKeys[i3]] = false;
        }
        for (int i4 = 0; i4 < this.sampledCount; i4++) {
            this.previous[this.sampledKeys[i4]] = this.current[this.sampledKeys[i4]];
        }
        int[] swap = this.previousKeys;
        this.previousKeys = this.sampledKeys;
        this.sampledKeys = swap;
        this.previousCount = this.sampledCount;
        this.wasAcceptingInput = acceptInput;
    }

    private List<ClientModule> boundModules(List<ClientModule> modules) {
        long revision = ModuleRevision.current();
        if (modules != this.boundFrom || revision != this.boundRevision) {
            this.bound.clear();
            for (ClientModule module : modules) {
                if (isBindable(module.keybind())) {
                    this.bound.add(module);
                }
            }
            this.boundFrom = modules;
            this.boundRevision = revision;
        }
        return this.bound;
    }

    public static boolean isBindable(int key) {
        return (key >= 32 && key <= 348) || (key >= 1000 && key < 1008);
    }
}
