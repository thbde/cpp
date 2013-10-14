package edu.uaskl.cpp.tools;

import static org.fest.assertions.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.uaskl.cpp.tools.CollectionTools;

public class CollectionToolsTest {
    private static final String emptyString = "";
    private static final String separatorSpace = " ";
    private static final String separatorEmpty = emptyString;
    private static final String separatorHyphen = " - ";
    private static final String separatorNull = null;

    @Test
    public void testConstructor() {
        assertThat(new CollectionTools()).isNotNull();
    }

    @Test
    public void testNullIterator() {
        final List<String> list = new ArrayList<String>() {
            private static final long serialVersionUID = 2131977313613615092L;

            @Override
            public Iterator<String> iterator() {
                return null;
            }
        };
        assertThat(CollectionTools.join(separatorSpace, list)).isEqualTo(emptyString);
    }

    @Test
    public void testEmptyCollection() {
        final List<String> list = new ArrayList<>();
        assertThat(CollectionTools.join(separatorSpace, list)).isEqualTo(emptyString);
    }

    @Test
    public void testNullSeparator() {
        final List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
        assertThat(CollectionTools.join(separatorNull, list)).isEqualTo(emptyString);
    }

    @Test
    public void testJoinString() {
        final String[] array = { "H", "e", "l", "l", "o" };
        assertThat(CollectionTools.join(separatorSpace, array)).isEqualTo("H e l l o");
    }

    @Test
    public void testJoin() {
        final List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 });
        assertThat(CollectionTools.join(separatorHyphen, list)).isEqualTo("1 - 2 - 3 - 4 - 5 - 6 - 7");
    }

    @Test
    public void testJoin2() {
        final List<Integer> list = Arrays.asList(new Integer[] { 1 });
        assertThat(CollectionTools.join(separatorHyphen, list)).isEqualTo("1");
    }

    @Test
    public void testJoin3() {
        final List<Integer> list = Arrays.asList(new Integer[] {});
        assertThat(CollectionTools.join(separatorHyphen, list)).isEqualTo(emptyString);
    }

    @Test
    public void testJoin4() {
        final List<Integer> list = Arrays.asList(new Integer[] { 1, 2 });
        assertThat(CollectionTools.join(separatorHyphen, list)).isEqualTo("1 - 2");
    }

    @Test
    public void testJoin5() {
        final List<Integer> list = Arrays.asList(new Integer[] { 1, 2 });
        assertThat(CollectionTools.join(separatorEmpty, list)).isEqualTo("12");
    }

    @Test
    public void testJoinVarArg() {
        assertThat(CollectionTools.join(separatorHyphen, 1, 2, 3, 4, 5, 6, 7)).isEqualTo("1 - 2 - 3 - 4 - 5 - 6 - 7");
    }

    @Test
    public void testJoinVarArg2() {
        assertThat(CollectionTools.join(separatorHyphen, 1)).isEqualTo("1");
    }

    @Test
    public void testJoinVarArg3() {
        assertThat(CollectionTools.join(separatorHyphen, (Object[]) new Integer[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinVarArg4() {
        assertThat(CollectionTools.join(separatorHyphen, 1, 2)).isEqualTo("1 - 2");
    }

    @Test
    public void testJoinVarArg5() {
        assertThat(CollectionTools.join(separatorEmpty, 1, 2)).isEqualTo("12");
    }

    @Test
    public void testJoinByte() {
        assertThat(CollectionTools.join(separatorSpace, new byte[] { 1, 2 })).isEqualTo("1 2");
    }

    @Test
    public void testJoinByteSingle() {
        assertThat(CollectionTools.join(separatorSpace, new byte[] { 1 })).isEqualTo("1");
    }

    @Test
    public void testJoinByteZero() {
        assertThat(CollectionTools.join(separatorSpace, new byte[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinShort() {
        assertThat(CollectionTools.join(separatorSpace, new short[] { 1, 2 })).isEqualTo("1 2");
    }

    @Test
    public void testJoinShortSingle() {
        assertThat(CollectionTools.join(separatorSpace, new short[] { 1 })).isEqualTo("1");
    }

    @Test
    public void testJoinShortZero() {
        assertThat(CollectionTools.join(separatorSpace, new short[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinInt() {
        assertThat(CollectionTools.join(separatorSpace, new int[] { 1, 2 })).isEqualTo("1 2");
    }

    @Test
    public void testJoinIntSingle() {
        assertThat(CollectionTools.join(separatorSpace, new int[] { 1 })).isEqualTo("1");
    }

    @Test
    public void testJoinIntZero() {
        assertThat(CollectionTools.join(separatorSpace, new int[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinLong() {
        assertThat(CollectionTools.join(separatorSpace, new long[] { 1, 2 })).isEqualTo("1 2");
    }

    @Test
    public void testJoinLongSingle() {
        assertThat(CollectionTools.join(separatorSpace, new long[] { 1 })).isEqualTo("1");
    }

    @Test
    public void testJoinLongZero() {
        assertThat(CollectionTools.join(separatorSpace, new long[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinFloat() {
        assertThat(CollectionTools.join(separatorSpace, new float[] { 1.25f, 2.25f })).isEqualTo("1.25 2.25");
    }

    @Test
    public void testJoinFloatSingle() {
        assertThat(CollectionTools.join(separatorSpace, new float[] { 1.25f })).isEqualTo("1.25");
    }

    @Test
    public void testJoinFloatZero() {
        assertThat(CollectionTools.join(separatorSpace, new float[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinDouble() {
        assertThat(CollectionTools.join(separatorSpace, new double[] { 1.25, 2.25 })).isEqualTo("1.25 2.25");
    }

    @Test
    public void testJoinDoubleSingle() {
        assertThat(CollectionTools.join(separatorSpace, new double[] { 1.25 })).isEqualTo("1.25");
    }

    @Test
    public void testJoinDoubleZero() {
        assertThat(CollectionTools.join(separatorSpace, new double[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinBoolean() {
        assertThat(CollectionTools.join(separatorSpace, new boolean[] { true, false })).isEqualTo("true false");
    }

    @Test
    public void testJoinBooleanSingle() {
        assertThat(CollectionTools.join(separatorSpace, new boolean[] { true })).isEqualTo("true");
    }

    @Test
    public void testJoinBooleanZero() {
        assertThat(CollectionTools.join(separatorSpace, new boolean[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testJoinChar() {
        assertThat(CollectionTools.join(separatorSpace, new char[] { '1', '2' })).isEqualTo("1 2");
    }

    @Test
    public void testJoinCharSingle() {
        assertThat(CollectionTools.join(separatorSpace, new char[] { '1' })).isEqualTo("1");
    }

    @Test
    public void testJoinCharZero() {
        assertThat(CollectionTools.join(separatorSpace, new char[] {})).isEqualTo(emptyString);
    }

    @Test
    public void testRemoveIndices() {
        final List<Integer> list = CollectionTools.getListFromToInclusive(0, 10);
        final List<Integer> indicesToRemove = new ArrayList<>(Arrays.asList(3, 7, 1, 5));
        CollectionTools.removeIndices(list, indicesToRemove);

        assertThat(list.contains(1)).isFalse();
        assertThat(list.contains(2)).isTrue();
        assertThat(list.contains(3)).isFalse();
        assertThat(list.contains(5)).isFalse();
        assertThat(list.contains(7)).isFalse();
        assertThat(list.contains(9)).isTrue();
        assertThat(list.contains(10)).isTrue();
        assertThat(list.get(0)).isEqualTo(0);
        assertThat(list.get(list.size() - 1)).isEqualTo(10);
    }

    @Test
    public void testGetListFromToInclusive() {
        final int start = 2;
        final int end = 5;
        final List<Integer> list = CollectionTools.getListFromToInclusive(start, end);
        assertThat(list.size()).isEqualTo((end + 1) - start); // end inclusive (tbach)
        for (int i = 0; i < list.size(); i++)
            assertThat(list.get(i)).isEqualTo(i + start);
    }
}
