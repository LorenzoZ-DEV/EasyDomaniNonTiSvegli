package net.fabricmc.fabric.impl.resource;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.fabric.impl.base.toposort.NodeSorting;
import net.fabricmc.fabric.impl.base.toposort.SortableNode;
import net.fabricmc.fabric.impl.resource.pack.BuiltinModResourcePackSource;
import net.fabricmc.fabric.impl.resource.pack.ModNioPackResources;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3288;
import net.minecraft.class_3302;
import net.minecraft.class_8614;
import net.minecraft.class_9224;
import net.minecraft.class_9225;
import org.slf4j.Logger;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceLoaderImpl.class */
public class ResourceLoaderImpl implements ResourceLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<class_3264, ResourceLoaderImpl> IMPL_MAP = new EnumMap(class_3264.class);
    private static final Set<BuiltinPackResourcesEntry> BUILTIN_PACK_RESOURCES = new HashSet();
    private static final boolean DEBUG_RELOADERS_IDENTITY = TriState.fromSystemProperty("fabric.resource_loader.debug.reloaders_identity").orElse(FabricLoader.getInstance().isDevelopmentEnvironment());
    public static final boolean DEBUG_PROFILE_RESOURCE_RELOADERS = Boolean.getBoolean("fabric.resource_loader.debug.profile_resource_reloaders");
    private static final boolean DEBUG_RELOADERS_ORDER = Boolean.getBoolean("fabric.resource_loader.debug.reloaders_order");
    private final Map<class_2960, class_3302> addedReloaders = new LinkedHashMap();
    private final Set<ReloaderOrder> reloadersOrdering = new LinkedHashSet();
    private final class_3264 type;

    public static ResourceLoaderImpl get(class_3264 type) {
        return IMPL_MAP.computeIfAbsent(type, target -> {
            return target == class_3264.field_14190 ? DataResourceLoaderImpl.INSTANCE : new ResourceLoaderImpl(type);
        });
    }

    ResourceLoaderImpl(class_3264 type) {
        this.type = type;
    }

    protected boolean hasResourceReloader(class_2960 id) {
        return this.addedReloaders.containsKey(id);
    }

    protected final void checkUniqueResourceReloader(class_2960 id) {
        if (hasResourceReloader(id)) {
            throw new IllegalStateException("Tried to register resource reloader %s twice!".formatted(id));
        }
    }

    @Override // net.fabricmc.fabric.api.resource.v1.ResourceLoader
    public void registerReloader(class_2960 id, class_3302 reloader) {
        Objects.requireNonNull(id, "The reloader identifier should not be null.");
        Objects.requireNonNull(reloader, "The reloader should not be null.");
        checkUniqueResourceReloader(id);
        for (Map.Entry<class_2960, class_3302> entry : this.addedReloaders.entrySet()) {
            if (entry.getValue() == reloader) {
                throw new IllegalStateException("Resource reloader with ID %s already in resource reloader set with ID %s!".formatted(id, entry.getKey()));
            }
        }
        this.addedReloaders.put(id, reloader);
    }

    @Override // net.fabricmc.fabric.api.resource.v1.ResourceLoader
    public void addReloaderOrdering(class_2960 firstReloader, class_2960 secondReloader) {
        Objects.requireNonNull(firstReloader, "The first reloader identifier should not be null.");
        Objects.requireNonNull(secondReloader, "The second reloader identifier should not be null.");
        if (firstReloader.equals(secondReloader)) {
            throw new IllegalArgumentException("Tried to add a phase that depends on itself.");
        }
        this.reloadersOrdering.add(new ReloaderOrder(firstReloader, secondReloader));
    }

    private class_2960 getResourceReloaderIdForSorting(class_3302 reloader) {
        if (reloader instanceof FabricResourceReloader) {
            FabricResourceReloader identifiable = (FabricResourceReloader) reloader;
            return identifiable.fabric$getId();
        }
        if (DEBUG_RELOADERS_IDENTITY) {
            LOGGER.warn("The resource reloader at {} does not use identifiable registration making ordering support more difficult for other modders.", reloader.getClass().getName());
        }
        return class_2960.method_60655("unknown", "private/" + reloader.getClass().getName().replace(".", "/").replace("$", "_").toLowerCase(Locale.ROOT));
    }

    public static List<class_3302> sort(class_3264 type, List<class_3302> listeners) {
        if (type == null) {
            return listeners;
        }
        ResourceLoaderImpl instance = get(type);
        ArrayList<class_3302> mutable = new ArrayList<>(listeners);
        instance.sort(mutable);
        return Collections.unmodifiableList(mutable);
    }

    protected Set<Map.Entry<class_2960, class_3302>> collectReloadersToAdd(SetupMarkerResourceReloader setupMarker) {
        return new LinkedHashSet(this.addedReloaders.entrySet());
    }

    private void sort(List<class_3302> reloaders) {
        ResourceReloaderPhaseData second;
        SetupMarkerResourceReloader setupReloader = extractSetupMarker(reloaders);
        Set<Map.Entry<class_2960, class_3302>> reloadersToAdd = collectReloadersToAdd(setupReloader);
        Stream<R> map = reloadersToAdd.stream().map((v0) -> {
            return v0.getValue();
        });
        Objects.requireNonNull(reloaders);
        map.forEach((v1) -> {
            r1.remove(v1);
        });
        Object2ObjectOpenHashMap<class_2960, ResourceReloaderPhaseData> runtimePhases = new Object2ObjectOpenHashMap<>();
        ResourceReloaderPhaseData last = new ResourceReloaderPhaseData(ResourceReloaderKeys.BEFORE_VANILLA, null);
        last.setVanillaStatus(ResourceReloaderPhaseData.VanillaStatus.VANILLA);
        runtimePhases.put(last.id, last);
        for (class_3302 currentReloader : reloaders) {
            class_2960 id = getResourceReloaderIdForSorting(currentReloader);
            ResourceReloaderPhaseData current = new ResourceReloaderPhaseData(id, currentReloader);
            current.setVanillaStatus(ResourceReloaderPhaseData.VanillaStatus.VANILLA);
            runtimePhases.put(id, current);
            SortableNode.link(last, current);
            last = current;
        }
        ResourceReloaderPhaseData.AfterVanilla afterVanilla = new ResourceReloaderPhaseData.AfterVanilla(ResourceReloaderKeys.AFTER_VANILLA);
        runtimePhases.put(afterVanilla.id, afterVanilla);
        SortableNode.link(last, afterVanilla);
        for (Map.Entry<class_2960, class_3302> moddedReloader : reloadersToAdd) {
            ResourceReloaderPhaseData phase = new ResourceReloaderPhaseData(moddedReloader.getKey(), moddedReloader.getValue());
            runtimePhases.put(phase.id, phase);
        }
        for (ReloaderOrder order : this.reloadersOrdering) {
            ResourceReloaderPhaseData first = (ResourceReloaderPhaseData) runtimePhases.get(order.first);
            if (first != null && (second = (ResourceReloaderPhaseData) runtimePhases.get(order.second)) != null) {
                SortableNode.link(first, second);
            }
        }
        ObjectIterator it = runtimePhases.values().iterator();
        while (it.hasNext()) {
            ResourceReloaderPhaseData putAfter = (ResourceReloaderPhaseData) it.next();
            if (putAfter != afterVanilla && (putAfter.vanillaStatus == ResourceReloaderPhaseData.VanillaStatus.NONE || putAfter.vanillaStatus == ResourceReloaderPhaseData.VanillaStatus.AFTER)) {
                SortableNode.link(afterVanilla, putAfter);
            }
        }
        ArrayList<ResourceReloaderPhaseData> phases = new ArrayList<>((Collection<? extends ResourceReloaderPhaseData>) runtimePhases.values());
        NodeSorting.sort(phases, "resource reloaders", Comparator.comparing(data -> {
            return data.id;
        }));
        reloaders.clear();
        if (setupReloader != null) {
            reloaders.add(setupReloader);
        }
        for (ResourceReloaderPhaseData phase2 : phases) {
            if (phase2.resourceReloader != null) {
                reloaders.add(phase2.resourceReloader);
            }
        }
        if (DEBUG_RELOADERS_ORDER) {
            LOGGER.info("Sorted reloaders: {}", phases.stream().map(data2 -> {
                String str = data2.id.toString();
                if (data2.resourceReloader == null) {
                    str = str + " (virtual)";
                }
                return str;
            }).collect(Collectors.joining(", ")));
        }
    }

    private SetupMarkerResourceReloader extractSetupMarker(List<class_3302> reloaders) {
        if (this.type == class_3264.field_14188) {
            return null;
        }
        Iterator<class_3302> it = reloaders.iterator();
        while (it.hasNext()) {
            SetupMarkerResourceReloader next = it.next();
            if (next instanceof SetupMarkerResourceReloader) {
                SetupMarkerResourceReloader marker = next;
                it.remove();
                return marker;
            }
        }
        throw new IllegalStateException("No SetupMarkerResourceReloader found in reloaders!");
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder.class */
    private static final class ReloaderOrder extends Record {
        private final class_2960 first;
        private final class_2960 second;

        private ReloaderOrder(class_2960 first, class_2960 second) {
            this.first = first;
            this.second = second;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ReloaderOrder.class), ReloaderOrder.class, "first;second", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->first:Lnet/minecraft/class_2960;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->second:Lnet/minecraft/class_2960;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ReloaderOrder.class), ReloaderOrder.class, "first;second", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->first:Lnet/minecraft/class_2960;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->second:Lnet/minecraft/class_2960;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ReloaderOrder.class, Object.class), ReloaderOrder.class, "first;second", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->first:Lnet/minecraft/class_2960;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$ReloaderOrder;->second:Lnet/minecraft/class_2960;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2960 first() {
            return this.first;
        }

        public class_2960 second() {
            return this.second;
        }
    }

    public static boolean registerBuiltinPack(class_2960 id, String subPath, ModContainer container, class_2561 displayName, PackActivationType activationType) {
        List<Path> paths = container.getRootPaths();
        String separator = ((Path) paths.getFirst()).getFileSystem().getSeparator();
        String subPath2 = subPath.replace("/", separator);
        ModNioPackResources resourcePack = ModNioPackResources.create(id.toString(), container, subPath2, class_3264.field_14188, activationType, false);
        ModNioPackResources dataPack = ModNioPackResources.create(id.toString(), container, subPath2, class_3264.field_14190, activationType, false);
        if (resourcePack == null && dataPack == null) {
            return false;
        }
        if (resourcePack != null) {
            BUILTIN_PACK_RESOURCES.add(new BuiltinPackResourcesEntry(displayName, resourcePack));
        }
        if (dataPack != null) {
            BUILTIN_PACK_RESOURCES.add(new BuiltinPackResourcesEntry(displayName, dataPack));
            return true;
        }
        return true;
    }

    public static boolean registerBuiltinPack(class_2960 id, String subPath, ModContainer container, PackActivationType activationType) {
        return registerBuiltinPack(id, subPath, container, class_2561.method_43470(id.method_12836() + "/" + id.method_12832()), activationType);
    }

    public static void registerBuiltinResourcePacks(class_3264 type, Consumer<class_3288> consumer) {
        for (BuiltinPackResourcesEntry entry : BUILTIN_PACK_RESOURCES) {
            final ModNioPackResources pack = entry.packResources();
            if (!pack.method_14406(type).isEmpty()) {
                class_9224 info = new class_9224(pack.method_14409(), entry.displayName(), new BuiltinModResourcePackSource(pack.getFabricModMetadata().getName()), pack.method_56929());
                class_9225 selectionInfo = new class_9225(pack.getActivationType() == PackActivationType.ALWAYS_ENABLED, class_3288.class_3289.field_14280, false);
                class_3288 profile = class_3288.method_45275(info, new class_3288.class_7680() { // from class: net.fabricmc.fabric.impl.resource.ResourceLoaderImpl.1
                    public class_3262 method_52424(class_9224 location) {
                        return pack;
                    }

                    public class_3262 method_52425(class_9224 location, class_3288.class_7679 metadata) {
                        if (metadata.comp_1584().isEmpty()) {
                            return pack;
                        }
                        List<class_3262> overlays = new ArrayList<>(metadata.comp_1584().size());
                        for (String overlay : metadata.comp_1584()) {
                            overlays.add(pack.createOverlay(overlay));
                        }
                        return new class_8614(pack, overlays);
                    }
                }, type, selectionInfo);
                consumer.accept(profile);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry.class */
    private static final class BuiltinPackResourcesEntry extends Record {
        private final class_2561 displayName;
        private final ModNioPackResources packResources;

        private BuiltinPackResourcesEntry(class_2561 displayName, ModNioPackResources packResources) {
            this.displayName = displayName;
            this.packResources = packResources;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, BuiltinPackResourcesEntry.class), BuiltinPackResourcesEntry.class, "displayName;packResources", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->displayName:Lnet/minecraft/class_2561;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->packResources:Lnet/fabricmc/fabric/impl/resource/pack/ModNioPackResources;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, BuiltinPackResourcesEntry.class), BuiltinPackResourcesEntry.class, "displayName;packResources", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->displayName:Lnet/minecraft/class_2561;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->packResources:Lnet/fabricmc/fabric/impl/resource/pack/ModNioPackResources;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, BuiltinPackResourcesEntry.class, Object.class), BuiltinPackResourcesEntry.class, "displayName;packResources", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->displayName:Lnet/minecraft/class_2561;", "FIELD:Lnet/fabricmc/fabric/impl/resource/ResourceLoaderImpl$BuiltinPackResourcesEntry;->packResources:Lnet/fabricmc/fabric/impl/resource/pack/ModNioPackResources;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2561 displayName() {
            return this.displayName;
        }

        public ModNioPackResources packResources() {
            return this.packResources;
        }
    }
}
