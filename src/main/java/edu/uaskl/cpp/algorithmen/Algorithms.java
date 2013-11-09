package edu.uaskl.cpp.algorithmen;

import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.graph.GraphBasic;
import edu.uaskl.cpp.model.node.NodeExtended;

/**
 * This interface models a relation between algorithms and graph. Each algorithms instance is responsible for a specific type of graph.<br>
 * This is only for convenience, no further benefit.<br>
 * A new algorithm can be implemented in its own class without implementing this interface at any time.
 * 
 * @author tbach
 */
public interface Algorithms<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> {
    public GraphBasic<T, V> getGraph();
}
