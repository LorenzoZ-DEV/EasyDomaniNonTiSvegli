package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksMessage.class */
public abstract class SocksMessage {
    private final SocksMessageType type;
    private final SocksProtocolVersion protocolVersion = SocksProtocolVersion.SOCKS5;

    @Deprecated
    public abstract void encodeAsByteBuf(ByteBuf byteBuf);

    protected SocksMessage(SocksMessageType type) {
        this.type = (SocksMessageType) ObjectUtil.checkNotNull(type, "type");
    }

    public SocksMessageType type() {
        return this.type;
    }

    public SocksProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }
}
