package edu.uaskl.cpp.model.edge;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * @author tbach
 */
public class EdgeCpp extends EdgeExtended<NodeCpp, EdgeCpp> {

    /** Copy constructor. Creates a new edge with the same properties (nodes + weight) */
    public EdgeCpp(final EdgeCpp edge) {
        super(edge);
    }

    public EdgeCpp(final NodeCpp node1, final NodeCpp node2) {
        super(node1, node2);
    }

    public EdgeCpp(final NodeCpp node1, final NodeCpp node2, final int weight) {
        super(node1, node2, weight);
    }

    public EdgeCpp(NodeCpp node1, NodeCpp node2, int weight,
			List<OsmNode> metaNodes) {
    	super(node1, node2, weight, metaNodes);
	}
}
