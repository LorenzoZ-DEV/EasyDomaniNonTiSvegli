package net.fabricmc.fabric.impl.resource;

import net.fabricmc.fabric.impl.base.toposort.SortableNode;
import net.minecraft.class_2960;
import net.minecraft.class_3302;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceReloaderPhaseData.class */
class ResourceReloaderPhaseData extends SortableNode<ResourceReloaderPhaseData> {
    final class_2960 id;
    class_3302 resourceReloader;
    VanillaStatus vanillaStatus = VanillaStatus.NONE;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceReloaderPhaseData$VanillaStatus.class */
    enum VanillaStatus {
        NONE,
        AFTER,
        BEFORE,
        VANILLA
    }

    ResourceReloaderPhaseData(class_2960 id, class_3302 resourceReloader) {
        this.id = id;
        this.resourceReloader = resourceReloader;
    }

    void markBefore() {
        boolean isAfter = this.vanillaStatus == VanillaStatus.AFTER;
        if (this.vanillaStatus == VanillaStatus.NONE || isAfter) {
            this.vanillaStatus = VanillaStatus.BEFORE;
            for (N prev : this.previousNodes) {
                prev.markBefore();
            }
        }
    }

    void markAfter() {
        if (this.vanillaStatus != VanillaStatus.NONE) {
            return;
        }
        this.vanillaStatus = VanillaStatus.AFTER;
        for (N next : this.subsequentNodes) {
            next.markAfter();
        }
    }

    void setVanillaStatus(VanillaStatus status) {
        if (this.vanillaStatus == VanillaStatus.NONE) {
            this.vanillaStatus = status;
        }
    }

    @Override // net.fabricmc.fabric.impl.base.toposort.SortableNode
    protected String getDescription() {
        return this.id.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.fabricmc.fabric.impl.base.toposort.SortableNode
    public void addSubsequentNode(ResourceReloaderPhaseData phase) {
        super.addSubsequentNode(phase);
        if (this.vanillaStatus == VanillaStatus.VANILLA || this.vanillaStatus == VanillaStatus.AFTER) {
            phase.markAfter();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.fabricmc.fabric.impl.base.toposort.SortableNode
    public void addPreviousNode(ResourceReloaderPhaseData phase) {
        super.addPreviousNode(phase);
        if (this.vanillaStatus == VanillaStatus.VANILLA || this.vanillaStatus == VanillaStatus.BEFORE) {
            phase.markBefore();
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/ResourceReloaderPhaseData$AfterVanilla.class */
    static class AfterVanilla extends ResourceReloaderPhaseData {
        @Override // net.fabricmc.fabric.impl.resource.ResourceReloaderPhaseData, net.fabricmc.fabric.impl.base.toposort.SortableNode
        protected /* bridge */ /* synthetic */ void addPreviousNode(SortableNode sortableNode) {
            super.addPreviousNode((ResourceReloaderPhaseData) sortableNode);
        }

        @Override // net.fabricmc.fabric.impl.resource.ResourceReloaderPhaseData, net.fabricmc.fabric.impl.base.toposort.SortableNode
        protected /* bridge */ /* synthetic */ void addSubsequentNode(SortableNode sortableNode) {
            super.addSubsequentNode((ResourceReloaderPhaseData) sortableNode);
        }

        AfterVanilla(class_2960 id) {
            super(id, null);
            setVanillaStatus(VanillaStatus.VANILLA);
        }

        @Override // net.fabricmc.fabric.impl.resource.ResourceReloaderPhaseData
        public void markBefore() {
        }

        @Override // net.fabricmc.fabric.impl.resource.ResourceReloaderPhaseData
        public void markAfter() {
        }
    }
}
