package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PrivateAuthResponseDecoder.class */
public final class Socks5PrivateAuthResponseDecoder extends ByteToMessageDecoder {
    private State state = State.INIT;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PrivateAuthResponseDecoder$State.class */
    private enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            switch (this.state) {
                case INIT:
                    if (in.readableBytes() < 2) {
                        return;
                    }
                    byte version = in.readByte();
                    if (version != 1) {
                        throw new DecoderException("unsupported subnegotiation version: " + ((int) version) + " (expected: 1)");
                    }
                    out.add(new DefaultSocks5PrivateAuthResponse(Socks5PrivateAuthStatus.valueOf(in.readByte())));
                    this.state = State.SUCCESS;
                    break;
                    break;
                case SUCCESS:
                    int readableBytes = in.readableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                    }
                    break;
                case FAILURE:
                    in.skipBytes(in.readableBytes());
                    break;
                default:
                    throw new Error("Unexpected response decoder state: " + this.state);
            }
        } catch (Exception e) {
            fail(out, e);
        }
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        this.state = State.FAILURE;
        Socks5Message m = new DefaultSocks5PrivateAuthResponse(Socks5PrivateAuthStatus.FAILURE);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
