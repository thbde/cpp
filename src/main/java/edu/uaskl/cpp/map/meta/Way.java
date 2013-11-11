package edu.uaskl.cpp.map.meta;

import java.util.ArrayList;
import java.util.List;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

public class Way implements Metadata {
	public enum WayType {
		UNSPECIFIED, HIGHWAY
	};

	private WayType type;
	private String name;
	private List<WayNode> nodes;
	
	public Way() {
		this(WayType.UNSPECIFIED);
	}
	
	public Way(WayType type) {
		this(type, "unspecified way");
	}
	
	public Way(WayType type, String name) {
		nodes = new ArrayList<WayNode>();
		this.type = type;
		this.name = name;
	}

	public WayType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public List<WayNode> getNodes() {
		return nodes;
	}
}
