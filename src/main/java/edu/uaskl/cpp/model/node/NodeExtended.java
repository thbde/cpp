package edu.uaskl.cpp.model.node;

import edu.uaskl.cpp.model.edge.EdgeCppOSMDirected;
import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;

public class NodeExtended<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> extends NodeBasic<T, V> {
    private boolean isAllEdgesVisited = false;
    private T nodeForCPP;

    public double lat,lon;

    public NodeExtended(EdgeCreator<T, V> edgeCreator) {
        super(edgeCreator);
    }
    public NodeExtended(Long id, EdgeCreator<T, V> edgeCreator) {
        super(edgeCreator);
        this.nodeId=id;
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeExtended(final T otherNode) {
        super(otherNode);
        this.nodeId = otherNode.getId();
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
    
    public NodeExtended<T, V> connectWithNodeAndWeigth(final T otherNode, final double weight) {
        addEdge(getEdgeCreator().create((T)this, otherNode, weight));
        return this;
    }
    
    public boolean removeEdge(V edge) {
    	T otherNode = edge.getNode1().equals(this) ? edge.getNode2() : edge.getNode1();
    	return otherNode.removeEdgeThis(edge) & edges.remove(edge);
    }
    
    public boolean removeEdgeThis(V edge) {
    	return edges.remove(edge);
    }
    
    public V getShortestConnection(T otherNode) {
    	V minEdge = null;
    	double minWeight = Double.POSITIVE_INFINITY;
		for(V edge : this.getEdges()) {
			if( edge.getWeight() < minWeight && ((edge.getNode1().equals(this) && edge.getNode2().equals(otherNode))||(edge.getNode1().equals(otherNode) && edge.getNode2().equals(this))) ) {
				minEdge = edge;
				minWeight = edge.getWeight();
			}
		}
		if(minEdge == null) {
			throw new IllegalArgumentException("no connection from node "+this+" to "+otherNode);
		}
		return minEdge;
	}
    
}
