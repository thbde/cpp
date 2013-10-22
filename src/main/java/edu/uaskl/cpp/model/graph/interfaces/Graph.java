package edu.uaskl.cpp.model.graph.interfaces;

import java.util.Map;

import edu.uaskl.cpp.algorithmen.Algorithms;
import edu.uaskl.cpp.model.edge.interfaces.Edge;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * A Graph consists of two parts: A set of nodes and a set of edges<br>
 * This interfaces models a graph as a set of nodes and each node has a set of edges.
 * Therefore it is based on the implementation if the graph knows all the edges directly or not.<br>
 * In addition, for convenience, an algorithms instance is required to provide all possible algorithms for the graph.<br>
 * New algorithms can be implemented outside the graph at any time.
 * 
 * @author tbach
 */
public interface Graph<T extends Node<T, V>, V extends Edge<T, V>> {
    public String getName();

    public Map<String,T> getNodes();

    public Graph<T, V> addNode(T node);

    public int getNumberOfNodes();

    public int getGetNumberOfEdges();

    public Algorithms getAlgorithms();
}
