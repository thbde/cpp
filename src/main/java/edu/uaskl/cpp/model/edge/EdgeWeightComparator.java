package edu.uaskl.cpp.model.edge;

import java.util.Comparator;

import edu.uaskl.cpp.model.meta.interfaces.Metadata;

/**
 * @author tbach
 */
public enum EdgeWeightComparator implements Comparator<EdgeCpp<? extends Metadata>> {
    INSTANCE();

    @Override
    public int compare(final EdgeCpp<? extends Metadata> first, final EdgeCpp<? extends Metadata> second) {
        return (first.getWeight() < second.getWeight()) ? -1 : ((first.getWeight() == second.getWeight()) ? 0 : 1);
    }
}
