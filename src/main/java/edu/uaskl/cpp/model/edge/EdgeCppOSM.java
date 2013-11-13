package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;
import edu.uaskl.cpp.model.node.NodeCppOSM;

public class EdgeCppOSM extends EdgeExtended<NodeCppOSM, EdgeCppOSM> implements MetadataAnnotated<WayOSM> {
	private WayOSM metadata;

    public EdgeCppOSM(final EdgeCppOSM edge) {
        super(edge);
    }

    public EdgeCppOSM(final NodeCppOSM node1, final NodeCppOSM node2) {
        super(node1, node2);
    }

    public EdgeCppOSM(final NodeCppOSM node1, final NodeCppOSM node2, final double weight) {
        super(node1, node2, weight);
    }

    public EdgeCppOSM(NodeCppOSM node1, NodeCppOSM node2, double weight,
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
