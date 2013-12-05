/**
 * 
 */
package edu.uaskl.cpp.model.graph;

import edu.uaskl.cpp.algorithms.AlgorithmsDirected;
import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.node.NodeExtended;

/**
 * @author malte
 *
 */
public class GraphDirected<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> extends GraphBasic<T, V> {
	private final AlgorithmsDirected<T, V> algorithms = new AlgorithmsDirected<>(this);
	
    public GraphDirected() {
        super("Directed Graph");
    }
	
    protected GraphDirected(final String string) {
        super(string);
    }
    
    @Override
    public AlgorithmsDirected<T, V> getAlgorithms() {
        return algorithms;
    }
    
    @Override
    public int getGetNumberOfEdges() {
        return (super.getGetNumberOfEdges() >> 1); // the same edge is counted 2 times for directed graphs, but only once for undirected
    }
    
    /** Running time: O(log(|nodes| + |edgesFromGivenNode|*|edgesFromRelatedNode|)) */
    public boolean removeNode(final Long id) {
        final T nodeToRemove = this.nodes.remove(id);
        if (nodeToRemove == null)
            return false;
        nodeToRemove.removeAllEdges();
        return true;
    }
}
