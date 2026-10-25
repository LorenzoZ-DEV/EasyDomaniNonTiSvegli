package net.fabricmc.fabric.mixin.client.rendering.fluid;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerInfo;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderingImpl;
import net.minecraft.class_1058;
import net.minecraft.class_1920;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3610;
import net.minecraft.class_4588;
import net.minecraft.class_775;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/mixin/client/rendering/fluid/LiquidBlockRendererMixin.class */
@Mixin({class_775.class})
@Environment(EnvType.CLIENT)
public class LiquidBlockRendererMixin {

    @Shadow
    @Final
    private class_1058 field_4164;

    @Shadow
    @Final
    private class_1058 field_64570;

    @Shadow
    @Final
    private class_1058 field_64571;

    @Shadow
    @Final
    private class_1058 field_64568;

    @Shadow
    @Final
    private class_1058 field_64569;

    @Inject(method = {"<init>(Lnet/minecraft/class_11701;)V"}, at = {@At("RETURN")})
    public void onResourceReloadReturn(CallbackInfo info) {
        class_775 self = (class_775) this;
        ((FluidRenderHandlerRegistryImpl) FluidRenderHandlerRegistry.INSTANCE).onFluidRendererReload(self, new class_1058[]{this.field_64570, this.field_64571, this.field_4164}, new class_1058[]{this.field_64568, this.field_64569}, this.field_4164);
    }

    @Inject(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = {@At("HEAD")}, cancellable = true)
    public void onHeadRender(class_1920 view, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState, CallbackInfo ci) {
        FluidRenderHandler handler;
        FluidRenderHandlerInfo info = FluidRenderingImpl.getCurrentInfo();
        if (info.handler == null && (handler = FluidRenderHandlerRegistry.INSTANCE.get(fluidState.method_15772())) != null) {
            handler.renderFluid(pos, view, vertexConsumer, blockState, fluidState);
            ci.cancel();
        }
    }

    @Unique
    private class_1058 getOrDefault(int index, class_1058 original) {
        FluidRenderHandlerInfo info = FluidRenderingImpl.getCurrentInfo();
        if (info.handler == null) {
            return original;
        }
        if (info.sprites.length == index - 1) {
            return original;
        }
        return info.sprites[index];
    }

    @ModifyVariable(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = @At("STORE"), ordinal = 0)
    public class_1058 modStill(class_1058 original) {
        return getOrDefault(0, original);
    }

    @ModifyVariable(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = @At("STORE"), ordinal = 1)
    public class_1058 modFlowing(class_1058 original) {
        return getOrDefault(1, original);
    }

    @ModifyExpressionValue(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = {@At(value = "CONSTANT", args = {"intValue=16777215"}), @At(value = "INVOKE", target = "Lnet/minecraft/class_1163;method_4961(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;)I")})
    public int modTintColor(int original, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState) {
        FluidRenderHandlerInfo info = FluidRenderingImpl.getCurrentInfo();
        return info.handler != null ? info.handler.getFluidColor(world, pos, fluidState) : original;
    }

    @ModifyVariable(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/class_775;field_4164:Lnet/minecraft/class_1058;")), ordinal = 2)
    @Definitions({@Definition(id = "getU", method = {"Lnet/minecraft/class_1058;method_4580(F)F"}), @Definition(id = "sprite2", local = {@Local(type = class_1058.class, ordinal = 2)})})
    @Expression({"@(sprite2).getU(0.0)"})
    private class_1058 modifyOverlaySprite(class_1058 waterOverlay, class_1920 world, @Local(ordinal = 1) class_2338 neighborPos, @Local(ordinal = 0) boolean isLava, @Local(ordinal = 1) class_1058 flowingSprite, @Share("useOverlay") LocalBooleanRef useOverlay) {
        boolean z;
        FluidRenderHandlerInfo info = FluidRenderingImpl.getCurrentInfo();
        if (info.handler != null) {
            z = info.hasOverlay;
        } else {
            z = !isLava;
        }
        boolean hasOverlay = z;
        class_2248 neighborBlock = world.method_8320(neighborPos).method_26204();
        useOverlay.set(hasOverlay && FluidRenderHandlerRegistry.INSTANCE.isBlockTransparent(neighborBlock));
        if (useOverlay.get()) {
            return info.handler != null ? info.overlaySprite : this.field_4164;
        }
        return flowingSprite;
    }

    @Definitions({@Definition(id = "sprite2", local = {@Local(type = class_1058.class, ordinal = 2)}), @Definition(id = "waterOverlaySprite", field = {"Lnet/minecraft/class_775;field_4164:Lnet/minecraft/class_1058;"})})
    @ModifyExpressionValue(method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}, at = {@At("MIXINEXTRAS:EXPRESSION")})
    @Expression({"sprite2 != this.waterOverlaySprite"})
    private boolean modifyNonOverlayCheck(boolean original, @Share("useOverlay") LocalBooleanRef useOverlay) {
        return !useOverlay.get();
    }
}
