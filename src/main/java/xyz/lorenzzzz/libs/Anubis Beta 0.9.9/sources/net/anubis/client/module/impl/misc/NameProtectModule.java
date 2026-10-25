package net.anubis.client.module.impl.misc;

import com.mojang.authlib.GameProfile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.nameprotect.AliasPolicy;
import net.anubis.client.module.impl.misc.nameprotect.NameMatcher;
import net.anubis.client.module.impl.misc.nameprotect.ProtectedText;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.StringSetting;
import net.anubis.client.util.NameFilter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1068;
import net.minecraft.class_2561;
import net.minecraft.class_2703;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_5348;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NameProtectModule.class */
@Environment(EnvType.CLIENT)
public final class NameProtectModule extends ClientModule {
    private static final String OTHERS_KEEP = "Keep";
    private static final String OTHERS_SAME = "Same Label";
    private static final String OTHERS_NUMBERED = "Numbered";
    private static final String DEFAULT_SELF_ALIAS = "You";
    private static final int DEPARTED_MEMORY = 256;
    private static final int SEEN_MEMORY = 8192;
    private static final int MIN_OTHER_NAME_LENGTH = 3;
    private static final int NPC_UUID_VERSION = 2;
    private static final int CHAT_REFRESH_INTERVAL_TICKS = 20;
    private static final int REFUSAL_NOTICE_DELAY_TICKS = 30;
    private static final int RECENT_CHAT_LINES = 100;
    private static final int DISPLAY_CACHE_SLOTS = 64;
    private static volatile NameMatcher active;
    private static volatile NameProtectModule running;
    private static volatile int generation;
    private static volatile boolean failureLogged;
    private static NameMatcher cacheMatcher;
    private static int recentChatNext;
    private final StringSetting selfAlias;
    private final StringSetting extraNames;
    private final ModeSetting others;
    private final ModeSetting othersLabel;
    private final LinkedHashMap<String, String> knownOthers;
    private final Map<String, Integer> numbers;
    private int nextNumber;
    private final LinkedHashSet<String> seenSkeletons;
    private WeakReference<class_634> trackedConnection;
    private volatile boolean dirty;
    private volatile boolean settingsEdited;
    private int builtListSize;
    private String builtProfileName;
    private NameMatcher matcher;
    private String refusedAlias;
    private AliasPolicy.Refusal refusalReason;
    private String noticedAlias;
    private int refusalNoticeDelay;
    private boolean chatRefreshPending;
    private int chatRefreshCooldown;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Object[] cacheInputs = new Object[64];
    private static final Object[] cacheOutputs = new Object[64];
    private static final String[] recentChat = new String[100];

    public NameProtectModule() {
        super("name-protect", "Name Protect", "Shows aliases instead of player names on your screen.", ModuleCategory.MISC);
        this.selfAlias = (StringSetting) addSetting("self-alias", new StringSetting("Your Alias", DEFAULT_SELF_ALIAS));
        this.extraNames = (StringSetting) addSetting("extra-names", new StringSetting("Extra Names", ""));
        this.others = (ModeSetting) addSetting("others", new ModeSetting("Others", OTHERS_KEEP, OTHERS_KEEP, OTHERS_SAME, OTHERS_NUMBERED));
        this.othersLabel = (ModeSetting) addSetting("others-label", new ModeSetting("Others Label", "Player", "Player", "Anonymous", "Unknown"));
        this.knownOthers = new LinkedHashMap<>();
        this.numbers = new HashMap();
        this.nextNumber = 1;
        this.seenSkeletons = new LinkedHashSet<>();
        this.trackedConnection = new WeakReference<>(null);
        this.dirty = true;
        this.builtListSize = -1;
        this.matcher = NameMatcher.EMPTY;
        usesNotifications();
        this.othersLabel.visibleWhen(() -> {
            return !this.others.is(OTHERS_KEEP);
        });
        this.extraNames.privateValue();
        this.selfAlias.onChange(this::markEdited);
        this.extraNames.onChange(this::markEdited);
        this.others.onChange(this::markEdited);
        this.othersLabel.onChange(this::markEdited);
    }

    public static class_2561 protect(class_2561 text) {
        NameMatcher current = active;
        if (current == null || text == null) {
            return text;
        }
        boolean cache = onRenderThread();
        if (cache) {
            Object objCached = cached(current, text);
            if (objCached instanceof class_2561) {
                class_2561 hit = (class_2561) objCached;
                return hit;
            }
        }
        try {
            class_2561 shown = ProtectedText.rewrite(current, text);
            if (cache) {
                remember(text, shown);
            }
            return shown;
        } catch (RuntimeException failure) {
            reportFailure(failure);
            return text;
        }
    }

    public static boolean hidesName(String name) {
        NameMatcher current = active;
        return (current == null || name == null || current.aliasAt(name, 0, name.length()) == null) ? false : true;
    }

    public static class_8685 hiddenHead(UUID profileId, class_8685 real) {
        class_8685 worn;
        SkinChangeModule skins = (SkinChangeModule) ModuleRegistry.get(SkinChangeModule.class);
        return (skins == null || !skins.isEnabled() || (worn = skins.skinFor(profileId, real)) == real) ? class_1068.method_4648(profileId) : worn;
    }

    public static class_5348 protectText(class_5348 text) {
        NameMatcher current = active;
        if (current == null || text == null) {
            return text;
        }
        boolean cache = onRenderThread();
        if (cache) {
            Object objCached = cached(current, text);
            if (objCached instanceof class_5348) {
                class_5348 hit = (class_5348) objCached;
                return hit;
            }
        }
        try {
            class_5348 shown = ProtectedText.rewrite(current, text);
            if (cache) {
                remember(text, shown);
            }
            return shown;
        } catch (RuntimeException failure) {
            reportFailure(failure);
            return text;
        }
    }

    public static class_5348 protectChatLine(class_5348 text) {
        NameMatcher current = active;
        if (current == null || text == null) {
            return text;
        }
        try {
            if (onRenderThread()) {
                recentChat[recentChatNext] = text.getString();
                recentChatNext = (recentChatNext + 1) % 100;
            }
            return ProtectedText.rewrite(current, text);
        } catch (RuntimeException failure) {
            reportFailure(failure);
            return text;
        }
    }

    public static int generation() {
        return generation;
    }

    public static void onPlayerInfoApplied(class_2703 packet) {
        NameProtectModule module = running;
        if (module == null || packet == null) {
            return;
        }
        EnumSet<class_2703.class_5893> actions = packet.method_46327();
        if (actions.contains(class_2703.class_5893.field_29136) || actions.contains(class_2703.class_5893.field_40700)) {
            class_310 mc = class_310.method_1551();
            if (mc.method_18854()) {
                module.syncConnection(mc);
                module.rebuild();
            }
        }
    }

    private static String filterName(String name) {
        NameMatcher current = active;
        return current != null ? current.replace(name) : name;
    }

    private static void reportFailure(RuntimeException failure) {
        if (failureLogged) {
            return;
        }
        failureLogged = true;
        LOGGER.error("Name Protect could not rewrite a text; showing it unchanged", failure);
    }

    private static boolean onRenderThread() {
        class_310 mc = class_310.method_1551();
        return mc != null && mc.method_18854();
    }

    private static Object cached(NameMatcher current, Object input) {
        if (cacheMatcher != current) {
            clearDisplayCache();
            cacheMatcher = current;
            return null;
        }
        int slot = System.identityHashCode(input) & 63;
        if (cacheInputs[slot] == input) {
            return cacheOutputs[slot];
        }
        return null;
    }

    private static void remember(Object input, Object output) {
        int slot = System.identityHashCode(input) & 63;
        cacheInputs[slot] = input;
        cacheOutputs[slot] = output;
    }

    private static void clearDisplayCache() {
        Arrays.fill(cacheInputs, (Object) null);
        Arrays.fill(cacheOutputs, (Object) null);
        cacheMatcher = null;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        running = this;
        class_310 mc = class_310.method_1551();
        syncConnection(mc);
        this.settingsEdited = false;
        rebuild();
        NameFilter.set(NameProtectModule::filterName);
        this.chatRefreshPending = true;
        this.chatRefreshCooldown = 0;
        on(TickEvent.TYPE, Stage.PRE, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        running = null;
        active = null;
        generation++;
        this.matcher = NameMatcher.EMPTY;
        NameFilter.set(null);
        this.chatRefreshPending = false;
        this.dirty = true;
        this.refusedAlias = null;
        this.noticedAlias = null;
        class_310 mc = class_310.method_1551();
        if (mc == null) {
            return;
        }
        mc.execute(() -> {
            clearDisplayCache();
            Arrays.fill(recentChat, (Object) null);
            if (mc.field_1705 != null) {
                mc.field_1705.method_1743().method_1817();
            }
        });
    }

    private void markEdited() {
        this.settingsEdited = true;
        this.dirty = true;
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        syncConnection(mc);
        if (listSize(mc.method_1562()) != this.builtListSize) {
            this.dirty = true;
        }
        if (!Objects.equals(profileName(mc), this.builtProfileName)) {
            this.dirty = true;
        }
        if (this.dirty) {
            rebuild();
        }
        announceRefusal(mc);
        if (this.chatRefreshCooldown > 0) {
            this.chatRefreshCooldown--;
        }
        if (this.chatRefreshPending && this.chatRefreshCooldown == 0 && !chatOpen(mc) && mc.field_1705 != null) {
            this.chatRefreshPending = false;
            this.chatRefreshCooldown = 20;
            mc.field_1705.method_1743().method_1817();
        }
    }

    private void syncConnection(class_310 mc) {
        class_634 connection = mc != null ? mc.method_1562() : null;
        if (connection == this.trackedConnection.get()) {
            return;
        }
        this.trackedConnection = new WeakReference<>(connection);
        this.knownOthers.clear();
        this.numbers.clear();
        this.nextNumber = 1;
        this.builtListSize = -1;
        this.dirty = true;
    }

    private void rebuild() {
        this.dirty = false;
        boolean edited = this.settingsEdited;
        this.settingsEdited = false;
        class_310 mc = class_310.method_1551();
        class_634 connection = mc != null ? mc.method_1562() : null;
        class_320 user = mc != null ? mc.method_1548() : null;
        String accountName = NameMatcher.nameCore(user != null ? user.method_1676() : null);
        this.builtProfileName = profileName(mc);
        String profile = NameMatcher.nameCore(this.builtProfileName);
        this.builtListSize = listSize(connection);
        ArrayList arrayList = new ArrayList();
        for (String raw : this.extraNames.get().split("[,;\\s]+")) {
            String extra = NameMatcher.nameCore(raw);
            if (extra != null) {
                arrayList.add(extra);
            }
        }
        List<String> ownNames = new ArrayList<>(arrayList.size() + 2);
        if (accountName != null) {
            ownNames.add(accountName);
        }
        if (profile != null) {
            ownNames.add(profile);
        }
        ownNames.addAll(arrayList);
        rememberOnline(connection, accountName, profile);
        String ownAlias = resolveOwnAlias(arrayList);
        Map<String, String> table = new LinkedHashMap<>();
        for (String own : ownNames) {
            put(table, own, ownAlias);
        }
        if (!this.others.is(OTHERS_KEEP)) {
            trackOthers(connection, ownNames);
            String label = this.othersLabel.get();
            boolean numbered = this.others.is(OTHERS_NUMBERED);
            for (Map.Entry<String, String> known : this.knownOthers.entrySet()) {
                put(table, known.getValue(), numbered ? label + " " + String.valueOf(this.numbers.get(known.getKey())) : label);
            }
        }
        publish(new NameMatcher(table), edited);
    }

    private void publish(NameMatcher next, boolean edited) {
        NameMatcher previous = this.matcher;
        this.matcher = next;
        active = next;
        if (next.sameMapping(previous)) {
            return;
        }
        generation++;
        if (edited || recentChatMentions(next.newNamesSince(previous))) {
            this.chatRefreshPending = true;
        }
    }

    private static boolean recentChatMentions(NameMatcher fresh) {
        if (fresh.isEmpty()) {
            return false;
        }
        for (String line : recentChat) {
            if (fresh.mentionsAny(line)) {
                return true;
            }
        }
        return false;
    }

    private String resolveOwnAlias(List<String> extras) {
        String alias = AliasPolicy.sanitize(this.selfAlias.get());
        if (alias.isEmpty() || alias.equalsIgnoreCase(DEFAULT_SELF_ALIAS)) {
            this.refusedAlias = null;
            return alias.isEmpty() ? DEFAULT_SELF_ALIAS : alias;
        }
        Set<String> extraSkeletons = new HashSet<>();
        for (String extra : extras) {
            extraSkeletons.add(AliasPolicy.skeleton(extra));
        }
        AliasPolicy.Refusal refusal = AliasPolicy.check(alias, skeleton -> {
            return this.seenSkeletons.contains(skeleton) || extraSkeletons.contains(skeleton);
        });
        if (refusal == null) {
            this.refusedAlias = null;
            return alias;
        }
        if (!alias.equals(this.refusedAlias)) {
            this.refusedAlias = alias;
            this.refusalNoticeDelay = 30;
        }
        this.refusalReason = refusal;
        return DEFAULT_SELF_ALIAS;
    }

    private void announceRefusal(class_310 mc) {
        if (this.refusedAlias == null) {
            this.noticedAlias = null;
            return;
        }
        if (this.refusedAlias.equals(this.noticedAlias)) {
            return;
        }
        if (this.refusalNoticeDelay > 0) {
            this.refusalNoticeDelay--;
        } else {
            if (mc.field_1724 == null) {
                return;
            }
            this.noticedAlias = this.refusedAlias;
            ModuleChat.send(this, ModuleChat.body().text("Alias ").name(this.refusedAlias).text(" ").danger("not used").text(": " + this.refusalReason.reason() + ", showing ").name(DEFAULT_SELF_ALIAS).text(" instead"));
        }
    }

    private void rememberOnline(class_634 connection, String accountName, String profile) {
        if (connection == null) {
            return;
        }
        for (class_640 info : connection.method_2880()) {
            GameProfile gameProfile = info.method_2966();
            String name = NameMatcher.nameCore(gameProfile != null ? gameProfile.name() : null);
            if (name != null && !name.equalsIgnoreCase(accountName) && !name.equalsIgnoreCase(profile)) {
                String skeleton = AliasPolicy.skeleton(name);
                this.seenSkeletons.remove(skeleton);
                this.seenSkeletons.add(skeleton);
            }
        }
        Iterator<String> oldest = this.seenSkeletons.iterator();
        while (this.seenSkeletons.size() > 8192 && oldest.hasNext()) {
            oldest.next();
            oldest.remove();
        }
    }

    private void trackOthers(class_634 connection, List<String> own) {
        if (connection == null) {
            return;
        }
        int online = 0;
        for (class_640 info : connection.method_45732()) {
            GameProfile profile = info.method_2966();
            if (profile != null && (profile.id() == null || profile.id().version() != 2)) {
                String name = NameMatcher.nameCore(profile.name());
                if (isLikelyPlayer(name) && !isOwn(name, own)) {
                    String key = name.toLowerCase(Locale.ROOT);
                    this.knownOthers.remove(key);
                    this.knownOthers.put(key, name);
                    this.numbers.computeIfAbsent(key, k -> {
                        int i = this.nextNumber;
                        this.nextNumber = i + 1;
                        return Integer.valueOf(i);
                    });
                    online++;
                }
            }
        }
        Iterator<String> oldest = this.knownOthers.keySet().iterator();
        while (this.knownOthers.size() > online + 256 && oldest.hasNext()) {
            this.numbers.remove(oldest.next());
            oldest.remove();
        }
    }

    private static void put(Map<String, String> table, String rawName, String alias) {
        String name = NameMatcher.nameCore(rawName);
        if (name == null || alias.equalsIgnoreCase(name)) {
            return;
        }
        table.putIfAbsent(name, alias);
    }

    private static boolean isLikelyPlayer(String name) {
        if (name == null || name.length() < 3) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOwn(String name, List<String> own) {
        for (String ownName : own) {
            if (ownName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private static String profileName(class_310 mc) {
        if (mc == null || mc.field_1724 == null) {
            return null;
        }
        return mc.field_1724.method_7334().name();
    }

    private static int listSize(class_634 connection) {
        if (connection != null) {
            return connection.method_45732().size();
        }
        return -1;
    }

    private static boolean chatOpen(class_310 mc) {
        return mc.field_1705 != null && mc.field_1705.method_1743().method_1819();
    }
}
