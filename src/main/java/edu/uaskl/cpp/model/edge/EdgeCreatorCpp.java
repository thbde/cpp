package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * @author tbach
 */
public class EdgeCreatorCpp implements EdgeCreator<NodeCpp, EdgeCpp> {

    @Override
    public EdgeCpp create(final NodeCpp node1, final NodeCpp node2, final double weight) {
        return new EdgeCpp(node1, node2, weight);
    }
}
