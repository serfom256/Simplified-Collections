package tests.ListTests;

import Lists.impl.ArrayList;
import Lists.impl.DoubleLinkedList;
import Lists.impl.LinkedList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class DoubleLinkedListTest {
    DoubleLinkedList<Integer> list;

    public DoubleLinkedListTest() {
        this.list = new DoubleLinkedList<>();
    }

    @Before
    public void setUp() {
        list = new DoubleLinkedList<>();
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
    public void addFirst() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.addFirst(integer);
        }
        assertEquals(12, list.getSize());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.removeFirst());
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
    public void addFrom() {
        ArrayList<Integer> testList = new ArrayList<>();
        testList.addAll(10, 11, 12, 13, 14, 15);
        list.addFrom(testList);
        assertEquals(15, list.getSize());
        list.clear();
        list.addFrom(testList);
        assertEquals(6, list.getSize());
    }

    @Test
    public void insert() {
        list.insert(0, 999);
        assertSame(0, list.indexOf(999));
        list.insert(9, 54);
        assertSame(9, list.indexOf(54));
        assertEquals(11, list.getSize());
    }

    @Test
    public void remove() {
        list.delete(7);
        assertSame(-1, list.indexOf(7));
        assertEquals(8, list.getSize());
    }

    @Test
    public void pop() {
        assertEquals(9, list.getSize());
        assertSame(9, list.deleteAtPosition(0));
        assertSame(-1, list.indexOf(9));

        assertEquals(8, list.getSize());
        assertSame(3, list.deleteAtPosition(7));
        assertSame(-1, list.indexOf(3));

        assertEquals(7, list.getSize());
        assertSame(8, list.deleteAtPosition(4));
        assertSame(-1, list.indexOf(8));

        assertEquals(6, list.getSize());
        assertSame(1, list.deleteAtPosition(3));
        assertSame(-1, list.indexOf(1));

        assertEquals(5, list.getSize());
        assertSame(5, list.deleteAtPosition(1));
        assertSame(-1, list.indexOf(5));
    }

    @Test
    public void removeLast() {
        assertEquals((Integer) 3, list.removeLast());
        assertEquals(8, list.getSize());
        while (list.getSize() != 0) {
            list.removeLast();
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.getSize());
    }

    @Test
    public void removeFirst() {
        assertEquals((Integer) 9, list.removeFirst());
        assertEquals(8, list.getSize());

        while (list.getSize() != 0) {
            Integer first = list.getFirst();
            assertEquals(first, list.removeFirst());
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.getSize());
    }

    @Test
    public void getFirst() {
        while (list.getSize() != 0) {
            Integer first = list.getFirst();
            assertEquals(first, list.removeFirst());
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertNull(list.getFirst());
    }

    @Test
    public void getLast() {
        while (list.getSize() != 0) {
            Integer last = list.getLast();
            assertEquals(last, list.removeLast());
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.getSize());
    }

    @Test
    public void get() {
        Integer[] arr = {9, 2, 5, 7, 1, 8, 0, 4, 3};
        for (int i = 0; i < 9; i++) {
            assertEquals(arr[i], list.get(i));
        }
    }

    @Test
    public void slice() {
        Integer[][] testArr = {{2, 5, 7}, {9, 2, 5, 7, 1, 8, 0}, {8, 0, 4}, {4, 3}};
        assertArrayEquals(testArr[0], list.slice(1, 4).toArray(Integer.class));
        assertArrayEquals(testArr[1], list.slice(0, 7).toArray(Integer.class));
        assertArrayEquals(testArr[2], list.slice(5, 8).toArray(Integer.class));
        assertArrayEquals(testArr[3], list.slice(7, list.getSize()).toArray(Integer.class));
        assertArrayEquals(list.toArray(Integer.class), list.slice(0, list.getSize()).toArray(Integer.class));
        assertEquals(9, list.getSize());
    }

    @Test
    public void setLength() {
        Integer[] temp = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer[][] testArr = {{0, 1, 2, 3, 4}, {0, 1}, {3, 4, 5, 6, 7}, {7, 8, 9}};

        list.clear();
        list.addAll(temp);
        list.setLength(0, 5);
        assertArrayEquals(testArr[0], list.toArray(Integer.class));
        assertEquals(5, list.getSize());

        list.clear();
        list.addAll(temp);
        list.setLength(0, 2);
        assertArrayEquals(testArr[1], list.toArray(Integer.class));
        assertEquals(2, list.getSize());

        list.clear();
        list.addAll(temp);
        list.setLength(3, 8);
        assertArrayEquals(testArr[2], list.toArray(Integer.class));
        assertEquals(5, list.getSize());

        list.clear();
        list.addAll(temp);
        list.setLength(7, 10);
        assertArrayEquals(testArr[3], list.toArray(Integer.class));
        assertEquals(3, list.getSize());

        list.clear();
        list.addAll(temp);
        list.setLength(0, 10);
        assertArrayEquals(temp, list.toArray(Integer.class));
        assertEquals(10, list.getSize());
    }

    @Test
    public void sliceFrom() {
        list.clear();
        Integer[] temp = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        list.addAll(temp);
        Integer[][] testArr = {{4, 5, 6, 7, 8, 9}, {7, 8, 9}, {9}, {8, 9}};

        assertArrayEquals(testArr[0], list.sliceFrom(4).toArray(Integer.class));
        assertEquals(6, list.sliceFrom(4).getSize());

        assertArrayEquals(testArr[1], list.sliceFrom(7).toArray(Integer.class));
        assertEquals(3, list.sliceFrom(7).getSize());

        assertArrayEquals(testArr[2], list.sliceFrom(9).toArray(Integer.class));
        assertEquals(1, list.sliceFrom(9).getSize());

        assertArrayEquals(testArr[3], list.sliceFrom(8).toArray(Integer.class));
        assertEquals(2, list.sliceFrom(8).getSize());

        assertArrayEquals(list.toArray(Integer.class), list.sliceFrom(0).toArray(Integer.class));
        assertEquals(10, list.sliceFrom(0).getSize());

        assertEquals(10, list.getSize());
    }

    @Test
    public void sliceBefore() {
        list.clear();
        Integer[] temp = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        list.addAll(temp);

        assertArrayEquals(list.sliceBefore(5).toArray(Integer.class), new Integer[]{0, 1, 2, 3, 4});
        assertEquals(5, list.sliceBefore(5).getSize());

        assertArrayEquals(list.toArray(Integer.class), list.sliceBefore(10).toArray(Integer.class));
        assertEquals(10, list.sliceBefore(10).getSize());

        assertEquals(10, list.getSize());
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
    public void toArray() {
        list.clear();
        list.addAll(1, 2, 3, 4);
        Integer[] testArr = {1, 2, 3, 4};
        assertArrayEquals(testArr, list.toArray(Integer.class));
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
    public void getSize() {
        assertEquals(9, list.getSize());
    }

    @Test
    public void clear() {
        list.clear();
        assertEquals(0, list.getSize());
    }

    @Test
    public void init_from_iterable() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.addAll(1, 2, 3, 4, 5, 6);

        DoubleLinkedList<Integer> doubleLinkedList = new DoubleLinkedList<>();
        doubleLinkedList.addFrom(linkedList);
        assertEquals(linkedList.getSize(), doubleLinkedList.getSize());
        for (int i = 0; i < doubleLinkedList.getSize(); i++) {
            assertEquals(linkedList.get(i), doubleLinkedList.get(i));
        }
    }

    @Test
    public void toObjectArray() {
        list.clear();
        list.addAll(1, 2, 3, 4);
        Integer[] testArr = {1, 2, 3, 4};
        assertArrayEquals(testArr, list.toObjectArray());
    }

    @Test
    public void replaceFromTo() {
        list.clear();
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
        assertEquals(30, list.getSize());
        list.replace(0, 10, 99);
        assertEquals((Integer) 99, list.get(0));

        assertEquals(21, list.getSize());
        list.replace(20, 21, 100);
        assertEquals((Integer) 100, list.get(list.getSize() - 1));

        assertEquals(21, list.getSize());
        list.replace(0, list.getSize(), 999);

        assertEquals((Integer) 999, list.get(0));
        assertEquals(1, list.getSize());
    }

    @Test
    public void replaceFrom() {
        list.clear();
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
        assertEquals(30, list.getSize());
        list.replace(29, 99);
        assertEquals((Integer) 99, list.get(list.getSize() - 1));
        assertEquals(30, list.getSize());

        list.replace(20, 100);
        assertEquals((Integer) 100, list.get(list.getSize() - 1));
        assertEquals(21, list.getSize());

        list.replace(0, 999);
        assertEquals((Integer) 999, list.get(0));
        assertEquals(1, list.getSize());
    }

    @Test
    public void removeRange() {
        list.clear();
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
        list.delete(5, 10);
        for (int i = 6; i <= 10; i++) {
            assertEquals(-1, list.indexOf(i));
        }
        assertEquals(25, list.getSize());
        list.delete(5, 10);
        for (int i = 6; i <= 15; i++) {
            assertEquals(-1, list.indexOf(i));
        }
        assertEquals(20, list.getSize());
        list.delete(18, 20);
        assertEquals(18, list.getSize());
    }

    @Test
    public void update() {
        for (int i = 0; i < list.getSize(); i++) {
            list.update(i, 0);
        }
        for (int i = 0; i < list.getSize(); i++) {
            assertEquals((Integer) 0, list.get(i));
        }
    }
}