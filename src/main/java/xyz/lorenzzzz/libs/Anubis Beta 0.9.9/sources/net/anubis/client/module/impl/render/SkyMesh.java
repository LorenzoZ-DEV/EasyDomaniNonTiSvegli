package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10799;
import net.minecraft.class_276;
import net.minecraft.class_287;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SkyMesh.class */
@Environment(EnvType.CLIENT)
final class SkyMesh {
    static final RenderPipeline GLOW = pipeline("sky_glow", BlendFunction.LIGHTNING);
    static final RenderPipeline PAINT = pipeline("sky_paint", null);
    private static final int VERTEX_SIZE = class_290.field_1576.getVertexSize();
    private final String label;
    private final RenderPipeline pipeline;
    private final int maxQuads;
    private class_9799 bytes;
    private GpuBuffer buffer;
    private int indexCount;
    private boolean built;

    SkyMesh(String label, RenderPipeline pipeline, int maxQuads) {
        this.label = label;
        this.pipeline = pipeline;
        this.maxQuads = maxQuads;
    }

    private static RenderPipeline pipeline(String name, BlendFunction blend) {
        RenderPipeline.Builder builder = RenderPipeline.builder(new RenderPipeline.Snippet[]{class_10799.field_60125}).withLocation(class_2960.method_60655(AddonManager.ENTRYPOINT, "pipeline/" + name)).withVertexShader("core/position_color").withFragmentShader("core/position_color").withDepthWrite(false).withCull(false).withVertexFormat(class_290.field_1576, VertexFormat.class_5596.field_27382);
        return (blend == null ? builder.withoutBlend() : builder.withBlend(blend)).build();
    }

    class_287 begin() {
        if (this.bytes == null) {
            this.bytes = new class_9799(this.maxQuads * 4 * VERTEX_SIZE);
        }
        return new class_287(this.bytes, VertexFormat.class_5596.field_27382, class_290.field_1576);
    }

    void upload(class_287 builder) {
        this.built = true;
        class_9801 mesh = builder.method_60794();
        try {
            if (mesh == null) {
                this.indexCount = 0;
                if (mesh != null) {
                    mesh.close();
                    return;
                }
                return;
            }
            if (this.buffer == null) {
                this.buffer = RenderSystem.getDevice().createBuffer(() -> {
                    return this.label;
                }, 40, ((long) this.maxQuads) * 4 * ((long) VERTEX_SIZE));
            }
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(0L, mesh.method_60818().remaining()), mesh.method_60818());
            this.indexCount = mesh.method_60822().comp_751();
            if (mesh != null) {
                mesh.close();
            }
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
    }

    boolean isBuilt() {
        return this.built;
    }

    void draw(class_4587 pose, float alpha) {
        if (this.indexCount == 0 || alpha <= 0.002f) {
            return;
        }
        RenderSystem.class_5590 indices = RenderSystem.getSequentialBuffer(VertexFormat.class_5596.field_27382);
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        try {
            modelView.mul(pose.method_23760().method_23761());
            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().method_71106(modelView, new Vector4f(1.0f, 1.0f, 1.0f, alpha), new Vector3f(), new Matrix4f());
            class_276 target = class_310.method_1551().method_1522();
            RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> {
                return this.label;
            }, target.method_71639(), OptionalInt.empty(), target.method_71640(), OptionalDouble.empty());
            try {
                pass.setPipeline(this.pipeline);
                RenderSystem.bindDefaultUniforms(pass);
                pass.setUniform("DynamicTransforms", transforms);
                pass.setVertexBuffer(0, this.buffer);
                pass.setIndexBuffer(indices.method_68274(this.indexCount), indices.method_31924());
                pass.drawIndexed(0, 0, this.indexCount, 1);
                if (pass != null) {
                    pass.close();
                }
                modelView.popMatrix();
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
        } catch (Throwable th3) {
            modelView.popMatrix();
            throw th3;
        }
    }

    void close() {
        if (this.buffer != null) {
            this.buffer.close();
        }
        if (this.bytes != null) {
            this.bytes.close();
        }
        this.buffer = null;
        this.bytes = null;
        this.indexCount = 0;
        this.built = false;
    }

    static void vertex(class_287 builder, float x, float y, float z, int argb) {
        builder.method_22912(x, y, z).method_39415(argb);
    }
}
