package io.netty.handler.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PrivateAuthRequest;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5ClientEncoder;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponseDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponseDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;
import io.netty.handler.codec.socksx.v5.Socks5PrivateAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PrivateAuthResponseDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PrivateAuthStatus;
import io.netty.util.NetUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collections;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/netty-handler-proxy-4.2.7.Final.jar:io/netty/handler/proxy/Socks5ProxyHandler.class */
public final class Socks5ProxyHandler extends ProxyHandler {
    private static final String PROTOCOL = "socks5";
    private static final String AUTH_PASSWORD = "password";
    private static final String AUTH_PRIVATE = "private";
    private static final byte NO_PRIVATE_AUTH_METHOD = Socks5AuthMethod.NO_AUTH.byteValue();
    private static final Socks5InitialRequest INIT_REQUEST_NO_AUTH = new DefaultSocks5InitialRequest(Collections.singletonList(Socks5AuthMethod.NO_AUTH));
    private static final Socks5InitialRequest INIT_REQUEST_PASSWORD = new DefaultSocks5InitialRequest(Arrays.asList(Socks5AuthMethod.NO_AUTH, Socks5AuthMethod.PASSWORD));
    private final String username;
    private final String password;
    private final byte privateAuthMethod;
    private final byte[] privateToken;
    private final Socks5ClientEncoder clientEncoder;
    private String decoderName;
    private String encoderName;

    public Socks5ProxyHandler(SocketAddress proxyAddress) {
        this(proxyAddress, null, null);
    }

    public Socks5ProxyHandler(SocketAddress proxyAddress, String username, String password) {
        super(proxyAddress);
        if (username != null && username.isEmpty()) {
            username = null;
        }
        if (password != null && password.isEmpty()) {
            password = null;
        }
        this.username = username;
        this.password = password;
        this.privateToken = null;
        this.privateAuthMethod = NO_PRIVATE_AUTH_METHOD;
        this.clientEncoder = Socks5ClientEncoder.DEFAULT;
    }

    public Socks5ProxyHandler(SocketAddress proxyAddress, byte privateAuthMethod, byte[] privateToken, Socks5ClientEncoder customEncoder) {
        super(proxyAddress);
        if (!Socks5AuthMethod.isPrivateMethod(privateAuthMethod)) {
            throw new IllegalArgumentException("privateAuthMethod: " + (privateAuthMethod & 255) + " (expected: 0x80-0xFE)");
        }
        this.password = null;
        this.username = null;
        this.privateToken = privateToken;
        this.privateAuthMethod = privateAuthMethod;
        this.clientEncoder = customEncoder != null ? customEncoder : Socks5ClientEncoder.DEFAULT;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    public String protocol() {
        return PROTOCOL;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    public String authScheme() {
        Socks5AuthMethod authMethod = socksAuthMethod();
        if (Socks5AuthMethod.isPrivateMethod(authMethod.byteValue())) {
            return AUTH_PRIVATE;
        }
        if (authMethod == Socks5AuthMethod.PASSWORD) {
            return AUTH_PASSWORD;
        }
        return "none";
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void addCodec(ChannelHandlerContext ctx) throws Exception {
        ChannelPipeline p = ctx.pipeline();
        String name = ctx.name();
        Socks5InitialResponseDecoder decoder = new Socks5InitialResponseDecoder();
        p.addBefore(name, (String) null, decoder);
        this.decoderName = p.context(decoder).name();
        this.encoderName = this.decoderName + ".encoder";
        p.addBefore(name, this.encoderName, this.clientEncoder);
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void removeEncoder(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().remove(this.encoderName);
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected void removeDecoder(ChannelHandlerContext ctx) throws Exception {
        ChannelPipeline p = ctx.pipeline();
        if (p.context(this.decoderName) != null) {
            p.remove(this.decoderName);
        }
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception {
        Socks5AuthMethod authMethod = socksAuthMethod();
        if (authMethod == Socks5AuthMethod.PASSWORD) {
            return INIT_REQUEST_PASSWORD;
        }
        if (Socks5AuthMethod.isPrivateMethod(authMethod.byteValue())) {
            return new DefaultSocks5InitialRequest(Arrays.asList(Socks5AuthMethod.NO_AUTH, authMethod));
        }
        return INIT_REQUEST_NO_AUTH;
    }

    @Override // io.netty.handler.proxy.ProxyHandler
    protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception {
        if (response instanceof Socks5InitialResponse) {
            Socks5InitialResponse res = (Socks5InitialResponse) response;
            Socks5AuthMethod authMethod = socksAuthMethod();
            Socks5AuthMethod resAuthMethod = res.authMethod();
            if (resAuthMethod != Socks5AuthMethod.NO_AUTH && resAuthMethod != authMethod && !Socks5AuthMethod.isPrivateMethod(resAuthMethod.byteValue())) {
                throw new ProxyConnectException(exceptionMessage("unexpected authMethod: " + res.authMethod()));
            }
            if (resAuthMethod == Socks5AuthMethod.NO_AUTH) {
                sendConnectCommand(ctx);
                return false;
            }
            if (resAuthMethod == Socks5AuthMethod.PASSWORD) {
                ctx.pipeline().replace(this.decoderName, this.decoderName, new Socks5PasswordAuthResponseDecoder());
                sendToProxyServer(new DefaultSocks5PasswordAuthRequest(this.username != null ? this.username : "", this.password != null ? this.password : ""));
                return false;
            }
            if (Socks5AuthMethod.isPrivateMethod(resAuthMethod.byteValue())) {
                ctx.pipeline().replace(this.decoderName, this.decoderName, new Socks5PrivateAuthResponseDecoder());
                sendToProxyServer(new DefaultSocks5PrivateAuthRequest(this.privateToken));
                return false;
            }
            throw new Error("Unexpected authMethod: " + resAuthMethod);
        }
        if (response instanceof Socks5PasswordAuthResponse) {
            Socks5PasswordAuthResponse res2 = (Socks5PasswordAuthResponse) response;
            if (res2.status() != Socks5PasswordAuthStatus.SUCCESS) {
                throw new ProxyConnectException(exceptionMessage("authStatus: " + res2.status()));
            }
            sendConnectCommand(ctx);
            return false;
        }
        if (response instanceof Socks5PrivateAuthResponse) {
            Socks5PrivateAuthResponse res3 = (Socks5PrivateAuthResponse) response;
            if (res3.status() != Socks5PrivateAuthStatus.SUCCESS) {
                throw new ProxyConnectException(exceptionMessage("privateAuthStatus: " + res3.status()));
            }
            sendConnectCommand(ctx);
            return false;
        }
        Socks5CommandResponse res4 = (Socks5CommandResponse) response;
        if (res4.status() != Socks5CommandStatus.SUCCESS) {
            throw new ProxyConnectException(exceptionMessage("status: " + res4.status()));
        }
        return true;
    }

    private Socks5AuthMethod socksAuthMethod() {
        Socks5AuthMethod authMethod;
        if (this.privateToken != null && this.privateToken.length > 0) {
            authMethod = new Socks5AuthMethod(this.privateAuthMethod & 255, "PRIVATE_" + (this.privateAuthMethod & 255));
        } else if (this.username == null && this.password == null) {
            authMethod = Socks5AuthMethod.NO_AUTH;
        } else {
            authMethod = Socks5AuthMethod.PASSWORD;
        }
        return authMethod;
    }

    private void sendConnectCommand(ChannelHandlerContext ctx) throws Exception {
        String rhost;
        Socks5AddressType addrType;
        InetSocketAddress raddr = (InetSocketAddress) destinationAddress();
        if (raddr.isUnresolved()) {
            addrType = Socks5AddressType.DOMAIN;
            rhost = raddr.getHostString();
        } else {
            rhost = raddr.getAddress().getHostAddress();
            if (NetUtil.isValidIpV4Address(rhost)) {
                addrType = Socks5AddressType.IPv4;
            } else if (NetUtil.isValidIpV6Address(rhost)) {
                addrType = Socks5AddressType.IPv6;
            } else {
                throw new ProxyConnectException(exceptionMessage("unknown address type: " + StringUtil.simpleClassName(rhost)));
            }
        }
        ctx.pipeline().replace(this.decoderName, this.decoderName, new Socks5CommandResponseDecoder());
        sendToProxyServer(new DefaultSocks5CommandRequest(Socks5CommandType.CONNECT, addrType, rhost, raddr.getPort()));
    }
}
