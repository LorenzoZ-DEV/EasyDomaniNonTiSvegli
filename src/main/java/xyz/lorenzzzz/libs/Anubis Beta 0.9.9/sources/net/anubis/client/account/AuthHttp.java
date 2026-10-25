package net.anubis.client.account;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/AuthHttp.class */
@Environment(EnvType.CLIENT)
final class AuthHttp {
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).followRedirects(HttpClient.Redirect.NEVER).build();
    private static final Duration TIMEOUT = Duration.ofSeconds(30);
    private static final String USER_AGENT = "AnubisClient";

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/AuthHttp$Reply.class */
    @Environment(EnvType.CLIENT)
    static final class Reply extends Record {
        private final int status;
        private final JsonObject json;
        private final HttpHeaders headers;

        Reply(int status, JsonObject json, HttpHeaders headers) {
            this.status = status;
            this.json = json;
            this.headers = headers;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Reply.class), Reply.class, "status;json;headers", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->status:I", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->json:Lcom/google/gson/JsonObject;", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->headers:Ljava/net/http/HttpHeaders;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Reply.class), Reply.class, "status;json;headers", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->status:I", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->json:Lcom/google/gson/JsonObject;", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->headers:Ljava/net/http/HttpHeaders;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Reply.class, Object.class), Reply.class, "status;json;headers", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->status:I", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->json:Lcom/google/gson/JsonObject;", "FIELD:Lnet/anubis/client/account/AuthHttp$Reply;->headers:Ljava/net/http/HttpHeaders;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int status() {
            return this.status;
        }

        public JsonObject json() {
            return this.json;
        }

        public HttpHeaders headers() {
            return this.headers;
        }

        boolean ok() {
            return this.status >= 200 && this.status < 300;
        }

        String text(String key) {
            return AuthHttp.text(this.json, key);
        }
    }

    private AuthHttp() {
    }

    static Reply postForm(String url, Map<String, String> form) throws InterruptedException {
        String body = (String) form.entrySet().stream().map(e -> {
            return URLEncoder.encode((String) e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode((String) e.getValue(), StandardCharsets.UTF_8);
        }).collect(Collectors.joining("&"));
        return send(url, HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(body)));
    }

    static Reply postJson(String url, String body, Map<String, String> headers) throws InterruptedException {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
        Objects.requireNonNull(request);
        headers.forEach(request::header);
        return send(url, request);
    }

    static Reply get(String url, String bearerToken) throws InterruptedException {
        return send(url, HttpRequest.newBuilder(URI.create(url)).header("Authorization", "Bearer " + bearerToken).GET());
    }

    static String text(JsonObject object, String key) {
        JsonElement value = object == null ? null : object.get(key);
        if (value == null || !value.isJsonPrimitive()) {
            return null;
        }
        return value.getAsString();
    }

    static JsonObject object(JsonObject object, String key) {
        JsonElement value = object == null ? null : object.get(key);
        if (value == null || !value.isJsonObject()) {
            return null;
        }
        return value.getAsJsonObject();
    }

    private static Reply send(String url, HttpRequest.Builder request) throws InterruptedException {
        try {
            HttpResponse<String> response = CLIENT.send(request.timeout(TIMEOUT).header("Accept", "application/json").header("User-Agent", USER_AGENT).build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return new Reply(response.statusCode(), parse((String) response.body()), response.headers());
        } catch (IOException e) {
            throw new AccountException("Couldn't reach " + URI.create(url).getHost() + " - check your connection", e);
        }
    }

    private static JsonObject parse(String body) {
        try {
            JsonElement root = JsonParser.parseString(body);
            return root.isJsonObject() ? root.getAsJsonObject() : new JsonObject();
        } catch (RuntimeException e) {
            return new JsonObject();
        }
    }
}
