package net.anubis.client.account;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/AccountType.class */
@Environment(EnvType.CLIENT)
public enum AccountType {
    CRACKED("Cracked"),
    SESSION("Session"),
    MICROSOFT("Microsoft");

    private final String label;

    AccountType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
