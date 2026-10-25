package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PasswordAuthRequestDecoder.class */
public class Socks5PasswordAuthRequestDecoder extends ReplayingDecoder<State> {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PasswordAuthRequestDecoder$State.class */
    public enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5PasswordAuthRequestDecoder() {
        super(State.INIT);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:11:0x00c1 A[Catch: Exception -> 0x00dd, TryCatch #0 {Exception -> 0x00dd, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x003c, B:7:0x005c, B:8:0x005d, B:9:0x00b6, B:11:0x00c1, B:12:0x00d1), top: B:17:0x0000 }] */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes;
        try {
            switch ((State) state()) {
                case INIT:
                    int startOffset = in.readerIndex();
                    byte version = in.getByte(startOffset);
                    if (version != 1) {
                        throw new DecoderException("unsupported subnegotiation version: " + ((int) version) + " (expected: 1)");
                    }
                    int usernameLength = in.getUnsignedByte(startOffset + 1);
                    int passwordLength = in.getUnsignedByte(startOffset + 2 + usernameLength);
                    int totalLength = usernameLength + passwordLength + 3;
                    in.skipBytes(totalLength);
                    out.add(new DefaultSocks5PasswordAuthRequest(in.toString(startOffset + 2, usernameLength, CharsetUtil.US_ASCII), in.toString(startOffset + 3 + usernameLength, passwordLength, CharsetUtil.US_ASCII)));
                    checkpoint(State.SUCCESS);
                    readableBytes = actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                    }
                    return;
                case SUCCESS:
                    readableBytes = actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                    }
                    return;
                case FAILURE:
                    in.skipBytes(actualReadableBytes());
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            fail(out, e);
        }
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        checkpoint(State.FAILURE);
        Socks5Message m = new DefaultSocks5PasswordAuthRequest("", "");
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
