package edu.uaskl.cpp.model.edge.interfaces;

import edu.uaskl.cpp.model.node.interfaces.Node;

/**
 * A weighted edge is an edge with weight.
 * 
 * @author tbach
 */
public interface EdgeWeighted<K extends Node<K, V>, V extends Edge<K, V>> extends Edge<K, V> {
    public double getWeight();

    public void setWeight(double gewicht);
}
