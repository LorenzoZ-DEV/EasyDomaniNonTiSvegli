package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/DefaultSocks5PrivateAuthResponse.class */
public final class DefaultSocks5PrivateAuthResponse extends AbstractSocks5Message implements Socks5PrivateAuthResponse {
    private final Socks5PrivateAuthStatus status;

    public DefaultSocks5PrivateAuthResponse(Socks5PrivateAuthStatus authStatus) {
        this.status = (Socks5PrivateAuthStatus) ObjectUtil.checkNotNull(authStatus, "authStatus");
    }

    @Override // io.netty.handler.codec.socksx.v5.Socks5PrivateAuthResponse
    public Socks5PrivateAuthStatus status() {
        return this.status;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
        DecoderResult decoderResult = decoderResult();
        if (!decoderResult.isSuccess()) {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", status: ");
            buf.append(this.status);
            buf.append(')');
        } else {
            buf.append("(status: ");
            buf.append(this.status);
            buf.append(')');
        }
        return buf.toString();
    }
}
