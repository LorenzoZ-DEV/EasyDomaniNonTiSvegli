package io.netty.handler.codec.socks;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksCmdType.class */
public enum SocksCmdType {
    CONNECT((byte) 1),
    BIND((byte) 2),
    UDP((byte) 3),
    UNKNOWN((byte) -1);

    private final byte b;

    SocksCmdType(byte b) {
        this.b = b;
    }

    @Deprecated
    public static SocksCmdType fromByte(byte b) {
        return valueOf(b);
    }

    public static SocksCmdType valueOf(byte b) {
        for (SocksCmdType code : values()) {
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
