package edu.uaskl.cpp.model.edge.interfaces;

import java.util.List;

import edu.uaskl.cpp.importer.OsmImporter.OsmNode;
import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * Creates an edge for two given nodes. We need this fabric to instantiate to correct sub types in our graph.
 * 
 * @author tbach
 */
public interface EdgeCreator<T extends Node<T, V>, V extends Edge<T, V>> {
    public V create(final Node<T, V> node1, final Node<T, V> node2, final int weight);
    public V createM(final Node<T, V> node1, final Node<T, V> node2, final int weight,List<OsmNode> metaNodes);
}