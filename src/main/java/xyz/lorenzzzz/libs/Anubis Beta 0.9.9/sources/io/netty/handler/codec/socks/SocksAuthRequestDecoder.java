package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksAuthRequestDecoder.class */
public class SocksAuthRequestDecoder extends ReplayingDecoder<State> {
    private String username;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socks/SocksAuthRequestDecoder$State.class */
    public enum State {
        CHECK_PROTOCOL_VERSION,
        READ_USERNAME,
        READ_PASSWORD
    }

    public SocksAuthRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        switch ((State) state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksSubnegotiationVersion.AUTH_PASSWORD.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
                } else {
                    checkpoint(State.READ_USERNAME);
                    break;
                }
                ctx.pipeline().remove(this);
                return;
            case READ_USERNAME:
                int fieldLength = byteBuf.readByte();
                this.username = byteBuf.readString(fieldLength, CharsetUtil.US_ASCII);
                checkpoint(State.READ_PASSWORD);
            case READ_PASSWORD:
                int fieldLength2 = byteBuf.readByte();
                String password = byteBuf.readString(fieldLength2, CharsetUtil.US_ASCII);
                out.add(new SocksAuthRequest(this.username, password));
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error("Unexpected request decoder state: " + state());
        }
    }
}
