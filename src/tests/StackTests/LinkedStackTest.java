package tests.StackTests;

import Stack.LinkedStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LinkedStackTest {

    LinkedStack<Integer> stack;

    public LinkedStackTest() {
        stack = new LinkedStack<>();
    }

    @Before
    public void setUp() {
        for (int i = 1; i <= 20; i++) {
            stack.push(i);
        }
    }

    @Test
    public void peek() {
        for (int i = 20; i > 0; i--) {
            assertEquals((Integer) i, stack.peek());
            stack.poll();
        }
        assertEquals(0, stack.getSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void peek_method_should_throw_exception() {
        stack.clear();
        stack.peek();
    }

    @Test
    public void poll() {
        for (int i = 20; i > 0; i--) {
            assertEquals((Integer) i, stack.poll());
        }
        assertEquals(0, stack.getSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void poll_method_should_throw_exception() {
        stack.clear();
        stack.poll();
    }


    @Test
    public void push() {
        for (int i = 21; i <= 40; i++) {
            stack.push(i);
            assertEquals((Integer) i, stack.peek());
        }
        assertEquals(40, stack.getSize());
    }

    @Test
    public void isEmpty() {
        int start = 20;
        while (!stack.isEmpty()) {
            assertFalse(stack.isEmpty());
            assertEquals((Integer) start--, stack.poll());
        }
        assertTrue(stack.isEmpty());
        assertEquals(0, start);
        assertEquals(0, stack.getSize());
    }

    @Test
    public void indexOf() {
        assertEquals(-1, stack.indexOf(0));
        for (int i = 1; i <= 20; i++) {
            assertEquals(20 - i, stack.indexOf(i));
        }
    }

    @Test
    public void clear() {
        assertEquals(20, stack.getSize());
        stack.clear();
        assertEquals(0, stack.getSize());
    }

    @Test
    public void getSize() {
        assertEquals(20, stack.getSize());
        stack.clear();
        assertEquals(0, stack.getSize());
    }
}


