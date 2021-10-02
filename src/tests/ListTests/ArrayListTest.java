package tests.ListTests;

import Lists.AbstractList;
import Lists.impl.ArrayList;
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
        assertEquals(list.getSize(), 34);
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
    public void insert() {
        Integer[] elements = {99, 54, 123, 543};
        Integer[] positions = {0, 8, 19, 16};
        for (int i = 0; i < positions.length; i++) {
            list.insert(elements[i], positions[i]);
            assertEquals(elements[i], list.get(positions[i]));
        }
        assertEquals(34, list.getSize());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void insert_at_invalid_position_should_trows_exception() {
        Integer[] elements = {99, 54, 123, 543};
        Integer[] positions = {-10, -8, 99, 58};
        for (int i = 0; i < positions.length; i++) {
            list.insert(elements[i], positions[i]);
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
            assertEquals((Integer) i, list.pop(0));
        }
        assertEquals(0, list.getSize());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void pop_value_at_empty_list_should_throw_exception() {
        list.clear();
        assertEquals(0, list.getSize());
        list.pop(0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void pop_out_of_list_bounds_should_throw_exception() {
        list.clear();
        list.pop(99);
        list.pop(-90);
    }

    @Test
    public void remove() {
        for (int i = 1; i <= 30; i++) {
            assertEquals((Integer) i, list.remove(i));
        }
        assertNull(list.remove(0));
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
        list.removeRange(5, 10);
        for (int i = 6; i <= 10; i++) {
            assertEquals(-1, list.indexOf(i));
        }
        assertEquals(25, list.getSize());
        list.removeRange(5, 10);
        for (int i = 6; i <= 15; i++) {
            assertEquals(-1, list.indexOf(i));
        }
        assertEquals(20, list.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeRange_should_throw_exception_Invalid_start_index() {
        list.removeRange(-10, 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeRange_should_throw_exception_Invalid_end_index() {
        list.removeRange(5, 40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeRange_should_throw_exception_Invalid_start_and_end_indexes() {
        list.removeRange(9, 1);
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
        list.insert(99, 26);
        list.insert(99, 5);
        assertEquals(5, list.indexOf(99));
        list.update(5, 0);
        assertEquals(27, list.indexOf(99));
    }

    @Test
    public void lastIndexOf() {
        list.insert(99, 26);
        list.insert(99, 5);
        assertEquals(27, list.lastIndexOf(99));
        list.update(27, 0);
        assertEquals(5, list.lastIndexOf(99));
    }

    @Test
    public void getSize() {
        assertEquals(30, list.getSize());
        list.remove(1);
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

}