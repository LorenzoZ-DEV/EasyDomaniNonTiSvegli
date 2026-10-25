package net.anubis.client.account;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/Account.class */
@Environment(EnvType.CLIENT)
public final class Account {
    private final String id;
    private final AccountType type;
    private final String name;
    private final String uuid;
    private final String secret;

    Account(String id, AccountType type, String name, String uuid, String secret) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.uuid = uuid;
        this.secret = secret;
    }

    public AccountType type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public String uuid() {
        return this.uuid;
    }

    public String id() {
        return this.id;
    }

    String secret() {
        return this.secret;
    }

    Account withLogin(String name, String uuid, String secret) {
        return new Account(this.id, this.type, name, uuid, secret);
    }

    public boolean equals(Object other) {
        if (other instanceof Account) {
            Account account = (Account) other;
            if (account.id.equals(this.id)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return this.type.label() + " " + this.name;
    }
}
