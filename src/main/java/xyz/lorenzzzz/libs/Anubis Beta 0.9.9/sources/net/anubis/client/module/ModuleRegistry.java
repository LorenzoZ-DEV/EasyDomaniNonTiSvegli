package net.anubis.client.module;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleRegistry.class */
@Environment(EnvType.CLIENT)
public final class ModuleRegistry {
    private static volatile ModuleList modules;
    private static volatile Reference2ReferenceOpenHashMap<Class<?>, ClientModule> byClass;
    private static volatile Reference2ReferenceOpenHashMap<ModuleCategory, ModuleList> byCategory;
    private static int revision;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final List<ModuleProvider> PROVIDERS = new ArrayList(List.of(new BuiltinModuleProvider()));
    private static final List<ClientModule> STAGED = new ArrayList();
    private static final Map<String, ClientModule> byName = new ConcurrentHashMap();
    private static final Map<String, ClientModule> byId = new ConcurrentHashMap();
    private static volatile ModuleList enabled = ModuleList.EMPTY;
    private static volatile TypeIndex types = new TypeIndex(List.of());

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleRegistry$TypeIndex.class */
    @Environment(EnvType.CLIENT)
    private static final class TypeIndex extends Record {
        private final List<ClientModule> source;
        private final Map<Class<?>, List<?>> lists;

        private TypeIndex(List<ClientModule> source, Map<Class<?>, List<?>> lists) {
            this.source = source;
            this.lists = lists;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, TypeIndex.class), TypeIndex.class, "source;lists", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->source:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->lists:Ljava/util/Map;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, TypeIndex.class), TypeIndex.class, "source;lists", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->source:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->lists:Ljava/util/Map;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, TypeIndex.class, Object.class), TypeIndex.class, "source;lists", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->source:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/ModuleRegistry$TypeIndex;->lists:Ljava/util/Map;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<ClientModule> source() {
            return this.source;
        }

        public Map<Class<?>, List<?>> lists() {
            return this.lists;
        }

        TypeIndex(List<ClientModule> source) {
            this(source, new ConcurrentHashMap());
        }
    }

    private ModuleRegistry() {
    }

    public static synchronized void registerProvider(ModuleProvider provider) {
        if (modules != null) {
            throw new IllegalStateException("ModuleRegistry is frozen — providers must register during client init, before the first read");
        }
        PROVIDERS.add(provider);
    }

    public static List<ClientModule> all() {
        List<ClientModule> m = modules;
        if (m == null) {
            freeze();
            m = modules;
        }
        return m;
    }

    public static ClientModule byName(String name) {
        all();
        return byName.get(name);
    }

    public static ClientModule resolve(String key) {
        all();
        ClientModule module = byId.get(key);
        return module != null ? module : byName.get(key);
    }

    public static <T extends ClientModule> T get(Class<T> type) {
        Reference2ReferenceOpenHashMap<Class<?>, ClientModule> classes = byClass;
        if (classes == null) {
            all();
            classes = byClass;
        }
        return (T) classes.get(type);
    }

    public static List<ClientModule> inCategory(ModuleCategory category) {
        all();
        return category(category);
    }

    public static List<ClientModule> enabled() {
        all();
        return enabled;
    }

    public static <T> List<T> instancesOf(Class<T> cls) {
        List<ClientModule> listAll = all();
        TypeIndex typeIndex = types;
        if (typeIndex.source() != listAll) {
            typeIndex = new TypeIndex(listAll);
            types = typeIndex;
        }
        List<?> listCopyOf = typeIndex.lists().get(cls);
        if (listCopyOf == null) {
            ArrayList arrayList = new ArrayList();
            for (ClientModule clientModule : listAll) {
                if (cls.isInstance(clientModule)) {
                    arrayList.add(clientModule);
                }
            }
            listCopyOf = List.copyOf(arrayList);
            typeIndex.lists().put(cls, listCopyOf);
        }
        return (List<T>) listCopyOf;
    }

    public static int revision() {
        all();
        return revision;
    }

    public static synchronized boolean add(ClientModule module) {
        if (modules == null) {
            for (ClientModule staged : STAGED) {
                if (staged.id().equals(module.id()) || staged.name().equals(module.name())) {
                    return false;
                }
            }
            STAGED.add(module);
            return true;
        }
        if (byId.containsKey(module.id()) || byName.containsKey(module.name())) {
            return false;
        }
        module.captureDefaults();
        byName.put(module.name(), module);
        byId.put(module.id(), module);
        if (!byClass.containsKey(module.getClass())) {
            Reference2ReferenceOpenHashMap<Class<?>, ClientModule> classes = byClass.clone();
            classes.put(module.getClass(), module);
            byClass = classes;
        }
        replaceCategory(module.category(), category(module.category()).with(module));
        module.registered = true;
        if (module.isEnabled()) {
            enabled = enabled.with(module);
        }
        publish(modules.with(module));
        return true;
    }

    public static synchronized void remove(ClientModule module) {
        all();
        if (byId.get(module.id()) != module) {
            return;
        }
        ModuleList next = modules.without(module);
        byName.remove(module.name(), module);
        byId.remove(module.id(), module);
        if (byClass.get(module.getClass()) == module) {
            Reference2ReferenceOpenHashMap<Class<?>, ClientModule> classes = byClass.clone();
            classes.remove(module.getClass());
            for (ClientModule other : next) {
                if (other.getClass() == module.getClass()) {
                    classes.put(other.getClass(), other);
                    break;
                }
            }
            byClass = classes;
        }
        replaceCategory(module.category(), category(module.category()).without(module));
        module.registered = false;
        enabled = enabled.without(module);
        publish(next);
    }

    static synchronized void switched(ClientModule module) {
        if (module.registered) {
            if (module.isEnabled()) {
                if (!enabled.containsIdentity(module)) {
                    enabled = enabled.with(module);
                    return;
                }
                return;
            }
            enabled = enabled.without(module);
        }
    }

    private static synchronized void freeze() {
        if (modules != null) {
            return;
        }
        List<ClientModule> collected = new ArrayList<>();
        Set<String> names = new HashSet<>();
        Set<String> ids = new HashSet<>();
        for (ModuleProvider provider : PROVIDERS) {
            int before = collected.size();
            for (ClientModule module : provider.createModules()) {
                if (ids.contains(module.id())) {
                    throw new IllegalStateException("Duplicate module id: " + module.id());
                }
                if (!names.add(module.name())) {
                    LOGGER.warn("[Anubis] Duplicate module name '{}' from provider '{}' — skipped", module.name(), provider.id());
                } else {
                    module.captureDefaults();
                    collected.add(module);
                    ids.add(module.id());
                }
            }
            LOGGER.info("[Anubis] Module provider '{}' contributed {} modules", provider.id(), Integer.valueOf(collected.size() - before));
        }
        int before2 = collected.size();
        for (ClientModule module2 : STAGED) {
            if (ids.contains(module2.id()) || !names.add(module2.name())) {
                LOGGER.warn("[Anubis] Module '{}' ({}) clashes with a provided module's name or id — skipped", module2.name(), module2.id());
            } else {
                module2.captureDefaults();
                collected.add(module2);
                ids.add(module2.id());
            }
        }
        if (!STAGED.isEmpty()) {
            LOGGER.info("[Anubis] Addons contributed {} modules", Integer.valueOf(collected.size() - before2));
        }
        STAGED.clear();
        Reference2ReferenceOpenHashMap<Class<?>, ClientModule> classes = new Reference2ReferenceOpenHashMap<>();
        Map<ModuleCategory, List<ClientModule>> grouped = new LinkedHashMap<>();
        List<ClientModule> on = new ArrayList<>();
        for (ClientModule module3 : collected) {
            byName.put(module3.name(), module3);
            byId.put(module3.id(), module3);
            classes.putIfAbsent(module3.getClass(), module3);
            grouped.computeIfAbsent(module3.category(), category -> {
                return new ArrayList();
            }).add(module3);
            module3.registered = true;
            if (module3.isEnabled()) {
                on.add(module3);
            }
        }
        Reference2ReferenceOpenHashMap<ModuleCategory, ModuleList> categories = new Reference2ReferenceOpenHashMap<>();
        grouped.forEach((category2, list) -> {
            categories.put(category2, ModuleList.of((Collection<ClientModule>) list));
        });
        byClass = classes;
        byCategory = categories;
        enabled = ModuleList.of((Collection<ClientModule>) on);
        publish(ModuleList.of((Collection<ClientModule>) collected));
    }

    private static void publish(ModuleList ordered) {
        revision++;
        ModuleRevision.changed();
        modules = ordered;
    }

    private static ModuleList category(ModuleCategory category) {
        ModuleList list = (ModuleList) byCategory.get(category);
        return list != null ? list : ModuleList.EMPTY;
    }

    private static void replaceCategory(ModuleCategory category, ModuleList list) {
        Reference2ReferenceOpenHashMap<ModuleCategory, ModuleList> categories = byCategory.clone();
        if (list.isEmpty()) {
            categories.remove(category);
        } else {
            categories.put(category, list);
        }
        byCategory = categories;
    }
}
