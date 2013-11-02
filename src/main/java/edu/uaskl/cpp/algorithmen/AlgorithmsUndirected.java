package edu.uaskl.cpp.algorithmen;

import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;

// Example, can be changed

public class AlgorithmsUndirected implements Algorithms {

    private final GraphUndirected<? extends Metadata> graph;

    public AlgorithmsUndirected(final GraphUndirected<? extends Metadata> graph) {
        this.graph = graph;
    }

    @Override
    public GraphUndirected<? extends Metadata> getGraph() {
        return graph;
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasEulerCircle() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
