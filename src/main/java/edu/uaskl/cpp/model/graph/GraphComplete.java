package edu.uaskl.cpp.model.graph;

import java.util.SortedSet;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * A complete graph is a graph where each node is connected to each other node by its own edge.
 * 
 * @author tbach
 */
public class GraphComplete<M extends Metadata> extends GraphUndirected<M> {
    public GraphComplete(final int numberOfNodes) {
        super("Complete Graph of size: " + numberOfNodes);
        createNodes(numberOfNodes);
    }

    @Override
    public void setNodes(final SortedSet<NodeCpp<M>> nodes) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    @Override
    public GraphBasic<M> addNode(final NodeCpp<M> newNode) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    private void createNodes(final int numberOfNodes) {
        for (int i = 0; i < numberOfNodes; i++) {
            final NodeCpp<M> newNode = new NodeCpp<M>(this.getEdgeCreator());
            connectNodeWithAllOthers(newNode);
            getNodes().add(newNode);
        }
    }

    private void connectNodeWithAllOthers(final NodeCpp<M> neuerKnoten) {
        for (final NodeCpp<M> nodesItem : getNodes())
            neuerKnoten.addEdge(new EdgeCpp<M>(neuerKnoten, nodesItem));
    }
}
