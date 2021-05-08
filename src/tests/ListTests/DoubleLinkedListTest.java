package tests.ListTests;

import Lists.impl.ArrayList;
import Lists.impl.DoubleLinkedList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class DoubleLinkedListTest {
    DoubleLinkedList<Integer> list;

    public DoubleLinkedListTest() {
        this.list = new DoubleLinkedList<>(Integer.class);
    }

    @Before
    public void setUp() {
        list = new DoubleLinkedList<>(Integer.class);
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
    public void pushFirst() {
        Integer[] testArr = new Integer[]{999, 99, 9};
        for (Integer integer : testArr) {
            list.pushFirst(integer);
        }
        assertEquals(12, list.getLength());
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], list.popFirst());
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
    public void addFrom() {
        ArrayList<Integer> testList = new ArrayList<>();
        testList.addAll(10, 11, 12, 13, 14, 15);
        list.addFrom(testList);
        assertEquals(15, list.getLength());
        list.clear();
        list.addFrom(testList);
        assertEquals(6, list.getLength());
    }


    @Test
    public void insert() {
        list.insert(999, 0);
        assertSame(0, list.indexOf(999));
        list.insert(54, 9);
        assertSame(9, list.indexOf(54));
        assertEquals(11, list.getLength());
    }

    @Test
    public void removeDuplicates() {
        assertEquals(9, list.getLength());
        list.insert(8, 4);
        list.insert(8, 3);
        list.insert(8, list.getLength() - 1);

        assertEquals(12, list.getLength());
        list.insert(99, 6);
        list.insert(99, 0);
        list.insert(99, list.getLength() - 1);
        assertEquals(15, list.getLength());

        list.removeDuplicates();

        assertEquals(1, list.count(8));
        assertEquals(1, list.count(99));
        assertEquals(10, list.getLength());

    }

    @Test
    public void remove() {
        list.remove(7);
        assertSame(-1, list.indexOf(7));
        assertEquals(8, list.getLength());
    }

    @Test
    public void removeAllLike() {
        assertEquals(9, list.getLength());
        list.insert(5, 4);
        list.insert(5, 3);
        list.insert(5, list.getLength() - 1);
        list.removeAllLike(5);
        assertEquals(8, list.getLength());
        assertEquals(-1, list.indexOf(5));

        list.insert(99, 6);
        list.insert(99, 0);
        list.insert(99, list.getLength() - 1);
        list.removeAllLike(99);
        assertEquals(8, list.getLength());
        assertEquals(-1, list.indexOf(99));

    }

    @Test
    public void removeAllLikeFromPosition() {

        list.insert(54, 4);
        list.insert(54, 3);
        list.insert(54, list.getLength() - 1);
        list.removeAllLike(54, 4);
        assertEquals(10, list.getLength());
        assertEquals(1, list.count(54));

        list.insert(99, 6);
        list.insert(99, 4);
        list.insert(99, list.getLength() - 1);
        list.removeAllLike(99, 0);
        assertEquals(10, list.getLength());
        assertEquals(0, list.count(99));

        assertEquals(0, list.count(999));
        list.removeAllLike(999, 0);
        assertEquals(0, list.count(999));
        assertEquals(10, list.getLength());

    }

    @Test
    public void pop() {
        assertSame(9, list.pop(0));
        assertSame(3, list.pop(7));
        list.pop(3);
        assertSame(-1, list.indexOf(1));
        assertEquals(6, list.getLength());
    }

    @Test
    //FIXME
    public void popLast() {
        assertEquals((Integer) 3, list.popLast());
        assertEquals(8, list.getLength());
        while (list.getLength() != 0) {
            list.popLast();
        }
        assertEquals(0, list.getLength());
    }

    @Test
    //FIXME
    public void popFirst() {
        assertEquals((Integer) 9, list.popFirst());
        assertEquals(8, list.getLength());
        while (list.getLength() != 0) {
            list.popFirst();
        }
        assertEquals(0, list.getLength());
    }

    @Test
    //FIXME
    public void peekFirst() {
        while (list.getLength() != 0) {
            Integer first = list.peekFirst();
            System.out.println(list.popFirst());
            //assertEquals(first, list.popFirst());
        }
        assertEquals((Integer) 0, list.peekFirst());
    }

    @Test
    //FIXME
    public void peekLast() {
        assertEquals((Integer) 3, list.peekLast());
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
    }

    @Test
    public void setLength() {
    }

    @Test
    public void sliceFrom() {
    }

    @Test
    public void sliceBefore() {
    }

    @Test
    public void sort() {
    }

    @Test
    public void toList() {
    }

    @Test
    public void toSet() {
    }

    @Test
    public void toObjectArray() {
    }

    @Test
    public void toArray() {
    }

    @Test
    public void count() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void lastIndexOf() {
    }

    @Test
    public void getLength() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void reverse() {
    }
}