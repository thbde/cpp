package edu.uaskl.cpp.model.path.interfaces;

import java.util.List;

import edu.uaskl.cpp.model.node.NodeCpp;

/**
 * A path is an ordered list of nodes together with a distance.
 * 
 * @author tbach
 */

public interface Path {
    public int getDistance();

    public int getLength();

    public List<NodeCpp> getNodes();
}
