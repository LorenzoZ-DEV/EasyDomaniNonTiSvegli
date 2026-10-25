package net.anubis.client.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/proxy/Proxy.class */
@Environment(EnvType.CLIENT)
public final class Proxy {
    public static final int UNCHECKED = -2;
    public static final int DEAD = -1;
    public int port;
    public boolean enabled;
    public String name = "";
    public ProxyType type = ProxyType.SOCKS5;
    public String address = "";
    public String username = "";
    public String password = "";
    public volatile int ping = -2;

    public boolean isValid() {
        return (this.name == null || this.name.isBlank() || this.address == null || this.address.isBlank() || this.type == null || this.port < 1 || this.port > 65535) ? false : true;
    }

    public String displayAddress() {
        String host = this.address == null ? "" : this.address.trim();
        return (host.indexOf(58) >= 0 ? "[" + host + "]" : host) + ":" + this.port;
    }

    Proxy copy() {
        Proxy copy = new Proxy();
        copy.name = this.name;
        copy.type = this.type;
        copy.address = this.address;
        copy.port = this.port;
        copy.username = this.username;
        copy.password = this.password;
        copy.enabled = this.enabled;
        return copy;
    }

    public String toString() {
        return (this.type == null ? "?" : this.type.label()) + " proxy " + displayAddress();
    }
}
