package edu.uaskl.cpp.map.meta;

import java.util.List;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

//TODO add javadoc with description and purpose of this class. -tbach

public class WayOSM implements Metadata {
    public enum WayType { // TODO why is this public? at least, it should be static, because it is not needed separately for each instance -tbach
        UNSPECIFIED, HIGHWAY;
    }

    private final long id;
    private final WayType type;
    private final String name;
    private final List<WayNodeOSM> nodes;

    public WayOSM(final long id) {
        this(id, WayType.UNSPECIFIED);
    }

    public WayOSM(final long id, final WayType type) {
        this(id, type, "unspecified way");
    }

    public WayOSM(final long id, final WayType type, final String name) {
        this(id, type, name, null);
    }

    public WayOSM(final long id, final WayType type, final String name, final List<WayNodeOSM> nodes) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.nodes = nodes;
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
