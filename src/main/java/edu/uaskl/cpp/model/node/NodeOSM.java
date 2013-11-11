package edu.uaskl.cpp.model.node;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.edge.EdgeCreatorOSM;
import edu.uaskl.cpp.model.edge.EdgeOSM;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;

public class NodeOSM extends NodeExtended<NodeOSM, EdgeOSM> {
    private static final EdgeCreator<NodeOSM, EdgeOSM> edgeCreator = new EdgeCreatorOSM();

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
    
    public NodeOSM connectWithNodeWeigthAndMeta(final NodeOSM otherNode, final int weight, List<OsmNode> metaNodes) {
        addEdge(((EdgeCreatorOSM)getEdgeCreator()).create(this, otherNode, weight, metaNodes));
        return this;
    }
}
