package net.anubis.client.account;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/Credentials.class */
@Environment(EnvType.CLIENT)
final class Credentials extends Record {
    private final String name;
    private final UUID uuid;
    private final String accessToken;
    private final String secret;

    Credentials(String name, UUID uuid, String accessToken, String secret) {
        this.name = name;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.secret = secret;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Credentials.class), Credentials.class, "name;uuid;accessToken;secret", "FIELD:Lnet/anubis/client/account/Credentials;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/account/Credentials;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->secret:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Credentials.class), Credentials.class, "name;uuid;accessToken;secret", "FIELD:Lnet/anubis/client/account/Credentials;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/account/Credentials;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->secret:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Credentials.class, Object.class), Credentials.class, "name;uuid;accessToken;secret", "FIELD:Lnet/anubis/client/account/Credentials;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/account/Credentials;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/Credentials;->secret:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public String name() {
        return this.name;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public String accessToken() {
        return this.accessToken;
    }

    public String secret() {
        return this.secret;
    }
}
