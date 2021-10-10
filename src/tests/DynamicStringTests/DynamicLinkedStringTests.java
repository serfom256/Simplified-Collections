package tests.DynamicStringTests;


import Additional.DynamicString.DynamicLinkedString;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicLinkedStringTests {

    @Test
    public void add() {
        DynamicLinkedString s = new DynamicLinkedString("");
        s.add('a');
        s.add('b');
        s.add('c');
        assertEquals("abc", s.toString());
        assertEquals(3, s.getSize());
        s = new DynamicLinkedString("xyz");
        s.add('a');
        s.add('b');
        s.add('c');
        assertEquals("xyzabc", s.toString());
        assertEquals(6, s.getSize());
        String c = "xyzabc";
        for (int i = 0; i < 6; i++) {
            c = c.substring(0, c.length() - 1);
            assertEquals(c, s.popLast().toString());
            assertEquals(c.length(), s.getSize());
        }
    }

    @Test
    public void testAdd1() {
    }

    @Test
    public void addFirst() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void testInsert() {
    }

    @Test
    public void testInsert1() {
    }

    @Test
    public void getLast() {
    }

    @Test
    public void getFirst() {
    }

    @Test
    public void popFirst() {
    }

    @Test
    public void popLast() {
    }

    @Test
    public void count() {
    }

    @Test
    public void subString() {
    }

    @Test
    public void testSubString() {
        DynamicLinkedString s = new DynamicLinkedString("abcd");

        System.out.println(s.subString(0,0));
    }

    @Test
    public void testToString() {
    }

    @Test
    public void getSize() {
    }
}