package edu.uaskl.cpp.model.node;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;

public class NodeExtended<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> extends NodeBasic<T, V> {
    private boolean isAllEdgesVisited = false;
    private T nodeForCPP;

    public NodeExtended(EdgeCreator<T, V> edgeCreator) {
        super(edgeCreator);
    }
    public NodeExtended(String id, EdgeCreator<T, V> edgeCreator) {
        super(edgeCreator);
        this.nodeId=id;
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeExtended(final T otherNode) {
        super(otherNode);
        this.nodeId = otherNode.getNodeId();
    }
    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeExtended(final T knoten, final boolean isForCpp) { // TODO static fabric method?
        super(knoten.getEdgeCreator());
        setName(knoten.getName() + "_" + getId());
        if (isForCpp)
            this.nodeForCPP = knoten;
        else
            throw new IllegalArgumentException("not for cpp? Wrong constructor?");
    }

    public T getNodeForCpp() {
        return nodeForCPP;
    }

    private void resetAllEdgesVisitedStates() {
        for (final V kantenItem : getEdges())
            kantenItem.resetState();
        this.isAllEdgesVisited = false;
    }

    public boolean isAllEdgesVisited() {
        if (this.isAllEdgesVisited)
            return true;
        for (final V kante : getEdges())
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
    

    public NodeExtended<T, V> connectWithNodeAndWeigth(final T otherNode, final int weight,List<OsmNode> metaNodes) {
        addEdge(getEdgeCreator().create(this, otherNode, weight,metaNodes));
        return this;
    }
}
