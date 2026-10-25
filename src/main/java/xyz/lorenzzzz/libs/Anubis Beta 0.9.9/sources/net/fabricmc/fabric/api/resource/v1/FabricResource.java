package net.fabricmc.fabric.api.resource.v1;

import net.minecraft.class_5352;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/FabricResource.class */
public interface FabricResource {
    default class_5352 getFabricPackSource() {
        LoggerFactory.getLogger(FabricResource.class).error("Unknown Resource implementation {}, returning PACK_SOURCE_NONE as the source", getClass().getName());
        return class_5352.field_25347;
    }
}
