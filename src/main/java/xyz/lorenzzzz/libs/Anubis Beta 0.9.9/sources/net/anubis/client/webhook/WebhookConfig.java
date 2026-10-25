package net.anubis.client.webhook;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/webhook/WebhookConfig.class */
@Environment(EnvType.CLIENT)
public final class WebhookConfig extends Record {
    private final String url;
    private final String username;
    private final String avatarUrl;
    public static final String DEFAULT_USERNAME = "Anubis";
    public static final String DEFAULT_AVATAR_URL = "https://i.imgur.com/W3cTqvc.png";

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, WebhookConfig.class), WebhookConfig.class, "url;username;avatarUrl", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->username:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->avatarUrl:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, WebhookConfig.class), WebhookConfig.class, "url;username;avatarUrl", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->username:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->avatarUrl:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, WebhookConfig.class, Object.class), WebhookConfig.class, "url;username;avatarUrl", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->username:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookConfig;->avatarUrl:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public String url() {
        return this.url;
    }

    public String username() {
        return this.username;
    }

    public String avatarUrl() {
        return this.avatarUrl;
    }

    public WebhookConfig(String url, String username, String avatarUrl) {
        String url2 = normalizeRequired(url, "url");
        String username2 = normalizeOptional(username, "Anubis");
        String avatarUrl2 = normalizeOptional(avatarUrl, DEFAULT_AVATAR_URL);
        this.url = url2;
        this.username = username2;
        this.avatarUrl = avatarUrl2;
    }

    public WebhookConfig(String url) {
        this(url, "Anubis", DEFAULT_AVATAR_URL);
    }

    private static String normalizeRequired(String value, String fieldName) {
        String normalized = normalizeOptional(value, null);
        if (normalized == null) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return normalized;
    }

    private static String normalizeOptional(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? fallback : normalized;
    }
}
