package net.anubis.client.webhook;

import com.google.gson.JsonObject;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/webhook/WebhookEmbed.class */
@Environment(EnvType.CLIENT)
public final class WebhookEmbed extends Record {
    private final String title;
    private final String description;
    private final Integer color;
    private final String thumbnailUrl;
    private final String imageUrl;
    private final String footerText;
    private final String footerIconUrl;

    public WebhookEmbed(String title, String description, Integer color, String thumbnailUrl, String imageUrl, String footerText, String footerIconUrl) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.footerText = footerText;
        this.footerIconUrl = footerIconUrl;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, WebhookEmbed.class), WebhookEmbed.class, "title;description;color;thumbnailUrl;imageUrl;footerText;footerIconUrl", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->description:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->color:Ljava/lang/Integer;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->thumbnailUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->imageUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerText:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerIconUrl:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, WebhookEmbed.class), WebhookEmbed.class, "title;description;color;thumbnailUrl;imageUrl;footerText;footerIconUrl", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->description:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->color:Ljava/lang/Integer;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->thumbnailUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->imageUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerText:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerIconUrl:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, WebhookEmbed.class, Object.class), WebhookEmbed.class, "title;description;color;thumbnailUrl;imageUrl;footerText;footerIconUrl", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->description:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->color:Ljava/lang/Integer;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->thumbnailUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->imageUrl:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerText:Ljava/lang/String;", "FIELD:Lnet/anubis/client/webhook/WebhookEmbed;->footerIconUrl:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public String title() {
        return this.title;
    }

    public String description() {
        return this.description;
    }

    public Integer color() {
        return this.color;
    }

    public String thumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String imageUrl() {
        return this.imageUrl;
    }

    public String footerText() {
        return this.footerText;
    }

    public String footerIconUrl() {
        return this.footerIconUrl;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        add(json, "title", this.title);
        add(json, "description", this.description);
        if (this.color != null) {
            json.addProperty("color", this.color);
        }
        addNestedUrl(json, "thumbnail", this.thumbnailUrl);
        addNestedUrl(json, "image", this.imageUrl);
        if (this.footerText != null || this.footerIconUrl != null) {
            JsonObject footer = new JsonObject();
            add(footer, "text", this.footerText);
            add(footer, "icon_url", this.footerIconUrl);
            json.add("footer", footer);
        }
        return json;
    }

    private static void addNestedUrl(JsonObject root, String name, String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        JsonObject object = new JsonObject();
        object.addProperty("url", url);
        root.add(name, object);
    }

    private static void add(JsonObject root, String name, String value) {
        if (value != null && !value.isBlank()) {
            root.addProperty(name, value);
        }
    }
}
