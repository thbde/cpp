package edu.uaskl.cpp.map.meta;

import java.util.Date;

public class WayNodeOSM extends WayNode {
	private final long id, changeset;
	private final Date timestamp;

	public WayNodeOSM(double latitude, double longitude, long id) {
		this(latitude, longitude, id, null, 0);
	}

	public WayNodeOSM(double latitude, double longitude, long id, Date timestamp, long changeset) {
		super(latitude, longitude);
		this.id = id;
		this.timestamp = timestamp;
		this.changeset = changeset;
	}
	
	public long getID() {
		return id;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public long getChangeset() {
		return changeset;
	}
}
