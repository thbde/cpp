package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.model.edge.interfaces.EdgeWeighted;
import edu.uaskl.cpp.model.meta.interfaces.Metadata;
import edu.uaskl.cpp.model.meta.interfaces.MetadataAnnotated;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * @author tbach
 */
public class EdgeCpp<M extends Metadata> extends EdgeBasic<NodeCpp<M>, EdgeCpp<M>> implements EdgeWeighted<NodeCpp<M>, EdgeCpp<M>>, MetadataAnnotated<M> {
    protected int weight = 0;
    private boolean isVisited = false;
    private M metadata;

    /** Copy constructor. Creates a new edge with the same properties (nodes + weight) */
    public EdgeCpp(final EdgeCpp<M> edge) {
        this(edge.getNode1(), edge.getNode2(), edge.getWeight(), null);
    }

    public EdgeCpp(final NodeCpp<M> node1, final NodeCpp<M> node2) {
        this(node1, node2, 0, null);
    }

    public EdgeCpp(final NodeCpp<M> node1, final NodeCpp<M> node2, final int weight, M metadata) {
        super(node1, node2);
        this.weight = weight;
        this.metadata = metadata;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(final int weight) {
        this.weight = weight;
    }

    private void resetVisited() {
        this.isVisited = false;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited() {
        this.isVisited = true;
    }

    public void resetState() {
        resetVisited();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(getNode1().getName()).append("<--").append(getWeight()).append("-->").append(getNode2().getName()).append(")");
        return stringBuilder.toString();
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
