package edu.uaskl.cpp.model.graph;

import java.util.Map;
import java.util.SortedSet;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * A complete graph is a graph where each node is connected to each other node by its own edge.
 * 
 * @author tbach
 */
public class GraphComplete extends GraphUndirected {

    public GraphComplete(final int numberOfNodes) {
        super("Complete Graph of size: " + numberOfNodes);
        createNodes(numberOfNodes);
    }

    @Override
    public void setNodes(final Map<String,NodeCpp> nodes) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    @Override
    public GraphBasic addNode(final NodeCpp newNode) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    private void createNodes(final int numberOfNodes) {
        for (int i = 0; i < numberOfNodes; i++) {
            final NodeCpp newNode = new NodeCpp();
            connectNodeWithAllOthers(newNode);
            getNodes().put(newNode.getId(),newNode);
        }
    }

    private void connectNodeWithAllOthers(final NodeCpp neuerKnoten) {
        for (final NodeCpp nodesItem : getNodes().values())
            neuerKnoten.addEdge(new EdgeCpp(neuerKnoten, nodesItem));
    }
}
