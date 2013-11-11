package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;
import edu.uaskl.cpp.model.node.NodeOSM;

public class EdgeOSM extends EdgeExtended<NodeOSM, EdgeOSM> implements MetadataAnnotated<WayOSM> {
	private WayOSM metadata;

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
