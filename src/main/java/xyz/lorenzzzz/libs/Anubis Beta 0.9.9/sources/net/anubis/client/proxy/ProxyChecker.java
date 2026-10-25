package net.anubis.client.proxy;

import com.google.common.net.InetAddresses;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.ProxyHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/proxy/ProxyChecker.class */
@Environment(EnvType.CLIENT)
public final class ProxyChecker {
    private static final int TIMEOUT_MS = 8000;
    private static final int IN_FLIGHT = 32;
    private final Set<Proxy> checking = ConcurrentHashMap.newKeySet();
    private final Deque<Proxy> queue = new ArrayDeque();
    private int running;
    private EventLoopGroup group;
    private static final InetSocketAddress TARGET = new InetSocketAddress(InetAddresses.forString("1.1.1.1"), 443);
    private static final ProxyChecker INSTANCE = new ProxyChecker();

    private ProxyChecker() {
    }

    public static ProxyChecker get() {
        return INSTANCE;
    }

    public synchronized void check(Collection<Proxy> proxies) {
        for (Proxy proxy : proxies) {
            if (this.checking.add(proxy)) {
                this.queue.add(proxy);
            }
        }
        pump();
    }

    public boolean isChecking(Proxy proxy) {
        return this.checking.contains(proxy);
    }

    public int pending() {
        return this.checking.size();
    }

    private synchronized void pump() {
        if (this.group == null) {
            this.group = new MultiThreadIoEventLoopGroup(2, new DefaultThreadFactory("Anubis proxy check", true), NioIoHandler.newFactory());
        }
        while (this.running < 32 && !this.queue.isEmpty()) {
            Proxy proxy = this.queue.poll();
            this.running++;
            this.group.execute(() -> {
                start(proxy);
            });
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void start(Proxy proxy) throws MatchException {
        AtomicBoolean done = new AtomicBoolean();
        if (!proxy.isValid()) {
            finish(proxy, -1, done);
            return;
        }
        try {
            final ProxyHandler handler = ProxyRouting.handler(proxy);
            handler.setConnectTimeoutMillis(8000L);
            long started = System.nanoTime();
            ChannelFuture connect = new Bootstrap().group(this.group).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(TIMEOUT_MS)).handler(new ChannelInitializer<Channel>(this) { // from class: net.anubis.client.proxy.ProxyChecker.1
                protected void initChannel(Channel channel) {
                    channel.pipeline().addLast(new ChannelHandler[]{handler, new ChannelInboundHandlerAdapter(this) { // from class: net.anubis.client.proxy.ProxyChecker.1.1
                        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
                            context.close();
                        }
                    }});
                }
            }).connect(TARGET);
            connect.addListener(future -> {
                if (!future.isSuccess()) {
                    finish(proxy, -1, done);
                }
            });
            handler.connectFuture().addListener(future2 -> {
                int ms = (int) ((System.nanoTime() - started) / 1000000);
                finish(proxy, future2.isSuccess() ? ms : -1, done);
                connect.channel().close();
            });
        } catch (RuntimeException e) {
            finish(proxy, -1, done);
        }
    }

    private void finish(Proxy proxy, int ping, AtomicBoolean done) {
        boolean last;
        if (done.compareAndSet(false, true)) {
            proxy.ping = ping;
            this.checking.remove(proxy);
            synchronized (this) {
                this.running--;
                last = this.running == 0 && this.queue.isEmpty();
                pump();
            }
            if (last) {
                Proxies.get().checked();
            }
        }
    }
}
