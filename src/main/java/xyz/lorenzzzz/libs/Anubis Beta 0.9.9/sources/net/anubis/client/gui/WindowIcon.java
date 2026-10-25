package net.anubis.client.gui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/WindowIcon.class */
@Environment(EnvType.CLIENT)
public final class WindowIcon {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final int[] SIZES = {16, 32, 48, 64, 128, 256};

    private WindowIcon() {
    }

    public static void apply(long window) {
        int platform = GLFW.glfwGetPlatform();
        if (platform == 393217 || platform == 393220) {
            List<ByteBuffer> pixels = new ArrayList<>(SIZES.length);
            try {
                MemoryStack stack = MemoryStack.stackPush();
                try {
                    GLFWImage.Buffer images = GLFWImage.malloc(SIZES.length, stack);
                    int count = 0;
                    for (int size : SIZES) {
                        ByteBuffer image = load("/assets/anubis/icons/icon_" + size + ".png", stack);
                        if (image != null) {
                            pixels.add(image);
                            int i = count;
                            count++;
                            images.position(i);
                            images.width(size);
                            images.height(size);
                            images.pixels(image);
                        }
                    }
                    if (count != 0) {
                        images.position(0);
                        images.limit(count);
                        GLFW.glfwSetWindowIcon(window, images);
                        if (stack != null) {
                            stack.close();
                        }
                        pixels.forEach(STBImage::stbi_image_free);
                        return;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                    pixels.forEach(STBImage::stbi_image_free);
                    return;
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
            } catch (Throwable th3) {
                pixels.forEach(STBImage::stbi_image_free);
                throw th3;
            }
            pixels.forEach(STBImage::stbi_image_free);
            throw th3;
        }
    }

    private static ByteBuffer load(String resource, MemoryStack stack) {
        try {
            InputStream in = WindowIcon.class.getResourceAsStream(resource);
            if (in != null) {
                try {
                    byte[] bytes = in.readAllBytes();
                    ByteBuffer file = MemoryUtil.memAlloc(bytes.length);
                    try {
                        file.put(bytes).flip();
                        IntBuffer w = stack.mallocInt(1);
                        IntBuffer h = stack.mallocInt(1);
                        IntBuffer channels = stack.mallocInt(1);
                        ByteBuffer byteBufferStbi_load_from_memory = STBImage.stbi_load_from_memory(file, w, h, channels, 4);
                        MemoryUtil.memFree(file);
                        if (in != null) {
                            in.close();
                        }
                        return byteBufferStbi_load_from_memory;
                    } catch (Throwable th) {
                        MemoryUtil.memFree(file);
                        throw th;
                    }
                } catch (Throwable th2) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                    }
                    throw th2;
                }
            }
            if (in != null) {
                in.close();
            }
            return null;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis+] Failed to load window icon {}", resource, e);
            return null;
        }
    }
}
