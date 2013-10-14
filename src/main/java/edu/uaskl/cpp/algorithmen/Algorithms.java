package edu.uaskl.cpp.algorithmen;

import edu.uaskl.cpp.model.graph.GraphBasic;

/**
 * This interface models a relation between algorithms and graph. Each algorithms instance is responsible for a specific type of graph.<br>
 * This is only for convenience, no further benefit.<br>
 * A new algorithm can be implemented in its own class without implementing this interface at any time.
 * 
 * @author tbach
 */
public interface Algorithms {
    public GraphBasic getGraph();
}
