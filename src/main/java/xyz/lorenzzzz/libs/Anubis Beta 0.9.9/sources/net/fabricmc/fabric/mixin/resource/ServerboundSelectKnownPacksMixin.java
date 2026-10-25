package net.fabricmc.fabric.mixin.resource;

import net.fabricmc.fabric.impl.resource.pack.ModResourcePackCreator;
import net.minecraft.class_9220;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/ServerboundSelectKnownPacksMixin.class */
@Mixin({class_9220.class})
public class ServerboundSelectKnownPacksMixin {
    @ModifyArg(method = {"<clinit>()V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_9135;method_58000(I)Lnet/minecraft/class_9139$class_9140;"))
    private static int setMaxKnownPacks(int constant) {
        return ModResourcePackCreator.MAX_KNOWN_PACKS;
    }
}
