package edu.uaskl.cpp.model.edge;

import java.util.concurrent.atomic.AtomicInteger;

import edu.uaskl.cpp.model.edge.interfaces.Edge;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * @author tbach
 */
public class EdgeBasic<K extends Node<K, V>, V extends Edge<K, V>> implements Edge<K, V> {
    private final static AtomicInteger counter = new AtomicInteger(0);
    private final int id = counter.getAndIncrement();
    private K node1;
    private K node2;

    /** Copy constructor. Create a new basic edge with the same properties (nodes) */
    public EdgeBasic(final V kante) {
        this(kante.getNode1(), kante.getNode2());
    }

    public EdgeBasic(final K node1, final K node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public K getNode1() {
        return node1;
    }

    @Override
    public void setNode1(final K node1) {
        this.node1 = node1;
    }

    @Override
    public K getNode2() {
        return node2;
    }

    @Override
    public void setNode2(final K node2) {
        this.node2 = node2;
    }

    /** Returns the node of the other side from this edge and the given node */
    @Override
    public K getRelatedNode(final Node<K, V> node) {
        if (node1.equals(node))
            return node2;
        else if (node2.equals(node))
            return node1;
        else
            throw new IllegalArgumentException("Given node is not connected to this edge. This edge: " + this + ", given node: " + node);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked")
        final EdgeBasic<K, V> other = (EdgeBasic<K, V>) object;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int compareTo(final Edge<K, V> other) {
        int result = node1.compareTo(other.getNode1());
        if (result == 0)
            result = node2.compareTo(other.getNode2());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(node1.getName()).append("<-->").append(node2.getName()).append(")");
        return stringBuilder.toString();
    }
}