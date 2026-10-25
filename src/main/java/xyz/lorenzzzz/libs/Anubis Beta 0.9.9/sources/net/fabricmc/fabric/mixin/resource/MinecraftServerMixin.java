package net.fabricmc.fabric.mixin.resource;

import com.mojang.datafixers.DataFixer;
import java.net.Proxy;
import java.util.List;
import net.fabricmc.fabric.api.resource.v1.DataResourceStore;
import net.fabricmc.fabric.impl.resource.pack.BuiltinModResourcePackSource;
import net.fabricmc.fabric.impl.resource.pack.FabricOriginalKnownPacksGetter;
import net.fabricmc.fabric.impl.resource.pack.ModNioPackResources;
import net.minecraft.class_11545;
import net.minecraft.class_32;
import net.minecraft.class_3262;
import net.minecraft.class_3283;
import net.minecraft.class_3288;
import net.minecraft.class_6904;
import net.minecraft.class_7497;
import net.minecraft.class_9226;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/MinecraftServerMixin.class */
@Mixin({MinecraftServer.class})
public class MinecraftServerMixin implements DataResourceStore, FabricOriginalKnownPacksGetter {

    @Unique
    private List<class_9226> originalKnownPacks;

    @Shadow
    private MinecraftServer.class_6897 field_25318;

    @Override // net.fabricmc.fabric.api.resource.v1.DataResourceStore
    public <T> T getOrThrow(DataResourceStore.Key<T> key) {
        return (T) this.field_25318.comp_353().fabric$getDataResourceStore().getOrThrow(key);
    }

    @Inject(method = {"<init>(Ljava/lang/Thread;Lnet/minecraft/class_32$class_5143;Lnet/minecraft/class_3283;Lnet/minecraft/class_6904;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/class_7497;Lnet/minecraft/class_11545;)V"}, at = {@At("TAIL")})
    private void init(Thread serverThread, class_32.class_5143 session, class_3283 dataPackManager, class_6904 saveLoader, Proxy proxy, DataFixer dataFixer, class_7497 apiServices, class_11545 chunkLoadProgress, CallbackInfo ci) {
        this.originalKnownPacks = saveLoader.comp_356().method_29213().flatMap(pack -> {
            return pack.method_56926().comp_2332().stream();
        }).toList();
    }

    /* JADX WARN: Code duplicated, block: B:14:0x004c  */
    @Redirect(method = {"method_29736(Lnet/minecraft/class_3283;Lnet/minecraft/class_7712;ZZ)Lnet/minecraft/class_7712;"}, at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"))
    private static boolean onCheckDisabled(List<String> list, Object o, class_3283 resourcePackManager) {
        boolean z;
        String profileId = (String) o;
        boolean contains = list.contains(profileId);
        if (contains) {
            return true;
        }
        class_3288 profile = resourcePackManager.method_14449(profileId);
        if (profile.method_29483() instanceof BuiltinModResourcePackSource) {
            class_3262 pack = profile.method_14458();
            try {
                if (pack instanceof ModNioPackResources) {
                    ModNioPackResources modPack = (ModNioPackResources) pack;
                    if (modPack.getActivationType().isEnabledByDefault()) {
                        z = false;
                    } else {
                        z = true;
                    }
                } else {
                    z = false;
                }
                boolean z2 = z;
                if (pack != null) {
                    pack.close();
                }
                return z2;
            } catch (Throwable th) {
                if (pack != null) {
                    try {
                        pack.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        return false;
    }

    @Override // net.fabricmc.fabric.impl.resource.pack.FabricOriginalKnownPacksGetter
    public List<class_9226> fabric$getOriginalKnownPacks() {
        return this.originalKnownPacks;
    }
}
