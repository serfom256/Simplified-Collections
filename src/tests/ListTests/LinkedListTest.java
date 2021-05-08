package tests.ListTests;

import Lists.impl.LinkedList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class LinkedListTest {
    LinkedList<Integer> list;

    public LinkedListTest() {
        this.list = new LinkedList<>();
    }

    @Before
    public void setUp() {
        list = new LinkedList<>();
        Integer[] testArr = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3};
        list.addAll(testArr);
    }

    @Test
    public void pushLast() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.pushLast(integer);
        }
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popLast());
        }
    }

    @Test
    public void pushFirst() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.pushFirst(integer);
        }
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popFirst());
        }
    }

    @Test
    public void peekLast() {
        list.pushFirst(999);
        assertEquals((Integer) 999,list.peekFirst());
    }

    @Test
    public void peekFirst() {
        list.pushLast(999);
        assertEquals((Integer) 999,list.peekLast());
    }

    @Test
    public void popFirst() {
        assertEquals((Integer) 9, list.popFirst());
    }

    @Test
    public void popLast() {
        assertEquals((Integer) 3, list.popLast());
    }

    @Test
    public void pop() {
        assertSame(9,list.pop(0));
        assertSame(3,list.pop(7));
        list.pop(3);
        assertSame(-1, list.indexOf(1));
    }

    @Test
    public void remove() {
        list.remove(7);
        assertSame(-1, list.indexOf(7));
    }

    @Test
    public void insert() {
        list.insert(999, 0);
        assertSame(0, list.indexOf(999));
        list.insert(54, 9);
        assertSame(9, list.indexOf(54));
    }

    @Test
    public void sort() {
        list.sort();
        Integer[] testArr = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3};
        Arrays.sort(testArr);
        for (Integer integer : testArr) {
            assertEquals(integer, list.popFirst());
        }
    }

    @Test
    public void lastIndexOf() {
        list.insert(999, 1);
        list.insert(999, 7);
        assertSame(7, list.lastIndexOf(999));
        list.pop(7);
        assertSame(1, list.lastIndexOf(999));

    }

    @Test
    public void indexOf() {
        list.insert(999, 1);
        list.insert(999, 7);
        assertSame(1, list.indexOf(999));
        list.pop(1);
        assertSame(6, list.lastIndexOf(999));
    }

    @Test
    public void get() {
        list.insert(999, 9);
        assertEquals((Integer) 999, list.get(9));
        assertEquals((Integer) 9, list.get(0));
        assertEquals((Integer) 8, list.get(5));
    }
}