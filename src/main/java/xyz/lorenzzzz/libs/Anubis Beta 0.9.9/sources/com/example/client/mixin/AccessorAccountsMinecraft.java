package com.example.client.mixin;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_5520;
import net.minecraft.class_7574;
import net.minecraft.class_7853;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/AccessorAccountsMinecraft.class */
@Mixin({class_310.class})
@Environment(EnvType.CLIENT)
public interface AccessorAccountsMinecraft {
    @Accessor("field_1726")
    @Mutable
    void anubis$setUser(class_320 class_320Var);

    @Accessor("field_26902")
    @Mutable
    void anubis$setUserApiService(UserApiService userApiService);

    @Accessor("field_47680")
    @Mutable
    void anubis$setUserPropertiesFuture(CompletableFuture<UserApiService.UserProperties> completableFuture);

    @Accessor("field_45899")
    @Mutable
    void anubis$setProfileFuture(CompletableFuture<ProfileResult> completableFuture);

    @Accessor("field_26842")
    @Mutable
    void anubis$setPlayerSocialManager(class_5520 class_5520Var);

    @Accessor("field_39068")
    @Mutable
    void anubis$setProfileKeyPairManager(class_7853 class_7853Var);

    @Accessor("field_39492")
    void anubis$setReportingContext(class_7574 class_7574Var);
}
