package net.anubis.client.module.impl.misc;

import com.mojang.authlib.GameProfile;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.StaffGhost;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.util.NameFilter;
import net.anubis.client.util.staff.ShardTracker;
import net.anubis.client.util.staff.StaffAlarm;
import net.anubis.client.util.staff.StaffMatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_1934;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_268;
import net.minecraft.class_2703;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_742;
import net.minecraft.class_8042;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule.class */
@Environment(EnvType.CLIENT)
public final class StaffListModule extends ClientModule {
    private static final int SCAN_INTERVAL_TICKS = 10;
    private static final int SETTLE_TICKS = 60;
    private static final int VANISH_CONFIRM_SCANS = 2;
    private static final int GROUP_THRESHOLD = 3;
    private static final int ALERT_GATHER_TICKS = 5;
    private static final int SWITCH_ECHO_TICKS = 20;
    public static final long ALERT_HIGHLIGHT_MS = 4000;
    private static final long ALERT_COOLDOWN_MS = 120000;
    private static final long SERVER_MARK_MS = 600000;
    private static final int MAX_ROWS = 8;
    private static final float ALARM_SECONDS = 3.0f;
    private static final long SHARED_ALARM_MS = 5000;
    private static final long NEARBY_GRACE_MS = 10000;
    private static final long SIGHTING_GRACE_MS = 10000;
    private static final long WATCH_GRACE_MS = 10000;
    private final BooleanSetting regionAlert;
    private final Map<UUID, Staff> staff;
    private List<Staff> sorted;
    private boolean resort;
    private final Map<UUID, Classified> classified;
    private final Map<UUID, Sighting> sightings;
    private final Queue<GameModeSwitch> switches;
    private final Map<UUID, String> pendingAlerts;
    private final StaffAlarm alarm;
    private class_634 connection;
    private final ShardTracker shard;
    private int ticksToScan;
    private int settleTicks;
    private boolean primed;
    private int alertGather;
    private int tickCount;
    public static final List<String> DONUT_STAFF = List.of((Object[]) new String[]{"FluffyMaster07", "archivePedro", "Munkerlich", "Frenk_Btw", "Napooo_", "auzzitech", "CryptoDaveYT", "W1zox_", "ZEEF69", "showered", "CaptainMoose35", "BobIsFound", "NoahvdAa", "0GSummer", "LzouzMP5", "Pastagamer08", "u_vv", "Owen1212055", "Splaterd", "Fallerfly", "Bloodspulse"});
    private static final Set<String> STAFF_KEYS = staffKeys();
    private static final StaffMatcher RANKS = new StaffMatcher(StaffMatcher.MODE_ALL, StaffMatcher.DEFAULT_RANK_WORDS, StaffMatcher.DEFAULT_MARKERS, false, 5, String.join(",", DONUT_STAFF), "");
    private static final Pattern ACCOUNT_NAME = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$Presence.class */
    @Environment(EnvType.CLIENT)
    public enum Presence {
        ONLINE,
        SPECTATOR,
        VANISHED
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$HudRow.class */
    @Environment(EnvType.CLIENT)
    public static final class HudRow extends Record {
        private final String name;
        private final UUID id;
        private final Presence presence;
        private final boolean watching;
        private final long watchingForMs;
        private final boolean nearby;
        private final float distance;
        private final float alertLevel;
        private final boolean nameHidden;

        public HudRow(String name, UUID id, Presence presence, boolean watching, long watchingForMs, boolean nearby, float distance, float alertLevel, boolean nameHidden) {
            this.name = name;
            this.id = id;
            this.presence = presence;
            this.watching = watching;
            this.watchingForMs = watchingForMs;
            this.nearby = nearby;
            this.distance = distance;
            this.alertLevel = alertLevel;
            this.nameHidden = nameHidden;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HudRow.class), HudRow.class, "name;id;presence;watching;watchingForMs;nearby;distance;alertLevel;nameHidden", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->presence:Lnet/anubis/client/module/impl/misc/StaffListModule$Presence;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watching:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watchingForMs:J", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nearby:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->distance:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->alertLevel:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nameHidden:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HudRow.class), HudRow.class, "name;id;presence;watching;watchingForMs;nearby;distance;alertLevel;nameHidden", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->presence:Lnet/anubis/client/module/impl/misc/StaffListModule$Presence;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watching:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watchingForMs:J", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nearby:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->distance:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->alertLevel:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nameHidden:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HudRow.class, Object.class), HudRow.class, "name;id;presence;watching;watchingForMs;nearby;distance;alertLevel;nameHidden", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->presence:Lnet/anubis/client/module/impl/misc/StaffListModule$Presence;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watching:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->watchingForMs:J", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nearby:Z", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->distance:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->alertLevel:F", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudRow;->nameHidden:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public UUID id() {
            return this.id;
        }

        public Presence presence() {
            return this.presence;
        }

        public boolean watching() {
            return this.watching;
        }

        public long watchingForMs() {
            return this.watchingForMs;
        }

        public boolean nearby() {
            return this.nearby;
        }

        public float distance() {
            return this.distance;
        }

        public float alertLevel() {
            return this.alertLevel;
        }

        public boolean nameHidden() {
            return this.nameHidden;
        }

        public String watchingFor() {
            return this.watching ? StaffListModule.formatAge(this.watchingForMs) : "";
        }

        public String distanceLabel() {
            return this.nearby ? Math.round(this.distance) + "m" : "";
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$HudList.class */
    @Environment(EnvType.CLIENT)
    public static final class HudList extends Record {
        private final List<HudRow> rows;
        private final int more;
        static final HudList EMPTY = new HudList(List.of(), 0);

        public HudList(List<HudRow> rows, int more) {
            this.rows = rows;
            this.more = more;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HudList.class), HudList.class, "rows;more", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->more:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HudList.class), HudList.class, "rows;more", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->more:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HudList.class, Object.class), HudList.class, "rows;more", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$HudList;->more:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<HudRow> rows() {
            return this.rows;
        }

        public int more() {
            return this.more;
        }
    }

    public StaffListModule() {
        super("staff-list", "Staff List", "Lists online DonutSMP staff.", ModuleCategory.DONUT);
        this.regionAlert = (BooleanSetting) addSetting("server-alert", new BooleanSetting("Region Alert", true));
        this.staff = new LinkedHashMap();
        this.sorted = List.of();
        this.classified = new HashMap();
        this.sightings = new HashMap();
        this.switches = new ConcurrentLinkedQueue();
        this.pendingAlerts = new LinkedHashMap();
        this.alarm = new StaffAlarm();
        this.shard = new ShardTracker();
        usesNotifications();
        this.regionAlert.onChange(() -> {
            if (!this.regionAlert.get()) {
                forgetShard();
            }
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        forget();
        class_310 mc = class_310.method_1551();
        this.connection = mc.method_1562();
        this.shard.clear();
        this.settleTicks = 0;
        this.ticksToScan = 0;
        this.primed = false;
        on(TickEvent.TYPE, Stage.POST, this::onTick);
        on(PacketReceiveNettyEvent.TYPE, this::onPacketNetty);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        forget();
        this.connection = null;
        this.shard.clear();
    }

    private void forget() {
        this.staff.clear();
        this.sorted = List.of();
        this.classified.clear();
        forgetShard();
    }

    private void forgetShard() {
        this.switches.clear();
        this.sightings.clear();
        this.pendingAlerts.clear();
        this.alarm.stop();
        this.resort = true;
    }

    public static boolean shouldShowHud() {
        StaffListModule module = instance();
        return (module == null || !module.isEnabled() || module.sorted.isEmpty()) ? false : true;
    }

    public static HudList hudRows() {
        StaffListModule module = instance();
        if (module == null || !module.isEnabled()) {
            return HudList.EMPTY;
        }
        List<Staff> all = module.sorted;
        int shown = all.size() <= 8 ? all.size() : 7;
        long now = System.currentTimeMillis();
        List<HudRow> rows = new ArrayList<>(shown);
        for (int i = 0; i < shown; i++) {
            Staff s = all.get(i);
            Sighting seen = module.sightings.get(s.id);
            boolean watching = seen != null && seen.watching(now);
            boolean nearby = seen != null && seen.nearby;
            float alert = seen == null ? 0.0f : Math.max(0.0f, Math.min(1.0f, (seen.highlightUntil - now) / 4000.0f));
            String shownName = NameFilter.apply(s.name);
            rows.add(new HudRow(shownName, s.id, s.presence, watching, watching ? Math.max(0L, now - seen.watchingSince) : -1L, nearby, nearby ? seen.distance : -1.0f, alert, !shownName.equals(s.name)));
        }
        return new HudList(rows, all.size() - shown);
    }

    public static String formatAge(long ms) {
        long total = Math.max(0L, ms) / 1000;
        long hours = total / 3600;
        long minutes = (total / 60) % 60;
        long seconds = total % 60;
        StringBuilder out = new StringBuilder(8);
        if (hours > 0) {
            out.append(hours).append(minutes < 10 ? ":0" : ":");
        }
        return out.append(minutes).append(seconds < 10 ? ":0" : ":").append(seconds).toString();
    }

    public static boolean isStaffName(String name) {
        return name != null && STAFF_KEYS.contains(name.toLowerCase(Locale.ROOT));
    }

    public static boolean isInYourRegion(UUID id) {
        Sighting seen;
        StaffListModule module = instance();
        return (module == null || !module.isEnabled() || id == null || (seen = module.sightings.get(id)) == null || !seen.marked(System.currentTimeMillis())) ? false : true;
    }

    public static boolean alarmedRecently(UUID id) {
        Sighting seen;
        StaffListModule module = instance();
        return (module == null || !module.isEnabled() || id == null || (seen = module.sightings.get(id)) == null || seen.alertedAt == 0 || System.currentTimeMillis() - seen.alertedAt >= SHARED_ALARM_MS) ? false : true;
    }

    private static Set<String> staffKeys() {
        Set<String> keys = new HashSet<>();
        for (String name : DONUT_STAFF) {
            keys.add(name.toLowerCase(Locale.ROOT));
        }
        return Set.copyOf(keys);
    }

    private static StaffListModule instance() {
        return (StaffListModule) ModuleRegistry.get(StaffListModule.class);
    }

    private void onPacketNetty(PacketReceiveNettyEvent event) {
        if (this.regionAlert.get()) {
            class_2596<?> class_2596Var = event.packet;
            if (class_2596Var instanceof class_2703) {
                class_2703 info = (class_2703) class_2596Var;
                queueSwitches(info);
                return;
            }
            class_8042 class_8042Var = event.packet;
            if (class_8042Var instanceof class_8042) {
                class_8042 bundle = class_8042Var;
                for (class_2596<?> packet : bundle.method_48324()) {
                    if (packet instanceof class_2703) {
                        class_2703 info2 = (class_2703) packet;
                        queueSwitches(info2);
                    }
                }
            }
        }
    }

    private void queueSwitches(class_2703 info) {
        EnumSet<class_2703.class_5893> actions = info.method_46327();
        if (!actions.contains(class_2703.class_5893.field_29137) || actions.contains(class_2703.class_5893.field_29136)) {
            return;
        }
        for (class_2703.class_2705 entry : info.method_46329()) {
            if (entry != null && entry.comp_1106() != null) {
                this.switches.offer(new GameModeSwitch(entry.comp_1106(), entry.comp_1110()));
            }
        }
    }

    private void onTick(TickEvent event) {
        this.alarm.tick();
        class_310 mc = class_310.method_1551();
        class_634 conn = mc.method_1562();
        if (conn == null || mc.field_1724 == null || mc.field_1687 == null) {
            if (this.connection != null || !this.staff.isEmpty() || !this.classified.isEmpty() || !this.sightings.isEmpty()) {
                forget();
            }
            this.switches.clear();
            this.connection = null;
            this.shard.clear();
            this.primed = false;
            return;
        }
        boolean moved = this.shard.moved(mc);
        if (conn != this.connection) {
            this.connection = conn;
            forget();
            this.settleTicks = SETTLE_TICKS;
            this.primed = false;
        } else if (moved) {
            forgetShard();
        }
        if (this.settleTicks > 0) {
            this.settleTicks--;
        }
        this.tickCount++;
        long now = System.currentTimeMillis();
        if (this.regionAlert.get()) {
            drainSwitches(conn, mc.field_1724.method_5667(), now);
            trackBodies(mc, now);
        }
        int i = this.ticksToScan - 1;
        this.ticksToScan = i;
        if (i <= 0) {
            this.ticksToScan = 10;
            Changes changes = scan(mc, conn, now);
            if (this.primed) {
                announce(mc, changes);
            } else if (this.settleTicks == 0) {
                this.primed = true;
            }
        }
        if (!this.pendingAlerts.isEmpty()) {
            int i2 = this.alertGather - 1;
            this.alertGather = i2;
            if (i2 <= 0) {
                raise();
            }
        }
        if (this.resort) {
            sortView(now);
        }
    }

    private Changes scan(class_310 mc, class_634 conn, long now) {
        Presence presence;
        UUID self = mc.field_1724.method_5667();
        Set<UUID> listed = new HashSet<>();
        Iterator it = conn.method_45732().iterator();
        while (it.hasNext()) {
            listed.add(((class_640) it.next()).method_2966().id());
        }
        Changes changes = new Changes();
        Set<UUID> inTab = new HashSet<>();
        for (class_640 info : conn.method_2880()) {
            GameProfile profile = info.method_2966();
            UUID id = profile.id();
            String name = profile.name();
            if (id != null && !id.equals(self) && isStaffName(name) && isAccount(id, name)) {
                inTab.add(id);
                StaffMatcher.Match rank = rank(info, id, name);
                if (info.method_2958() == class_1934.field_9219) {
                    presence = Presence.SPECTATOR;
                } else {
                    presence = listed.contains(id) ? Presence.ONLINE : Presence.VANISHED;
                }
                Presence presence2 = presence;
                Staff tracked = this.staff.get(id);
                if (tracked == null) {
                    Staff tracked2 = new Staff(id);
                    this.staff.put(id, tracked2);
                    tracked2.update(name, rank, presence2);
                    changes.joined.add(tracked2);
                } else {
                    Presence before = tracked.presence;
                    tracked.update(name, rank, presence2);
                    tracked.offTabScans = 0;
                    if (before != presence2) {
                        changes.changed.add(tracked);
                    }
                }
            }
        }
        this.classified.keySet().retainAll(inTab);
        Iterator<Staff> it2 = this.staff.values().iterator();
        while (it2.hasNext()) {
            Staff tracked3 = it2.next();
            if (!inTab.contains(tracked3.id)) {
                if (mc.field_1687.method_18470(tracked3.id) != null) {
                    int i = tracked3.offTabScans + 1;
                    tracked3.offTabScans = i;
                    if (i >= 2 && tracked3.presence != Presence.VANISHED) {
                        tracked3.presence = Presence.VANISHED;
                        changes.changed.add(tracked3);
                    }
                } else {
                    it2.remove();
                    changes.left.add(tracked3);
                }
            }
        }
        pruneSightings(now);
        sortView(now);
        return changes;
    }

    private void sortView(long now) {
        List<Staff> view = new ArrayList<>(this.staff.values());
        view.sort(Comparator.comparingInt(s -> {
            return group(s, now);
        }).thenComparingInt(s2 -> {
            return s2.seniority;
        }).thenComparing(s3 -> {
            return s3.name;
        }, String.CASE_INSENSITIVE_ORDER));
        this.sorted = List.copyOf(view);
        this.resort = false;
    }

    private int group(Staff s, long now) {
        Sighting seen = this.sightings.get(s.id);
        if (seen != null && seen.nearby) {
            return 0;
        }
        if (seen == null || !seen.marked(now)) {
            return s.presence != Presence.ONLINE ? 2 : 3;
        }
        return 1;
    }

    private static boolean isAccount(UUID id, String name) {
        return (name == null || id.version() == 2 || !ACCOUNT_NAME.matcher(name).matches()) ? false : true;
    }

    private StaffMatcher.Match rank(class_640 info, UUID id, String name) {
        class_268 team = info.method_2955();
        class_2561 display = info.method_2971();
        class_2561 prefix = team == null ? null : team.method_1144();
        class_2561 suffix = team == null ? null : team.method_1136();
        Classified last = this.classified.get(id);
        if (last != null && last.isFor(name, display, team, prefix, suffix)) {
            return last.match();
        }
        StaffMatcher.Match match = RANKS.match(name, team == null ? null : team.method_1197(), plain(display), plain(prefix), plain(suffix));
        this.classified.put(id, new Classified(name, display, team, prefix, suffix, match));
        return match;
    }

    private static String plain(class_2561 component) {
        if (component == null) {
            return null;
        }
        return component.getString();
    }

    private void drainSwitches(class_634 conn, UUID self, long now) {
        String name;
        while (true) {
            GameModeSwitch change = this.switches.poll();
            if (change != null) {
                UUID id = change.id();
                if (!id.equals(self)) {
                    Staff tracked = this.staff.get(id);
                    if (tracked != null) {
                        name = tracked.name;
                    } else {
                        class_640 info = conn.method_2871(id);
                        name = info == null ? null : info.method_2966().name();
                        if (!isStaffName(name) || !isAccount(id, name)) {
                        }
                    }
                    Sighting seen = this.sightings.computeIfAbsent(id, key -> {
                        return new Sighting();
                    });
                    if (!seen.marked(now)) {
                        this.resort = true;
                    }
                    seen.prove(now);
                    if (!seen.coolingDown(now)) {
                        class_1934 mode = change.mode();
                        seen.switchAlertTick = this.tickCount;
                        seen.switchedToSpectator = mode == class_1934.field_9219;
                        alert(id, seen, now, name);
                    }
                }
            } else {
                return;
            }
        }
    }

    private void trackBodies(class_310 mc, long now) {
        Staff tracked;
        class_742 class_742Var = mc.field_1724;
        for (class_742 body : mc.field_1687.method_18456()) {
            if (body != class_742Var && (tracked = this.staff.get(body.method_5667())) != null) {
                Sighting seen = this.sightings.computeIfAbsent(tracked.id, key -> {
                    return new Sighting();
                });
                seen.prove(now);
                seen.bodyTick = this.tickCount;
                seen.distance = class_742Var.method_5739(body);
                if (!seen.nearby) {
                    seen.nearby = true;
                    this.resort = true;
                    boolean newAppearance = seen.bodyLostAt == 0 || now - seen.bodyLostAt > 10000;
                    if (newAppearance) {
                        alert(tracked.id, seen, now, tracked.name);
                    }
                }
            }
        }
        for (Sighting seen2 : this.sightings.values()) {
            if (seen2.nearby && seen2.bodyTick != this.tickCount) {
                seen2.nearby = false;
                seen2.bodyLostAt = now;
                this.resort = true;
            }
        }
    }

    private void pruneSightings(long now) {
        Iterator<Map.Entry<UUID, Sighting>> it = this.sightings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Sighting> entry = it.next();
            Sighting seen = entry.getValue();
            if (this.staff.containsKey(entry.getKey())) {
                seen.untrackedSince = 0L;
            } else if (seen.untrackedSince == 0) {
                seen.untrackedSince = now;
            } else if (now - seen.untrackedSince > 10000) {
                it.remove();
                this.resort = true;
            }
        }
    }

    private void alert(UUID id, Sighting seen, long now, String name) {
        seen.alertedAt = now;
        seen.highlightUntil = now + ALERT_HIGHLIGHT_MS;
        if (this.pendingAlerts.isEmpty()) {
            this.alertGather = 5;
        }
        this.pendingAlerts.put(id, name);
    }

    private void raise() {
        Map.Entry<UUID, String> first = this.pendingAlerts.entrySet().iterator().next();
        UUID id = first.getKey();
        String name = first.getValue();
        int others = this.pendingAlerts.size() - 1;
        this.pendingAlerts.clear();
        this.alarm.start(StaffAlarm.GUARDIAN, ALARM_SECONDS, 1.0f);
        StaffGhost.watching(id, name, others);
    }

    private void announce(class_310 mc, Changes changes) {
        changes.changed.removeIf(this::echoesSwitchAlert);
        if (changes.isEmpty()) {
            return;
        }
        List<ModuleChat.Body> lines = new ArrayList<>();
        describe(lines, changes.joined, ModuleChat.body().warn("joined").build(), (line, s) -> {
            line.warn("joined");
            String note = note(s);
            if (note != null) {
                line.text(" ").danger(note);
            }
        });
        describe(lines, changes.changed, ModuleChat.body().warn("changed visibility").build(), (line2, s2) -> {
            switch (s2.presence) {
                case ONLINE:
                    line2.text("is ").good("visible again");
                    break;
                case SPECTATOR:
                    switchedTo(line2, class_1934.field_9219.method_8381());
                    break;
                case VANISHED:
                    line2.danger("vanished").text(s2.offTabScans > 0 ? " (off TAB, still nearby)" : " (hidden from TAB)");
                    break;
            }
        });
        describe(lines, changes.left, ModuleChat.body().good("left").build(), (line3, s3) -> {
            if (s3.presence != Presence.VANISHED) {
                line3.good("left");
            } else {
                line3.text("is ").warn("out of sight");
            }
        });
        for (ModuleChat.Body line4 : lines) {
            ModuleChat.send(this, line4);
        }
        boolean urgent = !changes.joined.isEmpty() || changes.changed.stream().anyMatch(s4 -> {
            return s4.presence != Presence.ONLINE;
        });
        if (urgent && mc.method_1483() != null) {
            mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_14725, 1.25f));
        }
    }

    private boolean echoesSwitchAlert(Staff s) {
        Sighting seen = this.sightings.get(s.id);
        if (seen == null || seen.switchAlertTick < 0 || this.tickCount - seen.switchAlertTick > 20) {
            return false;
        }
        if (s.presence == Presence.SPECTATOR) {
            return seen.switchedToSpectator;
        }
        return s.presence == Presence.ONLINE && !seen.switchedToSpectator;
    }

    private static void describe(List<ModuleChat.Body> lines, List<Staff> group, class_2561 groupVerb, BiConsumer<ModuleChat.Body, Staff> verb) {
        if (group.isEmpty()) {
            return;
        }
        if (group.size() > 3) {
            List<class_2561> labels = new ArrayList<>(group.size());
            Iterator<Staff> it = group.iterator();
            while (it.hasNext()) {
                labels.add(label(it.next()));
            }
            lines.add(list(ModuleChat.body().value(Integer.valueOf(group.size())).text(" staff ").append(groupVerb).text(": "), labels));
            return;
        }
        for (Staff s : group) {
            ModuleChat.Body line = ModuleChat.body().append(label(s)).text(" ");
            verb.accept(line, s);
            lines.add(line);
        }
    }

    private static class_2561 label(Staff s) {
        return label(s.name, s.rank);
    }

    private static class_2561 label(String name, String rank) {
        return ModuleChat.body().name(NameFilter.apply(name)).text(" (" + rank + ")").build();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static String note(Staff s) throws MatchException {
        switch (s.presence) {
            case ONLINE:
                return null;
            case SPECTATOR:
                return "spectating";
            case VANISHED:
                return "vanished";
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    private static ModuleChat.Body list(ModuleChat.Body body, List<class_2561> parts) {
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
                body.text(", ");
            }
            body.append(parts.get(i));
        }
        return body;
    }

    static ModuleChat.Body switchedTo(ModuleChat.Body body, String mode) {
        if (mode == null) {
            return body.text("switched game mode");
        }
        body.text("switched to ");
        if (class_1934.field_9219.method_8381().equals(mode)) {
            return body.danger(mode);
        }
        return class_1934.field_9220.method_8381().equals(mode) ? body.warn(mode) : body.name(mode);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$Staff.class */
    @Environment(EnvType.CLIENT)
    private static final class Staff {
        final UUID id;
        int seniority;
        int offTabScans;
        String name = "";
        String rank = "";
        Presence presence = Presence.ONLINE;

        Staff(UUID id) {
            this.id = id;
        }

        void update(String name, StaffMatcher.Match rank, Presence presence) {
            this.name = name;
            this.rank = rank.rank();
            this.seniority = rank.seniority();
            this.presence = presence;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$Sighting.class */
    @Environment(EnvType.CLIENT)
    private static final class Sighting {
        long provenAt;
        long watchingSince;
        long alertedAt;
        long highlightUntil;
        boolean nearby;
        float distance;
        long bodyLostAt;
        int bodyTick;
        int switchAlertTick = -1;
        boolean switchedToSpectator;
        long untrackedSince;

        private Sighting() {
        }

        boolean marked(long now) {
            return this.nearby || (this.provenAt != 0 && now - this.provenAt < StaffListModule.SERVER_MARK_MS);
        }

        void prove(long now) {
            if (!watching(now)) {
                this.watchingSince = now;
            }
            this.provenAt = now;
        }

        boolean watching(long now) {
            return this.nearby || (this.provenAt != 0 && now - this.provenAt < 10000);
        }

        boolean coolingDown(long now) {
            return this.alertedAt != 0 && now - this.alertedAt < StaffListModule.ALERT_COOLDOWN_MS;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch.class */
    @Environment(EnvType.CLIENT)
    private static final class GameModeSwitch extends Record {
        private final UUID id;
        private final class_1934 mode;

        private GameModeSwitch(UUID id, class_1934 mode) {
            this.id = id;
            this.mode = mode;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, GameModeSwitch.class), GameModeSwitch.class, "id;mode", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->mode:Lnet/minecraft/class_1934;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, GameModeSwitch.class), GameModeSwitch.class, "id;mode", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->mode:Lnet/minecraft/class_1934;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, GameModeSwitch.class, Object.class), GameModeSwitch.class, "id;mode", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->id:Ljava/util/UUID;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$GameModeSwitch;->mode:Lnet/minecraft/class_1934;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public UUID id() {
            return this.id;
        }

        public class_1934 mode() {
            return this.mode;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$Classified.class */
    @Environment(EnvType.CLIENT)
    private static final class Classified extends Record {
        private final String name;
        private final class_2561 display;
        private final class_268 team;
        private final class_2561 prefix;
        private final class_2561 suffix;
        private final StaffMatcher.Match match;

        private Classified(String name, class_2561 display, class_268 team, class_2561 prefix, class_2561 suffix, StaffMatcher.Match match) {
            this.name = name;
            this.display = display;
            this.team = team;
            this.prefix = prefix;
            this.suffix = suffix;
            this.match = match;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Classified.class), Classified.class, "name;display;team;prefix;suffix;match", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->display:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->team:Lnet/minecraft/class_268;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->prefix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->suffix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->match:Lnet/anubis/client/util/staff/StaffMatcher$Match;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Classified.class), Classified.class, "name;display;team;prefix;suffix;match", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->display:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->team:Lnet/minecraft/class_268;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->prefix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->suffix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->match:Lnet/anubis/client/util/staff/StaffMatcher$Match;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Classified.class, Object.class), Classified.class, "name;display;team;prefix;suffix;match", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->display:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->team:Lnet/minecraft/class_268;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->prefix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->suffix:Lnet/minecraft/class_2561;", "FIELD:Lnet/anubis/client/module/impl/misc/StaffListModule$Classified;->match:Lnet/anubis/client/util/staff/StaffMatcher$Match;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public class_2561 display() {
            return this.display;
        }

        public class_268 team() {
            return this.team;
        }

        public class_2561 prefix() {
            return this.prefix;
        }

        public class_2561 suffix() {
            return this.suffix;
        }

        public StaffMatcher.Match match() {
            return this.match;
        }

        boolean isFor(String name, class_2561 display, class_268 team, class_2561 prefix, class_2561 suffix) {
            return this.display == display && this.team == team && this.prefix == prefix && this.suffix == suffix && this.name.equals(name);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/StaffListModule$Changes.class */
    @Environment(EnvType.CLIENT)
    private static final class Changes {
        final List<Staff> joined = new ArrayList();
        final List<Staff> changed = new ArrayList();
        final List<Staff> left = new ArrayList();

        private Changes() {
        }

        boolean isEmpty() {
            return this.joined.isEmpty() && this.changed.isEmpty() && this.left.isEmpty();
        }
    }
}
