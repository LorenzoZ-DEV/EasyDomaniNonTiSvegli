package net.anubis.client.gui;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/DonutRegionMapData.class */
@Environment(EnvType.CLIENT)
public final class DonutRegionMapData {
    public static final int MAP_SIZE = 9;
    private static final double REGION_SIZE = 50000.0d;
    private static final double MAP_OFFSET = 225000.0d;
    private static final String[] REGION_TYPE_NAMES = {"EU Central", "EU West", "NA East", "NA West", "Asia", "Oceania"};
    private static final int[] REGION_TYPE_COLORS = {-10236257, -10246656, -1397425, -4559313, -12401931, -16545540};
    private static final Map<Integer, RegionInfo> REGION_MAP = buildRegionMap();

    private DonutRegionMapData() {
    }

    public static RegionInfo regionInfo(int index) {
        return REGION_MAP.get(Integer.valueOf(index));
    }

    public static int regionColor(int type) {
        if (type < 0 || type >= REGION_TYPE_COLORS.length) {
            return AnubisTheme.HUD_LABEL;
        }
        return REGION_TYPE_COLORS[type];
    }

    public static String[] regionTypeNames() {
        return REGION_TYPE_NAMES;
    }

    public static int[] regionTypeColors() {
        return REGION_TYPE_COLORS;
    }

    public static int regionAt(double x, double z) {
        RegionInfo info;
        int[] grid = worldToGrid(x, z);
        if (grid[0] < 0 || grid[0] >= 9 || grid[1] < 0 || grid[1] >= 9 || (info = REGION_MAP.get(Integer.valueOf((grid[1] * 9) + grid[0]))) == null) {
            return -1;
        }
        return info.regionId;
    }

    public static String regionTypeName(double x, double z) {
        RegionInfo info;
        int[] grid = worldToGrid(x, z);
        return (grid[0] < 0 || grid[0] >= 9 || grid[1] < 0 || grid[1] >= 9 || (info = REGION_MAP.get(Integer.valueOf((grid[1] * 9) + grid[0]))) == null) ? "Unknown" : REGION_TYPE_NAMES[info.regionType];
    }

    public static int[] worldToGrid(double x, double z) {
        return new int[]{(int) ((x + MAP_OFFSET) / REGION_SIZE), (int) ((z + MAP_OFFSET) / REGION_SIZE)};
    }

    public static double[] worldToCellPosition(double x, double z) {
        return new double[]{positiveModulo(x + MAP_OFFSET, REGION_SIZE) / REGION_SIZE, positiveModulo(z + MAP_OFFSET, REGION_SIZE) / REGION_SIZE};
    }

    private static double positiveModulo(double value, double divisor) {
        double mod = value % divisor;
        return mod < 0.0d ? mod + divisor : mod;
    }

    private static Map<Integer, RegionInfo> buildRegionMap() {
        int[][] layout = {new int[]{82, 5}, new int[]{100, 3}, new int[]{101, 3}, new int[]{102, 3}, new int[]{103, 2}, new int[]{104, 2}, new int[]{105, 2}, new int[]{106, 2}, new int[]{91, 2}, new int[]{83, 5}, new int[]{44, 3}, new int[]{75, 3}, new int[]{42, 3}, new int[]{41, 2}, new int[]{40, 2}, new int[]{39, 2}, new int[]{38, 2}, new int[]{92, 2}, new int[]{84, 5}, new int[]{45, 3}, new int[]{14, 3}, new int[]{13, 3}, new int[]{12, 2}, new int[]{11, 2}, new int[]{10, 2}, new int[]{37, 2}, new int[]{93, 2}, new int[]{85, 5}, new int[]{46, 5}, new int[]{74, 5}, new int[]{3, 3}, new int[]{2, 2}, new int[]{1, 2}, new int[]{25, 2}, new int[]{36, 2}, new int[]{94, 2}, new int[]{86, 4}, new int[]{47, 4}, new int[]{72, 4}, new int[]{71, 4}, new int[]{5, 2}, new int[]{4, 2}, new int[]{24, 2}, new int[]{35, 2}, new int[]{95, 2}, new int[]{87, 4}, new int[]{51, 1}, new int[]{17, 1}, new int[]{9, 0}, new int[]{8, 0}, new int[]{7, 0}, new int[]{23, 0}, new int[]{34, 0}, new int[]{96, 2}, new int[]{88, 4}, new int[]{54, 1}, new int[]{18, 1}, new int[]{61, 0}, new int[]{62, 0}, new int[]{21, 0}, new int[]{22, 0}, new int[]{33, 0}, new int[]{97, 0}, new int[]{89, 0}, new int[]{26, 1}, new int[]{27, 0}, new int[]{28, 0}, new int[]{29, 0}, new int[]{30, 0}, new int[]{59, 0}, new int[]{32, 0}, new int[]{98, 0}, new int[]{90, 0}, new int[]{107, 1}, new int[]{108, 1}, new int[]{109, 1}, new int[]{110, 1}, new int[]{111, 1}, new int[]{112, 1}, new int[]{113, 1}, new int[]{99, 0}};
        Map<Integer, RegionInfo> map = new HashMap<>();
        for (int i = 0; i < layout.length; i++) {
            map.put(Integer.valueOf(i), new RegionInfo(layout[i][0], layout[i][1]));
        }
        return map;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/DonutRegionMapData$RegionInfo.class */
    @Environment(EnvType.CLIENT)
    public static final class RegionInfo {
        public final int regionId;
        public final int regionType;

        RegionInfo(int regionId, int regionType) {
            this.regionId = regionId;
            this.regionType = regionType;
        }
    }
}
