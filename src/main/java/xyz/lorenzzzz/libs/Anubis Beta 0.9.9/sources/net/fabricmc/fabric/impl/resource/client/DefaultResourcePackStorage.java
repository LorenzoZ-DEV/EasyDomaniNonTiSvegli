package net.fabricmc.fabric.impl.resource.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.fabricmc.fabric.impl.resource.pack.ModNioPackResources;
import net.fabricmc.fabric.impl.resource.pack.ModResourcePackCreator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2487;
import net.minecraft.class_2505;
import net.minecraft.class_2507;
import net.minecraft.class_2509;
import net.minecraft.class_3262;
import net.minecraft.class_3288;
import org.slf4j.Logger;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/client/DefaultResourcePackStorage.class */
@Environment(EnvType.CLIENT)
public final class DefaultResourcePackStorage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path DATA_DIR = FabricLoader.getInstance().getGameDir().resolve("data");
    private static final Path TRACKER_FILE_PATH = DATA_DIR.resolve("fabric_default_resource_packs.json");
    private static final Path OLD_TRACKER_FILE_PATH = DATA_DIR.resolve("fabricDefaultResourcePacks.dat");
    private static final Codec<Set<String>> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.STRING.listOf().fieldOf("values").forGetter((v0) -> {
            return List.copyOf(v0);
        })).apply(instance, (v0) -> {
            return Set.copyOf(v0);
        });
    });

    public static List<String> process(Collection<String> originalResourcePacks) {
        if (Files.notExists(DATA_DIR, new LinkOption[0])) {
            try {
                Files.createDirectories(DATA_DIR, new FileAttribute[0]);
            } catch (IOException e) {
                LOGGER.warn("[Fabric Resource Loader] Could not create data directory: {}", DATA_DIR.toAbsolutePath());
            }
        }
        HashSet<String> trackedPacks = new HashSet<>(read());
        HashSet<String> removedPacks = new HashSet<>(trackedPacks);
        LinkedHashSet<String> resourcePacks = new LinkedHashSet<>(originalResourcePacks);
        ArrayList<class_3288> profiles = new ArrayList<>();
        ModResourcePackCreator modResourcePackCreator = ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER;
        Objects.requireNonNull(profiles);
        modResourcePackCreator.method_14453((v1) -> {
            r1.add(v1);
        });
        for (class_3288 class_3288Var : profiles) {
            if (!((FabricPack) class_3288Var).fabric$isHidden()) {
                class_3262 pack = class_3288Var.method_14458();
                try {
                    if (pack instanceof ModNioPackResources) {
                        ModNioPackResources builtinPack = (ModNioPackResources) pack;
                        if (builtinPack.getActivationType().isEnabledByDefault()) {
                            if (trackedPacks.add(builtinPack.method_14409())) {
                                resourcePacks.add(class_3288Var.method_14463());
                            } else {
                                removedPacks.remove(builtinPack.method_14409());
                            }
                        }
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
        trackedPacks.removeAll(removedPacks);
        write(trackedPacks);
        return new ArrayList(resourcePacks);
    }

    private static Set<String> read() {
        if (Files.exists(TRACKER_FILE_PATH, new LinkOption[0])) {
            try {
                Reader fileReader = Files.newBufferedReader(TRACKER_FILE_PATH);
                try {
                    JsonReader reader = new JsonReader(fileReader);
                    try {
                        Set<String> set = (Set) CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow();
                        reader.close();
                        if (fileReader != null) {
                            fileReader.close();
                        }
                        return set;
                    } catch (Throwable th) {
                        try {
                            reader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                    }
                    throw th3;
                }
            } catch (Exception e) {
                LOGGER.warn("[Fabric Resource Loader] Could not read {}", TRACKER_FILE_PATH.toAbsolutePath(), e);
            }
        }
        if (Files.exists(OLD_TRACKER_FILE_PATH, new LinkOption[0])) {
            try {
                class_2487 data = class_2507.method_30613(OLD_TRACKER_FILE_PATH, class_2505.method_53898());
                return (Set) CODEC.parse(class_2509.field_11560, data).result().orElse(Set.of());
            } catch (Exception e2) {
                LOGGER.warn("[Fabric Resource Loader] Could not read {}", OLD_TRACKER_FILE_PATH.toAbsolutePath(), e2);
            }
        }
        return Set.of();
    }

    private static void write(Set<String> values) {
        try {
            Files.writeString(TRACKER_FILE_PATH, ((JsonElement) CODEC.encodeStart(JsonOps.INSTANCE, values).getOrThrow()).toString(), new OpenOption[0]);
        } catch (Exception e) {
            LOGGER.warn("[Fabric Resource Loader] Could not read {}", TRACKER_FILE_PATH.toAbsolutePath(), e);
        }
    }
}
