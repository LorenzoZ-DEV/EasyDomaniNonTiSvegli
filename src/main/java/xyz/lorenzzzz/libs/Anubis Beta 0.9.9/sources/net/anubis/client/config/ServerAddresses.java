package net.anubis.client.config;

import com.google.common.net.HostAndPort;
import com.google.common.net.InetAddresses;
import com.google.common.net.InternetDomainName;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.net.IDN;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ServerAddresses.class */
@Environment(EnvType.CLIENT)
public final class ServerAddresses {
    public static final int DEFAULT_PORT = 25565;
    private static final int MAX_HOST = 253;
    private static final Pattern HOST = Pattern.compile("[a-z0-9_-]+(\\.[a-z0-9_-]+)*");
    private static final Set<String> FRONT_DOORS = Set.of("play", "mc", "www", "join", "hub", "lobby", "server", "connect", "go");

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ServerAddresses$Address.class */
    @Environment(EnvType.CLIENT)
    public static final class Address extends Record {
        private final String host;
        private final int port;

        public Address(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Address.class), Address.class, "host;port", "FIELD:Lnet/anubis/client/config/ServerAddresses$Address;->host:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ServerAddresses$Address;->port:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Address.class, Object.class), Address.class, "host;port", "FIELD:Lnet/anubis/client/config/ServerAddresses$Address;->host:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ServerAddresses$Address;->port:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String host() {
            return this.host;
        }

        public int port() {
            return this.port;
        }

        @Override // java.lang.Record
        public String toString() {
            String shown = this.host.indexOf(58) >= 0 ? "[" + this.host + "]" : this.host;
            return this.port == 25565 ? shown : shown + ":" + this.port;
        }
    }

    private ServerAddresses() {
    }

    public static Address parse(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if (trimmed.isEmpty() || trimmed.length() > 261) {
            return null;
        }
        try {
            HostAndPort parsed = HostAndPort.fromString(trimmed).withDefaultPort(DEFAULT_PORT);
            String host = parsed.getHost().toLowerCase(Locale.ROOT);
            while (host.endsWith(".")) {
                host = host.substring(0, host.length() - 1);
            }
            if (host.isEmpty()) {
                return null;
            }
            if (!InetAddresses.isInetAddress(host)) {
                host = IDN.toASCII(host).toLowerCase(Locale.ROOT);
                if (host.length() > MAX_HOST || !HOST.matcher(host).matches()) {
                    return null;
                }
            }
            return new Address(host, parsed.getPort());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return null;
        }
    }

    public static int score(Address entry, Address joined) {
        if (entry == null || joined == null || entry.port() != joined.port()) {
            return 0;
        }
        String listed = entry.host();
        String actual = joined.host();
        if (listed.equals(actual)) {
            return 3;
        }
        if (InetAddresses.isInetAddress(listed) || InetAddresses.isInetAddress(actual)) {
            return 0;
        }
        String site = site(listed);
        if (site.equals(site(actual))) {
            return 2;
        }
        return actual.endsWith("." + site) ? 1 : 0;
    }

    private static String site(String host) {
        int dot = host.indexOf(46);
        if (dot <= 0 || !FRONT_DOORS.contains(host.substring(0, dot))) {
            return host;
        }
        String rest = host.substring(dot + 1);
        try {
            return InternetDomainName.from(rest).isUnderPublicSuffix() ? rest : host;
        } catch (IllegalArgumentException e) {
            return rest.indexOf(46) > 0 ? rest : host;
        }
    }
}
