package edu.uaskl.cpp.model.edge;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.node.NodeOSM;

public class EdgeOSM extends EdgeExtended<NodeOSM, EdgeOSM> {
	private List<OsmNode> metaNodes;

    public EdgeOSM(final EdgeOSM edge) {
        super(edge);
    }

    public EdgeOSM(final NodeOSM node1, final NodeOSM node2) {
        super(node1, node2);
    }

    public EdgeOSM(final NodeOSM node1, final NodeOSM node2, final int weight) {
        super(node1, node2, weight);
    }

    public EdgeOSM(NodeOSM node1, NodeOSM node2, int weight,
			List<OsmNode> metaNodes) {
    	super(node1, node2, weight);
        this.metaNodes=metaNodes;
	}
	
	public List<OsmNode> getMetaNodes() {
		return metaNodes;
	}
}
