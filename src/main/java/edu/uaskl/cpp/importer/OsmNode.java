package edu.uaskl.cpp.importer;

public class OsmNode {
	public long lat;
	public long lon;
	public Long id;

	public OsmNode( String id,long lat, long lon) {
		this.lat = lat;
		this.lon = lon;
		this.id = Long.parseLong(id, 10);
	}
	
	public OsmNode( Long id,long lat, long lon) {
		this.lat = lat;
		this.lon = lon;
		this.id = id;
	}
}