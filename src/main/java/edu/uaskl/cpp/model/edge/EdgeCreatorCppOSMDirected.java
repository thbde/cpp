package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeCppOSMDirected;

public class EdgeCreatorCppOSMDirected implements EdgeCreator<NodeCppOSMDirected, EdgeCppOSMDirected> {

	public EdgeCppOSMDirected create(final NodeCppOSMDirected node1, final NodeCppOSMDirected node2, final double weight) {
        return new EdgeCppOSMDirected(node1, node2, weight, null);
    }
	
	public EdgeCppOSMDirected create(NodeCppOSMDirected node1, NodeCppOSMDirected node2, double weight, WayOSM metadata) {
		return new EdgeCppOSMDirected(node1, node2, weight, metadata);
	}

}
