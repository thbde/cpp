package edu.uaskl.cpp.model.node;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.edge.EdgeCreatorCpp;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;

/**
 * Chinese postman problem - node. Has all required special properties to solve Chinese postman problem
 * 
 * @author tbach
 */
public class NodeCpp<M extends Metadata> extends NodeBasic<NodeCpp<M>, EdgeCpp<M>> implements MetadataAnnotated<M> {
    private boolean isAllEdgesVisited = false;
    private NodeCpp<M> nodeForCPP;
    private M metadata;

    public NodeCpp(EdgeCreatorCpp<M> edgeCreator) {
        this(edgeCreator, null);
    }

    public NodeCpp(EdgeCreatorCpp<M> edgeCreator, M metadata) {
    	super(edgeCreator);
    	this.metadata = metadata;
    }

    /** Copy constructor, creates a new node with the same properties */
    public NodeCpp(final NodeCpp<M> otherNode) {
        super(otherNode);
        metadata = otherNode.getMetadata();
    }

    public NodeCpp(final NodeCpp<M> knoten, final boolean isForCpp) {
    	this(knoten, isForCpp, null);
    }

    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeCpp(final NodeCpp<M> knoten, final boolean isForCpp, M metadata) { // TODO static fabric method?
        super(knoten.getEdgeCreator());
        this.metadata = metadata;
        setName(knoten.getName() + "_" + getId());
        if (isForCpp)
            this.nodeForCPP = knoten;
        else
            throw new IllegalArgumentException("not for cpp? Wrong constructor?");
    }

    public NodeCpp<M> getNodeForCpp() {
        return nodeForCPP;
    }

    private void resetAllEdgesVisitedStates() {
        for (final EdgeCpp<M> kantenItem : getEdges())
            kantenItem.resetState();
        this.isAllEdgesVisited = false;
    }

    public boolean isAllEdgesVisited() {
        if (this.isAllEdgesVisited)
            return true;
        for (final EdgeCpp<M> kante : getEdges())
            if (!kante.isVisited())
                return false;
        setAllEdgesVisited();
        return true;
    }

    public void setAllEdgesVisited() {
        this.isAllEdgesVisited = true;
    }

    public void resetStates() {
        resetVisitedState();
        resetAllEdgesVisitedStates();
    }

	@Override
	public M getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(M metadata) {
		this.metadata = metadata;
	}
}
