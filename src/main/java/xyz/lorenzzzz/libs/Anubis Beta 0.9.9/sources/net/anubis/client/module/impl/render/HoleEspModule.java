package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.anubis.client.esp.EspStyle;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HoleEspModule.class */
@Environment(EnvType.CLIENT)
public class HoleEspModule extends ClientModule {
    private static final String MODE_ALL = "All";
    private static final String MODE_HOLES_TUNNELS = "Holes + Tunnels";
    private static final String MODE_HOLES_STAIRS = "Holes + Stairs";
    private static final String MODE_TUNNELS_STAIRS = "Tunnels + Stairs";
    private static final String MODE_HOLES = "Holes";
    private static final String MODE_TUNNELS = "Tunnels";
    private static final String MODE_STAIRS = "Stairs";
    private static final int TYPE_HOLE = 0;
    private static final int TYPE_HOLE_3X1 = 1;
    private static final int TYPE_TUNNEL = 2;
    private static final int TYPE_STAIR = 3;
    private static final int MAX_HITS_PER_CHUNK = 1024;
    private static final int MAX_RENDER_BOXES = 8192;
    private static final int HOLE_GRID_W = 20;
    private static final int ELYTRA_REFRESH_TICKS = 5;
    private static final int NORMAL_REFRESH_TICKS = 20;
    private static final float ELYTRA_SCAN_RADIUS = 12.0f;
    private static final float ELYTRA_SCAN_BUDGET_MS = 8.0f;
    private static final int[][] HORIZONTAL_DIRS = {new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{0, 1}};
    private static final ThreadLocal<class_2338.class_2339> PASSABLE_POS = ThreadLocal.withInitial(class_2338.class_2339::new);
    private final ConcurrentHashMap<Long, List<Hit>> chunkCache;
    private final Queue<Long> scanQueue;
    private final Set<Long> queued;
    private long lastPlayerChunk;
    private int refreshTicks;
    private final ModeSetting sMode;
    private final NumberSetting sScanRadius;
    private final NumberSetting sChunksPerTick;
    private final NumberSetting sScanBudgetMs;
    private final BooleanSetting sAirOnly;
    private final NumberSetting sMinYOffset;
    private final NumberSetting sMaxYOffset;
    private final NumberSetting sMinHoleDepth;
    private final NumberSetting sMinTunnelLength;
    private final NumberSetting sMinTunnelHeight;
    private final NumberSetting sMaxTunnelHeight;
    private final NumberSetting sMinStairLength;
    private final NumberSetting sMinStairHeight;
    private final NumberSetting sMaxStairHeight;
    private final BooleanSetting sFill;
    private final BooleanSetting sOutline;
    private final NumberSetting sLineWidth;
    private final ColorSetting sHoleLine;
    private final ColorSetting sHoleFill;
    private final ColorSetting sHole3Line;
    private final ColorSetting sHole3Fill;
    private final ColorSetting sTunnelLine;
    private final ColorSetting sTunnelFill;
    private final ColorSetting sStairLine;
    private final ColorSetting sStairFill;

    public HoleEspModule() {
        super("hole-esp", "Hole ESP", "Highlights holes, tunnels, and staircases.", ModuleCategory.RENDER);
        this.chunkCache = new ConcurrentHashMap<>();
        this.scanQueue = new ConcurrentLinkedQueue();
        this.queued = ConcurrentHashMap.newKeySet();
        this.lastPlayerChunk = Long.MIN_VALUE;
        this.refreshTicks = 0;
        this.sMode = (ModeSetting) addSetting("detection", new ModeSetting("Detection", MODE_HOLES, MODE_HOLES, MODE_ALL, MODE_HOLES_TUNNELS, MODE_HOLES_STAIRS, MODE_TUNNELS_STAIRS, MODE_TUNNELS, MODE_STAIRS));
        this.sScanRadius = (NumberSetting) addSetting("scan-radius", new NumberSetting("Scan Radius", ELYTRA_SCAN_BUDGET_MS, 1.0f, 24.0f).step(1.0f));
        this.sChunksPerTick = (NumberSetting) addSetting("chunks-tick", new NumberSetting("Chunks/Tick", 64.0f, 1.0f, 100.0f).step(1.0f));
        this.sScanBudgetMs = (NumberSetting) addSetting("scan-budget-ms", new NumberSetting("Scan Budget ms", 3.0f, 1.0f, 10.0f).step(1.0f));
        this.sAirOnly = (BooleanSetting) addSetting("only-air-blocks", new BooleanSetting("Only Air Blocks", true));
        this.sMinYOffset = (NumberSetting) addSetting("min-y-offset", new NumberSetting("Min Y Offset", 0.0f, 0.0f, 319.0f).step(1.0f));
        this.sMaxYOffset = (NumberSetting) addSetting("max-y-offset", new NumberSetting("Max Y Offset", 0.0f, 0.0f, 319.0f).step(1.0f));
        this.sMinHoleDepth = (NumberSetting) addSetting("min-hole-depth", new NumberSetting("Min Hole Depth", 4.0f, 1.0f, 20.0f).step(1.0f));
        this.sMinTunnelLength = (NumberSetting) addSetting("min-tunnel-length", new NumberSetting("Min Tunnel Length", 3.0f, 1.0f, 20.0f).step(1.0f));
        this.sMinTunnelHeight = (NumberSetting) addSetting("min-tunnel-height", new NumberSetting("Min Tunnel Height", 2.0f, 1.0f, 10.0f).step(1.0f));
        this.sMaxTunnelHeight = (NumberSetting) addSetting("max-tunnel-height", new NumberSetting("Max Tunnel Height", 3.0f, 2.0f, 10.0f).step(1.0f));
        this.sMinStairLength = (NumberSetting) addSetting("min-stair-length", new NumberSetting("Min Stair Length", 3.0f, 1.0f, 20.0f).step(1.0f));
        this.sMinStairHeight = (NumberSetting) addSetting("min-stair-height", new NumberSetting("Min Stair Height", 3.0f, 2.0f, 10.0f).step(1.0f));
        this.sMaxStairHeight = (NumberSetting) addSetting("max-stair-height", new NumberSetting("Max Stair Height", 5.0f, 2.0f, 10.0f).step(1.0f));
        this.sFill = (BooleanSetting) addSetting("fill", new BooleanSetting("Fill", true));
        this.sOutline = (BooleanSetting) addSetting("outline", new BooleanSetting(EspStyle.OUTLINE, true));
        this.sLineWidth = (NumberSetting) addSetting("line-width", new NumberSetting("Line Width", 1.5f, 0.5f, 5.0f).step(0.5f));
        this.sHoleLine = (ColorSetting) addSetting("1x1-line", new ColorSetting("1x1 Line", 1291845631));
        this.sHoleFill = (ColorSetting) addSetting("1x1-fill", new ColorSetting("1x1 Fill", 1342177279));
        this.sHole3Line = (ColorSetting) addSetting("3x1-line", new ColorSetting("3x1 Line", 644245094));
        this.sHole3Fill = (ColorSetting) addSetting("3x1-fill", new ColorSetting("3x1 Fill", 644245094));
        this.sTunnelLine = (ColorSetting) addSetting("tunnel-line", new ColorSetting("Tunnel Line", 1593835775));
        this.sTunnelFill = (ColorSetting) addSetting("tunnel-fill", new ColorSetting("Tunnel Fill", 503316735));
        this.sStairLine = (ColorSetting) addSetting("stair-line", new ColorSetting("Stair Line", 1610547455));
        this.sStairFill = (ColorSetting) addSetting("stair-fill", new ColorSetting("Stair Fill", 520028415));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.lastPlayerChunk = Long.MIN_VALUE;
        this.refreshTicks = 0;
        this.chunkCache.clear();
        this.scanQueue.clear();
        this.queued.clear();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.chunkCache.clear();
        this.scanQueue.clear();
        this.queued.clear();
        this.lastPlayerChunk = Long.MIN_VALUE;
        this.refreshTicks = 0;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        long jRound;
        float fValue;
        Long key;
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        int cx = mc.field_1724.method_31477() >> 4;
        int cz = mc.field_1724.method_31479() >> 4;
        long curChunk = class_1923.method_8331(cx, cz);
        int radius = scanRadius(mc);
        boolean fastTravel = isFastTravel(mc);
        int i = this.refreshTicks + 1;
        this.refreshTicks = i;
        boolean periodicRefresh = i >= (fastTravel ? 5 : 20);
        if (curChunk != this.lastPlayerChunk || periodicRefresh) {
            this.refreshTicks = 0;
            this.lastPlayerChunk = curChunk;
            this.chunkCache.keySet().removeIf(key2 -> {
                int kx = chunkX(key2.longValue());
                int kz = chunkZ(key2.longValue());
                return Math.abs(kx - cx) > radius || Math.abs(kz - cz) > radius;
            });
            List<long[]> toQueue = new ArrayList<>();
            class_243 velocity = mc.field_1724.method_18798();
            double vx = velocity.field_1352;
            double vz = velocity.field_1350;
            double horizontalSpeed = Math.sqrt((vx * vx) + (vz * vz));
            if (horizontalSpeed > 1.0E-4d) {
                vx /= horizontalSpeed;
                vz /= horizontalSpeed;
            }
            for (int x = cx - radius; x <= cx + radius; x++) {
                for (int z = cz - radius; z <= cz + radius; z++) {
                    long key3 = class_1923.method_8331(x, z);
                    if (!this.chunkCache.containsKey(Long.valueOf(key3)) && !this.queued.contains(Long.valueOf(key3)) && mc.field_1687.method_2935().method_12123(x, z)) {
                        int dx = x - cx;
                        int dz = z - cz;
                        long distScore = ((long) ((dx * dx) + (dz * dz))) * 1024;
                        if (fastTravel) {
                            jRound = Math.round(((((double) dx) * vx) + (((double) dz) * vz)) * 1536.0d);
                        } else {
                            jRound = 0;
                        }
                        long aheadScore = jRound;
                        toQueue.add(new long[]{key3, distScore - aheadScore});
                    }
                }
            }
            toQueue.sort(Comparator.comparingLong(a -> {
                return a[1];
            }));
            for (long[] entry : toQueue) {
                this.scanQueue.add(Long.valueOf(entry[0]));
                this.queued.add(Long.valueOf(entry[0]));
            }
        }
        int chunksPerTick = Math.max(1, Math.round(value(this.sChunksPerTick, 4.0f)));
        if (fastTravel) {
            fValue = Math.max(value(this.sScanBudgetMs, 3.0f), ELYTRA_SCAN_BUDGET_MS);
        } else {
            fValue = value(this.sScanBudgetMs, 3.0f);
        }
        float budgetMs = fValue;
        long deadline = System.nanoTime() + ((long) (Math.max(1.0f, budgetMs) * 1000000.0f));
        for (int i2 = 0; i2 < chunksPerTick && System.nanoTime() < deadline && (key = this.scanQueue.poll()) != null; i2++) {
            this.queued.remove(key);
            int sx = chunkX(key.longValue());
            int sz = chunkZ(key.longValue());
            if (mc.field_1687.method_2935().method_12123(sx, sz)) {
                scanChunk(mc.field_1687, sx, sz, key.longValue());
            }
        }
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (event.pos == null) {
            return;
        }
        long key = class_1923.method_8331(event.pos.method_10263() >> 4, event.pos.method_10260() >> 4);
        this.chunkCache.remove(Long.valueOf(key));
        if (this.queued.add(Long.valueOf(key))) {
            this.scanQueue.add(Long.valueOf(key));
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        class_12179 class_12179VarMethod_75539;
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        boolean fill = this.sFill == null || this.sFill.value().get();
        boolean outline = this.sOutline == null || this.sOutline.value().get();
        if (fill || outline) {
            int px = mc.field_1724.method_31477() >> 4;
            int pz = mc.field_1724.method_31479() >> 4;
            int radius = scanRadius(mc);
            float lineWidth = value(this.sLineWidth, 1.5f);
            int rendered = 0;
            for (Map.Entry<Long, List<Hit>> entry : this.chunkCache.entrySet()) {
                long key = entry.getKey().longValue();
                if (Math.abs(chunkX(key) - px) <= radius && Math.abs(chunkZ(key) - pz) <= radius) {
                    for (Hit hit : entry.getValue()) {
                        rendered++;
                        if (rendered > 8192) {
                            return;
                        }
                        if (fill) {
                            class_12179VarMethod_75539 = !outline ? class_12179.method_75539(fillColor(hit.type())) : class_12179.method_75537(lineColor(hit.type()), lineWidth, fillColor(hit.type()));
                        } else {
                            class_12179VarMethod_75539 = class_12179.method_75536(lineColor(hit.type()), lineWidth);
                        }
                        class_12179 style = class_12179VarMethod_75539;
                        class_12180.method_75541(hit.box(), style).method_75533();
                    }
                }
            }
        }
    }

    private void scanChunk(class_638 level, int chunkX, int chunkZ, long chunkKey) {
        class_2818 chunk = level.method_2935().method_21730(chunkX, chunkZ);
        if (chunk == null) {
            return;
        }
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        int minBuildY = level.method_8597().comp_651();
        int maxBuildY = minBuildY + (chunk.method_12006().length * 16);
        int minY = Math.min(maxBuildY - 1, minBuildY + Math.round(value(this.sMinYOffset, 0.0f)));
        int maxY = Math.max(minY + 1, maxBuildY - Math.round(value(this.sMaxYOffset, 0.0f)));
        Mode mode = currentMode();
        boolean airOnly = this.sAirOnly != null && this.sAirOnly.value().get();
        if (mode.holes() && !mode.tunnels() && !mode.stairs()) {
            scanHolesFast(level, chunkKey, baseX, baseZ, minY, maxY, airOnly);
            return;
        }
        List<Hit> hits = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        class_2338.class_2339 pos = new class_2338.class_2339();
        class_2826[] sections = chunk.method_12006();
        for (int si = 0; si < sections.length; si++) {
            class_2826 section = sections[si];
            if (section != null && !section.method_38292()) {
                int sectionBaseY = minBuildY + (si * 16);
                for (int ly = 0; ly < 16; ly++) {
                    int y = sectionBaseY + ly;
                    if (y > minY && y < maxY) {
                        for (int z = 0; z < 16; z++) {
                            for (int x = 0; x < 16; x++) {
                                int wx = baseX + x;
                                int wz = baseZ + z;
                                pos.method_10103(wx, y, wz);
                                if (isPassable(level, pos, airOnly)) {
                                    if (mode.holes() && !isPassable(level, wx, y - 1, wz, airOnly)) {
                                        checkHole(level, pos, airOnly, hits, seen);
                                        check3x1Hole(level, pos, airOnly, hits, seen);
                                    }
                                    if (mode.tunnels()) {
                                        checkTunnelStarts(level, pos, airOnly, hits, seen);
                                    }
                                    if (mode.stairs()) {
                                        checkStaircaseStarts(level, pos, airOnly, hits, seen);
                                    }
                                    if (hits.size() >= 1024) {
                                        this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (hits.isEmpty()) {
            this.chunkCache.remove(Long.valueOf(chunkKey));
        } else {
            this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
        }
    }

    private void scanHolesFast(class_638 level, long chunkKey, int baseX, int baseZ, int minY, int maxY, boolean airOnly) {
        int gridMinY = minY - 1;
        int yCount = (maxY - gridMinY) + 1;
        if (yCount <= 1) {
            this.chunkCache.remove(Long.valueOf(chunkKey));
            return;
        }
        boolean[] passable = new boolean[yCount * 20 * 20];
        class_2338.class_2339 pos = new class_2338.class_2339();
        for (int yi = 0; yi < yCount; yi++) {
            int y = gridMinY + yi;
            int yBase = yi * 20 * 20;
            for (int lz = -1; lz <= 18; lz++) {
                int zBase = yBase + ((lz + 1) * 20);
                for (int lx = -1; lx <= 18; lx++) {
                    pos.method_10103(baseX + lx, y, baseZ + lz);
                    passable[zBase + lx + 1] = isPassable(level, pos, airOnly);
                }
            }
        }
        int minDepth = Math.round(value(this.sMinHoleDepth, 4.0f));
        List<Hit> hits = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (int lz2 = 0; lz2 < 16; lz2++) {
            for (int lx2 = 0; lx2 < 16; lx2++) {
                int y2 = minY;
                while (y2 < maxY) {
                    if (!grid(passable, y2, lx2, lz2, gridMinY) || grid(passable, y2 - 1, lx2, lz2, gridMinY)) {
                        y2++;
                    } else {
                        int depth = count1x1Depth(passable, y2, lx2, lz2, gridMinY, maxY);
                        if (depth >= minDepth) {
                            addHit(hits, seen, new class_238(baseX + lx2, y2, baseZ + lz2, baseX + lx2 + 1, y2 + depth, baseZ + lz2 + 1), 0);
                            if (hits.size() >= 1024) {
                                this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
                                return;
                            }
                        }
                        int depthX = count3x1DepthX(passable, y2, lx2, lz2, gridMinY, maxY);
                        if (depthX >= minDepth) {
                            addHit(hits, seen, new class_238(baseX + lx2, y2, baseZ + lz2, baseX + lx2 + 3, y2 + depthX, baseZ + lz2 + 1), 1);
                            if (hits.size() >= 1024) {
                                this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
                                return;
                            }
                        }
                        int depthZ = count3x1DepthZ(passable, y2, lx2, lz2, gridMinY, maxY);
                        if (depthZ >= minDepth) {
                            addHit(hits, seen, new class_238(baseX + lx2, y2, baseZ + lz2, baseX + lx2 + 1, y2 + depthZ, baseZ + lz2 + 3), 1);
                            if (hits.size() >= 1024) {
                                this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
                                return;
                            }
                        }
                        y2 += Math.max(1, Math.max(depth, Math.max(depthX, depthZ)));
                    }
                }
            }
        }
        if (hits.isEmpty()) {
            this.chunkCache.remove(Long.valueOf(chunkKey));
        } else {
            this.chunkCache.put(Long.valueOf(chunkKey), List.copyOf(hits));
        }
    }

    private static int count1x1Depth(boolean[] grid, int startY, int lx, int lz, int gridMinY, int maxY) {
        int y = startY;
        while (y < maxY && grid(grid, y, lx, lz, gridMinY) && !grid(grid, y, lx - 1, lz, gridMinY) && !grid(grid, y, lx + 1, lz, gridMinY) && !grid(grid, y, lx, lz - 1, gridMinY) && !grid(grid, y, lx, lz + 1, gridMinY)) {
            y++;
        }
        return y - startY;
    }

    private static int count3x1DepthX(boolean[] grid, int startY, int lx, int lz, int gridMinY, int maxY) {
        int y = startY;
        while (y < maxY && grid(grid, y, lx, lz, gridMinY) && grid(grid, y, lx + 1, lz, gridMinY) && grid(grid, y, lx + 2, lz, gridMinY) && !grid(grid, y, lx - 1, lz, gridMinY) && !grid(grid, y, lx + 3, lz, gridMinY) && !grid(grid, y, lx, lz - 1, gridMinY) && !grid(grid, y, lx, lz + 1, gridMinY) && !grid(grid, y, lx + 1, lz - 1, gridMinY) && !grid(grid, y, lx + 1, lz + 1, gridMinY) && !grid(grid, y, lx + 2, lz - 1, gridMinY) && !grid(grid, y, lx + 2, lz + 1, gridMinY)) {
            y++;
        }
        return y - startY;
    }

    private static int count3x1DepthZ(boolean[] grid, int startY, int lx, int lz, int gridMinY, int maxY) {
        int y = startY;
        while (y < maxY && grid(grid, y, lx, lz, gridMinY) && grid(grid, y, lx, lz + 1, gridMinY) && grid(grid, y, lx, lz + 2, gridMinY) && !grid(grid, y, lx, lz - 1, gridMinY) && !grid(grid, y, lx, lz + 3, gridMinY) && !grid(grid, y, lx - 1, lz, gridMinY) && !grid(grid, y, lx + 1, lz, gridMinY) && !grid(grid, y, lx - 1, lz + 1, gridMinY) && !grid(grid, y, lx + 1, lz + 1, gridMinY) && !grid(grid, y, lx - 1, lz + 2, gridMinY) && !grid(grid, y, lx + 1, lz + 2, gridMinY)) {
            y++;
        }
        return y - startY;
    }

    private static boolean grid(boolean[] grid, int y, int lx, int lz, int gridMinY) {
        int yi = y - gridMinY;
        return grid[(yi * 20 * 20) + ((lz + 1) * 20) + lx + 1];
    }

    private void checkHole(class_638 level, class_2338 pos, boolean airOnly, List<Hit> hits, Set<String> seen) {
        if (isValidHoleSection(level, pos, airOnly)) {
            class_2338.class_2339 current = new class_2338.class_2339(pos.method_10263(), pos.method_10264(), pos.method_10260());
            while (isValidHoleSection(level, current, airOnly)) {
                current.method_10100(0, 1, 0);
            }
            int depth = current.method_10264() - pos.method_10264();
            if (depth >= Math.round(value(this.sMinHoleDepth, 4.0f))) {
                addHit(hits, seen, new class_238(pos.method_10263(), pos.method_10264(), pos.method_10260(), pos.method_10263() + 1, current.method_10264(), pos.method_10260() + 1), 0);
            }
        }
    }

    private void check3x1Hole(class_638 level, class_2338 pos, boolean airOnly, List<Hit> hits, Set<String> seen) {
        if (isValid3x1HoleSectionX(level, pos, airOnly)) {
            class_2338.class_2339 current = new class_2338.class_2339(pos.method_10263(), pos.method_10264(), pos.method_10260());
            while (isValid3x1HoleSectionX(level, current, airOnly)) {
                current.method_10100(0, 1, 0);
            }
            int depth = current.method_10264() - pos.method_10264();
            if (depth >= Math.round(value(this.sMinHoleDepth, 4.0f))) {
                addHit(hits, seen, new class_238(pos.method_10263(), pos.method_10264(), pos.method_10260(), pos.method_10263() + 3, current.method_10264(), pos.method_10260() + 1), 1);
            }
        }
        if (isValid3x1HoleSectionZ(level, pos, airOnly)) {
            class_2338.class_2339 current2 = new class_2338.class_2339(pos.method_10263(), pos.method_10264(), pos.method_10260());
            while (isValid3x1HoleSectionZ(level, current2, airOnly)) {
                current2.method_10100(0, 1, 0);
            }
            int depth2 = current2.method_10264() - pos.method_10264();
            if (depth2 >= Math.round(value(this.sMinHoleDepth, 4.0f))) {
                addHit(hits, seen, new class_238(pos.method_10263(), pos.method_10264(), pos.method_10260(), pos.method_10263() + 1, current2.method_10264(), pos.method_10260() + 3), 1);
            }
        }
    }

    private void checkTunnelStarts(class_638 level, class_2338 pos, boolean airOnly, List<Hit> hits, Set<String> seen) {
        int minLength = Math.round(value(this.sMinTunnelLength, 3.0f));
        for (int[] dir : HORIZONTAL_DIRS) {
            class_2338.class_2339 previous = new class_2338.class_2339(pos.method_10263() - dir[0], pos.method_10264(), pos.method_10260() - dir[1]);
            if (!isTunnelSection(level, previous, dir, airOnly)) {
                class_2338.class_2339 current = new class_2338.class_2339(pos.method_10263(), pos.method_10264(), pos.method_10260());
                class_2338 start = null;
                class_2338 end = null;
                int steps = 0;
                int maxHeight = 0;
                if (isTunnelSection(level, current, dir, airOnly)) {
                    start = current.method_10062();
                }
                while (isTunnelSection(level, current, dir, airOnly)) {
                    maxHeight = Math.max(maxHeight, getTunnelHeight(level, current, airOnly));
                    end = current.method_10062();
                    current.method_10100(dir[0], 0, dir[1]);
                    steps++;
                }
                if (start != null && end != null && steps >= minLength) {
                    addHit(hits, seen, new class_238(Math.min(start.method_10263(), end.method_10263()), start.method_10264(), Math.min(start.method_10260(), end.method_10260()), Math.max(start.method_10263(), end.method_10263()) + 1, start.method_10264() + maxHeight, Math.max(start.method_10260(), end.method_10260()) + 1), 2);
                }
            }
        }
    }

    private void checkStaircaseStarts(class_638 level, class_2338 pos, boolean airOnly, List<Hit> hits, Set<String> seen) {
        int minLength = Math.round(value(this.sMinStairLength, 3.0f));
        for (int[] dir : HORIZONTAL_DIRS) {
            class_2338.class_2339 previous = new class_2338.class_2339(pos.method_10263() - dir[0], pos.method_10264() - 1, pos.method_10260() - dir[1]);
            if (!isStaircaseSection(level, previous, dir, airOnly)) {
                class_2338.class_2339 current = new class_2338.class_2339(pos.method_10263(), pos.method_10264(), pos.method_10260());
                int steps = 0;
                List<class_238> boxes = new ArrayList<>();
                while (isStaircaseSection(level, current, dir, airOnly)) {
                    int height = getStaircaseHeight(level, current, airOnly);
                    boxes.add(new class_238(current.method_10263(), current.method_10264(), current.method_10260(), current.method_10263() + 1, current.method_10264() + height, current.method_10260() + 1));
                    current.method_10100(dir[0], 1, dir[1]);
                    steps++;
                }
                if (steps >= minLength) {
                    for (class_238 box : boxes) {
                        addHit(hits, seen, box, 3);
                    }
                }
            }
        }
    }

    private boolean isValidHoleSection(class_638 level, class_2338 pos, boolean airOnly) {
        return (!isPassable(level, pos, airOnly) || isPassable(level, pos.method_10263(), pos.method_10264(), pos.method_10260() - 1, airOnly) || isPassable(level, pos.method_10263(), pos.method_10264(), pos.method_10260() + 1, airOnly) || isPassable(level, pos.method_10263() + 1, pos.method_10264(), pos.method_10260(), airOnly) || isPassable(level, pos.method_10263() - 1, pos.method_10264(), pos.method_10260(), airOnly)) ? false : true;
    }

    private boolean isValid3x1HoleSectionX(class_638 level, class_2338 pos, boolean airOnly) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        return (!isPassable(level, x, y, z, airOnly) || !isPassable(level, x + 1, y, z, airOnly) || !isPassable(level, x + 2, y, z, airOnly) || isPassable(level, x, y, z - 1, airOnly) || isPassable(level, x, y, z + 1, airOnly) || isPassable(level, x + 1, y, z - 1, airOnly) || isPassable(level, x + 1, y, z + 1, airOnly) || isPassable(level, x + 2, y, z - 1, airOnly) || isPassable(level, x + 2, y, z + 1, airOnly) || isPassable(level, x - 1, y, z, airOnly) || isPassable(level, x + 3, y, z, airOnly)) ? false : true;
    }

    private boolean isValid3x1HoleSectionZ(class_638 level, class_2338 pos, boolean airOnly) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        return (!isPassable(level, x, y, z, airOnly) || !isPassable(level, x, y, z + 1, airOnly) || !isPassable(level, x, y, z + 2, airOnly) || isPassable(level, x - 1, y, z, airOnly) || isPassable(level, x + 1, y, z, airOnly) || isPassable(level, x - 1, y, z + 1, airOnly) || isPassable(level, x + 1, y, z + 1, airOnly) || isPassable(level, x - 1, y, z + 2, airOnly) || isPassable(level, x + 1, y, z + 2, airOnly) || isPassable(level, x, y, z - 1, airOnly) || isPassable(level, x, y, z + 3, airOnly)) ? false : true;
    }

    private boolean isTunnelSection(class_638 level, class_2338 pos, int[] dir, boolean airOnly) {
        int height = getTunnelHeight(level, pos, airOnly);
        if (height < Math.round(value(this.sMinTunnelHeight, 2.0f)) || height > Math.round(value(this.sMaxTunnelHeight, 3.0f)) || isPassable(level, pos.method_10263(), pos.method_10264() - 1, pos.method_10260(), airOnly) || isPassable(level, pos.method_10263(), pos.method_10264() + height, pos.method_10260(), airOnly)) {
            return false;
        }
        boolean alongX = dir[0] != 0;
        for (int i = 0; i < height; i++) {
            int y = pos.method_10264() + i;
            if (alongX) {
                if (isPassable(level, pos.method_10263(), y, pos.method_10260() - 1, airOnly) || isPassable(level, pos.method_10263(), y, pos.method_10260() + 1, airOnly)) {
                    return false;
                }
            } else if (isPassable(level, pos.method_10263() - 1, y, pos.method_10260(), airOnly) || isPassable(level, pos.method_10263() + 1, y, pos.method_10260(), airOnly)) {
                return false;
            }
        }
        return true;
    }

    private boolean isStaircaseSection(class_638 level, class_2338 pos, int[] dir, boolean airOnly) {
        int height = getStaircaseHeight(level, pos, airOnly);
        if (height < Math.round(value(this.sMinStairHeight, 3.0f)) || height > Math.round(value(this.sMaxStairHeight, 5.0f)) || isPassable(level, pos.method_10263(), pos.method_10264() - 1, pos.method_10260(), airOnly) || isPassable(level, pos.method_10263(), pos.method_10264() + height, pos.method_10260(), airOnly)) {
            return false;
        }
        boolean alongX = dir[0] != 0;
        for (int i = 0; i < height; i++) {
            int y = pos.method_10264() + i;
            if (alongX) {
                if (isPassable(level, pos.method_10263(), y, pos.method_10260() - 1, airOnly) || isPassable(level, pos.method_10263(), y, pos.method_10260() + 1, airOnly)) {
                    return false;
                }
            } else if (isPassable(level, pos.method_10263() - 1, y, pos.method_10260(), airOnly) || isPassable(level, pos.method_10263() + 1, y, pos.method_10260(), airOnly)) {
                return false;
            }
        }
        return true;
    }

    private int getTunnelHeight(class_638 level, class_2338 pos, boolean airOnly) {
        int height = 0;
        int max = Math.round(value(this.sMaxTunnelHeight, 3.0f));
        while (height < max && isPassable(level, pos.method_10263(), pos.method_10264() + height, pos.method_10260(), airOnly)) {
            height++;
        }
        return height;
    }

    private int getStaircaseHeight(class_638 level, class_2338 pos, boolean airOnly) {
        int height = 0;
        int max = Math.round(value(this.sMaxStairHeight, 5.0f));
        while (height < max && isPassable(level, pos.method_10263(), pos.method_10264() + height, pos.method_10260(), airOnly)) {
            height++;
        }
        return height;
    }

    private static boolean isPassable(class_638 level, int x, int y, int z, boolean airOnly) {
        return isPassable(level, PASSABLE_POS.get().method_10103(x, y, z), airOnly);
    }

    private static boolean isPassable(class_638 level, class_2338 pos, boolean airOnly) {
        class_2680 state = level.method_8320(pos);
        if (state.method_26215()) {
            return true;
        }
        if (airOnly) {
            return false;
        }
        class_265 shape = state.method_26220(level, pos);
        return shape.method_1110() || !class_259.method_1077().equals(shape);
    }

    private static void addHit(List<Hit> hits, Set<String> seen, class_238 box, int type) {
        double d = box.field_1323;
        double d2 = box.field_1322;
        double d3 = box.field_1321;
        double d4 = box.field_1320;
        double d5 = box.field_1325;
        double d6 = box.field_1324;
        String key = type + ":" + d + "," + type + "," + d2 + "," + type + "," + d3 + "," + type;
        if (seen.add(key)) {
            for (Hit hit : hits) {
                if (hit.type() == type && hit.box().method_994(box)) {
                    return;
                }
            }
            hits.add(new Hit(box, type));
        }
    }

    private Mode currentMode() {
        String mode = this.sMode == null ? MODE_ALL : this.sMode.value();
        switch (mode) {
            case "Holes + Tunnels":
                return new Mode(true, true, false);
            case "Holes + Stairs":
                return new Mode(true, false, true);
            case "Tunnels + Stairs":
                return new Mode(false, true, true);
            case "Holes":
                return new Mode(true, false, false);
            case "Tunnels":
                return new Mode(false, true, false);
            case "Stairs":
                return new Mode(false, false, true);
            default:
                return new Mode(true, true, true);
        }
    }

    private int lineColor(int type) {
        switch (type) {
            case 1:
                return color(this.sHole3Line, 1610589440);
            case 2:
                return color(this.sTunnelLine, 1593835775);
            case 3:
                return color(this.sStairLine, 1610547455);
            default:
                return color(this.sHoleLine, 1610547200);
        }
    }

    private int fillColor(int type) {
        switch (type) {
            case 1:
                return color(this.sHole3Fill, 520070400);
            case 2:
                return color(this.sTunnelFill, 503316735);
            case 3:
                return color(this.sStairFill, 520028415);
            default:
                return color(this.sHoleFill, 520028160);
        }
    }

    private static int chunkX(long key) {
        return (int) key;
    }

    private static int chunkZ(long key) {
        return (int) (key >> 32);
    }

    private int scanRadius(class_310 mc) {
        int renderDistance = ((Integer) mc.field_1690.method_42503().method_41753()).intValue();
        float configuredValue = value(this.sScanRadius, 4.0f);
        if (isFastTravel(mc)) {
            configuredValue = Math.max(configuredValue, ELYTRA_SCAN_RADIUS);
        }
        int configured = Math.max(1, Math.round(configuredValue));
        return Math.min(renderDistance, configured);
    }

    private static boolean isFastTravel(class_310 mc) {
        if (mc.field_1724 == null) {
            return false;
        }
        class_243 velocity = mc.field_1724.method_18798();
        double horizontalSpeedSq = (velocity.field_1352 * velocity.field_1352) + (velocity.field_1350 * velocity.field_1350);
        return mc.field_1724.method_6128() || horizontalSpeedSq > 0.18d;
    }

    private static float value(NumberSetting setting, float fallback) {
        return setting == null ? fallback : setting.value()[0];
    }

    private static int color(ColorSetting setting, int fallback) {
        return setting == null ? fallback : setting.argb();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HoleEspModule$Hit.class */
    @Environment(EnvType.CLIENT)
    private static final class Hit extends Record {
        private final class_238 box;
        private final int type;

        private Hit(class_238 box, int type) {
            this.box = box;
            this.type = type;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Hit.class), Hit.class, "box;type", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->type:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Hit.class), Hit.class, "box;type", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->type:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Hit.class, Object.class), Hit.class, "box;type", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Hit;->type:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_238 box() {
            return this.box;
        }

        public int type() {
            return this.type;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HoleEspModule$Mode.class */
    @Environment(EnvType.CLIENT)
    private static final class Mode extends Record {
        private final boolean holes;
        private final boolean tunnels;
        private final boolean stairs;

        private Mode(boolean holes, boolean tunnels, boolean stairs) {
            this.holes = holes;
            this.tunnels = tunnels;
            this.stairs = stairs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Mode.class), Mode.class, "holes;tunnels;stairs", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->holes:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->tunnels:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->stairs:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Mode.class), Mode.class, "holes;tunnels;stairs", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->holes:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->tunnels:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->stairs:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Mode.class, Object.class), Mode.class, "holes;tunnels;stairs", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->holes:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->tunnels:Z", "FIELD:Lnet/anubis/client/module/impl/render/HoleEspModule$Mode;->stairs:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public boolean holes() {
            return this.holes;
        }

        public boolean tunnels() {
            return this.tunnels;
        }

        public boolean stairs() {
            return this.stairs;
        }
    }
}
