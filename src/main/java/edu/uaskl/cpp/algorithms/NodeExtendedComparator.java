package edu.uaskl.cpp.algorithms;

import java.util.Comparator;

import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.node.NodeExtended;

public class NodeExtendedComparator<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> implements Comparator<T> {
	private T startNode;
	public NodeExtendedComparator(T start) {
		startNode = start;
	}
	public double getDistance(final T a, final T b) {
		double latA = a.lat/180*Math.PI;
		double latB = b.lat/180*Math.PI;
		double lonDif = (b.lon - a.lon)/180*Math.PI;
		return (Math.acos((Math.sin(latA) * Math.sin(latB)) + (Math.cos(latA) * Math.cos(latB) * Math.cos(lonDif))) * 6367500);
	}
	
	@Override
	public int compare(T arg0, T arg1) {
		double dist0 = getDistance(startNode,arg0);
		double dist1 = getDistance(startNode,arg1);
		if(dist0 < dist1) {
			return -1;
		}
		if(dist0 > dist1) {
			return 1;
		}
		return 0;
	}
	
}