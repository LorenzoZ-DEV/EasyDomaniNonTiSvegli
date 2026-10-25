package io.netty.handler.codec.socksx;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/SocksVersion.class */
public enum SocksVersion {
    SOCKS4a((byte) 4),
    SOCKS5((byte) 5),
    UNKNOWN((byte) -1);

    private final byte b;

    public static SocksVersion valueOf(byte b) {
        if (b == SOCKS4a.byteValue()) {
            return SOCKS4a;
        }
        if (b == SOCKS5.byteValue()) {
            return SOCKS5;
        }
        return UNKNOWN;
    }

    SocksVersion(byte b) {
        this.b = b;
    }

    public byte byteValue() {
        return this.b;
    }
}
