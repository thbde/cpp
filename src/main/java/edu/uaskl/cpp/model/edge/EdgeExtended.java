package edu.uaskl.cpp.model.edge;

import edu.uaskl.cpp.model.edge.interfaces.EdgeWeighted;
import edu.uaskl.cpp.model.node.NodeExtended;

public class EdgeExtended<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> extends EdgeBasic<T, V>  implements EdgeWeighted<T, V> {
    protected double weight = 0;
    private boolean isVisited = false;

    /** Copy constructor. Creates a new edge with the same properties (nodes + weight) */
    public EdgeExtended(final V edge) {
        this(edge.getNode1(), edge.getNode2(), edge.getWeight());
    }

    public EdgeExtended(final T node1, final T node2) {
        this(node1, node2, 0);
    }

    public EdgeExtended(final T node1, final T node2, final double weight) {
        super(node1, node2);
        this.weight = weight;
    }

	@Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(final double weight) {
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

}
