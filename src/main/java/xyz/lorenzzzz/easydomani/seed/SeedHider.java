package xyz.lorenzzzz.easydomani.seed;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;

/** Anti seed cracker: hashed seed casuale in JOIN_GAME/RESPAWN, stabile per mondo durante l'uptime. */
public final class SeedHider {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final EasyDomaniNonTiSvegli plugin;
    private final Map<String, Long> fakeSeeds = new ConcurrentHashMap<>();
    private volatile boolean enabled;
    private PacketListenerAbstract listener;

    public SeedHider(EasyDomaniNonTiSvegli plugin) {
        this.plugin = plugin;
    }

    public void reload(boolean enabled) {
        this.enabled = enabled;
    }

    public void start() {
        listener = new PacketListenerAbstract(PacketListenerPriority.HIGH) {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                if (!enabled) return;
                if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
                    WrapperPlayServerJoinGame w = new WrapperPlayServerJoinGame(event);
                    w.setHashedSeed(fakeSeed(w.getWorldName()));
                    event.markForReEncode(true);
                } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
                    WrapperPlayServerRespawn w = new WrapperPlayServerRespawn(event);
                    w.setHashedSeed(fakeSeed(w.getWorldName().orElse("")));
                    event.markForReEncode(true);
                }
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    public void stop() {
        if (listener != null) PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        listener = null;
    }

    private long fakeSeed(String world) {
        return fakeSeeds.computeIfAbsent(world == null ? "" : world, k -> {
            long seed = RANDOM.nextLong();
            if (plugin.debug()) plugin.getLogger().info("[Seed] hashed seed finto per " + k);
            return seed;
        });
    }
}
