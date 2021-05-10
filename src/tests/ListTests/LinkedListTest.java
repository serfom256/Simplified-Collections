package tests.ListTests;

import Lists.AbstractList;
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
        assertEquals(12, list.getLength());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popLast());
        }
        assertEquals(9, list.getLength());
    }

    @Test
    public void addAll() {
        Integer[] testArr = new Integer[]{99, 999, 9999};
        list.addAll(99, 999, 9999);
        assertEquals(12, list.getLength());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popLast());
        }
        assertEquals(9, list.getLength());
    }

    @Test
    public void add() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.add(integer);
        }
        assertEquals(12, list.getLength());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popLast());
        }
        assertEquals(9, list.getLength());
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
        assertEquals((Integer) 999, list.peekFirst());
    }

    @Test
    public void peekFirst() {
        list.pushLast(999);
        assertEquals((Integer) 999, list.peekLast());
    }

    @Test
    public void popFirst() {
        assertEquals((Integer) 9, list.popFirst());
        assertEquals(8, list.getLength());
    }

    @Test
    public void popLast() {
        assertEquals((Integer) 3, list.popLast());
        assertEquals(8, list.getLength());
    }

    @Test
    public void pop() {
        assertSame(9, list.pop(0));
        assertSame(3, list.pop(7));
        list.pop(3);
        assertSame(-1, list.indexOf(1));
    }

    @Test
    public void remove() {
        list.remove(7);
        assertSame(-1, list.indexOf(7));
        assertEquals(8, list.getLength());
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
        Integer[] testArr = new Integer[]{0, 1, 2, 3, 4, 5, 7, 8, 9};
        Arrays.sort(testArr);
        for (Integer integer : testArr) {
            assertEquals(integer, list.popFirst());
        }
        assertEquals(0, list.getLength());
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

    @Test
    public void slice() {
        list.clear();
        list.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer[] testArr = {6, 7, 8, 9, 10};
        AbstractList<Integer> sublist = list.slice(5, 10);
        for (int i = 0; i < testArr.length; i++) {
            assertEquals(testArr[i], sublist.get(i));
        }
        assertEquals(testArr.length, sublist.getLength());
    }

    @Test
    public void count() {
        list.insert(99, 6);
        list.insert(99, 0);
        list.insert(99, list.getLength() - 1);
        assertEquals(12, list.getLength());
        assertEquals(3, list.count(99));
        assertEquals(1, list.count(5));
        assertEquals(0, list.count(54));
    }

    @Test
    public void forEach() {
        list.clear();
        list.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, list.getLength());
        int start = 1;
        for (Integer i : list) {
            assertEquals((Integer) start++, i);
        }
    }
}