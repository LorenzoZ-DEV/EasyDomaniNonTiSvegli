package xyz.lorenzzzz.easydomani.tabcomplete.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.tabcomplete.config.TabCompleteRateSettings;
import xyz.lorenzzzz.easydomani.tabcomplete.service.TabCompleteRateService;

public final class TabCompletePacketListener extends PacketListenerAbstract {
    private static final ModProfile BAN = profile(true);
    private static final ModProfile ALERT = profile(false);

    private final TabCompleteRateService rate;
    private final ModDetectionService detection;
    private final MessageService messages;
    private final Executor main;

    public TabCompletePacketListener(TabCompleteRateService rate, ModDetectionService detection, MessageService messages, Executor main) {
        super(PacketListenerPriority.LOWEST);
        this.rate = rate;
        this.detection = detection;
        this.messages = messages;
        this.main = main;
    }

    private static ModProfile profile(boolean ban) {
        return new ModProfile("tabcomplete-rate", "67Client (tab-complete)", ban, List.of(), List.of(), List.of(), null, List.of(), false, false);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.TAB_COMPLETE) return;
        TabCompleteRateSettings s = rate.settings();
        User u = event.getUser();
        UUID uuid = u != null ? u.getUUID() : null;
        if (!s.enabled() || uuid == null || !rate.record(uuid)) return;
        main.execute(() -> flag(uuid, s));
    }

    private void flag(UUID uuid, TabCompleteRateSettings s) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;
        messages.notifyStaff(messages.msg("tab-complete-rate", "player", p.getName(),
                "count", String.valueOf(s.maxPackets()), "window", String.valueOf(s.windowMs() / 1000)));
        detection.report(p, s.experimentalBan() ? BAN : ALERT, "tab-complete-rate");
    }
}
