package net.anubis.client.account;

import com.mojang.util.UndashedUuid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/SessionLogin.class */
@Environment(EnvType.CLIENT)
final class SessionLogin {
    private SessionLogin() {
    }

    static Credentials signIn(String accessToken) throws InterruptedException {
        AuthHttp.Reply profile = AuthHttp.get("https://api.minecraftservices.com/minecraft/profile", accessToken);
        if (profile.status() == 401) {
            throw new AccountException("This session token is invalid or has expired");
        }
        if (profile.status() == 404) {
            throw new AccountException("This token's account doesn't own Minecraft: Java Edition");
        }
        String name = profile.text("name");
        String id = profile.text("id");
        if (!profile.ok() || name == null || id == null) {
            throw new AccountException("Couldn't load the profile for this token (HTTP " + profile.status() + ")");
        }
        return new Credentials(name, UndashedUuid.fromStringLenient(id), accessToken, accessToken);
    }
}
