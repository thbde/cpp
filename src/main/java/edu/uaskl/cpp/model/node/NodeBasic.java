package edu.uaskl.cpp.model.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.uaskl.cpp.model.edge.EdgeBasic;
import edu.uaskl.cpp.model.edge.interfaces.Edge;
import edu.uaskl.cpp.model.edge.interfaces.EdgeCreator;
import edu.uaskl.cpp.model.node.interfaces.Node;
import edu.uaskl.cpp.tools.CollectionTools;

/**
 * @author tbach
 */
public class NodeBasic<T extends NodeBasic<T, V>, V extends EdgeBasic<T, V>> implements Node<T, V> {
    private final static AtomicInteger counter = new AtomicInteger(0);
    protected Long nodeId = (long) (counter.getAndIncrement());
    private String name;
    protected List<V> edges = new ArrayList<>();
    private final EdgeCreator<T, V> edgeCreator;
    private double distance;
    private T previous;
    
    public double getDistance()
    {
    	return distance;
    }
    
    public void setDistance(double d)
    {
    	this.distance = d;
    }
    
    public T getPrevious()
    {
    	return previous;
    }
    
    public void setPrevious(T p)
    {
    	this.previous = p;
    }
    
    protected NodeBasic(final String name, final EdgeCreator<T, V> edgeCreator) {
        this.name = name.isEmpty() ? nodeId.toString() : name;
        this.edgeCreator = edgeCreator;
    }

    public NodeBasic(final EdgeCreator<T, V> kantenCreator) {
        this("", kantenCreator);
    }

    /** Copy constructor. Creates new node with same properties */
    public NodeBasic(final T knoten) {
        this(knoten.getName(), knoten.getEdgeCreator());
        this.setEdges(knoten.getEdges());
    }

    public EdgeCreator<T, V> getEdgeCreator() {
        return edgeCreator;
    }

    /** Running time: O(|AnzahlKantenAnDiesemKnoten|) */
    @Override
    public V getEdgeToNode(final T otherNode) {
        V result = null;
        for (final V edgesItem : edges)
            if (otherNode.equals(edgesItem.getRelatedNode(this))) {
                result = edgesItem;
                break;
            }
        if (result == null)
            throw new IllegalStateException("Node " + this + " has no connection to node: " + otherNode);
        return result;
    }

    /** Completely changes the current edges list */
    public void setEdges(final List<V> kanten) {
        this.edges = kanten;
    }

    @Override
    public List<V> getEdges() {
        return edges;
    }

    @Override
    public NodeBasic<T, V> addEdge(final V newEdge) {
        this.edges.add(newEdge);
        newEdge.getRelatedNode(this).addEdgeOnlyForThisNode(newEdge);
        return this;
    }

    @Override
    public NodeBasic<T, V> addEdgeOnlyForThisNode(final V newEdge) {
        this.edges.add(newEdge);
        return this;
    }

    @Override
    public NodeBasic<T, V> connectWithNode(final T otherNode) {
        return connectWithNodeAndWeigth(otherNode, 0);
    }

    @Override
    public NodeBasic<T, V> connectWithNodeAndWeigth(final T otherNode, final double weight) {
        addEdge(edgeCreator.create((T)this, otherNode, weight));
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getId() {
        return this.nodeId;
    }

    /** Constant running time ( Arraylist.size() ) **/
    @Override
    public int getDegree() {
        return edges.size();
    }

    /** Constant running time ( Arraylist.size() ) **/
    @Override
    public boolean isDegreeOdd() {
        return ((edges.size() & 1) == 1);
    }

    /** Constant running time ( Arraylist.size() ) **/
    @Override
    public boolean isDegreeEven() {
        return ((edges.size() & 1) == 0);
    }

    protected void resetVisitedState() {
        this.isVisited.set(false);
    }

    public boolean isVisited() {
        return isVisited.get();
    }

    public void setVisited() {
        this.isVisited.set(true);
    }

    @Override
    public int compareTo(final Node<T, V> otherNode) {
        return (getName().compareTo(otherNode.getName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
        final NodeBasic<T, V> other = (NodeBasic<T, V>) object;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(getName()).append("]");
        stringBuilder.append("-->").append("[");
        Collections.sort(edges);
        stringBuilder.append(CollectionTools.join(", ", edges));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /** Running time: O(|EdgesFromThisNode|*|EdgesFromOtherNode|), returns true if any changes were done */
    @Override
    public boolean removeAllEdges() {
        boolean result = false;
        for (final Edge<T, V> edgesItem : edges) {
            final Node<T, V> otherNode = edgesItem.getRelatedNode(this);
            result |= otherNode.removeEdgeTo(this);
        }
        edges = new ArrayList<>();
        return result;
    }

    /** Running Time: O(|edges|), returns true if any changes were done */
    @Override
    public boolean removeEdgeTo(final Node<T, V> otherNode) {
        Edge<T, V> edgeToRemove = null;
        for (final Edge<T, V> edgesItem : edges)
            if (edgesItem.getNode1().equals(otherNode) || edgesItem.getNode2().equals(otherNode)) {
                edgeToRemove = edgesItem;
                break;
            }
        if (edgeToRemove == null)
            throw new IllegalArgumentException("There is no edge from this node (" + this + " ) to node: " + otherNode);
        return edges.remove(edgeToRemove);
    }

    private final AtomicBoolean isVisited = new AtomicBoolean(false);

    /**
     * Multithread save, lock free.<br>
     * Returns true iff this thread changes the visited state from false to true.<br>
     * Returns false in all other cases.
     */
    public boolean changeVisitedState() {
        final boolean expect = false;
        final boolean update = true;
        final boolean result = isVisited.compareAndSet(expect, update); // only true for the thread who really changes the state
        return result;
    }
}
