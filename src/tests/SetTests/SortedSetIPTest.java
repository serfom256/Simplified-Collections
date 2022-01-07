package tests.SetTests;

import Sets.Set;
import Sets.SortedSetIP;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class SortedSetIPTest {
    SortedSetIP<String> set;

    public SortedSetIPTest() {
        set = new SortedSetIP<>();
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            set.add(i + "_element");
        }
    }

    @Test
    public void addAll() {
        set.addAll("data1", "data2", "data3", "data4");
        assertEquals(14, set.getSize());
    }

    @Test
    public void get() {
        for (int i = 0; i < 10; i++) {
            assertEquals(i + "_element", set.get(i));
        }
        assertEquals(10,set.getSize());
    }

    @Test
    public void update() {
        for (int i = 0; i < 20; i++) {
            set.update(i + "_element", i + "_data");
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(set.contains(i + "_data"));
            assertFalse(set.contains(i + "_element"));
        }
        assertFalse(set.contains(11 + "_data"));

        assertEquals(10, set.getSize());
    }

    @Test
    public void add() {
        for (int i = 0; i < 20000; i++) {
            set.add(i + "_element");
        }
        assertEquals(20000, set.getSize());
        for (int i = 0; i < 20000; i++) {
            assertTrue(set.contains(i + "_element"));
        }
        assertEquals(20000, set.getSize());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 1000; i++) {
            set.add(i + "_element");
        }
        for (int i = 0; i < 1000; i++) {
            set.remove(i + "_element");
        }
        assertFalse(set.remove("some_element"));
        assertEquals(0,set.getSize());
    }

    @Test
    public void contains() {
        for (int i = 0; i < 100; i++) {
            set.add(i + "_element");
        }
        assertEquals(100, set.getSize());
        for (int i = 0; i < 100; i++) {
            assertTrue(set.contains(i + "_element"));
        }
    }

    @Test
    public void getMin() {
        assertEquals("0_element",set.getMin());
    }

    @Test
    public void getMax() {
        assertEquals("9_element",set.getMax());
    }

    @Test
    public void clear() {
        set.clear();
        assertEquals(0, set.getSize());
        assertFalse(set.contains("0_value"));
    }

    @Test
    public void getSize() {
        assertEquals(10, set.getSize());
    }

    @Test
    public void forEach() {
        String[] elements = new String[10];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = i + "_element";
        }
        int count = 0;
        for (String s : set) {
            for (String element : elements) {
                if (s.equals(element)) {
                    count++;
                }
            }
        }
        assertEquals(elements.length, count);
    }

    @Test
    public void left() {
        Set<Integer> testSet = new Set<>();
        Set<Integer> fSet = new Set<>();
        for (int i = 0; i < 10; i++) {
            fSet.add(i);
        }
        testSet.add(1);
        testSet.add(2);
        testSet.add(11);
        testSet.add(12);
        Set<Integer> newSet = fSet.left(testSet);
        assertEquals(2, newSet.getSize());
        assertTrue(newSet.contains(11));
        assertTrue(newSet.contains(11));
        for (int i = 0; i < 10; i++) {
            assertFalse(newSet.contains(i));
        }
    }

    @Test
    public void right() {
        Set<Integer> testSet = new Set<>();
        Set<Integer> fSet = new Set<>();
        for (int i = 0; i < 10; i++) {
            fSet.add(i);
        }
        testSet.add(1);
        testSet.add(2);
        testSet.add(11);
        testSet.add(12);
        Set<Integer> newSet = testSet.right(fSet);
        assertEquals(2, newSet.getSize());
        assertTrue(newSet.contains(11));
        assertTrue(newSet.contains(11));
        for (int i = 0; i < 10; i++) {
            assertFalse(newSet.contains(i));
        }
    }

    @Test
    public void between() {
        Set<Integer> testSet = new Set<>();
        Set<Integer> fSet = new Set<>();
        for (int i = 0; i <= 5; i++) {
            testSet.add(i);
        }
        for (int i = 3; i < 10; i++) {
            fSet.add(i);
        }
        Set<Integer> newSet = testSet.between(fSet);
        assertEquals(3, newSet.getSize());
        assertTrue(newSet.contains(3));
        assertTrue(newSet.contains(4));
        assertTrue(newSet.contains(5));

        newSet = fSet.between(testSet);
        assertEquals(3, newSet.getSize());
        assertTrue(newSet.contains(3));
        assertTrue(newSet.contains(4));
        assertTrue(newSet.contains(5));
    }

    @Test
    public void union() {
        Set<Integer> testSet = new Set<>();
        Set<Integer> fSet = new Set<>();
        for (int i = 0; i <= 5; i++) {
            testSet.add(i);
        }
        for (int i = 3; i < 100; i++) {
            fSet.add(i);
        }
        Set<Integer> newSet = testSet.union(fSet);
        assertEquals(100, newSet.getSize());
        for (int i = 0; i < 100; i++) {
            assertTrue(newSet.contains(i));
        }
    }

    @Test
    public void toObjectArray() {
        set.clear();
        set.addAll("data1", "data2", "data3", "data4");
        String[] testArr = {"data1", "data2", "data3", "data4"};
        Object[] result = set.toObjectArray();
        assertArrayEquals(testArr, result);
    }

    @Test
    public void speedTest() {
        SortedSetIP<Integer> testSet = new SortedSetIP<>();
        testSet.clear();
        for (int i = 0; i < 1_000_000; i++) {
            testSet.add(i);
        }
        assertEquals(1_000_000, testSet.getSize());
    }
}