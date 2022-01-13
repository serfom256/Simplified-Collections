package ListTests;

import lists.impl.SortedList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedListTests {
    SortedList<Integer> list;

    public SortedListTests() {
        this.list = new SortedList<>();
    }

    @Before
    public void setUp() {
        Integer[] testArrP = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3, 6};
        Integer[] testArrN = new Integer[]{-9, -2, -5, -7, -1, -8, -10, -4, -3, -6};
        for (int i = 0; i < testArrN.length; i++) {
            list.add(testArrP[i]);
            list.add(testArrN[i]);
        }
        assertEquals(20, list.getSize());
    }

    @Test
    public void add() {
        Integer[] testArrP = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3, 6};
        Integer[] testArrN = new Integer[]{-9, -2, -5, -7, -1, -8, -10, -4, -3, -6};
        for (int i = 0; i < testArrN.length; i++) {
            list.add(testArrP[i]);
            list.add(testArrN[i]);
        }
        assertEquals(40, list.getSize());
        for (int i = -10, pos = 0; i < 10; i++, pos += 2) {
            assertEquals((Integer) i, list.get(pos));
            assertEquals((Integer) i, list.get(pos + 1));
        }
    }

    @Test
    public void get() {
        for (int i = -10, pos = 0; i < 10; i++, pos++) {
            assertEquals((Integer) i, list.get(pos));
        }
        list.clear();
        Integer[] test = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3, 6};
        for (Integer i : test) {
            list.add(i);
        }
        for (int i = 0; i < 10; i++) {
            assertEquals((Integer) i, list.get(i));
        }
    }

    @Test
    public void getMin() {
        Integer[] test = new Integer[20];
        for (int i = -10, pos = 0; i < 10; i++, pos++) {
            test[pos] = i;
        }
        int pos = 0;
        while (list.getSize() != 0) {
            Integer element = list.getMin();
            assertEquals(test[pos++], element);
            list.deleteAtPosition(0);
        }
    }

    @Test
    public void getMax() {
        Integer[] test = new Integer[20];
        for (int i = 9, pos = 0; i >= -10; i--, pos++) {
            test[pos] = i;
        }
        int pos = 0;
        while (list.getSize() != 0) {
            Integer element = list.getMax();
            assertEquals(test[pos++], element);
            list.deleteAtPosition(list.getSize() - 1);
        }
    }

    @Test
    public void delete() {
        Integer[] testArrP = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3, 6};
        Integer[] testArrN = new Integer[]{-9, -2, -5, -7, -1, -8, -10, -4, -3, -6};
        for (int i = 0; i < testArrN.length; i++) {
            list.add(testArrP[i]);
            list.add(testArrN[i]);
        }
        assertEquals(40, list.getSize());
        int initialSize = 40;
        for (Integer i = -10; i < 10; i++) {
            System.out.println(i);
            assertTrue(list.delete(i));
            assertEquals(--initialSize, list.getSize());
            assertTrue(list.delete(i));
            assertEquals(--initialSize, list.getSize());
            assertFalse(list.delete(i));
            assertEquals(initialSize, list.getSize());
        }
    }

    @Test
    public void deleteAtPosition() {
        Integer[] test = new Integer[]{19, 9, 0, 4, 7, 1, 5, 3};
        int len = 20;
        for (Integer i : test) {
            assertEquals(list.get(i), list.deleteAtPosition(i));
            assertEquals(--len, list.getSize());
        }
    }

    @Test
    public void slice() {
        Integer[][] testArr = {{0, 1, 2, 3}, {-10, -9, -8, -7, -6, -5}, {7, 8, 9}, {3, 4}};
        assertArrayEquals(testArr[0], list.slice(10, 14).toArray(Integer.class));
        assertArrayEquals(testArr[1], list.slice(0, 6).toArray(Integer.class));
        assertArrayEquals(testArr[2], list.slice(17, 20).toArray(Integer.class));
        assertArrayEquals(testArr[3], list.slice(13, 15).toArray(Integer.class));
        assertArrayEquals(list.toArray(Integer.class), list.slice(0, list.getSize()).toArray(Integer.class));
        assertArrayEquals(list.toArray(Integer.class), list.slice(0, list.getSize()).toArray(Integer.class));
        assertEquals(20, list.getSize());
    }

    @Test
    public void clear() {
        list.clear();
        assertEquals(0, list.getSize());
        assertEquals(3, list.getHeight());
    }

    @Test
    public void count() {
        Integer[] testArrP = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3, 6};
        Integer[] testArrN = new Integer[]{-9, -2, -5, -7, -1, -8, -10, -4, -3, -6};
        for (int i = 0; i < testArrN.length; i++) {
            list.add(testArrP[i]);
            list.add(testArrN[i]);
        }
        assertEquals(40, list.getSize());
        for (Integer i : testArrN) {
            assertEquals(2, list.count(i));
        }
        assertEquals(0, list.count(999));
        assertEquals(0, list.count(-11));
        list.clear();
        assertEquals(0, list.count(5));
        assertEquals(0, list.count(0));
    }

    @Test
    public void indexOf() {
        list.add(9);
        assertEquals(21, list.getSize());
        assertEquals(19, list.indexOf(9));
        assertEquals(10, list.indexOf(0));
        assertEquals(0, list.indexOf(-10));
    }

    @Test
    public void lastIndexOf() {
        list.add(9);
        list.add(-10);
        assertEquals(22, list.getSize());
        assertEquals(21, list.lastIndexOf(9));

        assertEquals(11, list.lastIndexOf(0));
        assertEquals(1, list.lastIndexOf(-10));
    }

    @Test
    public void toArray() {
        Integer[] testArr = new Integer[20];
        for (int i = -10, pos = 0; i < 10; i++, pos++) {
            testArr[pos] = i;
        }
        assertArrayEquals(testArr, list.toArray(Integer.class));
    }

    @Test
    public void toObjectArray() {
        Integer[] testArr = new Integer[20];
        for (int i = -10, pos = 0; i < 10; i++, pos++) {
            testArr[pos] = i;
        }
        assertArrayEquals(testArr, list.toObjectArray());
    }

}
