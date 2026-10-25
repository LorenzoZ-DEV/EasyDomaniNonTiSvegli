package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/DefaultSocks5PrivateAuthRequest.class */
public final class DefaultSocks5PrivateAuthRequest extends AbstractSocks5Message implements Socks5PrivateAuthRequest {
    private final byte[] privateToken;

    public DefaultSocks5PrivateAuthRequest(byte[] privateAuthToken) {
        this.privateToken = (byte[]) ((byte[]) ObjectUtil.checkNotNull(privateAuthToken, "privateToken")).clone();
    }

    @Override // io.netty.handler.codec.socksx.v5.Socks5PrivateAuthRequest
    public byte[] privateToken() {
        return (byte[]) this.privateToken.clone();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
        DecoderResult decoderResult = decoderResult();
        if (!decoderResult.isSuccess()) {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", privateToken: ****)");
        } else {
            buf.append("(privateToken: ****)");
        }
        return buf.toString();
    }
}
