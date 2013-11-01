package edu.uaskl.cpp.map.meta;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

public class WayNode implements Metadata {
	private static final double earthRadiusKm = 6371.0;

	private double latitude, longitude;

	public WayNode(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getDistanceKm(WayNode other) {
		double lon1 = longitude,
				lon2 = other.getLongitude(),
				lat1 = latitude,
				lat2 = other.getLatitude();
		return Math.acos(
					Math.sin(lat1) * Math.sin(lat2) +
					Math.cos(lat1) * Math.cos(lat2) *
					Math.cos(lon2 - lon1)) * earthRadiusKm;
	}
}
