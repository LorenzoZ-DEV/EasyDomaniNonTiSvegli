package net.anubis.client.account;

import java.util.concurrent.CompletionException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/AccountException.class */
@Environment(EnvType.CLIENT)
final class AccountException extends CompletionException {
    AccountException(String message) {
        super(message);
    }

    AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
