package net.fabricmc.fabric.mixin.resource.client;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.minecraft.class_310;
import net.minecraft.class_3283;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/GameOptionsWriteVisitorMixin.class */
@Mixin(targets = {"net/minecraft/class_315$3"})
@Environment(EnvType.CLIENT)
public class GameOptionsWriteVisitorMixin {
    @Unique
    private static List<String> toPackListString(List<String> packs) {
        ArrayList<String> copy = new ArrayList<>(packs.size());
        class_3283 manager = class_310.method_1551().method_1520();
        for (String pack : packs) {
            FabricPack fabricPackMethod_14449 = manager.method_14449(pack);
            if (fabricPackMethod_14449 == null || !fabricPackMethod_14449.fabric$isHidden()) {
                copy.add(pack);
            }
        }
        return copy;
    }

    @ModifyArg(method = {"method_33681(Ljava/lang/String;Ljava/lang/Object;Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/lang/Object;"}, at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;"))
    private <T> T skipHiddenPacks(T t, @Local String str) {
        if ("resourcePacks".equals(str) && (t instanceof List)) {
            return (T) toPackListString((List) t);
        }
        return t;
    }
}
