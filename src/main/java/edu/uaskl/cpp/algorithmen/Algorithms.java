package edu.uaskl.cpp.algorithmen;

import edu.uaskl.cpp.model.edge.interfaces.Edge;
import edu.uaskl.cpp.model.graph.interfaces.Graph;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * This interface models a relation between algorithms and graph. Each algorithms instance is responsible for a specific type of graph.<br>
 * This is only for convenience, no further benefit.<br>
 * A new algorithm can be implemented in its own class without implementing this interface at any time.
 * 
 * @author tbach
 */
public interface Algorithms<T extends Node<T, V>, V extends Edge<T, V>> {
    public Graph<T, V> getGraph();
}
