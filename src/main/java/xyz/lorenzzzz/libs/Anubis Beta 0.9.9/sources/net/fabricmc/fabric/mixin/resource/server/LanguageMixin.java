package net.fabricmc.fabric.mixin.resource.server;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.impl.resource.ServerLanguageUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2477;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/server/LanguageMixin.class */
@Mixin({class_2477.class})
class LanguageMixin {

    @Shadow
    @Final
    private static Logger field_11490;

    LanguageMixin() {
    }

    @Redirect(method = {"method_29429()Lnet/minecraft/class_2477;"}, at = @At(value = "INVOKE", target = "Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;"))
    private static Map<String, String> create(Map<String, String> map) {
        for (Path path : ServerLanguageUtil.getModLanguageFiles()) {
            Objects.requireNonNull(map);
            loadFromPath(path, (v1, v2) -> {
                r1.put(v1, v2);
            });
        }
        return ImmutableMap.copyOf(map);
    }

    @Redirect(method = {"method_51465(Ljava/util/function/BiConsumer;Ljava/lang/String;)V"}, at = @At(value = "INVOKE", target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"))
    private static InputStream readCorrectVanillaResource(Class instance, String path) throws IOException {
        ModContainer mod = (ModContainer) FabricLoader.getInstance().getModContainer("minecraft").orElseThrow();
        Path langPath = (Path) mod.findPath(path).orElse(null);
        if (langPath == null) {
            throw new IOException("Could not read %s from minecraft ModContainer".formatted(path));
        }
        return Files.newInputStream(langPath, new OpenOption[0]);
    }

    @Unique
    private static void loadFromPath(Path path, BiConsumer<String, String> entryConsumer) {
        try {
            InputStream stream = Files.newInputStream(path, new OpenOption[0]);
            try {
                field_11490.debug("Loading translations from {}", path);
                method_29425(stream, entryConsumer);
                if (stream != null) {
                    stream.close();
                }
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (JsonParseException | IOException e) {
            field_11490.error("Couldn't read strings from {}", path, e);
        }
    }

    @Shadow
    public static void method_29425(InputStream inputStream, BiConsumer<String, String> entryConsumer) {
    }
}
