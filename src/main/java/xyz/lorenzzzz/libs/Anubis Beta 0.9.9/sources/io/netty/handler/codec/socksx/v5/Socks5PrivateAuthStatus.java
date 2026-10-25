package io.netty.handler.codec.socksx.v5;

import io.netty.util.internal.ObjectUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PrivateAuthStatus.class */
public final class Socks5PrivateAuthStatus implements Comparable<Socks5PrivateAuthStatus> {
    public static final Socks5PrivateAuthStatus SUCCESS = new Socks5PrivateAuthStatus(0, "SUCCESS");
    public static final Socks5PrivateAuthStatus FAILURE = new Socks5PrivateAuthStatus(255, "FAILURE");
    private final byte byteValue;
    private final String name;
    private String text;

    public static Socks5PrivateAuthStatus valueOf(byte b) {
        switch (b) {
            case -1:
                return FAILURE;
            case 0:
                return SUCCESS;
            default:
                return new Socks5PrivateAuthStatus(b);
        }
    }

    private Socks5PrivateAuthStatus(int byteValue) {
        this(byteValue, "UNKNOWN");
    }

    public Socks5PrivateAuthStatus(int byteValue, String name) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.byteValue = (byte) byteValue;
    }

    public byte byteValue() {
        return this.byteValue;
    }

    public boolean isSuccess() {
        return this.byteValue == 0;
    }

    public int hashCode() {
        return this.byteValue;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Socks5PrivateAuthStatus) && this.byteValue == ((Socks5PrivateAuthStatus) obj).byteValue;
    }

    @Override // java.lang.Comparable
    public int compareTo(Socks5PrivateAuthStatus o) {
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
