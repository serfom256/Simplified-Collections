package SetTests;

import sets.HashedSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SetTest {

    HashedSet<String> set;

    public SetTest() {
        set = new HashedSet<>();
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
    public void delete() {
        for (int i = 0; i < 1000; i++) {
            set.add(i + "_element");
        }
        assertEquals(1000, set.getSize());
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.delete(i + "_element"));
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
    public void left() {
        HashedSet<Integer> testSet = new HashedSet<>();
        HashedSet<Integer> fSet = new HashedSet<>();
        for (int i = 0; i < 10; i++) {
            fSet.add(i);
        }
        testSet.add(1);
        testSet.add(2);
        testSet.add(11);
        testSet.add(12);
        HashedSet<Integer> newSet = fSet.left(testSet);
        assertEquals(2, newSet.getSize());
        assertTrue(newSet.contains(11));
        assertTrue(newSet.contains(11));
        for (int i = 0; i < 10; i++) {
            assertFalse(newSet.contains(i));
        }
    }

    @Test
    public void right() {
        HashedSet<Integer> testSet = new HashedSet<>();
        HashedSet<Integer> fSet = new HashedSet<>();
        for (int i = 0; i < 10; i++) {
            fSet.add(i);
        }
        testSet.add(1);
        testSet.add(2);
        testSet.add(11);
        testSet.add(12);
        HashedSet<Integer> newSet = testSet.right(fSet);
        assertEquals(2, newSet.getSize());
        assertTrue(newSet.contains(11));
        assertTrue(newSet.contains(11));
        for (int i = 0; i < 10; i++) {
            assertFalse(newSet.contains(i));
        }
    }

    @Test
    public void between() {
        HashedSet<Integer> testSet = new HashedSet<>();
        HashedSet<Integer> fSet = new HashedSet<>();
        for (int i = 0; i <= 5; i++) {
            testSet.add(i);
        }
        for (int i = 3; i < 10; i++) {
            fSet.add(i);
        }
        HashedSet<Integer> newSet = testSet.between(fSet);
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
        HashedSet<Integer> testSet = new HashedSet<>();
        HashedSet<Integer> fSet = new HashedSet<>();
        for (int i = 0; i <= 5; i++) {
            testSet.add(i);
        }
        for (int i = 3; i < 100; i++) {
            fSet.add(i);
        }
        HashedSet<Integer> newSet = testSet.union(fSet);
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
        Arrays.sort(result);
        assertArrayEquals(testArr, result);
    }

    @Test
    public void speedTest() {
        HashedSet<Integer> testSet = new HashedSet<>();
        testSet.clear();
        for (int i = 0; i < 1_000_000; i++) {
            testSet.add(i);
        }
        assertEquals(1_000_000, testSet.getSize());
    }
}