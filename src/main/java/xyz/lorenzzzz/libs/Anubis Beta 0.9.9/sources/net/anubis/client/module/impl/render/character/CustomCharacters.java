package net.anubis.client.module.impl.render.character;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.module.impl.render.character.gltf.CharacterBake;
import net.anubis.client.module.impl.render.character.gltf.ImageDecoder;
import net.anubis.client.module.impl.render.character.kit.BakedDesign;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CustomCharacters.class */
@Environment(EnvType.CLIENT)
final class CustomCharacters {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final long MAX_FILE = 268435456;
    private static final int MAX_TEXTURE = 8192;

    private CustomCharacters() {
    }

    static Path folder() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("characters");
    }

    private static Path cache() {
        return folder().resolve(".baked");
    }

    static List<BakedDesign> scan() {
        List<Path> files = new ArrayList<>();
        try {
            DirectoryStream<Path> dir = Files.newDirectoryStream(folder(), "*.{glb,GLB}");
            try {
                for (Path file : dir) {
                    if (Files.isRegularFile(file, new LinkOption[0])) {
                        files.add(file);
                    }
                }
                if (dir != null) {
                    dir.close();
                }
                files.sort(null);
                List<BakedDesign> out = new ArrayList<>();
                Set<String> ids = new HashSet<>();
                for (Path file2 : files) {
                    String name = file2.getFileName().toString();
                    String name2 = name.substring(0, name.length() - 4);
                    try {
                        long size = Files.size(file2);
                        Files.getLastModifiedTime(file2, new LinkOption[0]).toMillis();
                        String stamp = name2 + size + name2 + "3";
                        String id = "custom_" + slug(name2) + "_" + Integer.toHexString(stamp.hashCode());
                        ids.add(id);
                        out.add(new BakedDesign(id, name2, () -> {
                            return open(file2, id);
                        }));
                    } catch (IOException e) {
                        LOGGER.warn("[Anubis] Skipping custom character {}: {}", file2, e.toString());
                    }
                }
                prune(ids);
                return out;
            } catch (Throwable th) {
                if (dir != null) {
                    try {
                        dir.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e2) {
            return List.of();
        }
    }

    private static String slug(String name) {
        String s = name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
        if (s.isEmpty()) {
            return "model";
        }
        return s.length() > 32 ? s.substring(0, 32) : s;
    }

    private static void prune(Set<String> keep) {
        try {
            DirectoryStream<Path> dir = Files.newDirectoryStream(cache(), "*.anc");
            try {
                for (Path file : dir) {
                    String id = file.getFileName().toString().replace(".anc", "");
                    if (!keep.contains(id)) {
                        Files.deleteIfExists(file);
                    }
                }
                if (dir != null) {
                    dir.close();
                }
            } catch (Throwable th) {
                if (dir != null) {
                    try {
                        dir.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized InputStream open(Path file, String id) throws IOException {
        Path baked = cache().resolve(id + ".anc");
        if (Files.exists(baked, new LinkOption[0])) {
            return Files.newInputStream(baked, new OpenOption[0]);
        }
        if (Files.size(file) > MAX_FILE) {
            throw new IOException("the file is larger than 256 MB");
        }
        String name = file.getFileName().toString();
        CharacterModule.baking(name);
        long start = System.nanoTime();
        CharacterBake.Result result = CharacterBake.bake(Files.readAllBytes(file), new CharacterBake.Options(), CustomCharacters::decode);
        LOGGER.info("[Anubis] Baked custom character {} in {} ms, moving {}", new Object[]{name, Long.valueOf((System.nanoTime() - start) / 1000000), result.limbs()});
        if (result.limbs().size() < 3) {
            CharacterModule.notice(name, result.limbs());
        }
        Files.createDirectories(cache(), new FileAttribute[0]);
        Path temp = cache().resolve(id + ".tmp");
        Files.write(temp, result.data(), new OpenOption[0]);
        Files.move(temp, baked, StandardCopyOption.REPLACE_EXISTING);
        return new ByteArrayInputStream(result.data());
    }

    private static ImageDecoder.Picture decode(byte[] data) throws IOException {
        ByteBuffer encoded = MemoryUtil.memAlloc(data.length);
        try {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                encoded.put(data).flip();
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer rgba = STBImage.stbi_load_from_memory(encoded, w, h, channels, 4);
                if (rgba == null) {
                    throw new IOException("a texture can't be read: " + STBImage.stbi_failure_reason());
                }
                try {
                    int width = w.get(0);
                    int height = h.get(0);
                    if (width > 8192 || height > 8192) {
                        throw new IOException("a texture is over 8192 px");
                    }
                    int[] argb = new int[width * height];
                    for (int i = 0; i < argb.length; i++) {
                        int r = rgba.get(i * 4) & 255;
                        int g = rgba.get((i * 4) + 1) & 255;
                        int b = rgba.get((i * 4) + 2) & 255;
                        int a = rgba.get((i * 4) + 3) & 255;
                        argb[i] = (a << 24) | (r << 16) | (g << 8) | b;
                    }
                    ImageDecoder.Picture picture = new ImageDecoder.Picture(width, height, argb);
                    STBImage.stbi_image_free(rgba);
                    if (stack != null) {
                        stack.close();
                    }
                    MemoryUtil.memFree(encoded);
                    return picture;
                } catch (Throwable th) {
                    STBImage.stbi_image_free(rgba);
                    throw th;
                }
            } catch (Throwable th2) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        } catch (Throwable th4) {
            MemoryUtil.memFree(encoded);
            throw th4;
        }
    }
}
