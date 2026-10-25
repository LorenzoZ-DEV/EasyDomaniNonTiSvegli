package net.anubis.client.module.impl.misc.spawnerprotect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/spawnerprotect/DiscordWebhook.class */
@Environment(EnvType.CLIENT)
public final class DiscordWebhook {
    private static final int MAX_ATTEMPTS = 3;
    private static final double MAX_RETRY_SECONDS = 30.0d;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Gson GSON = new Gson();
    private static final DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy • HH:mm:ss").withZone(ZoneId.of("Europe/Istanbul"));
    private static final ThreadPoolExecutor WORKER = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue(64), task -> {
        Thread thread = new Thread(task, "Anubis-SpawnerProtect-Webhook");
        thread.setDaemon(true);
        return thread;
    }, (task2, executor) -> {
        LOGGER.warn("[Anubis] Spawner Protect webhook queue is full");
    });

    private DiscordWebhook() {
    }

    public static Map<String, Object> field(String name, String value, boolean inline) {
        return Map.of("name", name, "value", value, "inline", Boolean.valueOf(inline));
    }

    public static String payload(String title, String description, int color, List<Map<String, Object>> fields, Instant timestamp) {
        Map<String, Object> embed = Map.of("title", title, "description", description, "color", Integer.valueOf(color), "fields", new ArrayList(fields), "timestamp", timestamp.toString(), "author", Map.of("name", "ANUBIS  /  SPAWNER PROTECT"), "footer", Map.of("text", "Minecraft 1.21.11 • " + LOCAL_TIME.format(timestamp) + " (TR)"));
        return GSON.toJson(Map.of("username", "Spawner Protect • Anubis", "allowed_mentions", Map.of("parse", List.of()), "embeds", List.of(embed)));
    }

    public static void send(String url, String payload) {
        if (url == null || url.isBlank()) {
            return;
        }
        WORKER.execute(() -> {
            post(url.trim(), payload);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void post(String url, String payload) {
        for (int attempt = 0; attempt < 3; attempt++) {
            HttpURLConnection connection = null;
            try {
                try {
                    try {
                        HttpURLConnection connection2 = (HttpURLConnection) URI.create(url).toURL().openConnection();
                        connection2.setRequestMethod("POST");
                        connection2.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        connection2.setConnectTimeout(5000);
                        connection2.setReadTimeout(5000);
                        connection2.setDoOutput(true);
                        byte[] body = payload.getBytes(StandardCharsets.UTF_8);
                        connection2.setFixedLengthStreamingMode(body.length);
                        OutputStream out = connection2.getOutputStream();
                        try {
                            out.write(body);
                            if (out != null) {
                                out.close();
                            }
                            int status = connection2.getResponseCode();
                            if (status >= 200 && status < 300) {
                                if (connection2 != null) {
                                    connection2.disconnect();
                                    return;
                                }
                                return;
                            }
                            if (status != 429 || attempt >= 2) {
                                LOGGER.warn("[Anubis] Spawner Protect webhook failed (HTTP {})", Integer.valueOf(status));
                                if (connection2 != null) {
                                    connection2.disconnect();
                                    return;
                                }
                                return;
                            }
                            double seconds = retryAfter(connection2);
                            if (!Double.isFinite(seconds) || seconds > MAX_RETRY_SECONDS) {
                                if (connection2 == null) {
                                    break;
                                }
                                connection2.disconnect();
                                break;
                            } else {
                                connection2.disconnect();
                                Thread.sleep(((long) (Math.max(0.25d, seconds) * 1000.0d)) + 100);
                                if (connection2 != null) {
                                    connection2.disconnect();
                                }
                            }
                        } catch (Throwable th) {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } catch (Exception e) {
                        LOGGER.warn("[Anubis] Spawner Protect webhook could not be sent ({})", e.getClass().getSimpleName());
                        if (0 != 0) {
                            connection.disconnect();
                            return;
                        }
                        return;
                    }
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    if (0 != 0) {
                        connection.disconnect();
                        return;
                    }
                    return;
                }
            } catch (Throwable th3) {
                if (0 != 0) {
                    connection.disconnect();
                }
                throw th3;
            }
        }
        LOGGER.warn("[Anubis] Spawner Protect webhook is rate limited");
    }

    private static double retryAfter(HttpURLConnection connection) throws IOException {
        InputStream error = connection.getErrorStream();
        if (error != null) {
            try {
                JsonObject response = JsonParser.parseString(new String(error.readNBytes(4096), StandardCharsets.UTF_8)).getAsJsonObject();
                double asDouble = response.has("retry_after") ? response.get("retry_after").getAsDouble() : 1.0d;
                if (error != null) {
                    error.close();
                }
                return asDouble;
            } catch (Throwable th) {
                if (error != null) {
                    try {
                        error.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (error != null) {
            error.close();
        }
        return 1.0d;
    }
}
