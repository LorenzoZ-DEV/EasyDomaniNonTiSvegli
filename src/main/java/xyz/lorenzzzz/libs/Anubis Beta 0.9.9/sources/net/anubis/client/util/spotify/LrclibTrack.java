package net.anubis.client.util.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/LrclibTrack.class */
@Environment(EnvType.CLIENT)
public final class LrclibTrack extends Record {
    private final long id;
    private final String trackName;
    private final String artistName;
    private final String albumName;
    private final double duration;
    private final boolean instrumental;
    private final String plainLyrics;
    private final String syncedLyrics;

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LrclibTrack.class), LrclibTrack.class, "id;trackName;artistName;albumName;duration;instrumental;plainLyrics;syncedLyrics", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->id:J", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->trackName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->artistName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->albumName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->duration:D", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->instrumental:Z", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->plainLyrics:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->syncedLyrics:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LrclibTrack.class), LrclibTrack.class, "id;trackName;artistName;albumName;duration;instrumental;plainLyrics;syncedLyrics", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->id:J", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->trackName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->artistName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->albumName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->duration:D", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->instrumental:Z", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->plainLyrics:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->syncedLyrics:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LrclibTrack.class, Object.class), LrclibTrack.class, "id;trackName;artistName;albumName;duration;instrumental;plainLyrics;syncedLyrics", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->id:J", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->trackName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->artistName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->albumName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->duration:D", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->instrumental:Z", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->plainLyrics:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LrclibTrack;->syncedLyrics:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public long id() {
        return this.id;
    }

    public String trackName() {
        return this.trackName;
    }

    public String artistName() {
        return this.artistName;
    }

    public String albumName() {
        return this.albumName;
    }

    public double duration() {
        return this.duration;
    }

    public boolean instrumental() {
        return this.instrumental;
    }

    public String plainLyrics() {
        return this.plainLyrics;
    }

    public String syncedLyrics() {
        return this.syncedLyrics;
    }

    public LrclibTrack(long id, String trackName, String artistName, String albumName, double duration, boolean instrumental, String plainLyrics, String syncedLyrics) {
        String trackName2 = nonNull(trackName);
        String artistName2 = nonNull(artistName);
        String albumName2 = nonNull(albumName);
        String plainLyrics2 = nonNull(plainLyrics);
        String syncedLyrics2 = nonNull(syncedLyrics);
        duration = (!Double.isFinite(duration) || duration < 0.0d) ? 0.0d : duration;
        this.id = id;
        this.trackName = trackName2;
        this.artistName = artistName2;
        this.albumName = albumName2;
        this.duration = duration;
        this.instrumental = instrumental;
        this.plainLyrics = plainLyrics2;
        this.syncedLyrics = syncedLyrics2;
    }

    public boolean hasSynced() {
        return !this.syncedLyrics.isBlank();
    }

    public boolean hasPlain() {
        return !this.plainLyrics.isBlank();
    }

    public boolean usable() {
        return this.instrumental || hasSynced() || hasPlain();
    }

    public static LrclibTrack fromJson(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        return new LrclibTrack(number(object, "id", 0L), text(object, "trackName"), text(object, "artistName"), text(object, "albumName"), number(object, "duration", 0.0d), bool(object, "instrumental"), text(object, "plainLyrics"), text(object, "syncedLyrics"));
    }

    public static List<LrclibTrack> listFromJson(JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return List.of();
        }
        JsonArray<JsonElement> array = element.getAsJsonArray();
        List<LrclibTrack> tracks = new ArrayList<>(array.size());
        for (JsonElement item : array) {
            LrclibTrack track = fromJson(item);
            if (track != null) {
                tracks.add(track);
            }
        }
        return List.copyOf(tracks);
    }

    public static JsonElement parse(String json) {
        return JsonParser.parseString(json);
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("id", Long.valueOf(this.id));
        object.addProperty("trackName", this.trackName);
        object.addProperty("artistName", this.artistName);
        object.addProperty("albumName", this.albumName);
        object.addProperty("duration", Double.valueOf(this.duration));
        object.addProperty("instrumental", Boolean.valueOf(this.instrumental));
        object.addProperty("plainLyrics", this.plainLyrics);
        object.addProperty("syncedLyrics", this.syncedLyrics);
        return object;
    }

    private static String nonNull(String value) {
        return value == null ? "" : value;
    }

    private static String text(JsonObject object, String name) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isString()) {
                return primitive.getAsString();
            }
        }
        return "";
    }

    private static long number(JsonObject object, String name, long fallback) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isNumber()) {
                try {
                    return primitive.getAsLong();
                } catch (NumberFormatException e) {
                    return fallback;
                }
            }
        }
        return fallback;
    }

    private static double number(JsonObject object, String name, double fallback) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isNumber()) {
                return primitive.getAsDouble();
            }
        }
        return fallback;
    }

    private static boolean bool(JsonObject object, String name) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isBoolean() && primitive.getAsBoolean()) {
                return true;
            }
        }
        return false;
    }
}
