package net.fabricmc.fabric.api.util;

import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.class_3542;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-api-base-1.0.5+4ebb5c083e.jar:net/fabricmc/fabric/api/util/TriState.class */
public enum TriState implements class_3542 {
    FALSE("false"),
    DEFAULT("default"),
    TRUE("true");

    public static final Codec<TriState> CODEC = class_3542.method_28140(TriState::values);
    private final String name;

    public static TriState of(boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public static TriState of(Boolean bool) {
        return bool == null ? DEFAULT : of(bool.booleanValue());
    }

    public boolean get() {
        return this == TRUE;
    }

    public Boolean getBoxed() {
        if (this == DEFAULT) {
            return null;
        }
        return Boolean.valueOf(get());
    }

    public boolean orElse(boolean value) {
        return this == DEFAULT ? value : get();
    }

    public boolean orElseGet(BooleanSupplier supplier) {
        return this == DEFAULT ? supplier.getAsBoolean() : get();
    }

    public <T> Optional<T> map(BooleanFunction<? extends T> mapper) {
        Objects.requireNonNull(mapper, "Mapper function cannot be null");
        if (this == DEFAULT) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(get()));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: X extends java.lang.Throwable */
    public <X extends Throwable> boolean orElseThrow(Supplier<X> exceptionSupplier) throws Throwable {
        if (this != DEFAULT) {
            return get();
        }
        throw exceptionSupplier.get();
    }

    public static TriState fromSystemProperty(String property) {
        String value = System.getProperty(property);
        if (value != null) {
            return Boolean.parseBoolean(value) ? TRUE : FALSE;
        }
        return DEFAULT;
    }

    public String method_15434() {
        return this.name;
    }

    TriState(String name) {
        this.name = name;
    }
}
