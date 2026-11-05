package xyz.lorenzzzz.easydomani.packprobe.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ProbePackArchiveBuilder {
    public static final String KEY = "edn.probe";

    private static final Pattern LANG_KEY = Pattern.compile("[a-z0-9_.-]{1,128}");
    private static final int MAX_OVERRIDES = 26;
    private static final String MCMETA = "{\"pack\":{\"description\":\"edn\",\"pack_format\":46,"
            + "\"supported_formats\":{\"min_inclusive\":22,\"max_inclusive\":1000},\"min_format\":22,\"max_format\":1000}}";

    private ProbePackArchiveBuilder() {
    }

    static Map<String, String> overrides(String value, List<String> keys) {
        Map<String, String> out = new LinkedHashMap<>();
        for (String k : keys) {
            String key = k == null ? "" : k.trim();
            if (out.size() >= MAX_OVERRIDES || !LANG_KEY.matcher(key).matches() || key.equals(KEY)) continue;
            if (!out.containsKey(key)) out.put(key, value + "-" + (char) ('a' + out.size()));
        }
        return out;
    }

    static byte[] buildZip(String value, Map<String, String> overrides) throws IOException {
        StringBuilder lang = new StringBuilder("{\"").append(KEY).append("\":\"").append(value).append('"');
        overrides.forEach((k, v) -> lang.append(",\"").append(k).append("\":\"").append(v).append('"'));
        lang.append('}');
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            entry(zip, "pack.mcmeta", MCMETA);
            entry(zip, "assets/edn/lang/en_us.json", lang.toString());
        }
        return out.toByteArray();
    }

    static byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(data);
    }

    private static void entry(ZipOutputStream zip, String name, String content) throws IOException {
        ZipEntry e = new ZipEntry(name);
        e.setTime(0L);
        zip.putNextEntry(e);
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }
}
