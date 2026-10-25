package io.netty.handler.codec.socks;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksAuthStatus.class */
public enum SocksAuthStatus {
    SUCCESS((byte) 0),
    FAILURE((byte) -1);

    private final byte b;

    SocksAuthStatus(byte b) {
        this.b = b;
    }

    @Deprecated
    public static SocksAuthStatus fromByte(byte b) {
        return valueOf(b);
    }

    public static SocksAuthStatus valueOf(byte b) {
        for (SocksAuthStatus code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return FAILURE;
    }

    public byte byteValue() {
        return this.b;
    }
}
