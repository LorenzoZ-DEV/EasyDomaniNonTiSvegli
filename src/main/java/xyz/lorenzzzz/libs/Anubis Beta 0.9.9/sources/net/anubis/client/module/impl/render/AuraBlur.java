package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import java.util.function.Supplier;
import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10789;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_6367;
import org.lwjgl.BufferUtils;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AuraBlur.class */
@Environment(EnvType.CLIENT)
final class AuraBlur {
    private static final RenderPipeline PIPELINE = RenderPipeline.builder(new RenderPipeline.Snippet[0]).withLocation(class_2960.method_60655(AddonManager.ENTRYPOINT, "pipeline/aura_spread")).withVertexShader("core/screenquad").withFragmentShader(class_2960.method_60655(AddonManager.ENTRYPOINT, "core/aura_spread")).withSampler("InSampler").withUniform("AuraSpread", class_10789.field_60031).withoutBlend().withDepthWrite(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withVertexFormat(class_290.field_60033, VertexFormat.class_5596.field_27379).build();
    private static final int CONFIG_SIZE = 16;
    private static final float TAPS = 6.0f;
    private final String name;
    private final Supplier<String> passLabel;
    private class_6367 across;
    private class_6367 down;
    private GpuBuffer acrossConfig;
    private GpuBuffer downConfig;
    private GpuBufferSlice acrossSlice;
    private GpuBufferSlice downSlice;
    private ByteBuffer configData;

    AuraBlur(String name) {
        this.name = name;
        this.passLabel = () -> {
            return name + " spread";
        };
    }

    static boolean compiled(GpuDevice device) {
        return device.precompilePipeline(PIPELINE).isValid();
    }

    GpuTextureView spread(CommandEncoder encoder, GpuTextureView source, int width, int height, float reachPx) {
        int halfWidth = Math.max(1, (width + 1) / 2);
        int halfHeight = Math.max(1, (height + 1) / 2);
        if (this.across == null) {
            allocate(halfWidth, halfHeight);
        } else if (this.across.field_1482 != halfWidth || this.across.field_1481 != halfHeight) {
            this.across.method_1234(halfWidth, halfHeight);
            this.down.method_1234(halfWidth, halfHeight);
        }
        float step = Math.max(0.75f, reachPx / TAPS);
        run(encoder, source, this.across, this.acrossSlice, step / width, 0.0f);
        run(encoder, this.across.method_71639(), this.down, this.downSlice, 0.0f, step / height);
        return this.down.method_71639();
    }

    private void run(CommandEncoder encoder, GpuTextureView from, class_6367 into, GpuBufferSlice slice, float du, float dv) {
        this.configData.putFloat(0, du);
        this.configData.putFloat(4, dv);
        encoder.writeToBuffer(slice, this.configData);
        RenderPass pass = encoder.createRenderPass(this.passLabel, into.method_71639(), OptionalInt.empty());
        try {
            pass.setPipeline(PIPELINE);
            pass.setUniform("AuraSpread", slice);
            pass.bindTexture("InSampler", from, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
            pass.draw(0, 3);
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

    private void allocate(int width, int height) {
        GpuDevice device = RenderSystem.getDevice();
        this.across = new class_6367(this.name + " across", width, height, false);
        this.down = new class_6367(this.name + " down", width, height, false);
        this.acrossConfig = device.createBuffer(() -> {
            return this.name + " across config";
        }, 136, 16L);
        this.downConfig = device.createBuffer(() -> {
            return this.name + " down config";
        }, 136, 16L);
        this.acrossSlice = this.acrossConfig.slice();
        this.downSlice = this.downConfig.slice();
        this.configData = BufferUtils.createByteBuffer(16);
    }

    void release() {
        if (this.across != null) {
            this.across.method_1238();
            this.across = null;
        }
        if (this.down != null) {
            this.down.method_1238();
            this.down = null;
        }
        if (this.acrossConfig != null) {
            this.acrossConfig.close();
            this.acrossConfig = null;
        }
        if (this.downConfig != null) {
            this.downConfig.close();
            this.downConfig = null;
        }
        this.acrossSlice = null;
        this.downSlice = null;
        this.configData = null;
    }
}
