package edu.uaskl.cpp.model.node;

import edu.uaskl.cpp.map.meta.BasicMetadata;
import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.EdgeCreatorOSM;
import edu.uaskl.cpp.model.edge.EdgeOSM;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;

public class NodeOSM extends NodeExtended<NodeOSM, EdgeOSM> implements MetadataAnnotated<BasicMetadata> {
    private static final EdgeCreator<NodeOSM, EdgeOSM> edgeCreator = new EdgeCreatorOSM();

    private BasicMetadata metadata = null;

    public NodeOSM() {
        super(edgeCreator);
    }
    public NodeOSM(Long id) {
        super(id, edgeCreator);
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeOSM(final NodeOSM otherNode) {
        super(otherNode);
    }
    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeOSM(final NodeOSM knoten, final boolean isForCpp) { // TODO static fabric method?
        super(knoten, isForCpp);
    }
    
    public NodeOSM connectWithNodeWeigthAndMeta(final NodeOSM otherNode, final int weight, WayOSM metadata) {
        addEdge(((EdgeCreatorOSM)getEdgeCreator()).create(this, otherNode, weight, metadata));
        return this;
    }
	@Override
	public BasicMetadata getMetadata() {
		return metadata;
	}
	@Override
	public void setMetadata(BasicMetadata metadata) {
		this.metadata = metadata;
	}
    
}
