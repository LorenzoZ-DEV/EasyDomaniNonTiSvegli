package net.anubis.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.gui.HudLayoutStore;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles.class */
@Environment(EnvType.CLIENT)
public final class ConfigProfiles {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final ConfigProfiles INSTANCE = new ConfigProfiles();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern VALID_NAME = Pattern.compile("[\\p{L}\\p{Nd} _()-]{1,32}");
    private static final Pattern NUMBERED = Pattern.compile("(.+) \\(\\d{1,3}\\)");
    private static final Set<String> RESERVED = Set.of((Object[]) new String[]{"con", "prn", "aux", "nul", "com1", "com2", "com3", "com4", "com5", "com6", "com7", "com8", "com9", "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9"});
    private static final String EXTENSION = ".json";
    private static final String FORMAT_KEY = "anubisProfile";
    private static final int FORMAT = 2;
    private static final int OLDEST_FORMAT = 2;
    public static final String TOO_OLD = "Made by an old Anubis - old configs no longer work";
    private static final String UPGRADED_MARKER = ".format2";
    private static final int MAX_NAME = 32;
    private static final int MAX_FRIENDS = 1000;
    private static final long MAX_LOCAL_BYTES = 33554432;
    public static final int MAX_SERVERS = 16;
    public static final String NAME_RULE = "Use 1-32 letters, digits, spaces, _ - ( )";
    public static final String BACKUP_NAME = "Auto backup";
    private final ModuleConfigStore store;
    private final Map<String, Header> headers;
    private List<Profile> cached;
    private boolean upgradeChecked;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles$Part.class */
    @Environment(EnvType.CLIENT)
    public enum Part {
        MODULES("modules", "Modules"),
        HUD("hud", "HUD"),
        THEME("theme", GlyphTint.THEME),
        FRIENDS("friends", "Friends");

        public final String key;
        public final String label;

        Part(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles$Profile.class */
    @Environment(EnvType.CLIENT)
    public static final class Profile extends Record {
        private final String name;
        private final Set<Part> parts;
        private final List<String> servers;
        private final boolean broken;

        public Profile(String name, Set<Part> parts, List<String> servers, boolean broken) {
            this.name = name;
            this.parts = parts;
            this.servers = servers;
            this.broken = broken;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Profile.class), Profile.class, "name;parts;servers;broken", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->parts:Ljava/util/Set;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->servers:Ljava/util/List;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->broken:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Profile.class), Profile.class, "name;parts;servers;broken", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->parts:Ljava/util/Set;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->servers:Ljava/util/List;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->broken:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Profile.class, Object.class), Profile.class, "name;parts;servers;broken", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->parts:Ljava/util/Set;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->servers:Ljava/util/List;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Profile;->broken:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public Set<Part> parts() {
            return this.parts;
        }

        public List<String> servers() {
            return this.servers;
        }

        public boolean broken() {
            return this.broken;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles$Header.class */
    @Environment(EnvType.CLIENT)
    private static final class Header extends Record {
        private final FileTime modified;
        private final long size;
        private final Profile profile;

        private Header(FileTime modified, long size, Profile profile) {
            this.modified = modified;
            this.size = size;
            this.profile = profile;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Header.class), Header.class, "modified;size;profile", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->modified:Ljava/nio/file/attribute/FileTime;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->size:J", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->profile:Lnet/anubis/client/config/ConfigProfiles$Profile;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Header.class), Header.class, "modified;size;profile", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->modified:Ljava/nio/file/attribute/FileTime;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->size:J", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->profile:Lnet/anubis/client/config/ConfigProfiles$Profile;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Header.class, Object.class), Header.class, "modified;size;profile", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->modified:Ljava/nio/file/attribute/FileTime;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->size:J", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Header;->profile:Lnet/anubis/client/config/ConfigProfiles$Profile;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public FileTime modified() {
            return this.modified;
        }

        public long size() {
            return this.size;
        }

        public Profile profile() {
            return this.profile;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles$Match.class */
    @Environment(EnvType.CLIENT)
    private static final class Match extends Record {
        private final String name;
        private final int score;
        private final int length;

        private Match(String name, int score, int length) {
            this.name = name;
            this.score = score;
            this.length = length;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Match.class), Match.class, "name;score;length", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->score:I", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->length:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Match.class), Match.class, "name;score;length", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->score:I", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->length:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Match.class, Object.class), Match.class, "name;score;length", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->score:I", "FIELD:Lnet/anubis/client/config/ConfigProfiles$Match;->length:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public int score() {
            return this.score;
        }

        public int length() {
            return this.length;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ConfigProfiles$Contents.class */
    @Environment(EnvType.CLIENT)
    private static final class Contents {
        final List<String> servers = new ArrayList();
        final Map<Part, JsonElement> data = new EnumMap(Part.class);

        private Contents() {
        }
    }

    private ConfigProfiles() {
        this(ModuleConfigStore.getInstance());
    }

    private ConfigProfiles(ModuleConfigStore store) {
        this.headers = new HashMap();
        this.store = store;
    }

    public static ConfigProfiles getInstance() {
        return INSTANCE;
    }

    public static boolean isValidName(String name) {
        return name != null && name.equals(name.trim()) && VALID_NAME.matcher(name).matches() && !RESERVED.contains(name.toLowerCase(Locale.ROOT));
    }

    public Path directory() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("profiles").toAbsolutePath().normalize();
    }

    public synchronized List<Profile> list() {
        if (this.cached == null) {
            this.cached = scan();
        }
        return this.cached;
    }

    public synchronized void refresh() {
        this.cached = null;
    }

    public synchronized Profile find(String name) {
        if (name == null) {
            return null;
        }
        for (Profile profile : list()) {
            if (profile.name().equalsIgnoreCase(name)) {
                return profile;
            }
        }
        return null;
    }

    public synchronized String create(String name) throws ProfileException {
        String wanted = name == null ? "" : name.trim();
        if (!isValidName(wanted)) {
            throw new ProfileException(NAME_RULE);
        }
        Profile existing = find(wanted);
        if (existing != null) {
            throw new ProfileException(existing.name() + " already exists - use its Save button");
        }
        if (Files.exists(file(wanted), new LinkOption[0])) {
            this.cached = null;
            throw new ProfileException(wanted + " already exists - use its Save button");
        }
        Contents contents = new Contents();
        contents.data.put(Part.MODULES, capture(Part.MODULES));
        write(wanted, contents);
        activate(wanted);
        return wanted;
    }

    public synchronized Set<Part> save(String name) throws ProfileException {
        Profile profile = require(name);
        Contents contents = read(profile.name());
        Set<Part> parts = contents.data.isEmpty() ? EnumSet.of(Part.MODULES) : EnumSet.copyOf((Collection) contents.data.keySet());
        for (Part part : parts) {
            contents.data.put(part, capture(part));
        }
        write(profile.name(), contents);
        activate(profile.name());
        return Collections.unmodifiableSet(parts);
    }

    public synchronized Set<Part> load(String name) throws ProfileException {
        Profile profile = require(name);
        Contents contents = read(profile.name());
        Set<Part> applied = EnumSet.noneOf(Part.class);
        for (Map.Entry<Part, JsonElement> entry : contents.data.entrySet()) {
            if (apply(entry.getKey(), entry.getValue())) {
                applied.add(entry.getKey());
            }
        }
        if (applied.isEmpty()) {
            throw new ProfileException("Nothing in " + profile.name() + " could be applied");
        }
        activate(profile.name());
        return Collections.unmodifiableSet(applied);
    }

    public synchronized void delete(String name) throws ProfileException {
        Profile profile = require(name);
        try {
            Files.deleteIfExists(file(profile.name()));
            this.cached = null;
            if (profile.name().equalsIgnoreCase(this.store.activeProfile())) {
                this.store.setActiveProfile(null);
                this.store.save();
            }
        } catch (IOException e) {
            LOGGER.warn("[Anubis] Failed to delete profile '{}'", profile.name(), e);
            throw new ProfileException("Could not delete " + profile.name());
        }
    }

    public synchronized String rename(String name, String newName) throws ProfileException {
        Profile profile = require(name);
        String wanted = newName == null ? "" : newName.trim();
        if (wanted.equals(profile.name())) {
            return wanted;
        }
        if (!isValidName(wanted)) {
            throw new ProfileException(NAME_RULE);
        }
        Profile clash = find(wanted);
        boolean sameFile = clash != null && clash.name().equals(profile.name());
        if (clash != null && !sameFile) {
            throw new ProfileException(clash.name() + " already exists");
        }
        Path from = file(profile.name());
        Path to = file(wanted);
        try {
            if (sameFile) {
                Path step = from.resolveSibling(profile.name() + ".rename.tmp");
                Files.move(from, step, StandardCopyOption.REPLACE_EXISTING);
                try {
                    Files.move(step, to, new CopyOption[0]);
                } catch (IOException e) {
                    Files.move(step, from, StandardCopyOption.REPLACE_EXISTING);
                    throw e;
                }
            } else {
                Files.move(from, to, new CopyOption[0]);
            }
            this.cached = null;
            if (profile.name().equalsIgnoreCase(this.store.activeProfile())) {
                this.store.setActiveProfile(wanted);
                this.store.save();
            }
            return wanted;
        } catch (IOException e2) {
            LOGGER.warn("[Anubis] Failed to rename profile '{}' to '{}'", new Object[]{profile.name(), wanted, e2});
            throw new ProfileException("Could not rename " + profile.name());
        }
    }

    public synchronized void setPart(String name, Part part, boolean included) throws ProfileException {
        Profile profile = require(name);
        Contents contents = read(profile.name());
        if (included == contents.data.containsKey(part)) {
            return;
        }
        if (included) {
            contents.data.put(part, capture(part));
        } else {
            if (contents.data.size() == 1) {
                throw new ProfileException("A profile needs at least one part");
            }
            contents.data.remove(part);
        }
        write(profile.name(), contents);
    }

    public synchronized String addServer(String name, String address) throws ProfileException {
        ServerAddresses.Address parsed = ServerAddresses.parse(address);
        if (parsed == null) {
            throw new ProfileException("That isn't a server address");
        }
        Profile profile = require(name);
        Contents contents = read(profile.name());
        String shown = parsed.toString();
        if (contents.servers.contains(shown)) {
            throw new ProfileException(shown + " is already on the list");
        }
        if (contents.servers.size() >= 16) {
            throw new ProfileException("A profile can list up to 16 servers");
        }
        contents.servers.add(shown);
        write(profile.name(), contents);
        return shown;
    }

    public synchronized void removeServer(String name, String address) throws ProfileException {
        Profile profile = require(name);
        Contents contents = read(profile.name());
        if (contents.servers.remove(address)) {
            write(profile.name(), contents);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public synchronized void exportTo(String name, Path target) throws MatchException, ProfileException {
        JsonObject shared = exportJson(name);
        try {
            Files.writeString(target, GSON.toJson(shared), StandardCharsets.UTF_8, new OpenOption[0]);
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to export profile '{}' to {}", new Object[]{name, target, e});
            throw new ProfileException("Could not write " + String.valueOf(target.getFileName()));
        }
    }

    public synchronized String shareCode(String name) throws ProfileException {
        return ProfileCodes.encode(exportJson(name));
    }

    public synchronized String importFile(Path source) throws ProfileException {
        try {
            if (!Files.isRegularFile(source, new LinkOption[0])) {
                throw new ProfileException("That file doesn't exist");
            }
            if (Files.size(source) > 4194304) {
                throw new ProfileException("That file is too large to be a profile");
            }
            String text = Files.readString(source, StandardCharsets.UTF_8);
            String fileName = source.getFileName().toString();
            int dot = fileName.lastIndexOf(46);
            String fallback = dot > 0 ? fileName.substring(0, dot) : fileName;
            return importRoot(ProfileCodes.parseObject(text, "That file isn't an Anubis profile"), fallback);
        } catch (IOException | RuntimeException e) {
            throw new ProfileException("Couldn't read " + String.valueOf(source.getFileName()));
        }
    }

    public synchronized String importCode(String text) throws ProfileException {
        try {
            return importRoot(ProfileCodes.decode(text), "Shared profile");
        } catch (ProfileException e) {
            LOGGER.warn("[Anubis] Share code import failed ({} characters on the clipboard): {}", Integer.valueOf(text == null ? 0 : text.length()), e.getMessage());
            throw e;
        }
    }

    public synchronized void backup(Set<Part> parts) throws ProfileException {
        Contents contents = new Contents();
        for (Part part : parts) {
            contents.data.put(part, capture(part));
        }
        if (contents.data.isEmpty()) {
            contents.data.put(Part.MODULES, capture(Part.MODULES));
        }
        write(BACKUP_NAME, contents);
        this.cached = null;
    }

    public synchronized List<String> loadForServer(String address) {
        ServerAddresses.Address joined = ServerAddresses.parse(address);
        if (joined == null) {
            return List.of();
        }
        this.cached = null;
        List<Match> matches = new ArrayList<>();
        for (Profile profile : list()) {
            if (!profile.broken()) {
                int best = 0;
                int length = 0;
                for (String server : profile.servers()) {
                    ServerAddresses.Address entry = ServerAddresses.parse(server);
                    int score = ServerAddresses.score(entry, joined);
                    if (score > best || (score == best && score > 0 && entry.host().length() > length)) {
                        best = score;
                        length = entry.host().length();
                    }
                }
                if (best > 0) {
                    matches.add(new Match(profile.name(), best, length));
                }
            }
        }
        matches.sort(Comparator.comparingInt((v0) -> {
            return v0.score();
        }).thenComparingInt((v0) -> {
            return v0.length();
        }).thenComparing(match -> {
            return match.name().toLowerCase(Locale.ROOT);
        }));
        List<String> loaded = new ArrayList<>();
        for (Match match2 : matches) {
            try {
                load(match2.name());
                loaded.add(match2.name());
            } catch (ProfileException e) {
                LOGGER.warn("[Anubis] Could not load profile '{}' for {}: {}", new Object[]{match2.name(), joined, e.getMessage()});
            }
        }
        return loaded;
    }

    private Profile require(String name) throws ProfileException {
        Profile profile = find(name);
        if (profile == null) {
            throw new ProfileException("That profile no longer exists");
        }
        return profile;
    }

    private void activate(String name) {
        this.store.setActiveProfile(name);
        this.store.save();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private JsonObject exportJson(String name) throws MatchException, ProfileException {
        Profile profile = require(name);
        Contents contents = shareable(read(profile.name()), "Could not prepare " + profile.name() + " for sharing");
        if (contents.data.isEmpty()) {
            throw new ProfileException(profile.name() + " has nothing to share");
        }
        JsonObject root = new JsonObject();
        root.addProperty(FORMAT_KEY, 2);
        root.addProperty("name", profile.name());
        root.add("data", data(contents));
        return root;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private String importRoot(JsonObject root, String fallbackName) throws MatchException, ProfileException {
        Contents contents = shareable(parse(root, false, false), "That profile is damaged");
        if (contents.data.isEmpty()) {
            throw new ProfileException("That profile has nothing Anubis can use");
        }
        JsonElement named = root.get("name");
        String wanted = (named == null || !named.isJsonPrimitive()) ? fallbackName : named.getAsString();
        String name = uniqueName(wanted);
        write(name, contents);
        return name;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private JsonElement capture(Part part) throws MatchException, ProfileException {
        try {
            switch (part) {
                case MODULES:
                    return this.store.snapshot();
                case HUD:
                    JsonObject hud = new JsonObject();
                    hud.add("layout", HudLayoutStore.getInstance().profileSnapshot());
                    hud.add("theme", HudTheme.get().profileSnapshot());
                    return hud;
                case THEME:
                    return ThemeStore.get().profileSnapshot();
                case FRIENDS:
                    JsonArray friends = new JsonArray();
                    for (String friend : FriendStore.getInstance().all()) {
                        friends.add(friend);
                    }
                    return friends;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
        } catch (RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to capture the {} part of a profile", part.key, e);
            throw new ProfileException("Could not read your current " + part.label);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private boolean apply(Part part, JsonElement data) throws MatchException {
        try {
            switch (part) {
                case MODULES:
                    return data.isJsonObject() && this.store.applyProfile(data.getAsJsonObject());
                case HUD:
                    return applyHud(data);
                case THEME:
                    if (!data.isJsonObject()) {
                        return false;
                    }
                    ThemeStore theme = ThemeStore.get();
                    theme.applyProfile(data.getAsJsonObject());
                    theme.save();
                    return true;
                case FRIENDS:
                    if (!data.isJsonArray()) {
                        return false;
                    }
                    FriendStore.getInstance().replaceAll(friendNames(data.getAsJsonArray()));
                    return true;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
        } catch (RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to apply the {} part of a profile", part.key, e);
            return false;
        }
    }

    private static boolean applyHud(JsonElement data) {
        if (!data.isJsonObject()) {
            return false;
        }
        JsonObject hud = data.getAsJsonObject();
        boolean applied = false;
        if (hud.has("layout") && hud.get("layout").isJsonObject()) {
            applied = HudLayoutStore.getInstance().applyProfile(hud.getAsJsonObject("layout"));
        }
        if (hud.has("theme") && hud.get("theme").isJsonObject()) {
            HudTheme theme = HudTheme.get();
            theme.applyProfile(hud.getAsJsonObject("theme"));
            theme.save();
            applied = true;
        }
        return applied;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private Contents shareable(Contents contents, String failure) throws MatchException, ProfileException {
        Contents kept = new Contents();
        try {
            for (Map.Entry<Part, JsonElement> entry : contents.data.entrySet()) {
                JsonElement value = shareable(entry.getKey(), entry.getValue());
                if (value != null) {
                    kept.data.put(entry.getKey(), value);
                }
            }
            return kept;
        } catch (RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to filter a profile for sharing", e);
            throw new ProfileException(failure);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private JsonElement shareable(Part part, JsonElement data) throws MatchException {
        JsonObject layout;
        switch (part) {
            case MODULES:
                if (data.isJsonObject()) {
                    return this.store.shareable(data.getAsJsonObject());
                }
                return null;
            case HUD:
                if (!data.isJsonObject()) {
                    return null;
                }
                JsonObject hud = data.getAsJsonObject();
                JsonObject kept = new JsonObject();
                if (hud.has("layout") && hud.get("layout").isJsonObject() && (layout = HudLayoutStore.shareable(hud.getAsJsonObject("layout"))) != null) {
                    kept.add("layout", layout);
                }
                if (hud.has("theme") && hud.get("theme").isJsonObject()) {
                    kept.add("theme", HudTheme.get().shareable(hud.getAsJsonObject("theme")));
                }
                if (kept.size() == 0) {
                    return null;
                }
                return kept;
            case THEME:
                if (data.isJsonObject()) {
                    return ThemeStore.get().shareable(data.getAsJsonObject());
                }
                return null;
            case FRIENDS:
                if (!data.isJsonArray()) {
                    return null;
                }
                JsonArray kept2 = new JsonArray();
                for (String name : friendNames(data.getAsJsonArray())) {
                    kept2.add(name);
                }
                return kept2;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    private static List<String> friendNames(JsonArray array) {
        Map<String, String> names = new LinkedHashMap<>();
        Iterator it = array.iterator();
        while (it.hasNext()) {
            JsonElement element = (JsonElement) it.next();
            if (names.size() >= 1000) {
                break;
            }
            if (element.isJsonPrimitive()) {
                String name = element.getAsString().trim();
                if (FriendStore.isValidName(name)) {
                    names.putIfAbsent(name.toLowerCase(Locale.ROOT), name);
                }
            }
        }
        return List.copyOf(names.values());
    }

    /* JADX WARN: Code duplicated, block: B:38:0x00c2  */
    private static Contents parse(JsonObject root, boolean withServers, boolean allowOld) throws ProfileException {
        Contents contents = new Contents();
        if (root.has(FORMAT_KEY)) {
            try {
                int format = root.get(FORMAT_KEY).getAsInt();
                if (format > 2) {
                    throw new ProfileException("Made by a newer Anubis - update to open it");
                }
                if (format < 1) {
                    throw new ProfileException("That isn't an Anubis profile");
                }
                if (format < 2 && !allowOld) {
                    throw new ProfileException(TOO_OLD);
                }
                JsonElement data = root.get("data");
                if (data != null && data.isJsonObject()) {
                    for (Part part : Part.values()) {
                        JsonElement value = data.getAsJsonObject().get(part.key);
                        if (value != null) {
                            if (part == Part.FRIENDS) {
                                if (value.isJsonArray()) {
                                    contents.data.put(part, value);
                                }
                            } else if (value.isJsonObject()) {
                                contents.data.put(part, value);
                            }
                        }
                    }
                }
                JsonElement servers = root.get("loadOnJoin");
                if (withServers && servers != null && servers.isJsonArray()) {
                    for (JsonElement server : servers.getAsJsonArray()) {
                        if (server.isJsonPrimitive()) {
                            addServer(contents.servers, server.getAsString());
                        }
                    }
                }
            } catch (RuntimeException e) {
                throw new ProfileException("That isn't an Anubis profile");
            }
        } else if (root.has("modules") && root.get("modules").isJsonObject()) {
            if (!allowOld) {
                throw new ProfileException(TOO_OLD);
            }
            contents.data.put(Part.MODULES, root);
        } else {
            throw new ProfileException("That isn't an Anubis profile");
        }
        return contents;
    }

    private static void addServer(List<String> servers, String text) {
        ServerAddresses.Address address = ServerAddresses.parse(text);
        if (address == null || servers.size() >= 16) {
            return;
        }
        String shown = address.toString();
        if (!servers.contains(shown)) {
            servers.add(shown);
        }
    }

    private static JsonObject data(Contents contents) {
        JsonObject data = new JsonObject();
        for (Map.Entry<Part, JsonElement> entry : contents.data.entrySet()) {
            data.add(entry.getKey().key, entry.getValue());
        }
        return data;
    }

    private Contents read(String name) throws ProfileException {
        Path path = file(name);
        try {
            if (Files.size(path) > MAX_LOCAL_BYTES) {
                throw new ProfileException(name + " is too large to open");
            }
            Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            try {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (!parsed.isJsonObject()) {
                    throw new ProfileException(name + " isn't a profile file");
                }
                Contents contents = parse(parsed.getAsJsonObject(), true, false);
                if (reader != null) {
                    reader.close();
                }
                return contents;
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to read profile '{}'", name, e);
            throw new ProfileException("Could not read " + name);
        }
    }

    private void write(String name, Contents contents) throws ProfileException {
        JsonObject root = new JsonObject();
        root.addProperty(FORMAT_KEY, 2);
        JsonArray servers = new JsonArray();
        for (String server : contents.servers) {
            servers.add(server);
        }
        root.add("loadOnJoin", servers);
        root.add("data", data(contents));
        Path path = file(name);
        try {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Path tmp = path.resolveSibling(name + ".json.tmp");
            Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8, new OpenOption[0]);
            try {
                GSON.toJson(root, writer);
                if (writer != null) {
                    writer.close();
                }
                try {
                    Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
                }
                this.cached = null;
            } catch (Throwable th) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | RuntimeException e2) {
            LOGGER.warn("[Anubis] Failed to save profile '{}'", name, e2);
            throw new ProfileException("Could not save " + name);
        }
    }

    private Path file(String name) throws ProfileException {
        if (!isValidName(name)) {
            throw new ProfileException(NAME_RULE);
        }
        Path dir = directory();
        Path path = dir.resolve(name + ".json").normalize();
        if (dir.equals(path.getParent())) {
            return path;
        }
        throw new ProfileException(NAME_RULE);
    }

    private String uniqueName(String wanted) throws ProfileException {
        String base = cleanName(wanted);
        if (!taken(base)) {
            return base;
        }
        Matcher numbered = NUMBERED.matcher(base);
        String root = numbered.matches() ? numbered.group(1) : base;
        for (int i = 2; i < 1000; i++) {
            String suffix = " (" + i + ")";
            String candidate = cut(root, 32 - suffix.length()).stripTrailing() + suffix;
            if (isValidName(candidate) && !taken(candidate)) {
                return candidate;
            }
        }
        throw new ProfileException("Too many profiles share that name");
    }

    private boolean taken(String name) {
        return find(name) != null || Files.exists(directory().resolve(name + ".json"), new LinkOption[0]);
    }

    private static String cleanName(String raw) {
        StringBuilder out = new StringBuilder();
        String text = raw == null ? "" : raw;
        int i = 0;
        while (i < text.length()) {
            int cp = text.codePointAt(i);
            i += Character.charCount(cp);
            if (Character.isLetter(cp) || Character.isDigit(cp) || "_-()".indexOf(cp) >= 0) {
                out.appendCodePoint(cp);
            } else if (Character.isWhitespace(cp) || Character.isSpaceChar(cp)) {
                out.append(' ');
            }
        }
        String name = cut(out.toString().replaceAll(" {2,}", " ").trim(), 32).trim();
        return isValidName(name) ? name : "Imported";
    }

    private static String cut(String text, int codePoints) {
        return text.codePointCount(0, text.length()) <= codePoints ? text : text.substring(0, text.offsetByCodePoints(0, codePoints));
    }

    private List<Profile> scan() {
        Path dir = directory();
        upgradeLocalProfiles(dir);
        if (!Files.isDirectory(dir, new LinkOption[0])) {
            this.headers.clear();
            return List.of();
        }
        List<Profile> profiles = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(dir, "*.json");
            try {
                for (Path file : files) {
                    String fileName = file.getFileName().toString();
                    String name = fileName.substring(0, fileName.length() - EXTENSION.length());
                    if (isValidName(name) && Files.isRegularFile(file, new LinkOption[0]) && seen.add(name.toLowerCase(Locale.ROOT))) {
                        profiles.add(header(name, file));
                    }
                }
                if (files != null) {
                    files.close();
                }
            } catch (Throwable th) {
                if (files != null) {
                    try {
                        files.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            LOGGER.warn("[Anubis] Failed to list profiles", e);
        }
        Set<String> names = new HashSet<>();
        for (Profile profile : profiles) {
            names.add(profile.name());
        }
        this.headers.keySet().retainAll(names);
        profiles.sort(Comparator.comparing(profile2 -> {
            return profile2.name().toLowerCase(Locale.ROOT);
        }));
        return List.copyOf(profiles);
    }

    private void upgradeLocalProfiles(Path dir) {
        if (this.upgradeChecked) {
            return;
        }
        this.upgradeChecked = true;
        Path marker = dir.resolve(UPGRADED_MARKER);
        try {
            if (Files.exists(marker, new LinkOption[0])) {
                return;
            }
            Files.createDirectories(dir, new FileAttribute[0]);
            DirectoryStream<Path> files = Files.newDirectoryStream(dir, "*.json");
            try {
                for (Path file : files) {
                    String fileName = file.getFileName().toString();
                    String name = fileName.substring(0, fileName.length() - EXTENSION.length());
                    if (isValidName(name) && Files.isRegularFile(file, new LinkOption[0]) && Files.size(file) <= MAX_LOCAL_BYTES) {
                        upgrade(name, file);
                    }
                }
                if (files != null) {
                    files.close();
                }
                Files.writeString(marker, "Profiles here were moved to format 2.\n", StandardCharsets.UTF_8, new OpenOption[0]);
            } catch (Throwable th) {
                if (files != null) {
                    try {
                        files.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to move old profiles to the current format", e);
        }
    }

    private void upgrade(String name, Path file) {
        try {
            Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
            try {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (reader != null) {
                    reader.close();
                }
                if (parsed.isJsonObject()) {
                    JsonObject root = parsed.getAsJsonObject();
                    JsonElement format = root.get(FORMAT_KEY);
                    if (format == null || !format.isJsonPrimitive() || format.getAsInt() < 2) {
                        write(name, parse(root, true, true));
                        LOGGER.info("[Anubis] Moved profile '{}' to the current format", name);
                    }
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | RuntimeException | ProfileException e) {
            LOGGER.warn("[Anubis] Could not move profile '{}' to the current format", name, e);
        }
    }

    private Profile header(String name, Path file) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(file, (Class<BasicFileAttributes>) BasicFileAttributes.class, new LinkOption[0]);
            Header known = this.headers.get(name);
            if (known != null && known.modified().equals(attributes.lastModifiedTime()) && known.size() == attributes.size()) {
                return known.profile();
            }
            Profile profile = readHeader(name, file);
            this.headers.put(name, new Header(attributes.lastModifiedTime(), attributes.size(), profile));
            return profile;
        } catch (IOException e) {
            return new Profile(name, Set.of(), List.of(), true);
        }
    }

    private static Profile readHeader(String name, Path file) {
        Set<Part> parts = EnumSet.noneOf(Part.class);
        List<String> servers = new ArrayList<>();
        int format = -1;
        boolean legacy = false;
        try {
            JsonReader reader = new JsonReader(Files.newBufferedReader(file, StandardCharsets.UTF_8));
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "anubisProfile":
                            format = reader.nextInt();
                            break;
                        case "loadOnJoin":
                            readServers(reader, servers);
                            break;
                        case "data":
                            readParts(reader, parts);
                            break;
                        case "modules":
                            legacy = reader.peek() == JsonToken.BEGIN_OBJECT;
                            reader.skipValue();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                reader.close();
                if (format < 0 && legacy) {
                    return new Profile(name, Collections.unmodifiableSet(EnumSet.of(Part.MODULES)), List.of(), true);
                }
                boolean broken = format < 2 || format > 2 || parts.isEmpty();
                return new Profile(name, Collections.unmodifiableSet(parts), List.copyOf(servers), broken);
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to read profile '{}'", name, e);
            return new Profile(name, Set.of(), List.of(), true);
        }
    }

    private static void readServers(JsonReader reader, List<String> servers) throws IOException {
        if (reader.peek() != JsonToken.BEGIN_ARRAY) {
            reader.skipValue();
            return;
        }
        reader.beginArray();
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.STRING) {
                addServer(servers, reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endArray();
    }

    private static void readParts(JsonReader reader, Set<Part> parts) throws IOException {
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            reader.skipValue();
            return;
        }
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            JsonToken token = reader.peek();
            Part[] partArrValues = Part.values();
            int length = partArrValues.length;
            for (int i = 0; i < length; i++) {
                Part part = partArrValues[i];
                if (part.key.equals(key)) {
                    if (token == (part == Part.FRIENDS ? JsonToken.BEGIN_ARRAY : JsonToken.BEGIN_OBJECT)) {
                        parts.add(part);
                    }
                }
            }
            reader.skipValue();
        }
        reader.endObject();
    }
}
