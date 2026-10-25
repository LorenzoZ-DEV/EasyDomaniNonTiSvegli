package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResultProvider;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/SocksMessage.class */
public interface SocksMessage extends DecoderResultProvider {
    SocksVersion version();
}
