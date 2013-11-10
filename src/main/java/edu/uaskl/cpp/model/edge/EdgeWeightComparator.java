package edu.uaskl.cpp.model.edge;

import java.util.Comparator;

/**
 * @author tbach
 */
public enum EdgeWeightComparator implements Comparator<EdgeCpp> {
    INSTANCE();

    @Override
    public int compare(final EdgeCpp first, final EdgeCpp second) {
        return (first.getWeight() < second.getWeight()) ? -1 : ((first.getWeight() == second.getWeight()) ? 0 : 1);
    }
}
