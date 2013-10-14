package edu.uaskl.cpp.algorithmen;

import edu.uaskl.cpp.model.graph.GraphUndirected;

// Example, can be changed

public class AlgorithmsUndirected implements Algorithms {

    private final GraphUndirected graph;

    public AlgorithmsUndirected(final GraphUndirected graph) {
        this.graph = graph;
    }

    @Override
    public GraphUndirected getGraph() {
        return graph;
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasEulerCircle() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
