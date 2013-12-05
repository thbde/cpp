/**
 * 
 */
package edu.uaskl.cpp.model.node;

import java.util.List;

import edu.uaskl.cpp.map.meta.BasicMetadata;
import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSMDirected;
import edu.uaskl.cpp.model.edge.EdgeCreatorCppOSM;
import edu.uaskl.cpp.model.edge.EdgeCreatorCppOSMDirected;
import edu.uaskl.cpp.model.edge.interfaces.Edge;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * @author malte
 *
 */
public class NodeCppOSMDirected extends NodeExtended<NodeCppOSMDirected, EdgeCppOSMDirected> {
	private static final EdgeCreator<NodeCppOSMDirected, EdgeCppOSMDirected> edgeCreator = new EdgeCreatorCppOSMDirected();

    private BasicMetadata metadata = null;
    
    public NodeCppOSMDirected() {
        super(edgeCreator);
    }
    public NodeCppOSMDirected(Long id) {
        super(id, edgeCreator);
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeCppOSMDirected(final NodeCppOSMDirected otherNode) {
        super(otherNode);
    }
    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeCppOSMDirected(final NodeCppOSMDirected knoten, final boolean isForCpp) { // TODO static fabric method?
        super(knoten, isForCpp);
    }
    
    public NodeCppOSMDirected connectWithNodeWeigthAndMeta(final NodeCppOSMDirected otherNode, final double weight, WayOSM metadata) {
        addEdge(((EdgeCreatorCppOSMDirected)getEdgeCreator()).create(this, otherNode, weight, metadata));
        return this;
    }

	public BasicMetadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(BasicMetadata metadata) {
		this.metadata = metadata;
	}
	
	/** Constant running time ( Arraylist.size() ) **/
	
    @Override
    public int getDegree() {
    	return getOutDegree() - getInDegree();
    }
    
    public int getDirectedDegree() {
    	return getOutDegree() - getInDegree();
    }

    public int getInDegree(){
    	List<EdgeCppOSMDirected> edges = this.getEdges();
    	int degree = 0;
    	for(EdgeCppOSMDirected edge : edges) {
    		if(edge.getNode2().getId().equals(this.nodeId)){
    			++degree;
    		}
    	}
        return degree;
    }
    
    public int getOutDegree(){
    	List<EdgeCppOSMDirected> edges = this.getEdges();
    	int degree = 0;
    	for(EdgeCppOSMDirected edge : edges) {
    		if(edge.getNode1().getId().equals(this.nodeId)){
    			++degree;
    		}
    	}
        return degree;
    }
    
    /** Constant running time ( Arraylist.size() ) **/
    @Override
    public boolean isDegreeOdd() {
    	System.out.println("odd degree for directed graph?");
    	System.exit(0);
    	return false;
    }

    /** Constant running time ( Arraylist.size() ) **/
    @Override
    public boolean isDegreeEven() {
    	System.out.println("even degree for directed graph?");
    	System.exit(0);
    	return false;
    }
    
    public boolean removeEdge(EdgeCppOSMDirected edge) {
    	NodeCppOSMDirected otherNode = edge.getNode1().equals(this) ? edge.getNode2() : edge.getNode1();
    	return otherNode.removeEdgeThis(edge) & edges.remove(edge);
    }
    
    public boolean removeEdgeThis(EdgeCppOSMDirected edge) {
    	return edges.remove(edge);
    }
    
    
    public boolean removeEdgeTo(final NodeCppOSMDirected otherNode) {
        Edge<NodeCppOSMDirected, EdgeCppOSMDirected> edgeToRemove = null;
        for (final Edge<NodeCppOSMDirected, EdgeCppOSMDirected> edgesItem : edges)
            if (edgesItem.getNode2().equals(otherNode)) {
                edgeToRemove = edgesItem;
                break;
            }
        if (edgeToRemove == null)
            throw new IllegalArgumentException("There is no edge from this node (" + this + " ) to node: " + otherNode);
        return edges.remove(edgeToRemove);
    }

}
