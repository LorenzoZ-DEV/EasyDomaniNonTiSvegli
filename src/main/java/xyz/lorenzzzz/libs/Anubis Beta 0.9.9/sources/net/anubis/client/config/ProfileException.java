package net.anubis.client.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ProfileException.class */
@Environment(EnvType.CLIENT)
public final class ProfileException extends Exception {
    public ProfileException(String message) {
        super(message);
    }
}
