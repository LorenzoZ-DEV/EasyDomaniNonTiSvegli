package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksInitResponse.class */
public final class SocksInitResponse extends SocksResponse {
    private final SocksAuthScheme authScheme;

    public SocksInitResponse(SocksAuthScheme authScheme) {
        super(SocksResponseType.INIT);
        this.authScheme = (SocksAuthScheme) ObjectUtil.checkNotNull(authScheme, "authScheme");
    }

    public SocksAuthScheme authScheme() {
        return this.authScheme;
    }

    @Override // io.netty.handler.codec.socks.SocksMessage
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(protocolVersion().byteValue());
        byteBuf.writeByte(this.authScheme.byteValue());
    }
}
