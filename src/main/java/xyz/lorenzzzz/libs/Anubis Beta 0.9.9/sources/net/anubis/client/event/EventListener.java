package net.anubis.client.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/EventListener.class */
@Target({ElementType.METHOD})
@Environment(EnvType.CLIENT)
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    int priority() default 0;
}
