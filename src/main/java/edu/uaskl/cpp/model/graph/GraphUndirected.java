package edu.uaskl.cpp.model.graph;

import edu.uaskl.cpp.algorithmen.AlgorithmsUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * @author tbach
 */
public class GraphUndirected extends GraphBasic {
    private final AlgorithmsUndirected algorithms = new AlgorithmsUndirected(this);

    public GraphUndirected() {
        super("Undirected Graph");
    }

    protected GraphUndirected(final String string) {
        super(string);
    }

    @Override
    public AlgorithmsUndirected getAlgorithms() {
        return algorithms;
    }

    @Override
    public int getGetNumberOfEdges() {
        return (super.getGetNumberOfEdges() >> 1); // the same edge is counted 2 times for directed graphs, but only once for undirected
    }

    /** Running time: O(log(|nodes| + |edgesFromGivenNode|*|edgesFromRelatedNode|)) */
    public boolean removeNode(final String id) {
    	NodeCpp nodeToRemove = this.nodes.remove(id);
        if (nodeToRemove == null)
            return false;
        nodeToRemove.removeAllEdges();
        return true;
    }
}
