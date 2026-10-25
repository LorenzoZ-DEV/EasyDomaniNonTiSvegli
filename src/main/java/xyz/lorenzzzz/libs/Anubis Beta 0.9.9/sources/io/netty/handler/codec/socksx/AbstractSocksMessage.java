package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.ObjectUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/AbstractSocksMessage.class */
public abstract class AbstractSocksMessage implements SocksMessage {
    private DecoderResult decoderResult = DecoderResult.SUCCESS;

    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    public void setDecoderResult(DecoderResult decoderResult) {
        this.decoderResult = (DecoderResult) ObjectUtil.checkNotNull(decoderResult, "decoderResult");
    }
}
