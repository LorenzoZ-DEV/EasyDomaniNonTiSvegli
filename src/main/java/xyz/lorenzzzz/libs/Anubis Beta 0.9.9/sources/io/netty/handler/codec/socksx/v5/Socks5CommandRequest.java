package io.netty.handler.codec.socksx.v5;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandRequest.class */
public interface Socks5CommandRequest extends Socks5Message {
    Socks5CommandType type();

    Socks5AddressType dstAddrType();

    String dstAddr();

    int dstPort();
}
