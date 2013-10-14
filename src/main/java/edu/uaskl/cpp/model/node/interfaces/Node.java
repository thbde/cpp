package edu.uaskl.cpp.model.node.interfaces;

import java.util.List;

import edu.uaskl.cpp.model.edge.interfaces.Edge;

/**
 * Interface for a Node; the generics bind node and edge together.
 * 
 * @author tbach
 */
public interface Node<T extends Node<T, V>, V extends Edge<T, V>> extends Comparable<Node<T, V>> {

    /** Gets the edge between this node and the given node */
    public V getEdgeToNode(T otherNode);

    public List<? extends Edge<T, V>> getEdges();

    /** Adds the edge in a consistent way. E.g. for both corresponding nodes */
    public Node<T, V> addEdge(V newEdge);

    public Node<T, V> addEdgeOnlyForThisNode(V newEdge);

    public Node<T, V> connectWithNode(Node<T, V> otherNode);

    /** Only for weighted graphs */
    public Node<T, V> connectWithNodeAndWeigth(Node<T, V> otherNode, int weight);

    /** Gets an optional name representation */
    public String getName();

    /** Gets the degree, that means the number of connected edges */
    public int getDegree();

    /** Returns true if {@link #getDegree()} does not return a multiple of 2 */
    public boolean isDegreeOdd();

    /** Returns true if {@link #getDegree()} returns a multiple of 2 */
    public boolean isDegreeEven();

    /** Returns true if any changes were done, false if nothing happened */
    public boolean removeAllEdges();

    /** Returns true if any changes were done, false if nothing happened */
    public boolean removeEdgeTo(Node<T, V> knoten);

}
