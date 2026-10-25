package io.netty.handler.codec.socksx.v5;

import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5InitialRequest.class */
public interface Socks5InitialRequest extends Socks5Message {
    List<Socks5AuthMethod> authMethods();
}
