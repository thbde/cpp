package edu.uaskl.cpp.model.edge;

import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.edge.interfaces.EdgeWeighted;
import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * @author tbach
 */
public class EdgeCpp extends EdgeBasic<NodeCpp, EdgeCpp> implements EdgeWeighted<NodeCpp, EdgeCpp> {
    protected int weight = 0;
    private boolean isVisited = false;
	private List<OsmNode> metaNodes;

    /** Copy constructor. Creates a new edge with the same properties (nodes + weight) */
    public EdgeCpp(final EdgeCpp edge) {
        this(edge.getNode1(), edge.getNode2(), edge.getWeight());
        this.metaNodes = edge.getMetaNodes();
    }

    public EdgeCpp(final NodeCpp node1, final NodeCpp node2) {
        this(node1, node2, 0);
    }

    public EdgeCpp(final NodeCpp node1, final NodeCpp node2, final int weight) {
        super(node1, node2);
        this.weight = weight;
    }

    public EdgeCpp(NodeCpp node1, NodeCpp node2, int weight,
			List<OsmNode> metaNodes) {
    	super(node1, node2);
        this.weight = weight;
        this.metaNodes=metaNodes;
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

	public List<OsmNode> getMetaNodes() {
		return metaNodes;
	}
}
