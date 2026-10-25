package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/UnknownSocksRequest.class */
public final class UnknownSocksRequest extends SocksRequest {
    public UnknownSocksRequest() {
        super(SocksRequestType.UNKNOWN);
    }

    @Override // io.netty.handler.codec.socks.SocksMessage
    public void encodeAsByteBuf(ByteBuf byteBuf) {
    }
}
