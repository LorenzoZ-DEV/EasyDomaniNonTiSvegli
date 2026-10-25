package io.netty.handler.codec.socksx.v4;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v4/Socks4CommandResponse.class */
public interface Socks4CommandResponse extends Socks4Message {
    Socks4CommandStatus status();

    String dstAddr();

    int dstPort();
}
