package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandResponseDecoder.class */
public class Socks5CommandResponseDecoder extends ReplayingDecoder<State> {
    private final Socks5AddressDecoder addressDecoder;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandResponseDecoder$State.class */
    public enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5CommandResponseDecoder() {
        this(Socks5AddressDecoder.DEFAULT);
    }

    public Socks5CommandResponseDecoder(Socks5AddressDecoder addressDecoder) {
        super(State.INIT);
        this.addressDecoder = (Socks5AddressDecoder) ObjectUtil.checkNotNull(addressDecoder, "addressDecoder");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:11:0x00bc A[Catch: Exception -> 0x00d8, TryCatch #0 {Exception -> 0x00d8, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x0039, B:7:0x0067, B:8:0x0068, B:9:0x00b1, B:11:0x00bc, B:12:0x00cc), top: B:17:0x0000 }] */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes;
        try {
            switch ((State) state()) {
                case INIT:
                    byte version = in.readByte();
                    if (version != SocksVersion.SOCKS5.byteValue()) {
                        throw new DecoderException("unsupported version: " + ((int) version) + " (expected: " + ((int) SocksVersion.SOCKS5.byteValue()) + ')');
                    }
                    Socks5CommandStatus status = Socks5CommandStatus.valueOf(in.readByte());
                    in.skipBytes(1);
                    Socks5AddressType addrType = Socks5AddressType.valueOf(in.readByte());
                    String addr = this.addressDecoder.decodeAddress(addrType, in);
                    int port = ByteBufUtil.readUnsignedShortBE(in);
                    out.add(new DefaultSocks5CommandResponse(status, addrType, addr, port));
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
        Socks5Message m = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4, null, 0);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
