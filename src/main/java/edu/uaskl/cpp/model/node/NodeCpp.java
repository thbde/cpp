package edu.uaskl.cpp.model.node;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.edge.EdgeCreatorCpp;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;

/**
 * Chinese postman problem - node. Has all required special properties to solve Chinese postman problem
 * 
 * @author tbach
 */
public class NodeCpp extends NodeExtended<NodeCpp, EdgeCpp> {
    private static final EdgeCreator<NodeCpp, EdgeCpp> edgeCreator = new EdgeCreatorCpp();

    public NodeCpp() {
        super(edgeCreator);
    }
    public NodeCpp(Long id) {
        super(id, edgeCreator);
    }
    /** Copy constructor, creates a new node with the same properties */
    public NodeCpp(final NodeCpp otherNode) {
        super(otherNode);
    }
    /** Copy constructor for cpp algorithm, creates a new node with the same properties but marks it as a cpp node */
    public NodeCpp(final NodeCpp knoten, final boolean isForCpp) { // TODO static fabric method?
        super(knoten, isForCpp);
    }
}
