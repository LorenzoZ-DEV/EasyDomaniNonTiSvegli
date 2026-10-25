package net.anubis.client.module.impl.render.character.kit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.zip.InflaterInputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/BakedModel.class */
@Environment(EnvType.CLIENT)
public final class BakedModel {
    public static final int MAGIC = 1095648072;
    public static final int VERSION = 3;
    private static final int SHIPPED_VERSION = 4;

    private BakedModel() {
    }

    static CharacterModel load(BakedDesign design, Detail detail) {
        long start = System.nanoTime();
        try {
            InputStream raw = design.open();
            try {
                DataInputStream in = new DataInputStream(new BufferedInputStream(raw));
                int version = in.readInt() == 1095648072 ? in.readInt() : -1;
                if (version != 3 && version != 4) {
                    throw new IllegalStateException(design.id() + ": not a version 3 baked character");
                }
                int width = 0;
                int height = 0;
                int[] atlas = null;
                if (version == 4) {
                    width = in.readInt();
                    height = in.readInt();
                    byte[] filtered = new byte[width * height * 4];
                    in.readFully(filtered);
                    atlas = unfilter(filtered, width, height);
                }
                float minY = in.readFloat();
                float maxY = in.readFloat();
                float hipY = in.readFloat();
                float flat = in.readFloat();
                boolean firstPersonArms = in.readBoolean();
                boolean colored = in.readBoolean();
                int levels = in.readInt();
                int wanted = Math.min(detail.ordinal(), levels - 1);
                PartMesh[] parts = new PartMesh[in.readInt()];
                for (int i = 0; i < parts.length; i++) {
                    Bone bone = Bone.values()[in.readByte()];
                    float px = in.readFloat();
                    float py = in.readFloat();
                    float pz = in.readFloat();
                    float gx = in.readFloat();
                    float gy = in.readFloat();
                    float gz = in.readFloat();
                    float[] box = new float[6];
                    for (int k = 0; k < 6; k++) {
                        box[k] = in.readFloat();
                    }
                    float swing = in.readFloat();
                    float swingLimit = in.readFloat();
                    boolean jointed = in.readBoolean();
                    float jx = 0.0f;
                    float jy = 0.0f;
                    float jz = 0.0f;
                    if (jointed) {
                        jx = in.readFloat() - px;
                        jy = in.readFloat() - py;
                        jz = in.readFloat() - pz;
                    }
                    Surface solid = null;
                    Surface clear = null;
                    int level = 0;
                    while (level < levels) {
                        Surface a = surface(in, box, px, py, pz, flat, colored, jointed, level == wanted);
                        Surface b = surface(in, box, px, py, pz, flat, colored, jointed, level == wanted);
                        if (level == wanted) {
                            solid = a;
                            clear = b;
                        }
                        level++;
                    }
                    parts[i] = new PartMesh(bone.name(), bone, -1, false, px, py, pz, gx, gy, gz, null, solid, null, clear, -1, swing, swingLimit, jointed, jx, jy, jz, false, 0.0f, 0.0f, 0.0f);
                }
                if (version == 3) {
                    width = in.readInt();
                    height = in.readInt();
                    byte[] packed = new byte[in.readInt()];
                    in.readFully(packed);
                    atlas = pixels(packed, width, height);
                }
                long millis = (System.nanoTime() - start) / 1000000;
                CharacterModel characterModel = new CharacterModel(design.id(), design.name(), detail, 1.0f, parts, 0, width, height, Atlas.mips(atlas, width, height), 0.0f, 0.0f, minY, maxY, hipY, firstPersonArms, millis);
                if (raw != null) {
                    raw.close();
                }
                return characterModel;
            } catch (Throwable th) {
                if (raw != null) {
                    try {
                        raw.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(design.name() + ": " + e.getMessage(), e);
        }
    }

    private static int[] pixels(byte[] packed, int width, int height) throws IOException {
        byte[] raw = new byte[width * height * 4];
        InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(packed));
        try {
            int read = in.readNBytes(raw, 0, raw.length);
            if (read != raw.length) {
                throw new IOException("atlas is truncated");
            }
            in.close();
            return unfilter(raw, width, height);
        } catch (Throwable th) {
            try {
                in.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static int[] unfilter(byte[] raw, int width, int height) {
        int[] px = new int[width * height];
        for (int i = 0; i < px.length; i++) {
            int left = i % width == 0 ? 0 : px[i - 1];
            int p = 0;
            for (int c = 0; c < 4; c++) {
                int shift = 24 - (c * 8);
                p |= ((raw[(i * 4) + c] + (left >>> shift)) & 255) << shift;
            }
            px[i] = p;
        }
        return px;
    }

    private static Surface surface(DataInputStream in, float[] box, float px, float py, float pz, float flat, boolean colored, boolean jointed, boolean keep) throws IOException {
        int unsignedByte;
        int verts = in.readInt();
        if (!keep) {
            in.skipNBytes(((long) verts) * (13 + (colored ? 3L : 0L) + (jointed ? 1L : 0L)));
            in.skipNBytes(((long) in.readInt()) * (verts > 65535 ? 12L : 6L));
            return null;
        }
        float[] pos = new float[verts * 3];
        for (int i = 0; i < pos.length; i++) {
            int axis = i % 3;
            pos[i] = box[axis] + ((in.readChar() / 65535.0f) * (box[axis + 3] - box[axis]));
        }
        byte[] nrm = new byte[verts * 3];
        in.readFully(nrm);
        float[] uv = new float[verts * 2];
        for (int i2 = 0; i2 < uv.length; i2++) {
            uv[i2] = in.readChar() / 65535.0f;
        }
        int[] color = new int[verts];
        for (int i3 = 0; i3 < verts; i3++) {
            int i4 = i3;
            if (colored) {
                unsignedByte = (-16777216) | (in.readUnsignedByte() << 16) | (in.readUnsignedByte() << 8) | in.readUnsignedByte();
            } else {
                unsignedByte = -1;
            }
            color[i4] = unsignedByte;
        }
        float[] joint = null;
        if (jointed) {
            joint = new float[verts];
            for (int i5 = 0; i5 < verts; i5++) {
                joint[i5] = in.readUnsignedByte() / 255.0f;
            }
        }
        int count = in.readInt();
        int[] tris = new int[count * 3];
        for (int i6 = 0; i6 < tris.length; i6++) {
            tris[i6] = verts > 65535 ? in.readInt() : in.readChar();
        }
        if (verts == 0 || count == 0) {
            return null;
        }
        Surface.Builder b = new Surface.Builder();
        for (int i7 = 0; i7 < verts; i7++) {
            float nx = nrm[i7 * 3] / 127.0f;
            float ny = nrm[(i7 * 3) + 1] / 127.0f;
            float nz = nrm[(i7 * 3) + 2] / 127.0f;
            float l = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
            if (l > 1.0E-6f) {
                nx /= l;
                ny /= l;
                nz /= l;
            }
            b.vertex(pos[i7 * 3] - px, pos[(i7 * 3) + 1] - py, pos[(i7 * 3) + 2] - pz, nx, ny, nz, uv[i7 * 2], uv[(i7 * 2) + 1], color[i7], false, flat);
        }
        CharacterBuilder.pair(tris, count, verts, b);
        Surface out = b.build();
        if (out != null && joint != null) {
            for (float w : joint) {
                if (w > 0.0f) {
                    out.joint = joint;
                    break;
                }
            }
        }
        return out;
    }
}
