package edu.uaskl.cpp.tools.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wraps a primitive array in an iterator.
 * 
 * @author tbach
 */
public class PrimitiveIterator<T> implements Iterator<T> {
    private final ArrayWrapper<T> arrayWrapper;
    private int index = 0;

    public PrimitiveIterator(final ArrayWrapper<T> arrayWrapper) {
        this.arrayWrapper = arrayWrapper;
    }

    @Override
    public boolean hasNext() {
        return (index < arrayWrapper.getLength());
    }

    @Override
    public T next() {
        if (index >= arrayWrapper.getLength())
            throw new NoSuchElementException("Array index: " + index);
        return arrayWrapper.getValue(index++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(); // we can not remove elements of an array (tbach)
    }

    public static Iterator<Byte> getIterator(final byte[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Byte>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Byte getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Short> getIterator(final short[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Short>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Short getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Integer> getIterator(final int[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Integer>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Integer getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Long> getIterator(final long[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Long>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Long getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Float> getIterator(final float[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Float>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Float getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Double> getIterator(final double[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Double>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Double getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Character> getIterator(final char[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Character>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Character getValue(final int index) {
                return array[index];
            }
        });
    }

    public static Iterator<Boolean> getIterator(final boolean[] array) {
        return new PrimitiveIterator<>(new ArrayWrapper<Boolean>() {

            @Override
            public int getLength() {
                return array.length;
            }

            @Override
            public Boolean getValue(final int index) {
                return array[index];
            }
        });
    }

    public static interface ArrayWrapper<T> {
        public int getLength();

        public T getValue(int index);
    }
}
