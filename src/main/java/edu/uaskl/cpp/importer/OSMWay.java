package edu.uaskl.cpp.importer;

import java.util.List;

public class OSMWay {
	public List<Long> nodes;
	public boolean roundabout;
	public String name;
	public OSMWay(List<Long> nodes,boolean roundabout, String name){
		this.nodes = nodes;
		this.roundabout = roundabout;
		this.name = name;
	}
}
