package net.anubis.client.module.impl.misc;

import com.mojang.brigadier.suggestion.Suggestion;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.PacketSendEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.StaffGhost;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.util.NameFilter;
import net.anubis.client.util.antivanish.AntiVanishHeuristics;
import net.anubis.client.util.antivanish.AntiVanishText;
import net.anubis.client.util.staff.ShardTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1657;
import net.minecraft.class_1676;
import net.minecraft.class_1934;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2588;
import net.minecraft.class_2596;
import net.minecraft.class_2639;
import net.minecraft.class_2664;
import net.minecraft.class_2675;
import net.minecraft.class_2680;
import net.minecraft.class_2703;
import net.minecraft.class_2739;
import net.minecraft.class_2741;
import net.minecraft.class_2765;
import net.minecraft.class_2767;
import net.minecraft.class_2805;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import net.minecraft.class_746;
import net.minecraft.class_7828;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule.class */
@Environment(EnvType.CLIENT)
public final class AntiVanishModule extends ClientModule {
    private static final class_310 MC = class_310.method_1551();
    private static final String PROBE_COMMAND = "minecraft:msg";
    private static final int SENSOR_RANGE = 64;
    private static final long SIGNAL_WINDOW_MS = 15000;
    private static final long DETECTION_TTL_MS = 20000;
    private static final long CRITICAL_COOLDOWN_MS = 12000;
    private static final long ANNOUNCE_COOLDOWN_MS = 1500;
    private static final long ANNOUNCE_WINDOW_MS = 4000;
    private static final int MAX_ANNOUNCE_PER_WINDOW = 6;
    private static final int MAX_OBSERVATIONS_PER_TICK = 512;
    private static final int CRITICAL_SCORE = 35;
    private static final long SELF_PLACE_TTL_MS = 6000;
    private static final long CONTAINER_SELF_GRACE_MS = 2500;
    private static final long SPELL_SETTLE_MS = 1000;
    private final ConcurrentLinkedQueue<Observation> observations;
    private final Map<UUID, KnownPlayer> knownPlayers;
    private final Map<String, Detection> detections;
    private final Map<String, Long> signalCooldowns;
    private final Map<String, Long> announceCooldowns;
    private final Deque<Long> announceTimes;
    private final Map<Long, Long> selfPlacedBlocks;
    private final Map<UUID, Long> confirmedDepartures;
    private final Map<Long, Long> automatedMechanisms;
    private final Map<String, Deque<Long>> weakParticleBursts;
    private final Deque<Signal> signals;
    private final Deque<ExplosionEvent> recentExplosions;
    private final Deque<PendingVanish> pendingVanishes;
    private final Map<String, HiddenSpell> hiddenSpells;
    private final Deque<RecentMessage> recentMessages;
    private boolean serverSendsLeaveMessages;
    private final Set<Integer> completionRequestIds;
    private volatile List<String> pendingCompletionNames;
    private int nextCompletionId;
    private static final long RECENT_MESSAGE_TTL_MS = 8000;
    private static final long TRUSTED_LISTED_MS = 1500;
    private final Map<UUID, Long> listedSinceMs;
    private final ShardTracker shard;
    private int tickCounter;
    private volatile long lastLocalActionMs;
    private long lastContainerActivityMs;
    private long lastCriticalMs;
    private long criticalUntilMs;
    private String criticalWatcher;
    private final Set<UUID> pendingChimes;
    private UUID ghostId;
    private String ghostName;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$ObservationType.class */
    @Environment(EnvType.CLIENT)
    private enum ObservationType {
        TAB_REMOVE,
        TAB_HIDE,
        PLAYER_LEFT,
        SYSTEM_CHAT,
        ENTITY_METADATA,
        POSITIONAL_SOUND,
        ENTITY_SOUND,
        PARTICLE,
        EXPLOSION,
        GAMEMODE
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$SignalType.class */
    @Environment(EnvType.CLIENT)
    private enum SignalType {
        VANISH,
        INVISIBLE,
        PARTICLE,
        SOUND
    }

    public AntiVanishModule() {
        super("anti-vanish", "Anti Vanish", "Detects vanished players.", ModuleCategory.DONUT);
        this.observations = new ConcurrentLinkedQueue<>();
        this.knownPlayers = new HashMap();
        this.detections = new LinkedHashMap();
        this.signalCooldowns = new HashMap();
        this.announceCooldowns = new HashMap();
        this.announceTimes = new ArrayDeque();
        this.selfPlacedBlocks = new ConcurrentHashMap();
        this.confirmedDepartures = new HashMap();
        this.automatedMechanisms = new HashMap();
        this.weakParticleBursts = new HashMap();
        this.signals = new ArrayDeque();
        this.recentExplosions = new ArrayDeque();
        this.pendingVanishes = new ArrayDeque();
        this.hiddenSpells = new LinkedHashMap();
        this.recentMessages = new ArrayDeque();
        this.completionRequestIds = ConcurrentHashMap.newKeySet();
        this.nextCompletionId = 30000;
        this.listedSinceMs = new HashMap();
        this.shard = new ShardTracker();
        this.criticalWatcher = "";
        this.pendingChimes = new HashSet();
        usesNotifications();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        resetRuntime();
        if (MC.field_1724 == null || MC.field_1687 == null) {
            return;
        }
        trackListedPlayers();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        resetRuntime();
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (MC.field_1724 == null || MC.field_1687 == null || MC.method_1562() == null) {
            resetRuntime();
            return;
        }
        if (this.shard.moved(MC)) {
            resetRuntime();
        }
        playPendingChime();
        this.tickCounter++;
        if (MC.field_1724.field_7512 != MC.field_1724.field_7498) {
            this.lastContainerActivityMs = System.currentTimeMillis();
        }
        drainObservations();
        processPendingVanishes();
        processCompletionProbe();
        if (this.tickCounter % 100 == 0) {
            sendCompletionProbe();
        }
        if (this.tickCounter % 10 == 0) {
            trackListedPlayers();
            reviewSpells();
        }
        if (this.tickCounter % 5 == 0) {
            scanInvisiblePlayers();
        }
        pruneState();
    }

    @EventHandler
    public void onPacketSend(PacketSendEvent event) {
        handleOutbound(event.packet);
    }

    private void handleOutbound(class_2596<?> packet) {
        if (packet == null) {
            return;
        }
        if (packet instanceof class_2885) {
            class_2885 useOn = (class_2885) packet;
            this.lastLocalActionMs = System.currentTimeMillis();
            if (useOn.method_12543() != null && useOn.method_12543().method_17777() != null) {
                long until = System.currentTimeMillis() + SELF_PLACE_TTL_MS;
                class_2338 hit = useOn.method_12543().method_17777();
                class_2338 adjacent = hit.method_10093(useOn.method_12543().method_17780());
                this.selfPlacedBlocks.put(Long.valueOf(hit.method_10063()), Long.valueOf(until));
                this.selfPlacedBlocks.put(Long.valueOf(adjacent.method_10063()), Long.valueOf(until));
                String itemPath = "";
                if (MC.field_1724 != null && useOn.method_12546() != null) {
                    class_2960 itemId = class_7923.field_41178.method_10221(MC.field_1724.method_5998(useOn.method_12546()).method_7909());
                    itemPath = AntiVanishHeuristics.path(itemId == null ? "" : itemId.toString());
                }
                markSelfMultiBlockFootprint(hit, itemPath, this.selfPlacedBlocks, until);
                markSelfMultiBlockFootprint(adjacent, itemPath, this.selfPlacedBlocks, until);
                return;
            }
            return;
        }
        if ((packet instanceof class_2846) || (packet instanceof class_2886) || (packet instanceof class_2824) || (packet instanceof class_2879)) {
            this.lastLocalActionMs = System.currentTimeMillis();
        }
    }

    static void markSelfMultiBlockFootprint(class_2338 pos, String path, Map<Long, Long> targets, long until) {
        if (pos == null || targets == null) {
            return;
        }
        targets.put(Long.valueOf(pos.method_10063()), Long.valueOf(until));
        String id = path == null ? "" : path;
        boolean vertical = (id.endsWith("_door") && !id.endsWith("trapdoor")) || id.contains("sunflower") || id.contains("lilac") || id.contains("rose_bush") || id.contains("peony") || id.contains("tall_grass") || id.contains("large_fern") || id.contains("pitcher_plant");
        if (vertical) {
            targets.put(Long.valueOf(pos.method_10084().method_10063()), Long.valueOf(until));
            targets.put(Long.valueOf(pos.method_10074().method_10063()), Long.valueOf(until));
        }
        if (id.endsWith("_bed")) {
            targets.put(Long.valueOf(pos.method_10069(1, 0, 0).method_10063()), Long.valueOf(until));
            targets.put(Long.valueOf(pos.method_10069(-1, 0, 0).method_10063()), Long.valueOf(until));
            targets.put(Long.valueOf(pos.method_10069(0, 0, 1).method_10063()), Long.valueOf(until));
            targets.put(Long.valueOf(pos.method_10069(0, 0, -1).method_10063()), Long.valueOf(until));
        }
    }

    @EventHandler
    public void onPacketReceiveNetty(PacketReceiveNettyEvent event) {
        if (handleInbound(event.packet)) {
            event.setCancelled(true);
        }
    }

    private boolean handleInbound(class_2596<?> packet) {
        if (packet instanceof class_7828) {
            class_7828 remove = (class_7828) packet;
            if (remove.comp_1105().size() < 4) {
                Iterator it = remove.comp_1105().iterator();
                while (it.hasNext()) {
                    this.observations.offer(Observation.tabRemove((UUID) it.next()));
                }
                return false;
            }
            return false;
        }
        if (packet instanceof class_2703) {
            class_2703 info = (class_2703) packet;
            if (info.method_46327().contains(class_2703.class_5893.field_40700)) {
                int unlisted = 0;
                for (class_2703.class_2705 entry : info.method_46329()) {
                    if (entry != null && !entry.comp_1108()) {
                        unlisted++;
                    }
                }
                if (unlisted > 0 && unlisted < 4) {
                    for (class_2703.class_2705 entry2 : info.method_46329()) {
                        if (entry2 != null && !entry2.comp_1108()) {
                            this.observations.offer(Observation.tabHide(entry2.comp_1106()));
                        }
                    }
                    return false;
                }
                return false;
            }
        }
        if (packet instanceof class_2703) {
            class_2703 info2 = (class_2703) packet;
            if (info2.method_46327().contains(class_2703.class_5893.field_29137)) {
                queueGamemodeUpdates(info2);
                return false;
            }
        }
        if (packet instanceof class_7439) {
            class_7439 chat = (class_7439) packet;
            String text = chat.comp_763() == null ? "" : chat.comp_763().getString();
            if (!text.isBlank()) {
                this.observations.offer(Observation.systemChat(text));
            }
            String departedName = departedPlayerName(chat.comp_763());
            if (!departedName.isBlank()) {
                this.observations.offer(Observation.playerLeft(departedName));
                return false;
            }
            return false;
        }
        if (packet instanceof class_2639) {
            class_2639 suggestions = (class_2639) packet;
            if (this.completionRequestIds.remove(Integer.valueOf(suggestions.comp_2262()))) {
                List<String> names = new ArrayList<>();
                for (Suggestion suggestion : suggestions.method_11397().getList()) {
                    String text2 = suggestion.getText();
                    if (text2 != null && !text2.isBlank()) {
                        names.add(text2.trim());
                    }
                }
                this.pendingCompletionNames = names;
                return true;
            }
        }
        if (packet instanceof class_2739) {
            class_2739 metadata = (class_2739) packet;
            this.observations.offer(Observation.entityMetadata(metadata.comp_1127()));
            return false;
        }
        if (packet instanceof class_2664) {
            class_2664 explosion = (class_2664) packet;
            this.observations.offer(Observation.explosion(explosion.comp_2883(), explosion.comp_4594()));
            return false;
        }
        if (packet instanceof class_2765) {
            class_2765 sound = (class_2765) packet;
            this.observations.offer(Observation.entitySound(sound.method_11883(), soundId(((class_3414) sound.method_11882().comp_349()).comp_3319())));
            return false;
        }
        if (packet instanceof class_2675) {
            class_2675 particles = (class_2675) packet;
            class_2960 id = class_7923.field_41180.method_10221(particles.method_11551().method_10295());
            this.observations.offer(Observation.position(ObservationType.PARTICLE, particles.method_11544(), particles.method_11547(), particles.method_11546(), id == null ? "" : id.toString()));
            return false;
        }
        if (packet instanceof class_2767) {
            onSoundPacket((class_2767) packet);
            return false;
        }
        return false;
    }

    private void onSoundPacket(class_2767 packet) {
        if (packet == null || packet.method_11894() == null || packet.method_11894().comp_349() == null) {
            return;
        }
        String id = soundId(((class_3414) packet.method_11894().comp_349()).comp_3319());
        this.observations.offer(Observation.position(ObservationType.POSITIONAL_SOUND, packet.method_11890(), packet.method_11889(), packet.method_11893(), id));
    }

    public static boolean shouldShowHud() {
        AntiVanishModule module = instance();
        return module != null && module.isEnabled() && module.hasHudContent();
    }

    private boolean hasHudContent() {
        long now = System.currentTimeMillis();
        if (now < this.criticalUntilMs) {
            return true;
        }
        for (Detection detection : this.detections.values()) {
            if (detection.expiresAt > now && detectionWorthShowing(detection)) {
                return true;
            }
        }
        return false;
    }

    private static boolean detectionWorthShowing(Detection detection) {
        if (detection == null) {
            return false;
        }
        String name = detection.name == null ? "" : detection.name.trim();
        return (name.isBlank() || "Unknown".equalsIgnoreCase(name) || "You".equalsIgnoreCase(name) || "CRITICAL".equalsIgnoreCase(name)) ? false : true;
    }

    public static String hudTag(HudEntry entry) {
        if (entry == null) {
            return "WATCH";
        }
        if ("CRITICAL".equalsIgnoreCase(entry.name())) {
            return "ALERT";
        }
        String lower = entry.reason() == null ? "" : entry.reason().toLowerCase(Locale.ROOT);
        if (lower.startsWith("vanish event")) {
            return "VANISH";
        }
        if (lower.startsWith("invisible entity")) {
            return "INVIS";
        }
        if (lower.startsWith("suspicious sound")) {
            return "SOUND";
        }
        return lower.startsWith("ghost particle") ? "PARTICLE" : "WATCH";
    }

    public static String hudValue(HudEntry entry) {
        if (entry == null) {
            return "Staff";
        }
        if ("CRITICAL".equalsIgnoreCase(entry.name())) {
            return shortName(entry.reason());
        }
        String name = entry.name() == null ? "" : entry.name().trim();
        return (name.isBlank() || "Unknown".equalsIgnoreCase(name) || "You".equalsIgnoreCase(name)) ? "Staff" : shortName(name);
    }

    private static String shortName(String name) {
        String shown = name == null ? "" : NameFilter.apply(name.trim());
        return shown.length() <= 16 ? shown : shown.substring(0, 16);
    }

    public static List<HudEntry> hudEntries() {
        AntiVanishModule module = instance();
        return (module == null || !shouldShowHud()) ? List.of() : module.hudSnapshot();
    }

    private static AntiVanishModule instance() {
        return (AntiVanishModule) ModuleRegistry.get(AntiVanishModule.class);
    }

    private void drainObservations() {
        Observation observation;
        for (int i = 0; i < 512 && (observation = this.observations.poll()) != null; i++) {
            processObservation(observation);
        }
        while (this.observations.size() > 4096) {
            this.observations.poll();
        }
    }

    private void processObservation(Observation observation) {
        switch (observation.type) {
            case TAB_REMOVE:
                handleTabRemoval(observation.profileId);
                break;
            case TAB_HIDE:
                handleTabHidden(observation.profileId);
                break;
            case PLAYER_LEFT:
                this.serverSendsLeaveMessages = true;
                confirmDeparture(observation.detail);
                break;
            case SYSTEM_CHAT:
                cacheRecentMessage(observation.detail);
                break;
            case ENTITY_METADATA:
                inspectInvisibleEntity(observation.entityId);
                break;
            case POSITIONAL_SOUND:
                inspectPositionalSound(observation);
                break;
            case ENTITY_SOUND:
                inspectEntitySound(observation);
                break;
            case PARTICLE:
                inspectParticle(observation);
                break;
            case EXPLOSION:
                rememberExplosion(observation);
                break;
            case GAMEMODE:
                announceGamemode(observation.profileId, observation.detail);
                break;
        }
    }

    private void trackListedPlayers() {
        if (MC.method_1562() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        for (class_640 info : MC.method_1562().method_45732()) {
            if (info != null && info.method_2966() != null && info.method_2966().id() != null) {
                UUID uuid = info.method_2966().id();
                if (MC.field_1724 == null || !MC.field_1724.method_5667().equals(uuid)) {
                    this.listedSinceMs.putIfAbsent(uuid, Long.valueOf(now));
                    String name = info.method_2966().name();
                    if (name != null && !this.knownPlayers.containsKey(uuid)) {
                        this.knownPlayers.put(uuid, new KnownPlayer(uuid, name));
                    }
                    if (name != null && !this.hiddenSpells.isEmpty()) {
                        endSpell(name, false, SPELL_SETTLE_MS);
                    }
                }
            }
        }
        if (this.listedSinceMs.size() > 1024 || this.knownPlayers.size() > 1024) {
            Set<UUID> connected = new HashSet<>();
            for (class_640 info2 : MC.method_1562().method_2880()) {
                if (info2 != null && info2.method_2966() != null) {
                    connected.add(info2.method_2966().id());
                }
            }
            this.listedSinceMs.keySet().retainAll(connected);
            this.knownPlayers.keySet().retainAll(connected);
        }
    }

    private boolean trustedListed(UUID uuid) {
        Long since = uuid == null ? null : this.listedSinceMs.get(uuid);
        return since != null && System.currentTimeMillis() - since.longValue() >= 1500;
    }

    private boolean credibleSubject(UUID uuid, String name) {
        return uuid != null && AntiVanishText.isUsername(name) && StaffListModule.isStaffName(name) && trustedListed(uuid);
    }

    private void handleTabRemoval(UUID uuid) {
        if (uuid == null || MC.field_1724.method_5667().equals(uuid)) {
            return;
        }
        String name = knownName(uuid);
        if (credibleSubject(uuid, name) && AntiVanishText.isPlausiblePlayerName(name)) {
            this.pendingVanishes.removeIf(pending -> {
                return pending.uuid.equals(uuid);
            });
            this.pendingVanishes.addLast(new PendingVanish(uuid, name, this.tickCounter + 20));
        }
    }

    private void handleTabHidden(UUID uuid) {
        if (uuid == null || MC.field_1724.method_5667().equals(uuid)) {
            return;
        }
        KnownPlayer known = this.knownPlayers.get(uuid);
        String name = (known == null || known.name == null) ? knownName(uuid) : known.name;
        if (credibleSubject(uuid, name) && AntiVanishText.isPlausiblePlayerName(name) && !recentMessageNames(name)) {
            reportHidden(uuid, name, false, "Vanish Event: hidden from TAB", "hidden from TAB", 100);
        }
    }

    private String knownName(UUID uuid) {
        class_640 info;
        KnownPlayer known = this.knownPlayers.get(uuid);
        if (known != null && known.name != null && !known.name.isBlank()) {
            return known.name;
        }
        if (MC.method_1562() == null || (info = MC.method_1562().method_2871(uuid)) == null || info.method_2966() == null) {
            return null;
        }
        return info.method_2966().name();
    }

    private UUID knownUuid(String name) {
        for (KnownPlayer known : this.knownPlayers.values()) {
            if (known.name.equalsIgnoreCase(name)) {
                return known.uuid;
            }
        }
        return null;
    }

    private void confirmDeparture(String displayedName) {
        long now = System.currentTimeMillis();
        for (KnownPlayer known : this.knownPlayers.values()) {
            if (AntiVanishText.containsPlayerName(displayedName, known.name)) {
                this.confirmedDepartures.put(known.uuid, Long.valueOf(now + 5000));
                this.pendingVanishes.removeIf(pending -> {
                    return pending.uuid.equals(known.uuid);
                });
                this.hiddenSpells.remove(spellKey(known.name));
                Detection detection = this.detections.get(known.uuid.toString());
                if (detection != null && tabDepartureReason(detection.reason)) {
                    this.detections.remove(known.uuid.toString());
                }
                this.signals.removeIf(signal -> {
                    return signal.type == SignalType.VANISH && signal.subject.equalsIgnoreCase(known.name) && tabDepartureReason(signal.reason);
                });
            }
        }
    }

    private void processPendingVanishes() {
        while (!this.pendingVanishes.isEmpty() && this.pendingVanishes.peekFirst().dueTick <= this.tickCounter) {
            PendingVanish pending = this.pendingVanishes.removeFirst();
            if (MC.method_1562().method_2871(pending.uuid) == null) {
                long now = System.currentTimeMillis();
                if (this.confirmedDepartures.getOrDefault(pending.uuid, 0L).longValue() <= now) {
                    if (recentMessageNames(pending.name)) {
                        this.serverSendsLeaveMessages = true;
                    } else {
                        class_1657 remaining = MC.field_1687.method_18470(pending.uuid);
                        if (remaining != null && !remaining.method_31481()) {
                            reportHidden(pending.uuid, pending.name, false, "Vanish Event: entity remained", "off TAB, still nearby", 100);
                        } else {
                            int score = silentTabRemovalScore(this.serverSendsLeaveMessages);
                            if (this.serverSendsLeaveMessages) {
                                reportHidden(pending.uuid, pending.name, false, "Vanish Event: silent TAB disappearance", "left TAB silently", score);
                            } else {
                                RegionProof proof = regionProof(pending.uuid);
                                if (proof != null) {
                                    this.signals.addLast(new Signal(SignalType.VANISH, pending.name, proof, "Vanish Event: silent TAB disappearance", score, now));
                                    evaluateCritical();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static int silentTabRemovalScore(boolean serverSendsLeaveMessages) {
        return serverSendsLeaveMessages ? 100 : 30;
    }

    static boolean tabDepartureReason(String reason) {
        if (reason == null) {
            return false;
        }
        String lower = reason.toLowerCase(Locale.ROOT);
        return lower.startsWith("vanish event:") && (lower.contains("tab") || lower.contains("entity remained") || lower.contains("no leave packet"));
    }

    private void cacheRecentMessage(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        long now = System.currentTimeMillis();
        this.recentMessages.addLast(new RecentMessage(text, now));
        while (!this.recentMessages.isEmpty()) {
            if (now - this.recentMessages.peekFirst().atMs > RECENT_MESSAGE_TTL_MS || this.recentMessages.size() > 64) {
                this.recentMessages.removeFirst();
            } else {
                return;
            }
        }
    }

    private boolean recentMessageNames(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        long now = System.currentTimeMillis();
        for (RecentMessage message : this.recentMessages) {
            if (now - message.atMs <= RECENT_MESSAGE_TTL_MS && AntiVanishText.looksLikeLeaveMessage(message.text, name)) {
                return true;
            }
        }
        return false;
    }

    private void sendCompletionProbe() {
        if (MC.method_1562() == null) {
            return;
        }
        int id = this.nextCompletionId;
        this.nextCompletionId = id + 1;
        if (this.nextCompletionId > 40000) {
            this.nextCompletionId = 30000;
        }
        this.completionRequestIds.add(Integer.valueOf(id));
        while (this.completionRequestIds.size() > 8) {
            this.completionRequestIds.remove(this.completionRequestIds.iterator().next());
        }
        try {
            MC.method_1562().method_52787(new class_2805(id, PROBE_COMMAND.trim() + " "));
        } catch (Throwable th) {
        }
    }

    private void processCompletionProbe() {
        List<String> current = this.pendingCompletionNames;
        if (current == null) {
            return;
        }
        this.pendingCompletionNames = null;
        if (MC.method_1562() == null || MC.field_1724 == null) {
            return;
        }
        Set<String> tabNames = new HashSet<>();
        for (class_640 info : MC.method_1562().method_2880()) {
            if (info != null && info.method_2966() != null && info.method_2966().name() != null) {
                tabNames.add(info.method_2966().name().toLowerCase(Locale.ROOT));
            }
        }
        String self = MC.field_1724.method_5477().getString();
        List<String> hidden = new ArrayList<>();
        for (String name : current) {
            if (StaffListModule.isStaffName(name) && AntiVanishText.isPlausiblePlayerName(name) && !name.equalsIgnoreCase(self) && !tabNames.contains(name.toLowerCase(Locale.ROOT)) && !recentMessageNames(name)) {
                hidden.add(name);
            }
        }
        if (hidden.isEmpty() || hidden.size() > 3) {
            return;
        }
        for (String name2 : hidden) {
            reportHidden(knownUuid(name2), name2, false, "Vanish Event: hidden but targetable", "off TAB, still targetable", 90);
        }
    }

    private static String departedPlayerName(class_2561 component) {
        if (component == null) {
            return "";
        }
        class_2588 class_2588VarMethod_10851 = component.method_10851();
        if (!(class_2588VarMethod_10851 instanceof class_2588)) {
            return "";
        }
        class_2588 translated = class_2588VarMethod_10851;
        if (!"multiplayer.player.left".equals(translated.method_11022())) {
            return "";
        }
        Object[] args = translated.method_11023();
        if (args.length == 0 || args[0] == null) {
            return "";
        }
        Object obj = args[0];
        if (!(obj instanceof class_2561)) {
            return String.valueOf(args[0]);
        }
        class_2561 name = (class_2561) obj;
        return name.getString();
    }

    private void reportHidden(UUID uuid, String name, boolean spectator, String reason, String detail, int score) {
        upsertDetection(detectionKey(uuid, name), name, reason, score, System.currentTimeMillis() + DETECTION_TTL_MS);
        HiddenSpell spell = this.hiddenSpells.get(spellKey(name));
        if (spell == null || spell.spectator != spectator) {
            this.hiddenSpells.put(spellKey(name), new HiddenSpell(uuid, name, spectator, detail, regionProof(uuid), this.tickCounter));
        }
    }

    private void reviewSpells() {
        Iterator<HiddenSpell> it = this.hiddenSpells.values().iterator();
        while (it.hasNext()) {
            HiddenSpell spell = it.next();
            if (spell.spectator && MC.method_1562().method_2871(spell.uuid) == null) {
                it.remove();
            } else if (!spell.watched && spell.sinceTick < this.tickCounter) {
                RegionProof proof = regionProof(spell.uuid);
                if (proof == null && !spell.told) {
                    proof = spell.proof;
                }
                if (proof != null) {
                    spell.told = true;
                    spell.watched = true;
                    watchingYou(spell.name, proof);
                } else if (!spell.told) {
                    spell.told = true;
                    news(spell.name, spell.how(), spell.detail);
                }
            }
        }
    }

    private boolean endSpell(String name, boolean spectator, long settleMs) {
        HiddenSpell spell = this.hiddenSpells.get(spellKey(name));
        if (spell == null || spell.spectator != spectator || System.currentTimeMillis() - spell.sinceMs < settleMs) {
            return false;
        }
        this.hiddenSpells.remove(spellKey(name));
        this.detections.remove(detectionKey(spell.uuid, spell.name));
        if (spell.told) {
            news(spell.name, ModuleChat.body().text("is ").good("visible again").build(), "");
            return true;
        }
        return true;
    }

    private RegionProof regionProof(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        class_746 class_746VarMethod_18470 = MC.field_1687.method_18470(uuid);
        if (class_746VarMethod_18470 != null && class_746VarMethod_18470 != MC.field_1724) {
            return new RegionProof(uuid, Math.round(class_746VarMethod_18470.method_5739(MC.field_1724)));
        }
        if (StaffListModule.isInYourRegion(uuid)) {
            return new RegionProof(uuid, -1);
        }
        return null;
    }

    private static String spellKey(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    private static String detectionKey(UUID uuid, String name) {
        return uuid != null ? uuid.toString() : name;
    }

    private void scanInvisiblePlayers() {
        double rangeSq = sensorRangeSq();
        for (class_746 class_746Var : MC.field_1687.method_18456()) {
            if (class_746Var != null && class_746Var != MC.field_1724 && class_746Var.method_5767() && class_746Var.method_5858(MC.field_1724) <= rangeSq && realPlayer(class_746Var)) {
                triggerSensor(SignalType.INVISIBLE, (class_1657) class_746Var, "Invisible Entity: metadata flag", 35, 5000L);
            }
        }
    }

    private void inspectInvisibleEntity(int entityId) {
        class_746 class_746Var;
        class_746 class_746VarMethod_8469 = MC.field_1687.method_8469(entityId);
        if ((class_746VarMethod_8469 instanceof class_1657) && (class_746Var = (class_1657) class_746VarMethod_8469) != MC.field_1724 && class_746Var.method_5767() && class_746Var.method_5858(MC.field_1724) <= sensorRangeSq() && realPlayer(class_746Var)) {
            triggerSensor(SignalType.INVISIBLE, (class_1657) class_746Var, "Invisible Entity: metadata flag", 35, 5000L);
        }
    }

    private boolean realPlayer(class_1657 player) {
        return credibleSubject(player.method_5667(), profileName(player));
    }

    private static String profileName(class_1657 player) {
        return player.method_7334() == null ? player.method_5477().getString() : player.method_7334().name();
    }

    private void inspectPositionalSound(Observation observation) {
        if (AntiVanishHeuristics.suspiciousSound(observation.detail)) {
            class_243 source = observation.position();
            long now = System.currentTimeMillis();
            if (!nearPlayer(source) || hasVisibleCause(source) || isExplosionRelated(source) || isPoweredMechanism(source, observation.detail) || now - this.lastLocalActionMs < SPELL_SETTLE_MS || recentlySelfPlaced(source) || villagerToggledDoor(observation.detail, source)) {
                return;
            }
            if (selfContainerActive() && isContainerSignal(observation.detail)) {
                return;
            }
            triggerSensor(SignalType.SOUND, locatedSubject(source), "Suspicious Sound: " + shortId(observation.detail), 14, 3000L);
        }
    }

    private void inspectEntitySound(Observation observation) {
        class_746 class_746Var;
        if (AntiVanishHeuristics.suspiciousSound(observation.detail)) {
            class_746 class_746VarMethod_8469 = MC.field_1687.method_8469(observation.entityId);
            if ((class_746VarMethod_8469 instanceof class_1657) && (class_746Var = (class_1657) class_746VarMethod_8469) != MC.field_1724 && class_746Var.method_5767() && class_746Var.method_5858(MC.field_1724) <= sensorRangeSq() && realPlayer(class_746Var)) {
                triggerSensor(SignalType.SOUND, (class_1657) class_746Var, "Suspicious Sound: invisible source", 16, 3000L);
            }
        }
    }

    private void inspectParticle(Observation observation) {
        if (AntiVanishHeuristics.suspiciousParticle(observation.detail)) {
            class_243 source = observation.position();
            long now = System.currentTimeMillis();
            if (!nearPlayer(source) || hasVisibleCause(source) || isExplosionRelated(source) || now - this.lastLocalActionMs < 900 || hasAmbientParticleSource(source, observation.detail)) {
                return;
            }
            String particle = shortId(observation.detail);
            if ((particle.equals("block") || particle.contains("smoke")) && nearSelf(source, 6.25d)) {
                return;
            }
            if ((particle.equals("block") || particle.contains("smoke")) && !particleBurstReady(particle, now)) {
                return;
            }
            triggerSensor(SignalType.PARTICLE, locatedSubject(source), "Ghost Particle: " + shortId(observation.detail), 16, 3000L);
        }
    }

    private boolean villagerToggledDoor(String blockId, class_243 source) {
        String path = AntiVanishHeuristics.path(blockId);
        boolean doorLike = (path.contains("door") && !path.contains("trapdoor")) || path.contains("fence_gate");
        if (!doorLike || source == null || MC.field_1687 == null) {
            return false;
        }
        for (class_1297 entity : MC.field_1687.method_18112()) {
            if (entity.method_5864() == class_1299.field_6077 && entity.method_73189().method_1025(source) <= 9.0d) {
                return true;
            }
        }
        return false;
    }

    private boolean recentlySelfPlaced(class_243 source) {
        Long until;
        return (source == null || (until = this.selfPlacedBlocks.get(Long.valueOf(class_2338.method_49638(source).method_10063()))) == null || until.longValue() <= System.currentTimeMillis()) ? false : true;
    }

    private void rememberExplosion(Observation observation) {
        long now = System.currentTimeMillis();
        this.recentExplosions.addLast(new ExplosionEvent(observation.position(), Math.max(2.0d, parseDouble(observation.detail, 4.0d) + 4.0d), now));
        while (this.recentExplosions.size() > 8) {
            this.recentExplosions.removeFirst();
        }
    }

    private boolean particleBurstReady(String particle, long now) {
        Deque<Long> burst = this.weakParticleBursts.computeIfAbsent(particle, ignored -> {
            return new ArrayDeque();
        });
        burst.addLast(Long.valueOf(now));
        while (!burst.isEmpty() && now - burst.peekFirst().longValue() > 2000) {
            burst.removeFirst();
        }
        if (burst.size() < 2) {
            return false;
        }
        burst.clear();
        return true;
    }

    private void triggerSensor(SignalType type, String where, String reason, int weight, long cooldownMs) {
        addSignal(type, where, null, reason, weight, cooldownMs);
    }

    private void triggerSensor(SignalType type, class_1657 player, String reason, int weight, long cooldownMs) {
        addSignal(type, profileName(player), regionProof(player.method_5667()), reason, weight, cooldownMs);
    }

    private void addSignal(SignalType type, String subject, RegionProof proof, String reason, int weight, long cooldownMs) {
        long now = System.currentTimeMillis();
        String cooldownKey = type.name() + "|" + subject.toLowerCase(Locale.ROOT);
        long last = this.signalCooldowns.getOrDefault(cooldownKey, 0L).longValue();
        if (now - last < cooldownMs) {
            return;
        }
        this.signalCooldowns.put(cooldownKey, Long.valueOf(now));
        this.signals.addLast(new Signal(type, subject, proof, reason, weight, now));
        upsertDetection("signal:" + cooldownKey, subject, reason, weight, now + DETECTION_TTL_MS);
        announceTrigger(cooldownKey, subject, proof != null, reason);
        evaluateCritical();
    }

    private void evaluateCritical() {
        long now = System.currentTimeMillis();
        pruneSignals(now);
        EnumMap<SignalType, Integer> strongest = new EnumMap<>(SignalType.class);
        Signal watcher = null;
        for (Signal signal : this.signals) {
            strongest.merge(signal.type, Integer.valueOf(signal.weight), (v0, v1) -> {
                return Math.max(v0, v1);
            });
            if (signal.proof != null) {
                watcher = signal;
            }
        }
        int score = strongest.values().stream().mapToInt((v0) -> {
            return v0.intValue();
        }).sum();
        if (watcher == null || strongest.size() < 2 || score < 35 || now - this.lastCriticalMs < CRITICAL_COOLDOWN_MS) {
            return;
        }
        watchingYou(watcher.subject, watcher.proof);
    }

    private void watchingYou(String name, RegionProof proof) {
        long now = System.currentTimeMillis();
        this.lastCriticalMs = now;
        this.criticalUntilMs = now + 7000;
        this.criticalWatcher = name;
        this.pendingChimes.add(proof.id());
        this.ghostId = proof.id();
        this.ghostName = name;
    }

    private void playPendingChime() {
        if (this.pendingChimes.isEmpty()) {
            return;
        }
        boolean sirenCovers = this.pendingChimes.stream().allMatch(StaffListModule::alarmedRecently);
        this.pendingChimes.clear();
        if (sirenCovers) {
            return;
        }
        if (MC.method_1483() != null) {
            MC.method_1483().method_4873(class_1109.method_4757(class_3417.field_15203, 1.0f, 1.0f));
        }
        StaffGhost.watching(this.ghostId, this.ghostName, 0);
    }

    private void news(String name, class_2561 what, String detail) {
        ModuleChat.Body line = ModuleChat.body().name(NameFilter.apply(name)).text(" ").append(what);
        if (!detail.isEmpty()) {
            line.text(" (" + detail + ")");
        }
        ModuleChat.send(this, line);
    }

    private void queueGamemodeUpdates(class_2703 info) {
        if (info.method_46327().contains(class_2703.class_5893.field_29136)) {
            return;
        }
        for (class_2703.class_2705 entry : info.method_46329()) {
            if (entry != null && entry.comp_1110() != null && entry.comp_1106() != null) {
                this.observations.offer(Observation.gamemode(entry.comp_1106(), entry.comp_1110().method_8381()));
            }
        }
    }

    private void announceGamemode(UUID uuid, String gameMode) {
        KnownPlayer known;
        if (uuid != null) {
            if ((MC.field_1724 == null || !MC.field_1724.method_5667().equals(uuid)) && (known = this.knownPlayers.get(uuid)) != null && StaffListModule.isStaffName(known.name)) {
                if (class_1934.field_9219.method_8381().equals(gameMode)) {
                    if (credibleSubject(uuid, known.name)) {
                        reportHidden(uuid, known.name, true, "Vanish Event: switched to spectator", "", 100);
                        return;
                    }
                } else if (endSpell(known.name, true, 0L)) {
                    return;
                }
                ModuleChat.send(this, StaffListModule.switchedTo(ModuleChat.body().name(NameFilter.apply(known.name)).text(" "), gameMode));
            }
        }
    }

    private void announceTrigger(String eventKey, String subject, boolean named, String reason) {
        long now = System.currentTimeMillis();
        Long last = this.announceCooldowns.get(eventKey);
        if (last == null || now - last.longValue() >= 1500) {
            while (!this.announceTimes.isEmpty() && now - this.announceTimes.peekFirst().longValue() > 4000) {
                this.announceTimes.removeFirst();
            }
            if (this.announceTimes.size() >= 6) {
                return;
            }
            this.announceCooldowns.put(eventKey, Long.valueOf(now));
            this.announceTimes.addLast(Long.valueOf(now));
            int colon = reason.indexOf(": ");
            ModuleChat.Body line = colon < 0 ? ModuleChat.body().warn(reason) : ModuleChat.body().warn(reason.substring(0, colon)).text(reason.substring(colon));
            boolean located = (subject == null || subject.isBlank() || "Unknown".equalsIgnoreCase(subject) || "CRITICAL".equalsIgnoreCase(subject)) ? false : true;
            if (located) {
                line.text(" (");
                if (named) {
                    line.name(NameFilter.apply(subject));
                } else {
                    line.value(subject);
                }
                line.text(")");
            }
            ModuleChat.send(this, line);
        }
    }

    private void upsertDetection(String key, String name, String reason, int score, long expiresAt) {
        Detection existing = this.detections.get(key);
        if (existing == null) {
            this.detections.put(key, new Detection(name, reason, score, expiresAt));
            return;
        }
        existing.name = name;
        existing.expiresAt = Math.max(existing.expiresAt, expiresAt);
        if (score >= existing.score) {
            existing.reason = reason;
            existing.score = score;
        }
    }

    private List<HudEntry> hudSnapshot() {
        long now = System.currentTimeMillis();
        List<HudEntry> out = new ArrayList<>();
        if (now < this.criticalUntilMs) {
            out.add(new HudEntry("CRITICAL", this.criticalWatcher));
        }
        Set<String> seenRows = new HashSet<>();
        Stream streamLimit = this.detections.values().stream().filter(detection -> {
            return detection.expiresAt > now;
        }).filter(AntiVanishModule::detectionWorthShowing).sorted(Comparator.comparingInt(detection2 -> {
            return detection2.score;
        }).reversed().thenComparing(detection3 -> {
            return detection3.name;
        }, String.CASE_INSENSITIVE_ORDER)).map(detection4 -> {
            return new HudEntry(detection4.name, detection4.reason);
        }).filter(entry -> {
            return seenRows.add(hudTag(entry) + "\u0000" + hudValue(entry));
        }).limit(4L);
        Objects.requireNonNull(out);
        streamLimit.forEach((v1) -> {
            r1.add(v1);
        });
        return List.copyOf(out);
    }

    private void pruneState() {
        long now = System.currentTimeMillis();
        this.detections.entrySet().removeIf(entry -> {
            return ((Detection) entry.getValue()).expiresAt <= now;
        });
        this.signalCooldowns.entrySet().removeIf(entry2 -> {
            return now - ((Long) entry2.getValue()).longValue() > 60000;
        });
        this.announceCooldowns.entrySet().removeIf(entry3 -> {
            return now - ((Long) entry3.getValue()).longValue() > 60000;
        });
        this.selfPlacedBlocks.entrySet().removeIf(entry4 -> {
            return ((Long) entry4.getValue()).longValue() <= now;
        });
        this.confirmedDepartures.entrySet().removeIf(entry5 -> {
            return ((Long) entry5.getValue()).longValue() <= now;
        });
        this.automatedMechanisms.entrySet().removeIf(entry6 -> {
            return ((Long) entry6.getValue()).longValue() <= now;
        });
        this.weakParticleBursts.values().removeIf(burst -> {
            while (!burst.isEmpty() && now - ((Long) burst.peekFirst()).longValue() > 2000) {
                burst.removeFirst();
            }
            return burst.isEmpty();
        });
        while (!this.recentExplosions.isEmpty() && now - this.recentExplosions.peekFirst().timeMs > 3000) {
            this.recentExplosions.removeFirst();
        }
        pruneSignals(now);
    }

    private void pruneSignals(long now) {
        while (!this.signals.isEmpty() && now - this.signals.peekFirst().timeMs > SIGNAL_WINDOW_MS) {
            this.signals.removeFirst();
        }
    }

    private boolean nearPlayer(class_243 source) {
        return source != null && source.method_1025(MC.field_1724.method_73189()) <= sensorRangeSq();
    }

    private boolean nearSelf(class_243 source, double maxDistSq) {
        return (source == null || MC.field_1724 == null || source.method_1025(MC.field_1724.method_73189()) >= maxDistSq) ? false : true;
    }

    private boolean selfContainerActive() {
        return System.currentTimeMillis() - this.lastContainerActivityMs < CONTAINER_SELF_GRACE_MS;
    }

    private static boolean isContainerSignal(String id) {
        String path = AntiVanishHeuristics.path(id);
        return path.contains("chest") || path.contains("barrel") || path.contains("shulker");
    }

    private String locatedSubject(class_243 source) {
        if (source == null || MC.field_1724 == null) {
            return "nearby";
        }
        class_243 me = MC.field_1724.method_73189();
        double dx = source.field_1352 - me.field_1352;
        double dy = source.field_1351 - me.field_1351;
        double dz = source.field_1350 - me.field_1350;
        long dist = Math.round(Math.sqrt((dx * dx) + (dy * dy) + (dz * dz)));
        String dir = compass(dx, dz);
        return dir.isEmpty() ? dist + "m" : dist + "m " + dist;
    }

    private static String compass(double dx, double dz) {
        String str;
        String str2;
        if (dz < -1.0d) {
            str = "N";
        } else {
            str = dz > 1.0d ? "S" : "";
        }
        String ns = str;
        if (dx > 1.0d) {
            str2 = "E";
        } else {
            str2 = dx < -1.0d ? "W" : "";
        }
        String ew = str2;
        return ns + ew;
    }

    private boolean hasVisibleCause(class_243 source) {
        if (source == null) {
            return true;
        }
        for (class_1657 class_1657Var : MC.field_1687.method_18112()) {
            if (class_1657Var != MC.field_1724) {
                if (class_1657Var instanceof class_1657) {
                    class_1657 player = class_1657Var;
                    if (!player.method_5767() && class_1657Var.method_73189().method_1025(source) <= 16.0d) {
                        return true;
                    }
                } else if ((class_1657Var instanceof class_1676) && class_1657Var.method_73189().method_1025(source) <= 16.0d) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExplosionRelated(class_243 source) {
        if (source == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        while (!this.recentExplosions.isEmpty() && now - this.recentExplosions.peekFirst().timeMs > 3000) {
            this.recentExplosions.removeFirst();
        }
        for (ExplosionEvent explosion : this.recentExplosions) {
            if (explosion.center.method_1025(source) <= explosion.radius * explosion.radius) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAmbientParticleSource(class_243 source, String particleId) {
        if (source == null || !shortId(particleId).contains("smoke")) {
            return false;
        }
        class_2338 center = class_2338.method_49638(source);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    class_2338 pos = center.method_10069(dx, dy, dz);
                    class_2960 id = class_7923.field_41175.method_10221(MC.field_1687.method_8320(pos).method_26204());
                    String path = shortId(id == null ? "" : id.toString());
                    if (path.contains("campfire") || path.contains("furnace") || path.contains("smoker") || path.contains("torch") || path.contains("fire") || path.contains("candle") || path.contains("respawn_anchor")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPoweredMechanism(class_243 source, String soundId) {
        String path = shortId(soundId);
        if (!path.contains("door") && !path.contains("trapdoor")) {
            return false;
        }
        class_2338 center = class_2338.method_49638(source);
        long now = System.currentTimeMillis();
        for (int dy = -1; dy <= 1; dy++) {
            class_2338 pos = center.method_10069(0, dy, 0);
            class_2680 state = MC.field_1687.method_8320(pos);
            long key = pos.method_10063();
            boolean powered = state.method_28498(class_2741.field_12484) && ((Boolean) state.method_11654(class_2741.field_12484)).booleanValue();
            if (powered || MC.field_1687.method_49803(pos)) {
                this.automatedMechanisms.put(Long.valueOf(key), Long.valueOf(now + 5000));
                return true;
            }
            if (this.automatedMechanisms.getOrDefault(Long.valueOf(key), 0L).longValue() > now) {
                return true;
            }
        }
        return false;
    }

    private double sensorRangeSq() {
        return 64.0d * 64.0d;
    }

    private static double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static String soundId(class_2960 id) {
        return id == null ? "" : id.toString();
    }

    private static String shortId(String id) {
        return AntiVanishHeuristics.path(id);
    }

    private void resetRuntime() {
        this.observations.clear();
        this.knownPlayers.clear();
        this.detections.clear();
        this.signalCooldowns.clear();
        this.announceCooldowns.clear();
        this.announceTimes.clear();
        this.selfPlacedBlocks.clear();
        this.confirmedDepartures.clear();
        this.automatedMechanisms.clear();
        this.weakParticleBursts.clear();
        this.signals.clear();
        this.recentExplosions.clear();
        this.pendingVanishes.clear();
        this.hiddenSpells.clear();
        this.shard.clear();
        this.tickCounter = 0;
        this.lastLocalActionMs = 0L;
        this.lastContainerActivityMs = 0L;
        this.lastCriticalMs = 0L;
        this.criticalUntilMs = 0L;
        this.criticalWatcher = "";
        this.pendingChimes.clear();
        this.ghostId = null;
        this.ghostName = null;
        this.recentMessages.clear();
        this.listedSinceMs.clear();
        this.serverSendsLeaveMessages = false;
        this.completionRequestIds.clear();
        this.pendingCompletionNames = null;
        this.nextCompletionId = 30000;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$HudEntry.class */
    @Environment(EnvType.CLIENT)
    public static final class HudEntry extends Record {
        private final String name;
        private final String reason;

        public HudEntry(String name, String reason) {
            this.name = name;
            this.reason = reason;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HudEntry.class), HudEntry.class, "name;reason", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->reason:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HudEntry.class), HudEntry.class, "name;reason", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->reason:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HudEntry.class, Object.class), HudEntry.class, "name;reason", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$HudEntry;->reason:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public String reason() {
            return this.reason;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$Observation.class */
    @Environment(EnvType.CLIENT)
    private static final class Observation extends Record {
        private final ObservationType type;
        private final UUID profileId;
        private final int entityId;
        private final double x;
        private final double y;
        private final double z;
        private final String detail;

        private Observation(ObservationType type, UUID profileId, int entityId, double x, double y, double z, String detail) {
            this.type = type;
            this.profileId = profileId;
            this.entityId = entityId;
            this.x = x;
            this.y = y;
            this.z = z;
            this.detail = detail;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Observation.class), Observation.class, "type;profileId;entityId;x;y;z;detail", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ObservationType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->profileId:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->entityId:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->detail:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Observation.class), Observation.class, "type;profileId;entityId;x;y;z;detail", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ObservationType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->profileId:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->entityId:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->detail:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Observation.class, Object.class), Observation.class, "type;profileId;entityId;x;y;z;detail", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ObservationType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->profileId:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->entityId:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Observation;->detail:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public ObservationType type() {
            return this.type;
        }

        public UUID profileId() {
            return this.profileId;
        }

        public int entityId() {
            return this.entityId;
        }

        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }

        public double z() {
            return this.z;
        }

        public String detail() {
            return this.detail;
        }

        static Observation tabRemove(UUID id) {
            return new Observation(ObservationType.TAB_REMOVE, id, -1, 0.0d, 0.0d, 0.0d, "");
        }

        static Observation tabHide(UUID id) {
            return new Observation(ObservationType.TAB_HIDE, id, -1, 0.0d, 0.0d, 0.0d, "");
        }

        static Observation gamemode(UUID id, String mode) {
            return new Observation(ObservationType.GAMEMODE, id, -1, 0.0d, 0.0d, 0.0d, mode);
        }

        static Observation playerLeft(String name) {
            return new Observation(ObservationType.PLAYER_LEFT, null, -1, 0.0d, 0.0d, 0.0d, name);
        }

        static Observation systemChat(String text) {
            return new Observation(ObservationType.SYSTEM_CHAT, null, -1, 0.0d, 0.0d, 0.0d, text);
        }

        static Observation entityMetadata(int id) {
            return new Observation(ObservationType.ENTITY_METADATA, null, id, 0.0d, 0.0d, 0.0d, "");
        }

        static Observation explosion(class_243 center, float radius) {
            return new Observation(ObservationType.EXPLOSION, null, -1, center.field_1352, center.field_1351, center.field_1350, Float.toString(radius));
        }

        static Observation entitySound(int id, String sound) {
            return new Observation(ObservationType.ENTITY_SOUND, null, id, 0.0d, 0.0d, 0.0d, sound);
        }

        static Observation position(ObservationType type, double x, double y, double z, String detail) {
            return new Observation(type, null, -1, x, y, z, detail);
        }

        class_243 position() {
            return new class_243(this.x, this.y, this.z);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer.class */
    @Environment(EnvType.CLIENT)
    private static final class KnownPlayer extends Record {
        private final UUID uuid;
        private final String name;

        private KnownPlayer(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, KnownPlayer.class), KnownPlayer.class, "uuid;name", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->name:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, KnownPlayer.class), KnownPlayer.class, "uuid;name", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->name:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, KnownPlayer.class, Object.class), KnownPlayer.class, "uuid;name", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$KnownPlayer;->name:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public UUID uuid() {
            return this.uuid;
        }

        public String name() {
            return this.name;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$Signal.class */
    @Environment(EnvType.CLIENT)
    private static final class Signal extends Record {
        private final SignalType type;
        private final String subject;
        private final RegionProof proof;
        private final String reason;
        private final int weight;
        private final long timeMs;

        private Signal(SignalType type, String subject, RegionProof proof, String reason, int weight, long timeMs) {
            this.type = type;
            this.subject = subject;
            this.proof = proof;
            this.reason = reason;
            this.weight = weight;
            this.timeMs = timeMs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Signal.class), Signal.class, "type;subject;proof;reason;weight;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$SignalType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->subject:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->proof:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->reason:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->weight:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->timeMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Signal.class), Signal.class, "type;subject;proof;reason;weight;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$SignalType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->subject:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->proof:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->reason:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->weight:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->timeMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Signal.class, Object.class), Signal.class, "type;subject;proof;reason;weight;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->type:Lnet/anubis/client/module/impl/misc/AntiVanishModule$SignalType;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->subject:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->proof:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->reason:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->weight:I", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$Signal;->timeMs:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public SignalType type() {
            return this.type;
        }

        public String subject() {
            return this.subject;
        }

        public RegionProof proof() {
            return this.proof;
        }

        public String reason() {
            return this.reason;
        }

        public int weight() {
            return this.weight;
        }

        public long timeMs() {
            return this.timeMs;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$RegionProof.class */
    @Environment(EnvType.CLIENT)
    private static final class RegionProof extends Record {
        private final UUID id;
        private final int distance;

        private RegionProof(UUID id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RegionProof.class), RegionProof.class, "id;distance", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->distance:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RegionProof.class), RegionProof.class, "id;distance", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->distance:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RegionProof.class, Object.class), RegionProof.class, "id;distance", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RegionProof;->distance:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public UUID id() {
            return this.id;
        }

        public int distance() {
            return this.distance;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent.class */
    @Environment(EnvType.CLIENT)
    private static final class ExplosionEvent extends Record {
        private final class_243 center;
        private final double radius;
        private final long timeMs;

        private ExplosionEvent(class_243 center, double radius, long timeMs) {
            this.center = center;
            this.radius = radius;
            this.timeMs = timeMs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ExplosionEvent.class), ExplosionEvent.class, "center;radius;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->radius:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->timeMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ExplosionEvent.class), ExplosionEvent.class, "center;radius;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->radius:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->timeMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ExplosionEvent.class, Object.class), ExplosionEvent.class, "center;radius;timeMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->radius:D", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$ExplosionEvent;->timeMs:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 center() {
            return this.center;
        }

        public double radius() {
            return this.radius;
        }

        public long timeMs() {
            return this.timeMs;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish.class */
    @Environment(EnvType.CLIENT)
    private static final class PendingVanish extends Record {
        private final UUID uuid;
        private final String name;
        private final int dueTick;

        private PendingVanish(UUID uuid, String name, int dueTick) {
            this.uuid = uuid;
            this.name = name;
            this.dueTick = dueTick;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PendingVanish.class), PendingVanish.class, "uuid;name;dueTick", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->dueTick:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PendingVanish.class), PendingVanish.class, "uuid;name;dueTick", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->dueTick:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PendingVanish.class, Object.class), PendingVanish.class, "uuid;name;dueTick", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->uuid:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$PendingVanish;->dueTick:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public UUID uuid() {
            return this.uuid;
        }

        public String name() {
            return this.name;
        }

        public int dueTick() {
            return this.dueTick;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage.class */
    @Environment(EnvType.CLIENT)
    private static final class RecentMessage extends Record {
        private final String text;
        private final long atMs;

        private RecentMessage(String text, long atMs) {
            this.text = text;
            this.atMs = atMs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RecentMessage.class), RecentMessage.class, "text;atMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->atMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RecentMessage.class), RecentMessage.class, "text;atMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->atMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RecentMessage.class, Object.class), RecentMessage.class, "text;atMs", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/AntiVanishModule$RecentMessage;->atMs:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String text() {
            return this.text;
        }

        public long atMs() {
            return this.atMs;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$HiddenSpell.class */
    @Environment(EnvType.CLIENT)
    private static final class HiddenSpell {
        final UUID uuid;
        final String name;
        final boolean spectator;
        final String detail;
        final RegionProof proof;
        final int sinceTick;
        final long sinceMs = System.currentTimeMillis();
        boolean told;
        boolean watched;

        HiddenSpell(UUID uuid, String name, boolean spectator, String detail, RegionProof proof, int sinceTick) {
            this.uuid = uuid;
            this.name = name;
            this.spectator = spectator;
            this.detail = detail;
            this.proof = proof;
            this.sinceTick = sinceTick;
        }

        class_2561 how() {
            return this.spectator ? StaffListModule.switchedTo(ModuleChat.body(), class_1934.field_9219.method_8381()).build() : ModuleChat.body().danger("vanished").build();
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiVanishModule$Detection.class */
    @Environment(EnvType.CLIENT)
    private static final class Detection {
        String name;
        String reason;
        int score;
        long expiresAt;

        Detection(String name, String reason, int score, long expiresAt) {
            this.name = name;
            this.reason = reason;
            this.score = score;
            this.expiresAt = expiresAt;
        }
    }
}
