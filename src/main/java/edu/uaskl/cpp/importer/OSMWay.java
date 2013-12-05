package edu.uaskl.cpp.importer;

import java.util.List;

public class OSMWay {
	public List<Long> nodes;
	public boolean roundabout;
	public String name;
	public boolean directed;
	public OSMWay(List<Long> nodes,boolean roundabout, String name, boolean directed){
		this.nodes = nodes;
		this.roundabout = roundabout;
		this.name = name;
		this.directed = directed;
	}
}
