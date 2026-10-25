package net.anubis.client.module;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleList.class */
@Environment(EnvType.CLIENT)
final class ModuleList extends AbstractList<ClientModule> implements RandomAccess {
    static final ModuleList EMPTY = new ModuleList(new ClientModule[0]);
    private final ClientModule[] modules;

    private ModuleList(ClientModule[] modules) {
        this.modules = modules;
    }

    static ModuleList of(Collection<ClientModule> modules) {
        return modules.isEmpty() ? EMPTY : new ModuleList((ClientModule[]) modules.toArray(new ClientModule[0]));
    }

    ModuleList with(ClientModule module) {
        ClientModule[] next = (ClientModule[]) Arrays.copyOf(this.modules, this.modules.length + 1);
        next[this.modules.length] = module;
        return new ModuleList(next);
    }

    ModuleList without(ClientModule module) {
        int index = indexOfIdentity(module);
        if (index < 0) {
            return this;
        }
        if (this.modules.length == 1) {
            return EMPTY;
        }
        ClientModule[] next = new ClientModule[this.modules.length - 1];
        System.arraycopy(this.modules, 0, next, 0, index);
        System.arraycopy(this.modules, index + 1, next, index, (this.modules.length - index) - 1);
        return new ModuleList(next);
    }

    boolean containsIdentity(ClientModule module) {
        return indexOfIdentity(module) >= 0;
    }

    private int indexOfIdentity(ClientModule module) {
        for (int i = 0; i < this.modules.length; i++) {
            if (this.modules[i] == module) {
                return i;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public ClientModule get(int index) {
        return this.modules[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.modules.length;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<ClientModule> iterator() {
        return new Iterator<ClientModule>() { // from class: net.anubis.client.module.ModuleList.1
            private int next;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next < ModuleList.this.modules.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public ClientModule next() {
                if (this.next >= ModuleList.this.modules.length) {
                    throw new NoSuchElementException();
                }
                ClientModule[] clientModuleArr = ModuleList.this.modules;
                int i = this.next;
                this.next = i + 1;
                return clientModuleArr[i];
            }
        };
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super ClientModule> action) {
        for (ClientModule module : this.modules) {
            action.accept(module);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return Arrays.copyOf(this.modules, this.modules.length, Object[].class);
    }
}
