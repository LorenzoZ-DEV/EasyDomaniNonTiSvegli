package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socksx.SocksVersion;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5InitialRequestDecoder.class */
public class Socks5InitialRequestDecoder extends ReplayingDecoder<State> {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5InitialRequestDecoder$State.class */
    public enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5InitialRequestDecoder() {
        super(State.INIT);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:15:0x00b3 A[Catch: Exception -> 0x00cf, TryCatch #0 {Exception -> 0x00cf, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x0039, B:7:0x0067, B:8:0x0068, B:11:0x007f, B:12:0x0091, B:13:0x00a8, B:15:0x00b3, B:16:0x00c3), top: B:21:0x0000 }] */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes;
        try {
            switch ((State) state()) {
                case INIT:
                    byte version = in.readByte();
                    if (version != SocksVersion.SOCKS5.byteValue()) {
                        throw new DecoderException("unsupported version: " + ((int) version) + " (expected: " + ((int) SocksVersion.SOCKS5.byteValue()) + ')');
                    }
                    int authMethodCnt = in.readUnsignedByte();
                    Socks5AuthMethod[] authMethods = new Socks5AuthMethod[authMethodCnt];
                    for (int i = 0; i < authMethodCnt; i++) {
                        authMethods[i] = Socks5AuthMethod.valueOf(in.readByte());
                    }
                    out.add(new DefaultSocks5InitialRequest(authMethods));
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
        Socks5Message m = new DefaultSocks5InitialRequest(Socks5AuthMethod.NO_AUTH);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
