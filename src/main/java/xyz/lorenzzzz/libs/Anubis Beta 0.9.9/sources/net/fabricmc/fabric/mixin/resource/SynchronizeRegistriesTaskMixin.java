package net.fabricmc.fabric.mixin.resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.fabricmc.fabric.impl.resource.pack.ModResourcePackCreator;
import net.minecraft.class_2596;
import net.minecraft.class_9223;
import net.minecraft.class_9226;
import net.minecraft.class_9250;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/SynchronizeRegistriesTaskMixin.class */
@Mixin({class_9223.class})
public abstract class SynchronizeRegistriesTaskMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("SynchronizeRegistriesTaskMixin");

    @Shadow
    @Final
    private List<class_9226> field_49029;

    @Shadow
    protected abstract void method_56925(Consumer<class_2596<?>> consumer, Set<class_9226> set);

    @Inject(method = {"method_56923(Ljava/util/List;Ljava/util/function/Consumer;)V"}, at = {@At("HEAD")}, cancellable = true)
    public void onSelectKnownPacks(List<class_9226> clientKnownPacks, Consumer<class_2596<?>> sender, CallbackInfo ci) {
        if (new HashSet(this.field_49029).containsAll(clientKnownPacks)) {
            method_56925(sender, Set.copyOf(clientKnownPacks));
            ci.cancel();
        }
    }

    @Inject(method = {"method_56925(Ljava/util/function/Consumer;Ljava/util/Set;)V"}, at = {@At("HEAD")})
    public void syncRegistryAndTags(Consumer<class_2596<?>> sender, Set<class_9226> commonKnownPacks, CallbackInfo ci) {
        LOGGER.debug("Synchronizing registries with common known packs: {}", commonKnownPacks);
    }

    @Inject(method = {"method_52376(Ljava/util/function/Consumer;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void sendPacket(Consumer<class_2596<?>> sender, CallbackInfo ci) {
        if (this.field_49029.size() > ModResourcePackCreator.MAX_KNOWN_PACKS) {
            LOGGER.warn("Too many knownPacks: Found {}; max {}", Integer.valueOf(this.field_49029.size()), Integer.valueOf(ModResourcePackCreator.MAX_KNOWN_PACKS));
            sender.accept(new class_9250(this.field_49029.subList(0, ModResourcePackCreator.MAX_KNOWN_PACKS)));
            ci.cancel();
        }
    }
}
