package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.meta.MetadataCreatorCpp;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * @author tbach
 */
public class EdgeCreatorCpp<M extends Metadata> implements EdgeCreator<NodeCpp<M>, EdgeCpp<M>> {
	private MetadataCreatorCpp<M> metadataCreator;

	public EdgeCreatorCpp(MetadataCreatorCpp<M> metadataCreator) {
		this.metadataCreator = metadataCreator;
	}

    @Override
    public EdgeCpp<M> create(final Node<NodeCpp<M>, EdgeCpp<M>> node1, final Node<NodeCpp<M>, EdgeCpp<M>> node2, final int weight) {
        return new EdgeCpp<M>((NodeCpp<M>) node1, (NodeCpp<M>) node2, weight, metadataCreator.newEdgeMetadata((NodeCpp<M>)node1, (NodeCpp<M>)node2));
    }

}
