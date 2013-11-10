package edu.uaskl.cpp.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.uaskl.cpp.tools.iterators.PrimitiveIterator;

/**
 * @author tbach
 */
public class CollectionTools {

    private static String join(final Iterator<?> iterator, final CharSequence separator) {
        if ((iterator == null) || !iterator.hasNext() || (separator == null))
            return "";

        final StringBuilder stringBuilder = new StringBuilder(256);
        stringBuilder.append(iterator.next());
        while (iterator.hasNext())
            stringBuilder.append(separator).append(iterator.next());
        return stringBuilder.toString();
    }

    /**
     * Joins a collection. Uses the toString methods to generate the result string.<br>
     * Example: separator = " - ", collection = {1,2,3}<br>
     * Result: "1 - 2 - 3"<br>
     * Hint: Ordering for unordered collections is non deterministic
     */
    public static String join(final CharSequence separator, final Collection<?> collection) {
        return join(collection.iterator(), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final String[] array) {
        return join(Arrays.asList(array).iterator(), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final Object... objects) {
        return join(Arrays.asList(objects).iterator(), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final byte[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final short[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final int[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final long[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final float[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final double[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final char[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** See: {@link #join(CharSequence, Collection)} */
    public static String join(final CharSequence separator, final boolean[] array) {
        return join(PrimitiveIterator.getIterator(array), separator);
    }

    /** This removes all elements with the given indices in the given collection. Modifies the given collection and given indices. */
    public static void removeIndices(final List<?> list, final List<Integer> indices) {
        Collections.sort(indices, Collections.reverseOrder());
        for (final int indicesItem : indices)
            list.remove(indicesItem);
    }

    /**
     * Generates an incrementing list from from to inclusive toInclusive.<br>
     * E.g.: from:2, toInclusive:5<br>
     * result: List[2,3,4,5]
     */
    public static List<Integer> getListFromToInclusive(final int from, final int toInclusive) {
        final List<Integer> list = new ArrayList<>();
        for (int i = from; i <= toInclusive; ++i)
            list.add(i);
        return list;
    }
}