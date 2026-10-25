package net.anubis.client.gui;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/FileDialogs.class */
@Environment(EnvType.CLIENT)
final class FileDialogs {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final String PATTERN = "*.json";
    private static final String DESCRIPTION = "Anubis profile (*.json)";
    private static volatile boolean open;
    private static volatile Path lastFolder;

    private FileDialogs() {
    }

    static boolean busy() {
        return open;
    }

    static void openProfile(String title, Consumer<Path> picked) {
        show(stack -> {
            return TinyFileDialogs.tinyfd_openFileDialog(title, startIn(null), filters(stack), DESCRIPTION, false);
        }, picked);
    }

    static void saveProfile(String title, String fileName, Consumer<Path> picked) {
        show(stack -> {
            return TinyFileDialogs.tinyfd_saveFileDialog(title, startIn(fileName), filters(stack), DESCRIPTION);
        }, picked);
    }

    private static PointerBuffer filters(MemoryStack stack) {
        return stack.mallocPointer(1).put(stack.UTF8(PATTERN)).flip();
    }

    private static String startIn(String fileName) {
        Path folder = lastFolder;
        if (folder == null || !Files.isDirectory(folder, new LinkOption[0])) {
            folder = defaultFolder();
        }
        return fileName == null ? String.valueOf(folder) + File.separator : folder.resolve(fileName).toString();
    }

    private static Path defaultFolder() {
        try {
            Path home = Path.of(System.getProperty("user.home", "."), new String[0]);
            Path downloads = home.resolve("Downloads");
            return Files.isDirectory(downloads, new LinkOption[0]) ? downloads : home;
        } catch (InvalidPathException e) {
            return Path.of(".", new String[0]).toAbsolutePath();
        }
    }

    private static void show(Function<MemoryStack, String> dialog, Consumer<Path> picked) {
        if (open) {
            return;
        }
        open = true;
        Thread thread = new Thread(() -> {
            String result = null;
            try {
                try {
                    MemoryStack stack = MemoryStack.stackPush();
                    try {
                        result = (String) dialog.apply(stack);
                        if (stack != null) {
                            stack.close();
                        }
                        open = false;
                    } catch (Throwable th) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable t) {
                    LOGGER.warn("[Anubis] File dialog failed", t);
                    open = false;
                }
                if (result == null || result.isBlank()) {
                    return;
                }
                try {
                    Path path = Path.of(result, new String[0]).toAbsolutePath();
                    if (path.getParent() != null) {
                        lastFolder = path.getParent();
                    }
                    class_310.method_1551().execute(() -> {
                        picked.accept(path);
                    });
                } catch (InvalidPathException e) {
                }
            } catch (Throwable th3) {
                open = false;
                throw th3;
            }
        }, "Anubis file dialog");
        thread.setDaemon(true);
        thread.start();
    }
}
