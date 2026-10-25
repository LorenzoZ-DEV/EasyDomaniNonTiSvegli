package xyz.lorenzzzz.easydomani.detection.probe;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Test;
import xyz.lorenzzzz.easydomani.detection.ModProfile;

class PackProbeTest {

    private static Map<String, String> unzip(byte[] zip) throws IOException {
        Map<String, String> out = new HashMap<>();
        try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
            for (ZipEntry e; (e = in.getNextEntry()) != null; ) out.put(e.getName(), new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
        return out;
    }

    static final Map<String, String> OVR = PackProbe.overrides("edn-abc123", List.of("key.anubis.open_gui"));

    @Test
    void zipContenuto() throws Exception {
        Map<String, String> files = unzip(PackProbe.buildZip("edn-abc123", OVR));
        assertEquals(2, files.size());
        assertTrue(files.get("pack.mcmeta").contains("\"pack_format\":46"));
        assertTrue(files.get("pack.mcmeta").contains("\"min_format\":22"));
        assertEquals("{\"edn.probe\":\"edn-abc123\",\"key.anubis.open_gui\":\"edn-abc123-a\"}", files.get("assets/edn/lang/en_us.json"));
        assertEquals("{\"edn.probe\":\"edn-abc123\"}", unzip(PackProbe.buildZip("edn-abc123", Map.of())).get("assets/edn/lang/en_us.json"));
    }

    @Test
    void overrideKeys() {
        Map<String, String> o = PackProbe.overrides("v1234", Arrays.asList(" key.anubis.open_gui ", "key.wurst.zoom", "key.wurst.zoom",
                "Key.Maiuscola", "x\"y", "", null, "edn.probe", "key.category.meteor-client.meteor-client"));
        assertEquals(List.of("key.anubis.open_gui", "key.wurst.zoom", "key.category.meteor-client.meteor-client"), List.copyOf(o.keySet()));
        assertEquals(List.of("v1234-a", "v1234-b", "v1234-c"), List.copyOf(o.values()));
        List<String> tante = new ArrayList<>();
        for (int i = 0; i < 40; i++) tante.add("k" + i);
        Map<String, String> max = PackProbe.overrides("v1234", tante);
        assertEquals(26, max.size());
        assertEquals("v1234-z", max.get("k25"));
    }

    @Test
    void sha1Deterministico() throws Exception {
        byte[] a = PackProbe.sha1(PackProbe.buildZip("edn-abc123", OVR));
        assertEquals(20, a.length);
        assertArrayEquals(a, PackProbe.sha1(PackProbe.buildZip("edn-abc123", OVR)));
        assertFalse(java.util.Arrays.equals(a, PackProbe.sha1(PackProbe.buildZip("edn-def456", OVR))));
    }

    @Test
    void url() {
        assertTrue(PackProbe.isHttpUrl("http://mc.example.net:8164"));
        assertTrue(PackProbe.isHttpUrl("https://cdn.example.net/p.zip"));
        assertFalse(PackProbe.isHttpUrl(""));
        assertFalse(PackProbe.isHttpUrl("ftp://x/p.zip"));
        assertFalse(PackProbe.isHttpUrl("http://"));
        assertFalse(PackProbe.isHttpUrl("non un url"));
    }

    @Test
    void serverServeSoloIlPack() throws Exception {
        byte[] zip = PackProbe.buildZip("edn-abc123", OVR);
        HttpServer s = PackProbe.startServer("127.0.0.1", 0, "/p.zip", zip);
        try {
            String base = "http://127.0.0.1:" + s.getAddress().getPort();
            HttpClient c = HttpClient.newHttpClient();
            HttpResponse<byte[]> ok = c.send(HttpRequest.newBuilder(URI.create(base + "/p.zip")).build(), HttpResponse.BodyHandlers.ofByteArray());
            assertEquals(200, ok.statusCode());
            assertArrayEquals(zip, ok.body());
            assertEquals(404, c.send(HttpRequest.newBuilder(URI.create(base + "/p.zipX")).build(), HttpResponse.BodyHandlers.discarding()).statusCode());
            assertEquals(404, c.send(HttpRequest.newBuilder(URI.create(base + "/altro")).build(), HttpResponse.BodyHandlers.discarding()).statusCode());
            assertEquals(405, c.send(HttpRequest.newBuilder(URI.create(base + "/p.zip")).POST(HttpRequest.BodyPublishers.noBody()).build(),
                    HttpResponse.BodyHandlers.discarding()).statusCode());
            // porta occupata: IOException (gestita in reload con un warning)
            assertThrows(IOException.class, () -> PackProbe.startServer("127.0.0.1", s.getAddress().getPort(), "/p.zip", zip));
        } finally {
            s.stop(0);
        }
    }

    @Test
    void ctrlPack() {
        List<ModProfile> defs = SignProbe.packControls("edn-abc123", "key.anubis.open_gui", "edn-abc123-a");
        assertEquals(List.of("ctrl-pack", "ctrl-ovr-translate", "ctrl-ovr-keybind"), defs.stream().map(ModProfile::id).toList());
        assertTrue(defs.stream().allMatch(SignProbe::isControl));
        assertEquals(List.of("edn-abc123", "edn-abc123-a", "edn-abc123-a"), defs.stream().map(d -> d.probeExpected().get(0)).toList());
        assertEquals("{\"translate\":\"edn.probe\",\"fallback\":\"FB_TEST\"}", SignProbe.keyJson(defs.get(0)));
        assertEquals("{\"translate\":\"key.anubis.open_gui\",\"fallback\":\"FB_TEST\"}", SignProbe.keyJson(defs.get(1)));
        assertEquals("{\"keybind\":\"key.anubis.open_gui\"}", SignProbe.keyJson(defs.get(2)));
        NBTList<NBTCompound> m = SignProbe.buildSignNbt(defs, "edn-s", true).getCompoundTagOrNull("front_text").getCompoundListTagOrNull("messages");
        assertEquals("edn.probe", m.getTag(0).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(0).getStringTagValueOrNull("fallback"));
        assertEquals("key.anubis.open_gui", m.getTag(1).getStringTagValueOrNull("translate"));
        assertEquals("FB_TEST", m.getTag(1).getStringTagValueOrNull("fallback"));
        assertEquals("key.anubis.open_gui", m.getTag(2).getStringTagValueOrNull("keybind"));
        assertNull(m.getTag(2).getStringTagValueOrNull("fallback"));
        assertEquals("edn-s", m.getTag(3).getStringTagValueOrNull("text"));
        // senza override: solo ctrl-pack
        assertEquals(1, SignProbe.packControls("edn-abc123", null, null).size());
    }
}
