package net.anubis.client.proxy;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/proxy/ProxyType.class */
@Environment(EnvType.CLIENT)
public enum ProxyType {
    SOCKS4("Socks4"),
    SOCKS5("Socks5");

    private final String label;

    ProxyType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    static ProxyType parse(String text) {
        if (text == null) {
            return null;
        }
        switch (text.trim().toLowerCase(Locale.ROOT)) {
            case "socks4":
            case "socks4a":
                return SOCKS4;
            case "socks5":
            case "socks5h":
            case "socks":
                return SOCKS5;
            default:
                return null;
        }
    }
}
