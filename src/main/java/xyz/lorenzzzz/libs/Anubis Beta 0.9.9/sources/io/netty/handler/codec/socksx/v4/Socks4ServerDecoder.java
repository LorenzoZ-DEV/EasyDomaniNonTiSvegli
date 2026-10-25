package io.netty.handler.codec.socksx.v4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v4/Socks4ServerDecoder.class */
public class Socks4ServerDecoder extends ReplayingDecoder<State> {
    private static final int MAX_FIELD_LENGTH = 255;
    private Socks4CommandType type;
    private String dstAddr;
    private int dstPort;
    private String userId;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-codec-socks-4.2.7.Final.jar:io/netty/handler/codec/socksx/v4/Socks4ServerDecoder$State.class */
    public enum State {
        START,
        READ_USERID,
        READ_DOMAIN,
        SUCCESS,
        FAILURE
    }

    public Socks4ServerDecoder() {
        super(State.START);
        setSingleDecode(true);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:12:0x009f A[Catch: Exception -> 0x0101, TryCatch #0 {Exception -> 0x0101, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0030, B:6:0x0041, B:7:0x005c, B:8:0x005d, B:9:0x0082, B:10:0x0093, B:12:0x009f, B:14:0x00ab, B:15:0x00b5, B:16:0x00da, B:18:0x00e5, B:19:0x00f5), top: B:24:0x0000 }] */
    /* JADX WARN: Code duplicated, block: B:18:0x00e5 A[Catch: Exception -> 0x0101, TryCatch #0 {Exception -> 0x0101, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0030, B:6:0x0041, B:7:0x005c, B:8:0x005d, B:9:0x0082, B:10:0x0093, B:12:0x009f, B:14:0x00ab, B:15:0x00b5, B:16:0x00da, B:18:0x00e5, B:19:0x00f5), top: B:24:0x0000 }] */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes;
        try {
            switch ((State) state()) {
                case START:
                    int version = in.readUnsignedByte();
                    if (version != SocksVersion.SOCKS4a.byteValue()) {
                        throw new DecoderException("unsupported protocol version: " + version);
                    }
                    this.type = Socks4CommandType.valueOf(in.readByte());
                    this.dstPort = ByteBufUtil.readUnsignedShortBE(in);
                    this.dstAddr = NetUtil.intToIpAddress(ByteBufUtil.readIntBE(in));
                    checkpoint(State.READ_USERID);
                    this.userId = readString("userid", in);
                    checkpoint(State.READ_DOMAIN);
                    if (!"0.0.0.0".equals(this.dstAddr) && this.dstAddr.startsWith("0.0.0.")) {
                        this.dstAddr = readString("dstAddr", in);
                    }
                    out.add(new DefaultSocks4CommandRequest(this.type, this.dstAddr, this.dstPort, this.userId));
                    checkpoint(State.SUCCESS);
                    readableBytes = actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                    }
                    return;
                case READ_USERID:
                    this.userId = readString("userid", in);
                    checkpoint(State.READ_DOMAIN);
                    if (!"0.0.0.0".equals(this.dstAddr)) {
                        this.dstAddr = readString("dstAddr", in);
                    }
                    out.add(new DefaultSocks4CommandRequest(this.type, this.dstAddr, this.dstPort, this.userId));
                    checkpoint(State.SUCCESS);
                    readableBytes = actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                    }
                    return;
                case READ_DOMAIN:
                    if (!"0.0.0.0".equals(this.dstAddr)) {
                        this.dstAddr = readString("dstAddr", in);
                    }
                    out.add(new DefaultSocks4CommandRequest(this.type, this.dstAddr, this.dstPort, this.userId));
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
        Socks4CommandRequest m = new DefaultSocks4CommandRequest(this.type != null ? this.type : Socks4CommandType.CONNECT, this.dstAddr != null ? this.dstAddr : "", this.dstPort != 0 ? this.dstPort : 65535, this.userId != null ? this.userId : "");
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
        checkpoint(State.FAILURE);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: io.netty.handler.codec.DecoderException */
    private static String readString(String fieldName, ByteBuf in) throws DecoderException {
        int length = in.bytesBefore(256, (byte) 0);
        if (length < 0) {
            throw new DecoderException("field '" + fieldName + "' longer than " + MAX_FIELD_LENGTH + " chars");
        }
        String value = in.readSlice(length).toString(CharsetUtil.US_ASCII);
        in.skipBytes(1);
        return value;
    }
}
