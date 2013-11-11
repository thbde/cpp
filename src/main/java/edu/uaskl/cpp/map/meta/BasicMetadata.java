package edu.uaskl.cpp.map.meta;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

public class BasicMetadata implements Metadata {
	private final long id;
		
	BasicMetadata(long id) {
		this.id = id;
	}
		
	public long getID() {
		return id;
	}
}
