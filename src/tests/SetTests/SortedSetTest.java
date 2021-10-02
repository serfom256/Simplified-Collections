package tests.SetTests;

import HashSet.SortedSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SortedSetTest {
    SortedSet<String> set;

    public SortedSetTest() {
        set = new SortedSet<>();
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
        assertNull(set.remove("some_element"));
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
    public void toObjectArray() {
        set.clear();
        set.addAll("data1", "data2", "data3", "data4");
        String[] testArr = {"data1", "data2", "data3", "data4"};
        Object[] result = set.toObjectArray();
        assertArrayEquals(testArr, result);
    }
}