package net.fabricmc.fabric.impl.resource.client;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_5481;
import net.minecraft.class_5632;
import net.minecraft.class_5684;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/client/PackTooltipComponent.class */
@Environment(EnvType.CLIENT)
public final class PackTooltipComponent extends Record implements class_5632, class_5684 {
    private final Optional<class_2561> name;
    private final Optional<List<class_5481>> description;

    public PackTooltipComponent(Optional<class_2561> name, Optional<List<class_5481>> description) {
        this.name = name;
        this.description = description;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PackTooltipComponent.class), PackTooltipComponent.class, "name;description", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->name:Ljava/util/Optional;", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->description:Ljava/util/Optional;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PackTooltipComponent.class), PackTooltipComponent.class, "name;description", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->name:Ljava/util/Optional;", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->description:Ljava/util/Optional;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PackTooltipComponent.class, Object.class), PackTooltipComponent.class, "name;description", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->name:Ljava/util/Optional;", "FIELD:Lnet/fabricmc/fabric/impl/resource/client/PackTooltipComponent;->description:Ljava/util/Optional;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public Optional<class_2561> name() {
        return this.name;
    }

    public Optional<List<class_5481>> description() {
        return this.description;
    }

    public int method_32661(class_327 font) {
        int height = 0;
        if (this.name.isPresent()) {
            Objects.requireNonNull(font);
            height = 0 + 9 + 2;
        }
        if (this.description.isPresent()) {
            int size = this.description.get().size();
            Objects.requireNonNull(font);
            height += (size * 9) + 3;
        }
        if (this.name.isPresent() && this.description.isPresent()) {
            Objects.requireNonNull(font);
            height += 9;
        }
        return height;
    }

    public int method_32664(class_327 font) {
        Optional<class_2561> optional = this.name;
        Objects.requireNonNull(font);
        return Math.max(((Integer) optional.map((v1) -> {
            return r1.method_27525(v1);
        }).orElse(0)).intValue(), ((Integer) this.description.map(description -> {
            Stream stream = description.stream();
            Objects.requireNonNull(font);
            return Integer.valueOf(stream.mapToInt(font::method_30880).max().orElse(0));
        }).orElse(0)).intValue());
    }

    public void method_32665(class_332 graphics, class_327 font, int x, int y) {
        if (this.name.isPresent()) {
            graphics.method_51439(font, this.name.get(), x, y, -1, true);
            Objects.requireNonNull(font);
            y += 9 + 1;
            if (this.description.isPresent()) {
                Objects.requireNonNull(font);
                y += 9;
            }
        }
        if (this.description.isPresent()) {
            for (class_5481 line : this.description.get()) {
                graphics.method_51430(font, line, x, y, -1, true);
                Objects.requireNonNull(font);
                y += 9 + 1;
            }
        }
    }

    public void method_32666(class_327 font, int x, int y, int width, int height, class_332 graphics) {
        if (this.name.isPresent() && this.description.isPresent()) {
            Objects.requireNonNull(font);
            int iMethod_32664 = x + method_32664(font);
            Objects.requireNonNull(font);
            graphics.method_25294(x, y + 9 + 4, iMethod_32664, y + 9 + 5, (-16777216) | class_124.field_1080.method_532().intValue());
        }
    }
}
