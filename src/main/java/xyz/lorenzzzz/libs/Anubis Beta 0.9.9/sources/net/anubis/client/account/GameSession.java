package net.anubis.client.account;

import com.example.client.mixin.AccessorAccountsMinecraft;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_5520;
import net.minecraft.class_7569;
import net.minecraft.class_7574;
import net.minecraft.class_7853;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/GameSession.class */
@Environment(EnvType.CLIENT)
public final class GameSession {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private YggdrasilAuthenticationService mojangAuth;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/GameSession$Prepared.class */
    @Environment(EnvType.CLIENT)
    static final class Prepared extends Record {
        private final class_320 user;
        private final UserApiService api;
        private final UserApiService.UserProperties properties;
        private final ProfileResult profile;
        private final boolean offline;

        Prepared(class_320 user, UserApiService api, UserApiService.UserProperties properties, ProfileResult profile, boolean offline) {
            this.user = user;
            this.api = api;
            this.properties = properties;
            this.profile = profile;
            this.offline = offline;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Prepared.class), Prepared.class, "user;api;properties;profile;offline", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->user:Lnet/minecraft/class_320;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->api:Lcom/mojang/authlib/minecraft/UserApiService;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->properties:Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->profile:Lcom/mojang/authlib/yggdrasil/ProfileResult;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->offline:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Prepared.class), Prepared.class, "user;api;properties;profile;offline", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->user:Lnet/minecraft/class_320;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->api:Lcom/mojang/authlib/minecraft/UserApiService;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->properties:Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->profile:Lcom/mojang/authlib/yggdrasil/ProfileResult;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->offline:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Prepared.class, Object.class), Prepared.class, "user;api;properties;profile;offline", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->user:Lnet/minecraft/class_320;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->api:Lcom/mojang/authlib/minecraft/UserApiService;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->properties:Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->profile:Lcom/mojang/authlib/yggdrasil/ProfileResult;", "FIELD:Lnet/anubis/client/account/GameSession$Prepared;->offline:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_320 user() {
            return this.user;
        }

        public UserApiService api() {
            return this.api;
        }

        public UserApiService.UserProperties properties() {
            return this.properties;
        }

        public ProfileResult profile() {
            return this.profile;
        }

        public boolean offline() {
            return this.offline;
        }
    }

    GameSession() {
    }

    Prepared prepare(AccountType type, Credentials credentials) {
        UserApiService.UserProperties properties;
        class_310 mc = class_310.method_1551();
        boolean offline = type == AccountType.CRACKED;
        class_320 user = new class_320(credentials.name(), credentials.uuid(), credentials.accessToken(), Optional.empty(), Optional.empty());
        UserApiService api = offline ? UserApiService.OFFLINE : mojangAuth(mc).createUserApiService(credentials.accessToken());
        try {
            properties = api.fetchProperties();
        } catch (AuthenticationException e) {
            LOGGER.warn("[Anubis] Couldn't fetch user properties for {}", credentials.name(), e);
            properties = UserApiService.OFFLINE_PROPERTIES;
        }
        ProfileResult profile = offline ? null : mc.method_73361().comp_837().fetchProfile(credentials.uuid(), true);
        return new Prepared(user, api, properties, profile, offline);
    }

    void apply(Prepared prepared) {
        class_7853 class_7853VarMethod_46532;
        AccessorAccountsMinecraft accessorAccountsMinecraftMethod_1551 = class_310.method_1551();
        AccessorAccountsMinecraft game = accessorAccountsMinecraftMethod_1551;
        game.anubis$setUser(prepared.user());
        game.anubis$setUserApiService(prepared.api());
        game.anubis$setUserPropertiesFuture(CompletableFuture.completedFuture(prepared.properties()));
        game.anubis$setProfileFuture(CompletableFuture.completedFuture(prepared.profile()));
        game.anubis$setPlayerSocialManager(new class_5520(accessorAccountsMinecraftMethod_1551, prepared.api()));
        if (prepared.offline()) {
            class_7853VarMethod_46532 = class_7853.field_40800;
        } else {
            class_7853VarMethod_46532 = class_7853.method_46532(prepared.api(), prepared.user(), ((class_310) accessorAccountsMinecraftMethod_1551).field_1697.toPath());
        }
        game.anubis$setProfileKeyPairManager(class_7853VarMethod_46532);
        game.anubis$setReportingContext(class_7574.method_44599(class_7569.method_44586(), prepared.api()));
    }

    private YggdrasilAuthenticationService mojangAuth(class_310 mc) {
        if (this.mojangAuth == null) {
            this.mojangAuth = new YggdrasilAuthenticationService(mc.method_1487());
        }
        return this.mojangAuth;
    }
}
