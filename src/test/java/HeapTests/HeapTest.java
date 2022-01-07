package HeapTests;

import Heap.MaxHeap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class HeapTest {
    Integer[] testArr = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3};
    MaxHeap<Integer> heap;

    public HeapTest() {
        this.heap = new MaxHeap<>();
    }

    @Before
    public void prepareToTest() {
        heap = new MaxHeap<>();
        Integer[] tempArr = new Integer[]{9, 2, 5, 7, 1, 8, 0, 4, 3};
        System.arraycopy(tempArr, 0, testArr, 0, testArr.length);
        for (Integer integer : testArr) {
            heap.push(integer);
        }
    }

    @Test
    public void push() {
        heap.clear();
        for (int i = 0; i < 10; i++) {
            heap.push(i);
        }
        assertEquals(10,heap.getSize());
    }

    @Test
    public void poll() {
        Arrays.sort(testArr);
        for (int i = testArr.length - 1; i >= 0; i--) {
            assertEquals(testArr[i], heap.poll());
        }
    }

    @Test
    public void peek() {
        Arrays.sort(testArr);
        assertEquals(testArr[testArr.length - 1], heap.peek());
    }

    @Test
    public void updateEquals() {
        heap.push(5);
        heap.updateEquals(5, 99);
        assertSame(false, heap.contains(5));
    }
    @Test
    public void update() {
        heap.push(5);
        heap.update(5, 99);
        assertNotSame(false, heap.contains(5));
    }

    @Test
    public void remove() {
        heap.push(6);
        heap.remove(6);
        assertSame(false, heap.contains(999));
    }

    @Test
    public void contains() {
        heap.push(54);
        assertSame(true, heap.contains(54));
    }


    @Test
    public void testLastIndexOf() {
        for (int i = 0; i < testArr.length; i++) {
            heap.push(i);
        }
    }

    @Test
    public void getSize() {
        assertEquals(testArr.length, heap.getSize());
    }

    @Test
    public void clear() {
        for (Integer integer : testArr) {
            heap.push(integer);
        }
        heap.clear();
        assertEquals(heap.getSize(), 0);
    }
}