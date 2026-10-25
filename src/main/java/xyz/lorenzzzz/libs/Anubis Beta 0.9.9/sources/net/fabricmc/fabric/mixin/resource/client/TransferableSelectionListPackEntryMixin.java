package net.fabricmc.fabric.mixin.resource.client;

import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.client.PackTooltipComponent;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_521;
import net.minecraft.class_5481;
import net.minecraft.class_7842;
import net.minecraft.class_7940;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/TransferableSelectionListPackEntryMixin.class */
@Mixin({class_521.class_4271.class})
@Environment(EnvType.CLIENT)
class TransferableSelectionListPackEntryMixin {

    @Shadow
    @Final
    private static int field_32403;

    @Shadow
    @Final
    protected class_310 field_19128;

    @Shadow
    @Final
    private class_7842 field_64205;

    @Shadow
    @Final
    private class_7940 field_64206;

    @Shadow
    @Final
    class_521 field_62184;

    TransferableSelectionListPackEntryMixin() {
    }

    @Inject(method = {"method_25343(Lnet/minecraft/class_332;IIZF)V"}, at = {@At("RETURN")})
    private void onRenderContent(class_332 graphics, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (hovered) {
            class_2561 name = null;
            boolean includeDescription = false;
            if (this.field_64205.method_25368() < this.field_19128.field_1772.method_30880(this.field_64205.method_25369().method_30937())) {
                name = this.field_64205.method_25369();
            }
            List<class_5481> splitDescription = this.field_19128.field_1772.method_1728(this.field_64206.method_25369(), field_32403 - (this.field_62184.method_44390() > 0 ? 6 : 0));
            if (splitDescription.size() > 2) {
                includeDescription = true;
            }
            if (name != null || includeDescription) {
                graphics.method_64038(this.field_19128.field_1772, List.of(), Optional.of(new PackTooltipComponent(Optional.ofNullable(name), includeDescription ? Optional.of(splitDescription) : Optional.empty())), mouseX, mouseY);
            }
        }
    }
}
