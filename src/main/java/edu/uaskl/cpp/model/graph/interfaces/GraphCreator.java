package edu.uaskl.cpp.model.graph.interfaces;

import edu.uaskl.cpp.model.edge.interfaces.*;
import edu.uaskl.cpp.model.node.interfaces.*;

public interface GraphCreator<T extends Node<T, V>, V extends Edge<T, V>> {
	public Graph<T, V> create(); 
}
