package edu.uaskl.cpp.map.meta;

import java.util.ArrayList;
import java.util.List;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

public class WayOSM implements Metadata {
	public enum WayType {
		UNSPECIFIED, HIGHWAY
	};

	private final long id;
	private final WayType type;
	private final String name;
	private final List<WayNodeOSM> nodes;
	
	public WayOSM(long id) {
		this(id, WayType.UNSPECIFIED);
	}
	
	public WayOSM(long id, WayType type) {
		this(id, type, "unspecified way");
	}

	public WayOSM(long id, WayType type, String name) {
		nodes = new ArrayList<WayNodeOSM>();
		this.id = id;
		this.type = type;
		this.name = name;
	}

	public long getID() {
		return id;
	}

	public WayType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public List<WayNodeOSM> getNodes() {
		return nodes;
	}
}
