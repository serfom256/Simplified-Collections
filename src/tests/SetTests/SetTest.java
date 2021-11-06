package tests.SetTests;

import HashSet.Set;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SetTest {

    Set<String> set;

    public SetTest() {
        set = new Set<>();
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 20; i++) {
            set.add(i + "_element");
        }
    }

    @Test
    public void add() {
        for (int i = 0; i < 1000; i++) {
            set.add(i + "_element");
        }
        assertEquals(1000, set.getSize());
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.contains(i + "_element"));
        }
    }

    @Test
    public void remove() {
        for (int i = 0; i < 1000; i++) {
            set.add(i + "_element");
        }
        assertEquals(1000, set.getSize());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i + "_element", set.remove(i + "_element"));
        }
        assertEquals(0, set.getSize());
    }

    @Test
    public void contains() {
        for (int i = 0; i < 10000; i++) {
            set.add(i + "_element");
        }
        assertEquals(10000, set.getSize());
        for (int i = 0; i < 10000; i++) {
            assertTrue(set.contains(i + "_element"));
        }
    }

    @Test
    public void update() {
        for (int i = 0; i < 20; i++) {
            set.update(i + "_element", i + "_data");
        }

        for (int i = 0; i < 20; i++) {
            assertTrue(set.contains(i + "_data"));
        }
        assertEquals(20, set.getSize());
    }

    @Test
    public void getSize() {
        assertEquals(20, set.getSize());
    }

    @Test
    public void clear() {
        set.clear();
        assertEquals(0, set.getSize());
    }

    @Test
    public void addAll() {
        set.addAll("data1", "data2", "data3", "data4");
        assertEquals(24, set.getSize());
    }

    @Test
    public void forEach() {
        String[] elements = new String[20];
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
        Arrays.sort(result);
        assertArrayEquals(testArr, result);
    }

    @Test
    public void speedTest() {
        Set<Integer> testSet = new Set<>();
        testSet.clear();
        for (int i = 0; i < 1_000_000; i++) {
            testSet.add(i);
        }
        assertEquals(1_000_000, testSet.getSize());
    }
}