package edu.uaskl.cpp.model.node;

import edu.uaskl.cpp.map.meta.BasicMetadata;
import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.EdgeCreatorCppOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;

public class NodeCppOSM extends NodeExtended<NodeCppOSM, EdgeCppOSM> implements MetadataAnnotated<BasicMetadata> {
    private static final EdgeCreator<NodeCppOSM, EdgeCppOSM> edgeCreator = new EdgeCreatorCppOSM();

    private BasicMetadata metadata = null;

    public NodeCppOSM() {
        super(edgeCreator);
    }
    public NodeCppOSM(Long id) {
        super(id, edgeCreator);
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeCppOSM(final NodeCppOSM otherNode) {
        super(otherNode);
    }
    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeCppOSM(final NodeCppOSM knoten, final boolean isForCpp) { // TODO static fabric method?
        super(knoten, isForCpp);
    }
    
    public NodeCppOSM connectWithNodeWeigthAndMeta(final NodeCppOSM otherNode, final int weight, WayOSM metadata) {
        addEdge(((EdgeCreatorCppOSM)getEdgeCreator()).create(this, otherNode, weight, metadata));
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
