package net.anubis.client.webhook;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import net.anubis.client.AnubisClientMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/webhook/WebhookService.class */
@Environment(EnvType.CLIENT)
public final class WebhookService {
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();

    public boolean send(WebhookConfig config, WebhookPayloadBuilder payloadBuilder) {
        try {
            JsonObject payload = payloadBuilder.build(config);
            HttpRequest request = HttpRequest.newBuilder(URI.create(config.url())).timeout(Duration.ofSeconds(10L)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();
            try {
                HttpResponse<Void> response = this.httpClient.send(request, HttpResponse.BodyHandlers.discarding());
                int status = response.statusCode();
                if (status >= 200 && status < 300) {
                    return true;
                }
                AnubisClientMod.LOGGER.warn("[Anubis] Webhook request failed with status {}", Integer.valueOf(status));
                return false;
            } catch (IOException | InterruptedException exception) {
                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                AnubisClientMod.LOGGER.warn("[Anubis] Webhook request failed", exception);
                return false;
            }
        } catch (RuntimeException exception2) {
            AnubisClientMod.LOGGER.warn("[Anubis] Failed to build webhook payload", exception2);
            return false;
        }
    }
}
