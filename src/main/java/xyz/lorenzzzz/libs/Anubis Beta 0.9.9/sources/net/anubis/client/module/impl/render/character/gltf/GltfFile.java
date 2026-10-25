package net.anubis.client.module.impl.render.character.gltf;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/GltfFile.class */
@Environment(EnvType.CLIENT)
final class GltfFile {
    private static final int GLTF = 1179937895;
    private static final int JSON = 1313821514;
    private static final int BIN = 5130562;
    private static final int MAX_ELEMENTS = 33554432;
    final JsonObject json;
    private final ByteBuffer bin;

    private GltfFile(JsonObject json, ByteBuffer bin) {
        this.json = json;
        this.bin = bin;
    }

    static GltfFile read(byte[] data) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if (data.length < 20 || buf.getInt(0) != GLTF) {
            throw new IOException("not a .glb file");
        }
        if (buf.getInt(4) != 2) {
            throw new IOException("only glTF 2.0 files are supported");
        }
        int end = Math.min(buf.getInt(8), data.length);
        int off = 12;
        JsonObject json = null;
        ByteBuffer bin = ByteBuffer.allocate(0);
        while (off + 8 <= end) {
            int length = buf.getInt(off);
            int type = buf.getInt(off + 4);
            if (length < 0 || length > (end - off) - 8) {
                throw new IOException("the file is cut short");
            }
            if (type == JSON) {
                json = JsonParser.parseString(new String(data, off + 8, length, StandardCharsets.UTF_8)).getAsJsonObject();
            } else if (type == BIN) {
                bin = ByteBuffer.wrap(data, off + 8, length).slice().order(ByteOrder.LITTLE_ENDIAN);
            }
            off += 8 + length;
        }
        if (json == null) {
            throw new IOException("the file has no glTF data");
        }
        JsonArray required = array(json, "extensionsRequired");
        if (required.isEmpty()) {
            return new GltfFile(json, bin);
        }
        throw new IOException("it needs " + String.valueOf(required) + ", export it without compression");
    }

    static JsonArray array(JsonObject o, String key) {
        JsonElement e = o.get(key);
        return (e == null || !e.isJsonArray()) ? new JsonArray() : e.getAsJsonArray();
    }

    static JsonObject object(JsonObject o, String key) {
        JsonElement e = o.get(key);
        return (e == null || !e.isJsonObject()) ? new JsonObject() : e.getAsJsonObject();
    }

    static int integer(JsonObject o, String key, int fallback) {
        JsonElement e = o.get(key);
        return (e == null || !e.isJsonPrimitive()) ? fallback : e.getAsInt();
    }

    private static String string(JsonObject o, String key, String fallback) {
        JsonElement e = o.get(key);
        return (e == null || !e.isJsonPrimitive()) ? fallback : e.getAsString();
    }

    int nodes() {
        return array(this.json, "nodes").size();
    }

    JsonObject node(int i) {
        return array(this.json, "nodes").get(i).getAsJsonObject();
    }

    String nodeName(int i) {
        return string(node(i), "name", "node" + i);
    }

    private JsonObject material(int m) {
        return m < 0 ? new JsonObject() : array(this.json, "materials").get(m).getAsJsonObject();
    }

    String materialName(int m) {
        return string(material(m), "name", "material" + m);
    }

    boolean translucent(int m) {
        String mode = string(material(m), "alphaMode", "OPAQUE");
        return mode.equals("BLEND") || mode.equals("MASK");
    }

    boolean doubleSided(int m) {
        JsonElement e = material(m).get("doubleSided");
        return e != null && e.getAsBoolean();
    }

    private JsonObject baseColorTexture(int m) {
        return object(object(material(m), "pbrMetallicRoughness"), "baseColorTexture");
    }

    int texCoord(int m) {
        return integer(baseColorTexture(m), "texCoord", 0);
    }

    float[] baseColorFactor(int m) {
        JsonArray f = array(object(material(m), "pbrMetallicRoughness"), "baseColorFactor");
        return f.size() != 4 ? new float[]{1.0f, 1.0f, 1.0f, 1.0f} : new float[]{f.get(0).getAsFloat(), f.get(1).getAsFloat(), f.get(2).getAsFloat(), f.get(3).getAsFloat()};
    }

    byte[] baseColorImage(int m) throws IOException {
        JsonObject tex = baseColorTexture(m);
        if (!tex.has("index")) {
            return null;
        }
        JsonObject texture = array(this.json, "textures").get(tex.get("index").getAsInt()).getAsJsonObject();
        int source = integer(texture, "source", -1);
        if (source < 0) {
            return null;
        }
        JsonObject image = array(this.json, "images").get(source).getAsJsonObject();
        if (image.has("bufferView")) {
            ByteBuffer b = view(image.get("bufferView").getAsInt(), 0);
            byte[] out = new byte[b.remaining()];
            b.get(out);
            return out;
        }
        String uri = string(image, "uri", "");
        if (uri.startsWith("data:")) {
            return Base64.getDecoder().decode(uri.substring(uri.indexOf(44) + 1));
        }
        throw new IOException("its textures are separate files, export it as a single .glb");
    }

    float[] floats(int accessor) throws IOException {
        float fMax;
        JsonObject a = accessor(accessor);
        int count = a.get("count").getAsInt();
        int comps = components(a.get("type").getAsString());
        int type = a.get("componentType").getAsInt();
        JsonElement norm = a.get("normalized");
        boolean normalized = norm != null && norm.getAsBoolean();
        float[] out = new float[count * comps];
        if (!a.has("bufferView")) {
            return out;
        }
        ByteBuffer b = view(a.get("bufferView").getAsInt(), integer(a, "byteOffset", 0));
        int stride = stride(a, comps, type);
        int size = size(type);
        for (int i = 0; i < count; i++) {
            for (int c = 0; c < comps; c++) {
                int at = (i * stride) + (c * size);
                int i2 = (i * comps) + c;
                switch (type) {
                    case 5120:
                        fMax = normalized ? Math.max(b.get(at) / 127.0f, -1.0f) : b.get(at);
                        break;
                    case 5121:
                        fMax = (b.get(at) & 255) / (normalized ? 255.0f : 1.0f);
                        break;
                    case 5122:
                        fMax = normalized ? Math.max(b.getShort(at) / 32767.0f, -1.0f) : b.getShort(at);
                        break;
                    case 5123:
                        fMax = (b.getShort(at) & 65535) / (normalized ? 65535.0f : 1.0f);
                        break;
                    case 5124:
                    default:
                        throw new IOException("unknown component type " + type);
                    case 5125:
                        fMax = b.getInt(at);
                        break;
                    case 5126:
                        fMax = b.getFloat(at);
                        break;
                }
                out[i2] = fMax;
            }
        }
        return out;
    }

    int[] ints(int accessor) throws IOException {
        int i;
        JsonObject a = accessor(accessor);
        int count = a.get("count").getAsInt();
        int comps = components(a.get("type").getAsString());
        int type = a.get("componentType").getAsInt();
        ByteBuffer b = view(a.get("bufferView").getAsInt(), integer(a, "byteOffset", 0));
        int stride = stride(a, comps, type);
        int size = size(type);
        int[] out = new int[count * comps];
        for (int i2 = 0; i2 < count; i2++) {
            for (int c = 0; c < comps; c++) {
                int at = (i2 * stride) + (c * size);
                int i3 = (i2 * comps) + c;
                switch (type) {
                    case 5121:
                        i = b.get(at) & 255;
                        break;
                    case 5122:
                    case 5124:
                    default:
                        throw new IOException("unknown index type " + type);
                    case 5123:
                        i = b.getShort(at) & 65535;
                        break;
                    case 5125:
                        i = b.getInt(at);
                        break;
                }
                out[i3] = i;
            }
        }
        return out;
    }

    private JsonObject accessor(int index) throws IOException {
        JsonObject a = array(this.json, "accessors").get(index).getAsJsonObject();
        if (a.has("sparse")) {
            throw new IOException("sparse accessors aren't supported");
        }
        long elements = ((long) a.get("count").getAsInt()) * ((long) components(a.get("type").getAsString()));
        if (elements < 0 || elements > 33554432) {
            throw new IOException("a mesh is far too large");
        }
        return a;
    }

    private ByteBuffer view(int index, int extra) throws IOException {
        JsonObject v = array(this.json, "bufferViews").get(index).getAsJsonObject();
        if (integer(v, "buffer", 0) != 0) {
            throw new IOException("its data is split over several files");
        }
        int off = integer(v, "byteOffset", 0) + extra;
        int length = v.get("byteLength").getAsInt() - extra;
        if (off < 0 || length < 0 || off + length > this.bin.capacity()) {
            throw new IOException("the file is cut short");
        }
        return this.bin.slice(off, length).order(ByteOrder.LITTLE_ENDIAN);
    }

    private int stride(JsonObject a, int comps, int type) {
        JsonObject v = array(this.json, "bufferViews").get(a.get("bufferView").getAsInt()).getAsJsonObject();
        return integer(v, "byteStride", comps * size(type));
    }

    private static int size(int type) {
        switch (type) {
            case 5120:
            case 5121:
                return 1;
            case 5122:
            case 5123:
                return 2;
            default:
                return 4;
        }
    }

    private static int components(String type) throws IOException {
        switch (type) {
            case "SCALAR":
                return 1;
            case "VEC2":
                return 2;
            case "VEC3":
                return 3;
            case "VEC4":
                return 4;
            case "MAT4":
                return 16;
            default:
                throw new IOException("unknown accessor type " + type);
        }
    }
}
