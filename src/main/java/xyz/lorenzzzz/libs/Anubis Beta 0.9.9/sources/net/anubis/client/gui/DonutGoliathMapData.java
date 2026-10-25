package net.anubis.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/DonutGoliathMapData.class */
@Environment(EnvType.CLIENT)
public final class DonutGoliathMapData {
    public static final int GRID = 36;
    private static final double CELL_SIZE = 12500.0d;
    private static final double MAP_OFFSET = 225000.0d;
    private static final String[] REGIONS = {"NA West", "NA East", "Oceania", "Asia", "EU West", "EU Central", "Europe"};
    private static final int[][][] GOLIATHS = {new int[][]{new int[]{4, 0, 2, 4}, new int[]{6, 0, 2, 4}, new int[]{8, 0, 2, 4}, new int[]{10, 0, 2, 4}, new int[]{12, 0, 2, 4}, new int[]{14, 0, 2, 4}, new int[]{4, 4, 2, 4}, new int[]{6, 4, 2, 4}, new int[]{8, 4, 2, 4}, new int[]{10, 4, 2, 4}, new int[]{12, 4, 2, 4}, new int[]{14, 4, 2, 4}, new int[]{4, 8, 2, 4}, new int[]{6, 8, 2, 4}, new int[]{8, 8, 2, 4}, new int[]{10, 8, 2, 4}, new int[]{12, 8, 2, 4}, new int[]{14, 8, 2, 4}, new int[]{12, 12, 2, 4}, new int[]{14, 12, 2, 4}}, new int[][]{new int[]{16, 0, 2, 4}, new int[]{18, 0, 2, 4}, new int[]{20, 0, 2, 4}, new int[]{22, 0, 2, 4}, new int[]{24, 0, 4, 4}, new int[]{28, 0, 2, 4}, new int[]{30, 0, 2, 4}, new int[]{32, 0, 2, 4}, new int[]{34, 0, 2, 4}, new int[]{16, 4, 2, 2}, new int[]{18, 4, 2, 2}, new int[]{20, 4, 2, 2}, new int[]{22, 4, 2, 2}, new int[]{24, 4, 2, 2}, new int[]{26, 4, 2, 4}, new int[]{28, 4, 2, 2}, new int[]{30, 4, 2, 2}, new int[]{32, 4, 2, 4}, new int[]{34, 4, 2, 4}, new int[]{16, 6, 2, 2}, new int[]{18, 6, 2, 2}, new int[]{20, 6, 2, 2}, new int[]{22, 6, 2, 2}, new int[]{24, 6, 2, 2}, new int[]{28, 6, 2, 2}, new int[]{30, 6, 2, 2}, new int[]{16, 8, 2, 4}, new int[]{18, 8, 2, 4}, new int[]{20, 8, 4, 4}, new int[]{24, 8, 2, 4}, new int[]{26, 8, 2, 4}, new int[]{28, 8, 2, 4}, new int[]{30, 8, 2, 4}, new int[]{32, 8, 2, 4}, new int[]{34, 8, 2, 4}, new int[]{16, 12, 2, 4}, new int[]{18, 12, 2, 4}, new int[]{20, 12, 4, 4}, new int[]{24, 12, 2, 4}, new int[]{26, 12, 2, 4}, new int[]{28, 12, 2, 4}, new int[]{30, 12, 2, 4}, new int[]{32, 12, 2, 4}, new int[]{34, 12, 2, 4}, new int[]{16, 16, 4, 4}, new int[]{20, 16, 2, 4}, new int[]{22, 16, 2, 2}, new int[]{24, 16, 4, 4}, new int[]{28, 16, 2, 4}, new int[]{30, 16, 2, 4}, new int[]{32, 16, 2, 4}, new int[]{34, 16, 2, 4}, new int[]{22, 18, 2, 2}, new int[]{32, 20, 2, 4}, new int[]{34, 20, 2, 4}, new int[]{32, 24, 2, 4}, new int[]{34, 24, 2, 4}}, new int[][]{new int[]{0, 0, 2, 2}, new int[]{2, 0, 2, 4}, new int[]{0, 2, 2, 2}, new int[]{0, 4, 2, 2}, new int[]{2, 4, 2, 4}, new int[]{0, 6, 2, 2}, new int[]{0, 8, 2, 2}, new int[]{2, 8, 2, 4}, new int[]{0, 10, 2, 2}, new int[]{0, 12, 2, 2}, new int[]{2, 12, 2, 4}, new int[]{4, 12, 2, 4}, new int[]{6, 12, 2, 4}, new int[]{8, 12, 2, 4}, new int[]{10, 12, 2, 4}, new int[]{0, 14, 2, 2}}, new int[][]{new int[]{0, 16, 2, 4}, new int[]{2, 16, 2, 4}, new int[]{4, 16, 2, 4}, new int[]{6, 16, 2, 4}, new int[]{8, 16, 2, 2}, new int[]{10, 16, 2, 2}, new int[]{12, 16, 2, 2}, new int[]{14, 16, 2, 2}, new int[]{8, 18, 2, 2}, new int[]{10, 18, 2, 2}, new int[]{12, 18, 2, 2}, new int[]{14, 18, 2, 2}, new int[]{0, 20, 2, 4}, new int[]{2, 20, 2, 4}, new int[]{0, 24, 2, 4}, new int[]{2, 24, 2, 4}}, new int[][]{new int[]{4, 20, 2, 4}, new int[]{6, 20, 2, 4}, new int[]{8, 20, 2, 4}, new int[]{10, 20, 2, 4}, new int[]{4, 24, 2, 4}, new int[]{6, 24, 2, 4}, new int[]{8, 24, 2, 4}, new int[]{10, 24, 2, 4}, new int[]{4, 28, 2, 4}, new int[]{6, 28, 2, 4}, new int[]{4, 32, 2, 4}, new int[]{6, 32, 2, 2}, new int[]{8, 32, 2, 2}, new int[]{10, 32, 2, 2}, new int[]{12, 32, 2, 2}, new int[]{14, 32, 2, 4}, new int[]{16, 32, 2, 4}, new int[]{18, 32, 2, 2}, new int[]{20, 32, 2, 4}, new int[]{22, 32, 2, 4}, new int[]{24, 32, 2, 4}, new int[]{26, 32, 2, 4}, new int[]{6, 34, 2, 2}, new int[]{8, 34, 2, 2}, new int[]{10, 34, 2, 2}, new int[]{12, 34, 2, 2}, new int[]{18, 34, 2, 2}}, new int[][]{new int[]{12, 20, 2, 4}, new int[]{14, 20, 2, 4}, new int[]{16, 20, 2, 4}, new int[]{18, 20, 2, 4}, new int[]{20, 20, 2, 4}, new int[]{22, 20, 2, 2}, new int[]{26, 20, 2, 4}, new int[]{28, 20, 2, 4}, new int[]{30, 20, 2, 2}, new int[]{22, 22, 2, 2}, new int[]{24, 22, 1, 2}, new int[]{25, 22, 1, 2}, new int[]{12, 24, 2, 4}, new int[]{14, 24, 2, 4}, new int[]{16, 24, 2, 4}, new int[]{18, 24, 2, 4}, new int[]{20, 24, 2, 4}, new int[]{22, 24, 2, 4}, new int[]{24, 24, 2, 4}, new int[]{26, 24, 2, 2}, new int[]{28, 24, 2, 2}, new int[]{30, 24, 2, 4}, new int[]{26, 26, 2, 2}, new int[]{0, 28, 4, 1}, new int[]{8, 28, 2, 2}, new int[]{10, 28, 2, 4}, new int[]{12, 28, 2, 2}, new int[]{14, 28, 2, 2}, new int[]{16, 28, 2, 2}, new int[]{18, 28, 2, 2}, new int[]{20, 28, 2, 2}, new int[]{22, 28, 2, 2}, new int[]{24, 28, 2, 2}, new int[]{26, 28, 2, 4}, new int[]{28, 28, 2, 2}, new int[]{30, 28, 2, 2}, new int[]{32, 28, 2, 2}, new int[]{0, 29, 4, 1}, new int[]{0, 30, 2, 2}, new int[]{2, 30, 2, 2}, new int[]{16, 30, 2, 2}, new int[]{18, 30, 2, 2}, new int[]{22, 30, 2, 2}, new int[]{24, 30, 2, 2}, new int[]{32, 30, 2, 2}, new int[]{34, 30, 2, 2}, new int[]{0, 32, 2, 2}, new int[]{28, 32, 2, 4}, new int[]{32, 32, 2, 4}, new int[]{0, 34, 4, 2}, new int[]{30, 34, 2, 2}, new int[]{34, 34, 2, 2}}, new int[][]{new int[]{24, 20, 2, 2}, new int[]{30, 22, 2, 2}, new int[]{28, 26, 2, 2}, new int[]{34, 28, 2, 2}, new int[]{8, 30, 2, 2}, new int[]{12, 30, 2, 2}, new int[]{14, 30, 2, 2}, new int[]{20, 30, 2, 2}, new int[]{28, 30, 2, 2}, new int[]{30, 30, 2, 2}, new int[]{2, 32, 2, 2}, new int[]{30, 32, 2, 2}, new int[]{34, 32, 2, 2}}};
    private static final short[] CELLS = new short[1296];
    private static final String[] NAMES;

    static {
        Arrays.fill(CELLS, (short) -1);
        List<String> names = new ArrayList<>();
        for (int region = 0; region < GOLIATHS.length; region++) {
            int number = 1;
            for (int[] rect : GOLIATHS[region]) {
                short id = (short) names.size();
                int i = number;
                number++;
                names.add(REGIONS[region] + " " + i);
                for (int row = rect[1]; row < rect[1] + rect[3]; row++) {
                    for (int col = rect[0]; col < rect[0] + rect[2]; col++) {
                        CELLS[(row * 36) + col] = id;
                    }
                }
            }
        }
        NAMES = (String[]) names.toArray(x$0 -> {
            return new String[x$0];
        });
    }

    private DonutGoliathMapData() {
    }

    public static int goliathAt(double x, double z) {
        int col = (int) Math.floor((x + MAP_OFFSET) / CELL_SIZE);
        int row = (int) Math.floor((z + MAP_OFFSET) / CELL_SIZE);
        if (col < 0 || row < 0 || col >= 36 || row >= 36) {
            return -1;
        }
        return CELLS[(row * 36) + col];
    }

    public static String name(int goliath) {
        return (goliath < 0 || goliath >= NAMES.length) ? "Unknown" : NAMES[goliath];
    }
}
