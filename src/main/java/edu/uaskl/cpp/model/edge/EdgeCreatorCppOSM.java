package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeCppOSM;

public class EdgeCreatorCppOSM implements EdgeCreator<NodeCppOSM, EdgeCppOSM> {
    public EdgeCppOSM create(final NodeCppOSM node1, final NodeCppOSM node2, final double weight) {
        return new EdgeCppOSM(node1, node2, weight, null);
    }
	
	public EdgeCppOSM create(NodeCppOSM node1, NodeCppOSM node2, double weight, WayOSM metadata) {
		return new EdgeCppOSM(node1, node2, weight, metadata);
	}
}
