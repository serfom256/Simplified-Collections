package tests.SetTests;

import HashSet.SortedSetIP;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortedSetIPTest {
    SortedSetIP<Integer> set;

    public SortedSetIPTest() {
        set = new SortedSetIP<>();
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
    }

    @Test
    public void speedTest() {
        set.clear();
        for (int i = 0; i < 1_000_000; i++) {
            set.add(i);
        }
        assertEquals(1_000_000, set.getSize());
    }
}