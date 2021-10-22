package tests.DynamicStringTests;


import Additional.DynamicString.DynamicLinkedString;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicLinkedStringTests {

    @Test
    public void add() {
        DynamicLinkedString s = new DynamicLinkedString("");
        s.add('a').add('b').add('c');
        assertEquals("abc", s.toString());
        assertEquals(3, s.getSize());
        s = new DynamicLinkedString("xyz" + s.toString());
        s.add("m").add('n').add("qwerty");
        assertEquals("xyzabcmnqwerty", s.toString());
        assertEquals(14, s.getSize());
        assertEquals("xyzabcmnqwerty", new DynamicLinkedString(s).toString());
        assertEquals(14, new DynamicLinkedString(s).getSize());
        assertEquals(14, new DynamicLinkedString(s.toCharArray()).getSize());
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

        System.out.println(s.subSequence(0, 0));
    }

    @Test
    public void testToString() {
    }

    @Test
    public void getSize() {
    }
}