package edu.uaskl.cpp.model.edge.interfaces;

import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * Creates an edge for two given nodes. We need this fabric to instantiate to correct sub types in our graph.
 * 
 * @author tbach
 */
public interface EdgeCreator<T extends Node<T, V>, V extends Edge<T, V>> {
    public V create(final T node1, final T node2, final int weight);
 }