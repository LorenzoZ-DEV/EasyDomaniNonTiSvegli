package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.socksx.AbstractSocksMessage;
import io.netty.handler.codec.socksx.SocksVersion;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/AbstractSocks5Message.class */
public abstract class AbstractSocks5Message extends AbstractSocksMessage implements Socks5Message {
    @Override // io.netty.handler.codec.socksx.SocksMessage
    public final SocksVersion version() {
        return SocksVersion.SOCKS5;
    }
}
