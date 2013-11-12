package edu.uaskl.cpp.map.meta;

import java.util.Date;

//TODO add javadoc with description and purpose of this class. -tbach

public class WayNodeOSM extends WayNode {
    private final long id;
    private final Date timestamp;
    private final long changeset;

    public WayNodeOSM(final double latitude, final double longitude, final long id) {
        this(latitude, longitude, id, null, 0);
    }

    public WayNodeOSM(final double latitude, final double longitude, final long id, final Date timestamp, final long changeset) {
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
