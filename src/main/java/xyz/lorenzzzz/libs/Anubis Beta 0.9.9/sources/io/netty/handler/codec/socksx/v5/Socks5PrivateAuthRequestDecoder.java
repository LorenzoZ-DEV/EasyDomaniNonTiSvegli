package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.EmptyArrays;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PrivateAuthRequestDecoder.class */
public final class Socks5PrivateAuthRequestDecoder extends ByteToMessageDecoder {
    private boolean decoded;

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (this.decoded) {
                int readableBytes = in.readableBytes();
                if (readableBytes > 0) {
                    out.add(in.readRetainedSlice(readableBytes));
                    return;
                }
                return;
            }
            if (in.readableBytes() < 2) {
                return;
            }
            int startOffset = in.readerIndex();
            byte version = in.getByte(startOffset);
            if (version != 1) {
                throw new DecoderException("unsupported subnegotiation version: " + ((int) version) + " (expected: 1)");
            }
            int tokenLength = in.getUnsignedByte(startOffset + 1);
            if (in.readableBytes() < 2 + tokenLength) {
                return;
            }
            in.skipBytes(2);
            byte[] token = new byte[tokenLength];
            in.readBytes(token);
            out.add(new DefaultSocks5PrivateAuthRequest(token));
            this.decoded = true;
        } catch (Exception e) {
            fail(out, e);
        }
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        this.decoded = true;
        Socks5Message m = new DefaultSocks5PrivateAuthRequest(EmptyArrays.EMPTY_BYTES);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
