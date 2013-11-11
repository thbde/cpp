package edu.uaskl.cpp.model.graph;

import edu.uaskl.cpp.algorithmen.AlgorithmsUndirected;
import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.node.NodeExtended;

/**
 * @author tbach
 */
public class GraphUndirected<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> extends GraphBasic<T, V> {
    private final AlgorithmsUndirected<T, V> algorithms = new AlgorithmsUndirected<T, V>(this);

    public GraphUndirected() {
        super("Undirected Graph");
    }

    protected GraphUndirected(final String string) {
        super(string);
    }

    @Override
    public AlgorithmsUndirected<T,V> getAlgorithms() {
        return algorithms;
    }

    @Override
    public int getGetNumberOfEdges() {
        return (super.getGetNumberOfEdges() >> 1); // the same edge is counted 2 times for directed graphs, but only once for undirected
    }

    /** Running time: O(log(|nodes| + |edgesFromGivenNode|*|edgesFromRelatedNode|)) */
    public boolean removeNode(final String id) {
    	T nodeToRemove = this.nodes.remove(id);
        if (nodeToRemove == null)
            return false;
        nodeToRemove.removeAllEdges();
        return true;
    }
}
