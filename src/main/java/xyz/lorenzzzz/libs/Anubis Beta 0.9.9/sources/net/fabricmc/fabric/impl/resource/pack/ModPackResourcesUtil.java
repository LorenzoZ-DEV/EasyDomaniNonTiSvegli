package net.fabricmc.fabric.impl.resource.pack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.resource.v1.pack.ModPackResources;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.class_11555;
import net.minecraft.class_155;
import net.minecraft.class_2561;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3272;
import net.minecraft.class_3283;
import net.minecraft.class_3285;
import net.minecraft.class_3286;
import net.minecraft.class_3288;
import net.minecraft.class_5359;
import net.minecraft.class_6497;
import net.minecraft.class_7701;
import net.minecraft.class_7712;
import net.minecraft.class_8580;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/pack/ModPackResourcesUtil.class */
public final class ModPackResourcesUtil {
    public static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(ModPackResourcesUtil.class);
    private static final String LOAD_ORDER_KEY = "fabric:resource_load_order";

    private ModPackResourcesUtil() {
    }

    public static List<ModPackResources> getModResourcePacks(FabricLoader fabricLoader, class_3264 type, String subPath) {
        ModPackResources pack;
        ModPackResourcesSorter sorter = new ModPackResourcesSorter();
        Collection<ModContainer> containers = fabricLoader.getAllMods();
        List<String> allIds = containers.stream().map((v0) -> {
            return v0.getMetadata();
        }).map((v0) -> {
            return v0.getId();
        }).toList();
        for (ModContainer container : containers) {
            ModMetadata metadata = container.getMetadata();
            String id = metadata.getId();
            if (!metadata.getType().equals("builtin") && (pack = ModNioPackResources.create(id, container, subPath, type, PackActivationType.ALWAYS_ENABLED, true)) != null) {
                sorter.addPack(pack);
                CustomValue loadOrder = metadata.getCustomValue(LOAD_ORDER_KEY);
                if (loadOrder != null) {
                    if (loadOrder.getType() == CustomValue.CvType.OBJECT) {
                        CustomValue.CvObject object = loadOrder.getAsObject();
                        addLoadOrdering(object, allIds, sorter, Order.BEFORE, id);
                        addLoadOrdering(object, allIds, sorter, Order.AFTER, id);
                    } else {
                        LOGGER.error("[Fabric] Resource load order should be an object");
                    }
                }
            }
        }
        return sorter.getPacks();
    }

    public static void addLoadOrdering(CustomValue.CvObject object, List<String> allIds, ModPackResourcesSorter sorter, Order order, String currentId) {
        ArrayList<String> modIds = new ArrayList<>();
        CustomValue array = object.get(order.jsonKey);
        if (array == null) {
            return;
        }
        switch (AnonymousClass1.$SwitchMap$net$fabricmc$loader$api$metadata$CustomValue$CvType[array.getType().ordinal()]) {
            case 1:
                modIds.add(array.getAsString());
                break;
            case 2:
                for (CustomValue id : array.getAsArray()) {
                    if (id.getType() == CustomValue.CvType.STRING) {
                        modIds.add(id.getAsString());
                    }
                }
                break;
            default:
                LOGGER.error("[Fabric] {} should be a string or an array", order.jsonKey);
                return;
        }
        Stream stream = modIds.stream();
        Objects.requireNonNull(allIds);
        stream.filter((v1) -> {
            return r1.contains(v1);
        }).forEach(modId -> {
            sorter.addLoadOrdering(modId, currentId, order);
        });
    }

    /* JADX INFO: renamed from: net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/pack/ModPackResourcesUtil$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$fabricmc$loader$api$metadata$CustomValue$CvType = new int[CustomValue.CvType.values().length];

        static {
            try {
                $SwitchMap$net$fabricmc$loader$api$metadata$CustomValue$CvType[CustomValue.CvType.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$fabricmc$loader$api$metadata$CustomValue$CvType[CustomValue.CvType.ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public static void refreshAutoEnabledPacks(List<class_3288> enabledProfiles, Map<String, class_3288> allProfiles) {
        LOGGER.debug("[Fabric] Starting internal pack sorting with: {}", enabledProfiles.stream().map((v0) -> {
            return v0.method_14463();
        }).toList());
        enabledProfiles.removeIf(profile -> {
            return ((FabricPack) profile).fabric$isHidden();
        });
        LOGGER.debug("[Fabric] Removed all internal packs, result: {}", enabledProfiles.stream().map((v0) -> {
            return v0.method_14463();
        }).toList());
        ListIterator<class_3288> it = enabledProfiles.listIterator();
        Set<String> seen = new LinkedHashSet<>();
        while (it.hasNext()) {
            class_3288 profile2 = it.next();
            seen.add(profile2.method_14463());
            Iterator<class_3288> it2 = allProfiles.values().iterator();
            while (it2.hasNext()) {
                FabricPack fabricPack = (class_3288) it2.next();
                FabricPack fp = fabricPack;
                if (fp.fabric$isHidden() && fp.fabric$parentsEnabled(seen) && seen.add(fabricPack.method_14463())) {
                    it.add(fabricPack);
                    LOGGER.debug("[Fabric] cur @ {}, auto-enabled {}, currently enabled: {}", new Object[]{profile2.method_14463(), fabricPack.method_14463(), seen});
                }
            }
        }
        LOGGER.debug("[Fabric] Final sorting result: {}", enabledProfiles.stream().map((v0) -> {
            return v0.method_14463();
        }).toList());
    }

    public static boolean containsDefault(String filename, boolean modBundled) {
        return "pack.mcmeta".equals(filename) || (modBundled && "pack.png".equals(filename));
    }

    public static InputStream getDefaultIcon() throws IOException {
        Optional<Path> loaderIconPath = FabricLoader.getInstance().getModContainer("fabric-resource-loader-v1").flatMap(resourceLoaderContainer -> {
            Optional iconPath = resourceLoaderContainer.getMetadata().getIconPath(512);
            Objects.requireNonNull(resourceLoaderContainer);
            return iconPath.flatMap(resourceLoaderContainer::findPath);
        });
        if (loaderIconPath.isPresent()) {
            return Files.newInputStream(loaderIconPath.get(), new OpenOption[0]);
        }
        return null;
    }

    public static InputStream openDefault(ModContainer container, class_3264 type, String filename) throws IOException {
        switch (filename) {
            case "pack.mcmeta":
                String description = (String) Objects.requireNonNullElse(container.getMetadata().getId(), "");
                String metadata = serializeMetadata(class_155.method_16673().method_70592(type), description, type);
                return IOUtils.toInputStream(metadata, StandardCharsets.UTF_8);
            case "pack.png":
                Optional iconPath = container.getMetadata().getIconPath(512);
                Objects.requireNonNull(container);
                Optional<Path> path = iconPath.flatMap(container::findPath);
                if (path.isPresent()) {
                    return Files.newInputStream(path.get(), new OpenOption[0]);
                }
                return getDefaultIcon();
            default:
                return null;
        }
    }

    public static class_3272 getMetadataPack(class_11555 packVersion, class_2561 description) {
        return new class_3272(description, new class_6497(packVersion));
    }

    public static JsonObject getMetadataPackJson(class_11555 packVersion, class_2561 description, class_3264 type) {
        return ((JsonElement) class_3272.method_72360(type).encodeStart(JsonOps.INSTANCE, getMetadataPack(packVersion, description)).getOrThrow()).getAsJsonObject();
    }

    public static String serializeMetadata(class_11555 packVersion, String description, class_3264 type) {
        JsonObject pack = getMetadataPackJson(packVersion, class_2561.method_43470(description), type);
        JsonObject metadata = new JsonObject();
        metadata.add("pack", pack);
        return GSON.toJson(metadata);
    }

    public static class_2561 getName(ModMetadata info) {
        if (info.getId() != null) {
            return class_2561.method_43470(info.getId());
        }
        return class_2561.method_43469("pack.name.fabricMod", new Object[]{info.getId()});
    }

    /* JADX WARN: Code duplicated, block: B:14:0x00a0 A[Catch: Throwable -> 0x00b9, TryCatch #1 {Throwable -> 0x00b9, blocks: (B:9:0x0079, B:11:0x0081, B:13:0x0093, B:14:0x00a0), top: B:31:0x0079 }] */
    public static class_7712 createDefaultDataConfiguration() {
        ModResourcePackCreator modResourcePackCreator = new ModResourcePackCreator(class_3264.field_14190);
        ArrayList<class_3288> moddedResourcePacks = new ArrayList<>();
        Objects.requireNonNull(moddedResourcePacks);
        modResourcePackCreator.method_14453((v1) -> {
            r1.add(v1);
        });
        ArrayList<String> enabled = new ArrayList<>(class_5359.field_25393.method_29547());
        ArrayList<String> disabled = new ArrayList<>(class_5359.field_25393.method_29550());
        for (class_3288 profile : moddedResourcePacks) {
            if (profile.method_29483() == ModResourcePackCreator.RESOURCE_PACK_SOURCE) {
                enabled.add(profile.method_14463());
            } else {
                class_3262 pack = profile.method_14458();
                try {
                    if (pack instanceof ModNioPackResources) {
                        ModNioPackResources nioPack = (ModNioPackResources) pack;
                        if (nioPack.getActivationType().isEnabledByDefault()) {
                            enabled.add(profile.method_14463());
                        } else {
                            disabled.add(profile.method_14463());
                        }
                    } else {
                        disabled.add(profile.method_14463());
                    }
                    if (pack != null) {
                        pack.close();
                    }
                } catch (Throwable th) {
                    if (pack != null) {
                        try {
                            pack.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
        }
        return new class_7712(new class_5359(enabled, disabled), class_7701.field_40183);
    }

    public static class_5359 createTestServerSettings(List<String> enabled, List<String> disabled) {
        HashSet<String> moddedProfiles = new HashSet<>();
        ModResourcePackCreator modResourcePackCreator = new ModResourcePackCreator(class_3264.field_14190);
        modResourcePackCreator.method_14453(profile -> {
            moddedProfiles.add(profile.method_14463());
        });
        ArrayList<String> moveToTheEnd = new ArrayList<>();
        Iterator<String> it = enabled.iterator();
        while (it.hasNext()) {
            String profile2 = it.next();
            if (moddedProfiles.contains(profile2)) {
                moveToTheEnd.add(profile2);
                it.remove();
            }
        }
        enabled.addAll(moveToTheEnd);
        return new class_5359(enabled, disabled);
    }

    public static class_3283 createClientManager() {
        return new class_3283(new class_3285[]{new class_3286(new class_8580(path -> {
            return true;
        })), new ModResourcePackCreator(class_3264.field_14190, true)});
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/pack/ModPackResourcesUtil$Order.class */
    public enum Order {
        BEFORE("before"),
        AFTER("after");

        private final String jsonKey;

        Order(String jsonKey) {
            this.jsonKey = jsonKey;
        }
    }
}
