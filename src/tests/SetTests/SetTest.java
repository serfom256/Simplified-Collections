package tests.SetTests;

import HashSet.Set;
import org.junit.Before;
import org.junit.Test;

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
}