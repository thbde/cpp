package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * @author tbach
 */
public class EdgeCreatorCpp implements EdgeCreator<NodeCpp, EdgeCpp> {

    @Override
    public EdgeCpp create(final Node<NodeCpp, EdgeCpp> node1, final Node<NodeCpp, EdgeCpp> node2, final int weight) {
        return new EdgeCpp((NodeCpp) node1, (NodeCpp) node2, weight);
    }

}
