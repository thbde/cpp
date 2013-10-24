package edu.uaskl.cpp.model.edge;

import java.util.List;

import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.model.node.interfaces.Node;
import edu.uaskl.cpp.importer.OsmImporter.OsmNode;

/**
 * @author tbach
 */
public class EdgeCreatorCpp implements EdgeCreator<NodeCpp, EdgeCpp> {

    @Override
    public EdgeCpp create(final Node<NodeCpp, EdgeCpp> node1, final Node<NodeCpp, EdgeCpp> node2, final int weight) {
        return new EdgeCpp((NodeCpp) node1, (NodeCpp) node2, weight);
    }

	
	public EdgeCpp create(Node<NodeCpp, EdgeCpp> node1, Node<NodeCpp, EdgeCpp> node2, int weight,List<OsmNode> metaNodes) {
		return new EdgeCpp((NodeCpp) node1, (NodeCpp) node2, weight,metaNodes);
	}
}
