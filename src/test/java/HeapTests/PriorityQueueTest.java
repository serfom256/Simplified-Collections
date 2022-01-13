package HeapTests;

import heap.PriorityQueue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PriorityQueueTest {
    String[] testArr = {"one", "two", "three", "four", "five", "six", "seven", "eight","nine", "ten"};
    PriorityQueue<String> queue;

    public PriorityQueueTest() {
        this.queue = new PriorityQueue<>();
    }

    @Before
    public void prepareToTest() {
        queue = new PriorityQueue<>();
        String[] tempArr = {"one", "two", "three", "four", "five", "six", "seven", "eight","nine", "ten"};
        System.arraycopy(tempArr, 0, testArr, 0, testArr.length);
        for (int i = 0; i < tempArr.length; i++) {
            queue.push(tempArr[i], i);
        }
    }

    @Test
    public void push() {
        queue.clear();
        for (int i = 0; i < 10; i++) {
            queue.push(testArr[i],i);
        }
        assertEquals(10,queue.getSize());
    }

    @Test
    public void poll() {
        for (int i = testArr.length-1; i >= 0 ; i--) {
            assertEquals(testArr[i],queue.poll());
        }
    }

    @Test
    public void peek() {
        for (int i = testArr.length-1; i >= 0 ; i--) {
            assertEquals(testArr[i],queue.peek());
            queue.poll();
        }
        assertNull(queue.peek());
    }

    @Test
    public void setPriority() {
        for (int i = testArr.length-1; i >= 0 ; i--) {
            queue.setPriority(testArr[i],testArr.length-i);
        }
        for (String s : testArr) {
            assertEquals(s, queue.poll());
        }
    }

    @Test
    public void update() {
        queue.push("NewITEM",999);
        queue.update("NewITEM", "NewELEMENT");
        assertSame(false, queue.contains("NewITEM"));
        assertSame(true, queue.contains("NewELEMENT"));

        queue.update("111", "111");
        assertSame(false, queue.contains("111"));
    }

    @Test
    public void updateEquals() {
        for (int i = 0; i < 5; i++) {
            queue.push("000",i);
        }
        queue.updateEquals("000","111");
        assertSame(false,queue.contains("000"));
        assertSame(true,queue.contains("111"));
    }

    @Test
    public void countOf() {
        for (int i = 0; i < queue.getSize(); i++) {
            assertSame(1, queue.count(testArr[i]));
        }
    }

    @Test
    public void remove() {
        queue.push("some item",10);
        queue.remove("some item");
        assertSame(false, queue.contains("some item"));
        for (String s : testArr) {
            queue.remove(s);
            assertSame(false, queue.contains(s));
        }
        assertSame(0,queue.getSize());
    }

    @Test
    public void contains() {
        queue.push("Integer",0);
        assertSame(true, queue.contains("Integer"));
    }

    @Test
    public void getSize() {
        for (int i = testArr.length-1; i >= 0; i--) {
            queue.poll();
            assertSame(i, queue.getSize());
        }
    }

    @Test
    public void clear() {
        queue.clear();
        assertSame(0, queue.getSize());
    }
}