package net.fabricmc.fabric.impl.resource;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.fabricmc.fabric.impl.resource.pack.ModNioPackResources;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_3264;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ServerLanguageUtil.class */
public final class ServerLanguageUtil {
    private static final String ASSETS_PREFIX = class_3264.field_14188.method_14413() + "/";

    private ServerLanguageUtil() {
    }

    public static Collection<Path> getModLanguageFiles() {
        Set<Path> paths = new LinkedHashSet<>();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (!mod.getMetadata().getType().equals("builtin")) {
                Map<class_3264, Set<String>> map = ModNioPackResources.readNamespaces(mod.getRootPaths(), mod.getMetadata().getId());
                for (String ns : map.get(class_3264.field_14188)) {
                    Optional optionalFilter = mod.findPath(ASSETS_PREFIX + ns + "/lang/en_us.json").filter(x$0 -> {
                        return Files.isRegularFile(x$0, new LinkOption[0]);
                    });
                    Objects.requireNonNull(paths);
                    optionalFilter.ifPresent((v1) -> {
                        r1.add(v1);
                    });
                }
            }
        }
        return Collections.unmodifiableCollection(paths);
    }
}
