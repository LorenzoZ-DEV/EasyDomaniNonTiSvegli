package io.netty.handler.codec.socksx.v5;

import io.netty.util.internal.ObjectUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5AuthMethod.class */
public class Socks5AuthMethod implements Comparable<Socks5AuthMethod> {
    public static final Socks5AuthMethod NO_AUTH = new Socks5AuthMethod(0, "NO_AUTH");
    public static final Socks5AuthMethod GSSAPI = new Socks5AuthMethod(1, "GSSAPI");
    public static final Socks5AuthMethod PASSWORD = new Socks5AuthMethod(2, "PASSWORD");
    public static final Socks5AuthMethod UNACCEPTED = new Socks5AuthMethod(255, "UNACCEPTED");
    private final byte byteValue;
    private final String name;
    private String text;

    public static boolean isPrivateMethod(byte b) {
        int ubyte = b & 255;
        return ubyte >= 128 && ubyte <= 254;
    }

    public static Socks5AuthMethod valueOf(byte b) {
        switch (b) {
            case -1:
                return UNACCEPTED;
            case 0:
                return NO_AUTH;
            case 1:
                return GSSAPI;
            case 2:
                return PASSWORD;
            default:
                if (isPrivateMethod(b)) {
                    return new Socks5AuthMethod(b, "PRIVATE_" + (b & 255));
                }
                return new Socks5AuthMethod(b);
        }
    }

    public Socks5AuthMethod(int byteValue) {
        this(byteValue, "UNKNOWN");
    }

    public Socks5AuthMethod(int byteValue, String name) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.byteValue = (byte) byteValue;
    }

    public byte byteValue() {
        return this.byteValue;
    }

    public int hashCode() {
        return this.byteValue;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Socks5AuthMethod) && this.byteValue == ((Socks5AuthMethod) obj).byteValue;
    }

    @Override // java.lang.Comparable
    public int compareTo(Socks5AuthMethod o) {
        return this.byteValue - o.byteValue;
    }

    public String toString() {
        String text = this.text;
        if (text == null) {
            String str = this.name + '(' + (this.byteValue & 255) + ')';
            text = str;
            this.text = str;
        }
        return text;
    }
}
