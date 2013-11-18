package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSMDirected;

public class EdgeCppOSMDirected extends EdgeExtended<NodeCppOSMDirected, EdgeCppOSMDirected> implements MetadataAnnotated<WayOSM> {
	private WayOSM metadata;

    public EdgeCppOSMDirected(final EdgeCppOSMDirected edge) {
        super(edge);
    }

    public EdgeCppOSMDirected(final NodeCppOSMDirected node1, final NodeCppOSMDirected node2) {
        super(node1, node2);
    }

    public EdgeCppOSMDirected(final NodeCppOSMDirected node1, final NodeCppOSMDirected node2, final double weight) {
        super(node1, node2, weight);
    }

    public EdgeCppOSMDirected(NodeCppOSMDirected node1, NodeCppOSMDirected node2, double weight,
			WayOSM metadata) {
    	super(node1, node2, weight);
        this.metadata=metadata;
	}

	@Override
	public WayOSM getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(WayOSM metadata) {
		this.metadata = metadata;
	}
}
