package io.netty.handler.codec.socks;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksProtocolVersion.class */
public enum SocksProtocolVersion {
    SOCKS4a((byte) 4),
    SOCKS5((byte) 5),
    UNKNOWN((byte) -1);

    private final byte b;

    SocksProtocolVersion(byte b) {
        this.b = b;
    }

    @Deprecated
    public static SocksProtocolVersion fromByte(byte b) {
        return valueOf(b);
    }

    public static SocksProtocolVersion valueOf(byte b) {
        for (SocksProtocolVersion code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}
