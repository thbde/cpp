package edu.uaskl.cpp.model.edge.interfaces;

import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * An edge consists of the nodes: node1 and node2. An edge virtually connects them together.
 * 
 * @author tbach
 */
public interface Edge<K extends Node<K, V>, V extends Edge<K, V>> extends Comparable<Edge<K, V>> {

    public K getNode1();

    public void setNode1(K node1);

    public K getNode2();

    public void setNode2(K node2);

    /** Returns the node of the other side from this edge and the given node */
    public K getRelatedNode(Node<K, V> node);

}
