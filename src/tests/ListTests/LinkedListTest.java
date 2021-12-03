package tests.ListTests;

import Lists.AbstractList;
import Lists.impl.LinkedList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


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
    public void addLast() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.addLast(integer);
        }
        assertEquals(12, list.getSize());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.removeLast());
        }
        assertEquals(9, list.getSize());
    }

    @Test
    public void addAll() {
        Integer[] testArr = new Integer[]{99, 999, 9999};
        list.addAll(99, 999, 9999);
        assertEquals(12, list.getSize());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.removeLast());
        }
        assertEquals(9, list.getSize());
    }

    @Test
    public void add() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.add(integer);
        }
        assertEquals(12, list.getSize());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.removeLast());
        }
        assertEquals(9, list.getSize());
    }

    @Test
    public void addFirst() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.addFirst(integer);
        }
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.removeFirst());
        }
    }

    @Test
    public void getLast() {
        list.addLast(999);
        assertEquals((Integer) 999, list.getLast());
    }

    @Test
    public void getFirst() {
        list.addFirst(999);
        assertEquals((Integer) 999, list.getFirst());
    }

    @Test
    public void removeFirst() {
        assertEquals((Integer) 9, list.removeFirst());
        assertEquals(8, list.getSize());
    }

    @Test
    public void removeLast() {
        assertEquals((Integer) 3, list.removeLast());
        assertEquals(8, list.getSize());
    }

    @Test
    public void pop() {
        assertSame(9, list.deleteAtPosition(0));
        assertSame(3, list.deleteAtPosition(7));
        list.deleteAtPosition(3);
        assertSame(-1, list.indexOf(1));
    }

    @Test
    public void remove() {
        list.delete(7);
        assertSame(-1, list.indexOf(7));
        assertEquals(8, list.getSize());
    }

    @Test
    public void insert() {
        list.insert(0, 999);
        assertSame(0, list.indexOf(999));
        list.insert(9, 54);
        assertSame(9, list.indexOf(54));
    }

    @Test
    public void sort() {
        list.sort();
        Integer[] testArr = new Integer[]{0, 1, 2, 3, 4, 5, 7, 8, 9};
        Arrays.sort(testArr);
        for (Integer integer : testArr) {
            assertEquals(integer, list.removeFirst());
        }
        assertEquals(0, list.getSize());
    }

    @Test
    public void lastIndexOf() {
        list.insert(1, 999);
        list.insert(7, 999);
        assertSame(7, list.lastIndexOf(999));
        list.deleteAtPosition(7);
        assertSame(1, list.lastIndexOf(999));

    }

    @Test
    public void indexOf() {
        list.insert(1, 999);
        list.insert(7, 999);
        assertSame(1, list.indexOf(999));
        list.deleteAtPosition(1);
        assertSame(6, list.lastIndexOf(999));
    }

    @Test
    public void get() {
        list.insert(9, 999);
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
        assertEquals(testArr.length, sublist.getSize());
    }

    @Test
    public void count() {
        list.insert(6, 99);
        list.insert(0, 99);
        list.insert(list.getSize() - 1, 99);
        assertEquals(12, list.getSize());
        assertEquals(3, list.count(99));
        assertEquals(1, list.count(5));
        assertEquals(0, list.count(54));
    }

    @Test
    public void getSize() {
        assertEquals(9, list.getSize());
    }

    @Test
    public void forEach() {
        list.clear();
        list.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, list.getSize());
        int start = 1;
        for (Integer i : list) {
            assertEquals((Integer) start++, i);
        }
    }

    @Test
    public void toObjectArray() {
        list.clear();
        list.addAll(1, 2, 3, 4);
        Integer[] testArr = {1, 2, 3, 4};
        assertArrayEquals(testArr, list.toObjectArray());
    }
}