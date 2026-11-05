package xyz.lorenzzzz.easydomani.signprobe.model;

import com.github.retrooper.packetevents.util.Vector3i;
import java.util.List;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;

public record ActiveSignProbe(long id, Vector3i pos, int originalState, String sentinel, List<ModProfile> defs, long lateUntil) {}
