package edu.uaskl.cpp.model.edge;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeOSM;

public class EdgeCreatorOSM implements EdgeCreator<NodeOSM, EdgeOSM> {
    public EdgeOSM create(final NodeOSM node1, final NodeOSM node2, final int weight) {
        return new EdgeOSM(node1, node2, weight, null);
    }
	
	public EdgeOSM create(NodeOSM node1, NodeOSM node2, int weight, List<OsmNode> metaNodes) {
		return new EdgeOSM(node1, node2, weight, metaNodes);
	}
}
