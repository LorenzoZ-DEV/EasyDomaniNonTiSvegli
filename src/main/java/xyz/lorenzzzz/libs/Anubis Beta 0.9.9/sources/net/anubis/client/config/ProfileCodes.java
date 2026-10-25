package net.anubis.client.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ProfileCodes.class */
@Environment(EnvType.CLIENT)
final class ProfileCodes {
    static final String PREFIX = "AnubisProfile:";
    static final int MAX_JSON_BYTES = 4194304;
    private static final int MAX_CODE_CHARS = 1048576;
    private static final String DAMAGED = "That code is damaged or incomplete";
    private static final byte[] DICTIONARY = ShareDictionary.bytes();

    private ProfileCodes() {
    }

    static String encode(JsonObject profile) {
        return encode(profile, PREFIX);
    }

    static String encode(JsonObject profile, String prefix) {
        byte[] json = tidyNumbers(profile.deepCopy()).toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream packed = new ByteArrayOutputStream((json.length / 3) + 64);
        Deflater deflater = new Deflater(9, true);
        try {
            deflater.setDictionary(DICTIONARY);
            deflater.setInput(json);
            deflater.finish();
            byte[] chunk = new byte[4096];
            while (!deflater.finished()) {
                packed.write(chunk, 0, deflater.deflate(chunk));
            }
            deflater.end();
            return prefix + Base64.getEncoder().withoutPadding().encodeToString(packed.toByteArray());
        } catch (Throwable th) {
            deflater.end();
            throw th;
        }
    }

    static JsonObject decode(String text) throws ProfileException {
        return decode(text, PREFIX, "No Anubis profile code on the clipboard");
    }

    static JsonObject decode(String text, String prefix, String missing) throws ProfileException {
        String body = extract(text, prefix, missing);
        try {
            InputStream in = unpack(Base64.getDecoder().decode(body));
            try {
                byte[] json = in.readNBytes(4194305);
                if (in != null) {
                    in.close();
                }
                if (json.length > 4194304) {
                    throw new ProfileException("That code is too large");
                }
                return parseObject(new String(json, StandardCharsets.UTF_8), DAMAGED);
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new ProfileException(DAMAGED);
        }
    }

    private static InputStream unpack(byte[] packed) throws IOException {
        if (packed.length > 1 && (packed[0] & 255) == 31 && (packed[1] & 255) == 139) {
            return new GZIPInputStream(new ByteArrayInputStream(packed));
        }
        Inflater inflater = new Inflater(true);
        inflater.setDictionary(DICTIONARY);
        return new InflaterInputStream(new ByteArrayInputStream(Arrays.copyOf(packed, packed.length + 1)), inflater);
    }

    private static JsonElement tidyNumbers(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (String key : List.copyOf(object.keySet())) {
                object.add(key, tidyNumbers(object.get(key)));
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                array.set(i, tidyNumbers(array.get(i)));
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            double value = element.getAsDouble();
            if (value == Math.rint(value) && Math.abs(value) < 1.0E15d) {
                return new JsonPrimitive(Long.valueOf((long) value));
            }
        }
        return element;
    }

    static JsonObject parseObject(String text, String failure) throws ProfileException {
        String json = text.startsWith("\ufeff") ? text.substring(1) : text;
        try {
            JsonElement parsed = JsonParser.parseString(json);
            if (parsed.isJsonObject()) {
                return parsed.getAsJsonObject();
            }
            throw new ProfileException(failure);
        } catch (RuntimeException e) {
            throw new ProfileException(failure);
        }
    }

    static boolean contains(String text, String prefix) {
        if (text == null) {
            return false;
        }
        for (int i = 0; i + prefix.length() <= text.length(); i++) {
            if (text.regionMatches(true, i, prefix, 0, prefix.length())) {
                return true;
            }
        }
        return false;
    }

    private static String extract(String text, String prefix, String missing) throws ProfileException {
        if (text == null || text.isBlank()) {
            throw new ProfileException("The clipboard is empty");
        }
        if (text.length() > 2097152) {
            throw new ProfileException("That code is too large");
        }
        int at = -1;
        for (int i = 0; i + prefix.length() <= text.length() && at < 0; i++) {
            if (text.regionMatches(true, i, prefix, 0, prefix.length())) {
                at = i;
            }
        }
        if (at < 0) {
            throw new ProfileException(missing);
        }
        int start = at + prefix.length();
        int end = start;
        while (end < text.length() && isCodeChar(text.charAt(end))) {
            end++;
        }
        if (end == start) {
            throw new ProfileException(DAMAGED);
        }
        if (end - start > 1048576) {
            throw new ProfileException("That code is too large");
        }
        return text.substring(start, end).replace('-', '+').replace('_', '/');
    }

    private static boolean isCodeChar(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ((ch >= '0' && ch <= '9') || ch == '+' || ch == '/' || ch == '=' || ch == '-' || ch == '_');
    }
}
