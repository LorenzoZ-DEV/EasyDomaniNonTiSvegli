package net.anubis.client.module.impl.render.character.gltf;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/ImageDecoder.class */
@Environment(EnvType.CLIENT)
public interface ImageDecoder {
    Picture decode(byte[] bArr) throws IOException;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture.class */
    @Environment(EnvType.CLIENT)
    public static final class Picture extends Record {
        private final int width;
        private final int height;
        private final int[] argb;

        public Picture(int width, int height, int[] argb) {
            this.width = width;
            this.height = height;
            this.argb = argb;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Picture.class), Picture.class, "width;height;argb", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->width:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->argb:[I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Picture.class), Picture.class, "width;height;argb", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->width:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->argb:[I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Picture.class, Object.class), Picture.class, "width;height;argb", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->width:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ImageDecoder$Picture;->argb:[I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public int[] argb() {
            return this.argb;
        }
    }
}
