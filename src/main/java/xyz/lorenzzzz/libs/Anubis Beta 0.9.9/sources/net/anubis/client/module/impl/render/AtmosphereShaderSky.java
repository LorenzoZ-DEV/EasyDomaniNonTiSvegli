package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_276;
import net.minecraft.class_287;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereShaderSky.class */
@Environment(EnvType.CLIENT)
final class AtmosphereShaderSky {
    static final String OFF = "Off";
    static final String SPACE = "Space";
    static final String NORTHERN_LIGHTS = "Northern Lights";
    static final String RINGED_PLANET = "Ringed Planet";
    static final String BLACK_HOLE = "Black Hole";
    private static final List<String> THEMES = List.of(SPACE, NORTHERN_LIGHTS, RINGED_PLANET, BLACK_HOLE);
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final RenderPipeline PIPELINE = RenderPipeline.builder(new RenderPipeline.Snippet[]{class_10799.field_60125}).withLocation(class_2960.method_60655(AddonManager.ENTRYPOINT, "pipeline/sky_theme")).withVertexShader(class_2960.method_60655(AddonManager.ENTRYPOINT, "core/sky_theme")).withFragmentShader(class_2960.method_60655(AddonManager.ENTRYPOINT, "core/sky_theme")).withUniform("AnubisSky", class_10789.field_60031).withBlend(BlendFunction.TRANSLUCENT).withDepthWrite(false).withCull(false).withVertexFormat(class_290.field_1592, VertexFormat.class_5596.field_27382).build();
    private static final int UNIFORM_SIZE = 112;
    private static final float CUBE = 10.0f;
    private static final int CUBE_INDICES = 36;
    private static final float MAX_STEP = 0.1f;
    private static final float TIME_WRAP = 4096.0f;
    private GpuBuffer cube;
    private GpuBuffer uniforms;
    private ByteBuffer uniformData;
    private boolean invalidLogged;
    private float time;
    private long lastNanos;

    AtmosphereShaderSky() {
    }

    void render(String theme, float visibility, float brightness, float speed, float stars, float effect, float band, int low, int high, int bandColor, int zenith, int horizon) {
        int mode = THEMES.indexOf(theme);
        long now = System.nanoTime();
        float dt = this.lastNanos == 0 ? 0.0f : Math.min((now - this.lastNanos) / 1.0E9f, MAX_STEP);
        this.lastNanos = now;
        this.time = (this.time + (dt * speed)) % TIME_WRAP;
        if (mode < 0 || visibility <= 0.002f) {
            return;
        }
        GpuDevice device = RenderSystem.getDevice();
        if (!device.precompilePipeline(PIPELINE).isValid()) {
            if (!this.invalidLogged) {
                this.invalidLogged = true;
                LOGGER.warn("[Anubis] Atmosphere sky shader {} did not compile; the sky is left as it is", PIPELINE.getFragmentShader());
                return;
            }
            return;
        }
        if (this.cube == null) {
            this.cube = buildCube(device);
        }
        if (this.uniforms == null) {
            this.uniforms = device.createBuffer(() -> {
                return "Anubis sky theme settings";
            }, 136, 112L);
            this.uniformData = BufferUtils.createByteBuffer(UNIFORM_SIZE);
        }
        putVec(0, mode, visibility, this.time, brightness);
        putVec(16, stars, effect, band, 0.0f);
        putColor(32, low);
        putColor(48, high);
        putColor(64, bandColor);
        putColor(80, zenith);
        putColor(96, horizon);
        CommandEncoder encoder = device.createCommandEncoder();
        encoder.writeToBuffer(this.uniforms.slice(), this.uniformData);
        GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().method_71106(RenderSystem.getModelViewStack(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector3f(), new Matrix4f());
        RenderSystem.class_5590 indices = RenderSystem.getSequentialBuffer(VertexFormat.class_5596.field_27382);
        class_276 target = class_310.method_1551().method_1522();
        RenderPass pass = encoder.createRenderPass(() -> {
            return "Anubis sky theme";
        }, target.method_71639(), OptionalInt.empty(), target.method_71640(), OptionalDouble.empty());
        try {
            pass.setPipeline(PIPELINE);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", transforms);
            pass.setUniform("AnubisSky", this.uniforms.slice());
            pass.setVertexBuffer(0, this.cube);
            pass.setIndexBuffer(indices.method_68274(36), indices.method_31924());
            pass.drawIndexed(0, 0, 36, 1);
            if (pass != null) {
                pass.close();
            }
        } catch (Throwable th) {
            if (pass != null) {
                try {
                    pass.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    void close() {
        if (this.cube != null) {
            this.cube.close();
        }
        if (this.uniforms != null) {
            this.uniforms.close();
        }
        this.cube = null;
        this.uniforms = null;
        this.uniformData = null;
        this.lastNanos = 0L;
    }

    private void putVec(int offset, float x, float y, float z, float w) {
        this.uniformData.putFloat(offset, x);
        this.uniformData.putFloat(offset + 4, y);
        this.uniformData.putFloat(offset + 8, z);
        this.uniformData.putFloat(offset + 12, w);
    }

    private void putColor(int offset, int rgb) {
        putVec(offset, ((rgb >> 16) & 255) / 255.0f, ((rgb >> 8) & 255) / 255.0f, (rgb & 255) / 255.0f, 1.0f);
    }

    private static GpuBuffer buildCube(GpuDevice device) {
        class_9799 bytes = new class_9799(24 * class_290.field_1592.getVertexSize());
        try {
            class_287 builder = new class_287(bytes, VertexFormat.class_5596.field_27382, class_290.field_1592);
            face(builder, CUBE, -CUBE, -CUBE, CUBE, CUBE, -CUBE, CUBE, CUBE, CUBE, CUBE, -CUBE, CUBE);
            face(builder, -CUBE, -CUBE, CUBE, -CUBE, CUBE, CUBE, -CUBE, CUBE, -CUBE, -CUBE, -CUBE, -CUBE);
            face(builder, -CUBE, CUBE, -CUBE, -CUBE, CUBE, CUBE, CUBE, CUBE, CUBE, CUBE, CUBE, -CUBE);
            face(builder, -CUBE, -CUBE, CUBE, -CUBE, -CUBE, -CUBE, CUBE, -CUBE, -CUBE, CUBE, -CUBE, CUBE);
            face(builder, CUBE, -CUBE, CUBE, CUBE, CUBE, CUBE, -CUBE, CUBE, CUBE, -CUBE, -CUBE, CUBE);
            face(builder, -CUBE, -CUBE, -CUBE, -CUBE, CUBE, -CUBE, CUBE, CUBE, -CUBE, CUBE, -CUBE, -CUBE);
            class_9801 mesh = builder.method_60800();
            try {
                GpuBuffer gpuBufferCreateBuffer = device.createBuffer(() -> {
                    return "Anubis sky theme cube";
                }, 32, mesh.method_60818());
                if (mesh != null) {
                    mesh.close();
                }
                bytes.close();
                return gpuBufferCreateBuffer;
            } catch (Throwable th) {
                if (mesh != null) {
                    try {
                        mesh.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            try {
                bytes.close();
            } catch (Throwable th4) {
                th3.addSuppressed(th4);
            }
            throw th3;
        }
    }

    private static void face(class_287 builder, float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz) {
        builder.method_22912(ax, ay, az);
        builder.method_22912(bx, by, bz);
        builder.method_22912(cx, cy, cz);
        builder.method_22912(dx, dy, dz);
    }
}
