package xyz.lorenzzzz.easydomani.packprobe.integration;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public final class ProbePackHttpServer {

    private ProbePackHttpServer() {
    }

    public static HttpServer start(String bind, int port, String path, byte[] zip) throws IOException {
        HttpServer s = HttpServer.create(new InetSocketAddress(bind, port), 0);
        s.createContext(path, ex -> serve(ex, path, zip));
        s.start();
        return s;
    }

    private static void serve(HttpExchange ex, String path, byte[] zip) throws IOException {
        try (ex) {
            String method = ex.getRequestMethod();
            if (!path.equals(ex.getRequestURI().getPath())) {
                ex.sendResponseHeaders(404, -1);
                return;
            }
            boolean head = "HEAD".equals(method);
            if (!head && !"GET".equals(method)) {
                ex.sendResponseHeaders(405, -1);
                return;
            }
            ex.getResponseHeaders().set("Content-Type", "application/zip");
            ex.sendResponseHeaders(200, head ? -1 : zip.length);
            if (!head) ex.getResponseBody().write(zip);
        }
    }
}
