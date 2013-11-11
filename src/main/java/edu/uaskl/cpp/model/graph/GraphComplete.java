package edu.uaskl.cpp.model.graph;

import java.util.Map;
import edu.uaskl.cpp.model.edge.EdgeOSM;
import edu.uaskl.cpp.model.node.NodeOSM;

/**
 * A complete graph is a graph where each node is connected to each other node by its own edge.
 * 
 * @author tbach
 */
public class GraphComplete extends GraphUndirected<NodeOSM, EdgeOSM> {

    public GraphComplete(final int numberOfNodes) {
        super("Complete Graph of size: " + numberOfNodes);
        createNodes(numberOfNodes);
    }

    @Override
    public void setNodes(final Map<Long, NodeOSM> nodes) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    @Override
    public GraphBasic<NodeOSM, EdgeOSM> addNode(final NodeOSM newNode) {
        throw new IllegalStateException("It is not allowed to change nodes of a complete graph");
    }

    private void createNodes(final int numberOfNodes) {
        for (int i = 0; i < numberOfNodes; i++) {
            final NodeOSM newNode = new NodeOSM();
            connectNodeWithAllOthers(newNode);
            getNodesMap().put(newNode.getId(), newNode);
        }
    }

    private void connectNodeWithAllOthers(final NodeOSM neuerKnoten) {
        for (final NodeOSM nodesItem : getNodes())
            neuerKnoten.addEdge(new EdgeOSM(neuerKnoten, nodesItem));
    }
}
