package ListTests;

import additional.exceptions.IndexOutOfCollectionBoundsException;
import lists.AbstractList;
import lists.impl.ArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayListTest {

    ArrayList<Integer> list;

    public ArrayListTest() {
        list = new ArrayList<>();
    }

    @Before
    public void setUp() {
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
    }

    @Test
    public void addAll() {
        Integer[] testArr = {99, 999, 9999, 9999};
        list.addAll(99, 999, 9999, 9999);
        int len = list.getSize();
        for (int i = len - 1, pos = testArr.length - 1; pos >= 0; i--, pos--) {
            assertEquals(testArr[pos], list.get(i));
        }
        assertEquals(34, list.getSize());
    }

    @Test
    public void add() {
        list.clear();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        assertEquals(100, list.getSize());
        for (int i = 0; i < 100; i++) {
            assertEquals((Integer) i, list.get(i));
        }
    }

    @Test
    public void addIfAbsent() {
        list.clear();
        Integer[] testArr = {99, 999, 9999, 9999, 99, 9999};
        for (Integer i: testArr) {
            list.addIfAbsent(i);
        }
        assertEquals(3, list.getSize());
    }

    @Test
    public void insert() {
        Integer[] elements = {99, 54, 123, 543};
        Integer[] positions = {0, 8, 19, 16};
        for (int i = 0; i < positions.length; i++) {
            list.insert(positions[i], elements[i]);
            assertEquals(elements[i], list.get(positions[i]));
        }
        assertEquals(34, list.getSize());
    }

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void insert_at_invalid_position_should_trows_exception() {
        Integer[] elements = {99, 54, 123, 543};
        Integer[] positions = {-10, -8, 99, 58};
        for (int i = 0; i < positions.length; i++) {
            list.insert(positions[i], elements[i]);
        }
    }

    @Test
    public void sort() {
        list.clear();
        for (int i = 0; i < 100; i++) {
            list.add(new Random().nextInt());
        }
        list.sort();
        for (int i = 1; i < 100; i++) {
            assertTrue(list.get(i - 1) <= list.get(i));
        }
    }

    @Test
    public void clear() {
        list.clear();
        assertEquals(0, list.getSize());
    }

    @Test
    public void pop() {
        for (int i = 1; i <= 30; i++) {
            assertEquals((Integer) i, list.deleteAtPosition(0));
        }
        assertEquals(0, list.getSize());
    }

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void pop_value_at_empty_list_should_throw_exception() {
        list.clear();
        list.deleteAtPosition(0);
    }

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void pop_out_of_list_bounds_should_throw_exception() {
        list.clear();
        list.deleteAtPosition(99);
        list.deleteAtPosition(-90);
    }

    @Test
    public void delete() {
        for (int i = 1; i <= 30; i++) {
            assertTrue(list.delete(i));
        }
        assertFalse(list.delete(0));
        assertEquals(0, list.getSize());
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

    @Test
    public void removeRange() {
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

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void removeRange_should_throw_exception_Invalid_start_index() {
        list.delete(-10, 8);
    }

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void removeRange_should_throw_exception_Invalid_end_index() {
        list.delete(5, 40);
    }

    @Test(expected = IndexOutOfCollectionBoundsException.class)
    public void removeRange_should_throw_exception_Invalid_start_and_end_indexes() {
        list.delete(9, 1);
    }


    @Test
    public void slice() {
        Integer[] testArr = {6, 7, 8, 9, 10};
        AbstractList<Integer> sublist = list.slice(5, 10);
        for (int i = 0; i < testArr.length; i++) {
            assertEquals(testArr[i], sublist.get(i));
        }
        assertEquals(testArr.length, sublist.getSize());
    }

    @Test
    public void get() {
        for (int i = 0; i < list.getSize(); i++) {
            assertEquals((Integer) (i + 1), list.get(i));
        }
    }

    @Test
    public void indexOf() {
        list.insert(26, 99);
        list.insert(5, 99);
        assertEquals(5, list.indexOf(99));
        list.update(5, 0);
        assertEquals(27, list.indexOf(99));
    }

    @Test
    public void lastIndexOf() {
        list.insert(26, 99);
        list.insert(5, 99);
        assertEquals(27, list.lastIndexOf(99));
        list.update(27, 0);
        assertEquals(5, list.lastIndexOf(99));
    }

    @Test
    public void getSize() {
        assertEquals(30, list.getSize());
        list.delete(1);
        assertEquals(29, list.getSize());
    }

    @Test
    public void forEach() {
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

    @Test
    public void replaceFromTo() {
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
    public void count() {
        list.insert(6, 99);
        list.insert(0, 99);
        list.insert(list.getSize() - 1, 99);
        assertEquals(33, list.getSize());
        assertEquals(3, list.count(99));
        assertEquals(1, list.count(5));
        assertEquals(0, list.count(54));
    }

    @Test
    public void getFirst() {
        while (list.getSize() != 0) {
            Integer first = list.getFirst();
            assertEquals(first, list.deleteAtPosition(0));
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertNull(list.getFirst());
    }

    @Test
    public void getLast() {
        while (list.getSize() != 0) {
            Integer last = list.getLast();
            assertEquals(last, list.deleteAtPosition(list.getSize() - 1));
        }
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.getSize());
    }

    @Test
    public void toArray() {
        list.clear();
        list.addAll(1, 2, 3, 4);
        Integer[] testArr = {1, 2, 3, 4};
        assertArrayEquals(testArr, list.toArray(Integer.class));
    }


}