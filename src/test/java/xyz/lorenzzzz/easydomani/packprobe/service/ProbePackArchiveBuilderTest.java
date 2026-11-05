package xyz.lorenzzzz.easydomani.packprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Test;

class ProbePackArchiveBuilderTest {

    private static Map<String, String> unzip(byte[] zip) throws IOException {
        Map<String, String> out = new HashMap<>();
        try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
            for (ZipEntry e; (e = in.getNextEntry()) != null; ) out.put(e.getName(), new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
        return out;
    }

    static final Map<String, String> OVR = ProbePackArchiveBuilder.overrides("edn-abc123", List.of("key.anubis.open_gui"));

    @Test
    void zipContenuto() throws Exception {
        Map<String, String> files = unzip(ProbePackArchiveBuilder.buildZip("edn-abc123", OVR));
        assertEquals(2, files.size());
        assertTrue(files.get("pack.mcmeta").contains("\"pack_format\":46"));
        assertTrue(files.get("pack.mcmeta").contains("\"min_format\":22"));
        assertEquals("{\"edn.probe\":\"edn-abc123\",\"key.anubis.open_gui\":\"edn-abc123-a\"}", files.get("assets/edn/lang/en_us.json"));
        assertEquals("{\"edn.probe\":\"edn-abc123\"}", unzip(ProbePackArchiveBuilder.buildZip("edn-abc123", Map.of())).get("assets/edn/lang/en_us.json"));
    }

    @Test
    void overrideKeys() {
        Map<String, String> o = ProbePackArchiveBuilder.overrides("v1234", Arrays.asList(" key.anubis.open_gui ", "key.wurst.zoom", "key.wurst.zoom",
                "Key.Maiuscola", "x\"y", "", null, "edn.probe", "key.category.meteor-client.meteor-client"));
        assertEquals(List.of("key.anubis.open_gui", "key.wurst.zoom", "key.category.meteor-client.meteor-client"), List.copyOf(o.keySet()));
        assertEquals(List.of("v1234-a", "v1234-b", "v1234-c"), List.copyOf(o.values()));
        List<String> tante = new ArrayList<>();
        for (int i = 0; i < 40; i++) tante.add("k" + i);
        Map<String, String> max = ProbePackArchiveBuilder.overrides("v1234", tante);
        assertEquals(26, max.size());
        assertEquals("v1234-z", max.get("k25"));
    }

    @Test
    void sha1Deterministico() throws Exception {
        byte[] a = ProbePackArchiveBuilder.sha1(ProbePackArchiveBuilder.buildZip("edn-abc123", OVR));
        assertEquals(20, a.length);
        assertArrayEquals(a, ProbePackArchiveBuilder.sha1(ProbePackArchiveBuilder.buildZip("edn-abc123", OVR)));
        assertFalse(Arrays.equals(a, ProbePackArchiveBuilder.sha1(ProbePackArchiveBuilder.buildZip("edn-def456", OVR))));
    }
}
