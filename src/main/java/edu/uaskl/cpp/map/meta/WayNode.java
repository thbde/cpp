package edu.uaskl.cpp.map.meta;

//TODO add javadoc with description and purpose of this class.

public class WayNode {
    private static final long EARTH_RADIUS_IN_KM = 6371; // average radius of 6.371.000,785 according to http://de.wikipedia.org/w/index.php?title=Erdradius&oldid=124362845 -tbach

    private final double latitude;
    private final double longitude;

    public WayNode(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // TODO could provide getLat/LongitudeAsInt getter -tbach

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // FIXME: Untested.
    public double getDistanceKm(final WayNode other) {
        // TODO could be a bit more readable + see comments from OsmImporter#getDistance -tbach
        final double lon1 = longitude, lon2 = other.getLongitude(), lat1 = latitude, lat2 = other.getLatitude();
        return Math.acos((Math.sin(lat1) * Math.sin(lat2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))) * EARTH_RADIUS_IN_KM;
    }

    public static double getDistanceKm(final WayNode first, final WayNode second) {
        return first.getDistanceKm(second);
    }
}
