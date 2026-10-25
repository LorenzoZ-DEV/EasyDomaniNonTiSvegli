package io.netty.handler.proxy;

import java.net.ConnectException;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-handler-proxy-4.2.7.Final.jar:io/netty/handler/proxy/ProxyConnectException.class */
public class ProxyConnectException extends ConnectException {
    private static final long serialVersionUID = 5211364632246265538L;

    public ProxyConnectException() {
    }

    public ProxyConnectException(String msg) {
        super(msg);
    }

    public ProxyConnectException(Throwable cause) {
        initCause(cause);
    }

    public ProxyConnectException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }
}
