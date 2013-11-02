package edu.uaskl.cpp.model.graph;

import java.util.SortedSet;
import java.util.TreeSet;

import edu.uaskl.cpp.algorithmen.Algorithms;
import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.edge.EdgeCreatorCpp;
import edu.uaskl.cpp.model.graph.interfaces.Graph;
import edu.uaskl.cpp.model.meta.MetadataCreatorCpp;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.tools.CollectionTools;

/**
 * @author tbach
 */
public class GraphBasic<M extends Metadata> implements Graph<NodeCpp<M>, EdgeCpp<M>> {
    private final String name;
    private final EdgeCreatorCpp<M> edgeCreator;
    protected SortedSet<NodeCpp<M>> nodes = new TreeSet<>();

    public GraphBasic() {
        this("Default Basisgraph");
    }

    protected GraphBasic(final String name) {
        this(name, new MetadataCreatorCpp<M>());
    }

    protected GraphBasic(final String name, MetadataCreatorCpp<M> metadataCreator) {
    	this.name = name;
    	this.edgeCreator = new EdgeCreatorCpp<M>(metadataCreator);
    }

    /** BasicGraph has no algorithms, therefore returns null */
    @Override
    public Algorithms getAlgorithms() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SortedSet<NodeCpp<M>> getNodes() {
        return nodes;
    }

    public void setNodes(final SortedSet<NodeCpp<M>> newNodes) {
        this.nodes = newNodes;
    }

    @Override
    public GraphBasic<M> addNode(final NodeCpp<M> newNode) {
        this.nodes.add(newNode);
        return this;
    }

    @Override
    public int getNumberOfNodes() {
        return nodes.size();
    }

    @Override
    public int getGetNumberOfEdges() {
        int result = 0;
        for (final NodeCpp<M> nodesItem : nodes)
            result += nodesItem.getEdges().size();
        return result;
    }

    /** Resets the state of all nodes. This includes states like visited and similar. */
    public void resetStates() {
        for (final NodeCpp<M> nodesItem : nodes)
            nodesItem.resetStates();
    }

    /** Returns name, number of nodes and number of edges */
    public String getStatistics() {
        final StringBuilder stringBuilder = new StringBuilder(getName()).append(", NumberOfNodes: ").append(getNumberOfNodes()).append(", NumberOfEdges: ")
                .append(getGetNumberOfEdges());
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder(getStatistics());
        stringBuilder.append("\n");
        stringBuilder.append(CollectionTools.join("\n", nodes));
        return stringBuilder.toString();
    }
    
    @Override
    public EdgeCreatorCpp<M> getEdgeCreator() {
    	return edgeCreator;
    }
}
