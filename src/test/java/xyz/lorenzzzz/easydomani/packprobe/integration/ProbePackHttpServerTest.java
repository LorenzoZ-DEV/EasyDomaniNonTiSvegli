package xyz.lorenzzzz.easydomani.packprobe.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ProbePackHttpServerTest {

    @Test
    void serverServeSoloIlPack() throws Exception {
        byte[] zip = "contenuto-zip".getBytes(StandardCharsets.UTF_8);
        HttpServer s = ProbePackHttpServer.start("127.0.0.1", 0, "/p.zip", zip);
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

            assertThrows(IOException.class, () -> ProbePackHttpServer.start("127.0.0.1", s.getAddress().getPort(), "/p.zip", zip));
        } finally {
            s.stop(0);
        }
    }
}
