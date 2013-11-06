package edu.uaskl.cpp.model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.uaskl.cpp.algorithmen.Algorithms;
import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.graph.interfaces.Graph;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.tools.CollectionTools;

/**
 * @author tbach
 */
public class GraphBasic implements Graph<NodeCpp, EdgeCpp> {
    private final String name;
    protected Map<String, NodeCpp> nodes = new HashMap<>();

    public GraphBasic() {
        this("Default Basisgraph");
    }

    protected GraphBasic(final String name) {
        this.name = name;
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
    public Collection<NodeCpp> getNodes() {
        return nodes.values();
    }

    public Map<String, NodeCpp> getNodesMap() // TODO should be in a GraphCPP class -tbach
    {
        return nodes;
    }

    public void setNodes(final Map<String, NodeCpp> newNodes) {
        this.nodes = newNodes;
    }

    @Override
    public GraphBasic addNode(final NodeCpp newNode) {
        this.nodes.put(newNode.getId(), newNode);
        return this;
    }

    @Override
    public int getNumberOfNodes() {
        return nodes.size();
    }

    @Override
    public int getGetNumberOfEdges() {
        int result = 0;
        for (final NodeCpp nodesItem : nodes.values())
            result += nodesItem.getEdges().size();
        return result;
    }

    /** Resets the state of all nodes. This includes states like visited and similar. */
    public void resetStates() {
        for (final NodeCpp nodesItem : nodes.values())
            nodesItem.resetStates();
    }

    /** Returns name, number of nodes and number of edges */
    public String getStatistics() {
        final StringBuilder stringBuilder = new StringBuilder(getName()).append(", NumberOfNodes: ").append(getNumberOfNodes()).append(", NumberOfEdges: ")
                .append(getGetNumberOfEdges());
        return stringBuilder.toString();
    }

    public NodeCpp getNode(final String id) {
        return nodes.get(id);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder(getStatistics());
        stringBuilder.append("\n");
        stringBuilder.append(CollectionTools.join("\n", nodes.values()));
        return stringBuilder.toString();
    }
}
