package tests.DynamicStringTests;


import Additional.DynamicString.DynamicLinkedString;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DynamicLinkedStringTests {

    private DynamicLinkedString string;

    @Before
    public void setup() {
        string = new DynamicLinkedString();
    }

    @Test
    public void add() {
        string.add('a').add('b').add('c');
        assertEquals("abc", string.toString());
        assertEquals(3, string.getSize());
        string = new DynamicLinkedString("xyz" + string.toString());
        string.add("m").add('n').add("qwerty");
        assertEquals("xyzabcmnqwerty", string.toString());
        assertEquals(14, string.getSize());
        assertEquals("xyzabcmnqwerty", new DynamicLinkedString(string).toString());
        assertEquals(14, new DynamicLinkedString(string).getSize());
        assertEquals(14, new DynamicLinkedString(string.toCharArray()).getSize());
    }

    @Test
    public void addFirst() {
        String str = "";
        for (int i = 0; i < 10; i++) {
            str = i + str;
            string.addFirst(str.charAt(0));
            assertEquals(str, string.toString());
        }
        assertEquals("a9876543210", string.addFirst('a').toString());
    }

    @Test
    public void insertStringAtPos() {
        assertEquals("abc", string.insert(0, "abc").toString());
        assertEquals("abc123", string.insert(999, "123").toString());
        assertEquals("abc000123", string.insert(3, "000").toString());
        assertEquals("abc000q123", string.insert(6, "q").toString());
        assertEquals("abc000q12test3", string.insert(string.getSize() - 1, "test").toString());
    }

    @Test
    public void insertCharAtPos() {
        assertEquals("1", string.insert(0, '1').toString());
        assertEquals("12", string.insert(999, '2').toString());
        assertEquals("a12", string.insert(0, 'a').toString());
        assertEquals("aq12", string.insert(1, 'q').toString());
        assertEquals("aq1z2", string.insert(3, 'z').toString());
    }

    @Test
    public void insertAbstractDynamicStringAtPos() {
        assertEquals("abc", string.insert(0, new DynamicLinkedString("abc")).toString());
        assertEquals("abc123", string.insert(999, new DynamicLinkedString("123")).toString());
        assertEquals("abc000123", string.insert(3, new DynamicLinkedString("000")).toString());
        assertEquals("abc000q123", string.insert(6, new DynamicLinkedString("q")).toString());
        assertEquals("abc000q12test3", string.insert(string.getSize() - 1, new DynamicLinkedString("test")).toString());
    }

    @Test
    public void getLast() {
        String res = "123456789";
        string.add(res);
        for (int i = res.length() - 1; i >= 0; i--) {
            assertEquals(res.charAt(i), string.getLast());
            string.removeLast();
        }
    }

    @Test
    public void getFirst() {
        String res = "123456789";
        string.add(res);
        for (int i = 0; i < res.length(); i++) {
            assertEquals(res.charAt(i), string.getFirst());
            string.removeFirst();
        }
    }

    @Test
    public void removeFirst() {
        String res = "123456789";
        string.add(res);
        for (int i = 0; i < res.length(); i++) {
            assertEquals(res.substring(i + 1), string.removeFirst().toString());
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals("", string.removeFirst().toString());
        }
    }

    @Test
    public void removeLast() {
        String res = "123456789";
        string.add(res);
        for (int i = res.length() - 1; i >= 0; i--) {
            assertEquals(res.substring(0, i), string.removeLast().toString());
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals("", string.removeLast().toString());
        }
    }

    @Test
    public void count() {
        string.clear();
        assertEquals(0, string.count('0'));
        string.add("01234511111679890ab1c");
        assertEquals(2, string.count('0'));
        assertEquals(0, string.count('q'));
        assertEquals(7, string.count('1'));
        assertEquals(1, string.count('c'));
    }

    @Test
    public void subStringFrom() {
        string.add("01234").add("56789");
        assertEquals(string.toString(), string.subString(0));
        assertEquals("", string.subString(10));
        assertEquals("56789", string.subString(5));
        assertEquals("9", string.subString(9));
    }

    @Test
    public void subSequenceFrom() {
        string.add("01234").add("56789");
        assertEquals(string.toString(), string.subSequence(0).toString());
        assertEquals("", string.subSequence(10).toString());
        assertEquals("56789", string.subSequence(5).toString());
        assertEquals("9", string.subSequence(9).toString());
    }

    @Test
    public void subStringFromTo() {
        string.add("01234").add("56789");
        assertEquals("012", string.subString(0, 3));
        assertEquals("0123456789", string.subString(0, string.getSize()));
        assertEquals("", string.subString(0, 0));
        assertEquals("234", string.subString(2, 5));
        assertEquals("789", string.subString(7, 10));
    }

    @Test
    public void subSequenceFromTo() {
        string.add("01234").add("56789");
        assertEquals("012", string.subSequence(0, 3).toString());
        assertEquals("0123456789", string.subSequence(0, string.getSize()).toString());
        assertEquals("", string.subSequence(0, 0).toString());
        assertEquals("234", string.subSequence(2, 5).toString());
        assertEquals("789", string.subSequence(7, 10).toString());
    }

    @Test
    public void deleteFrom() {
        string.add("01234").add("56789");
        assertEquals(10, string.getSize());

        assertEquals("012345678", string.delete(9).toString());
        assertEquals(9, string.getSize());

        assertEquals("0", string.delete(1).toString());
        assertEquals(1, string.getSize());
        assertEquals("", string.delete(0).toString());
        assertEquals(0, string.getSize());

        string.add("01234").add("56789");
        assertEquals(10, string.getSize());

        assertEquals("", string.delete(0).toString());
        assertEquals(0, string.getSize());

    }

    @Test
    public void deleteFromTo() {
        string.add("01234").add("56789");
        assertEquals("01234789", string.delete(4, 6).toString());
        assertEquals(8, string.getSize());

        assertEquals("012347", string.delete(5, string.getSize() - 1).toString());
        assertEquals(6, string.getSize());

        assertEquals("347", string.delete(0, 3).toString());
        assertEquals(3, string.getSize());

        assertEquals("", string.delete(0, 3).toString());
        assertEquals(0, string.getSize());
        string.add("12");
        assertEquals("12", string.toString());
        assertEquals(2, string.getSize());

        assertEquals("1", string.delete(1, 2).toString());
        assertEquals(1, string.getSize());
    }

    @Test
    public void delete() {
        string.add("01234").add("56789");
        assertEquals("012356789", string.deleteAtPosition(4).toString());
        String s = "012356789";
        for (int i = 0; i < 6; i++) {
            assertEquals(s.substring(0, s.length() - (i + 1)), string.deleteAtPosition(string.getSize() - 1).toString());
        }
        assertEquals("12", string.deleteAtPosition(0).toString());
        assertEquals("2", string.deleteAtPosition(0).toString());
        assertEquals("", string.deleteAtPosition(0).toString());
    }

    @Test
    public void testToString() {
        string.add("01234").add("56789");
        assertEquals("0123456789", string.toString());

        string = new DynamicLinkedString("");
        assertEquals("", string.toString());
    }

    @Test
    public void getSize() {
        assertEquals(0, string.getSize());
        string.add("01234").add("56789");
        assertEquals(10, string.getSize());
        string.add("abc");
        assertEquals(13, string.getSize());
        assertEquals(5, string.subSequence(0, 5).getSize());
        string = new DynamicLinkedString();
        assertEquals(0, string.getSize());
    }

    @Test
    public void equals() {
        string.add("01234").add("56789");
        assertEquals(new DynamicLinkedString("0123456789"), string);
        assertEquals("0123456789", string.toString());
        assertNotEquals(new Object(), string.equals(new Object()));
        assertNotEquals(null, string);
        assertNotEquals(new DynamicLinkedString("012345678"), string);
    }


    @Test
    public void clear() {
        assertEquals(0, string.getSize());
        string.clear();
        assertEquals(0, string.getSize());
        string.add("01234").add("56789");
        string.clear();
        assertEquals(0, string.getSize());
        string = new DynamicLinkedString("abc");
        assertEquals(0, string.clear().getSize());
    }
}