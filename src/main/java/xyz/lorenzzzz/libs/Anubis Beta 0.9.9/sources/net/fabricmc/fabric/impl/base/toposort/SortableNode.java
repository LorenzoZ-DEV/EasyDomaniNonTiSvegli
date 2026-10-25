package net.fabricmc.fabric.impl.base.toposort;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.impl.base.toposort.SortableNode;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-api-base-1.0.5+4ebb5c083e.jar:net/fabricmc/fabric/impl/base/toposort/SortableNode.class */
public abstract class SortableNode<N extends SortableNode<N>> {
    protected final List<N> subsequentNodes = new ArrayList();
    protected final List<N> previousNodes = new ArrayList();
    boolean visited = false;

    protected abstract String getDescription();

    protected void addSubsequentNode(N node) {
        this.subsequentNodes.add(node);
    }

    protected void addPreviousNode(N node) {
        this.previousNodes.add(node);
    }

    public static <N extends SortableNode<N>> void link(N first, N second) {
        if (first == second) {
            throw new IllegalArgumentException("Cannot link a node to itself!");
        }
        first.addSubsequentNode(second);
        second.addPreviousNode(first);
    }
}
