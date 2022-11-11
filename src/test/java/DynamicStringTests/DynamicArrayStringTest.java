//package DynamicStringTests;
//
//import additional.dynamicstring.DynamicArrayString;
//import additional.dynamicstring.DynamicLinkedString;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;
//
//public class DynamicArrayStringTest {
//
//    private DynamicArrayString string;
//
//    @Before
//    public void setup() {
//        string = new DynamicArrayString();
//    }
//
//    @Test
//    public void add() {
//        string.add('a').add('b').add('c');
//        assertEquals("abc", string.toString());
//        assertEquals(3, string.getSize());
//        String xyz = "xyz" + string.toString();
//        string = new DynamicArrayString();
//        string.add(xyz);
//        string.add("m").add('n').add("qwerty");
//        assertEquals("xyzabcmnqwerty", string.toString());
//        assertEquals(14, string.getSize());
//        assertEquals("xyzabcmnqwerty", new DynamicArrayString(string).toString());
//        assertEquals(14, new DynamicArrayString(string).getSize());
//        assertEquals(14, new DynamicArrayString(string.toCharArray()).getSize());
//    }
//
//    @Test
//    public void addFirst() {
//        String str = "";
//        for (int i = 0; i < 10; i++) {
//            str = i + str;
//            string.addFirst(str.charAt(0));
//            assertEquals(str, string.toString());
//        }
//        assertEquals("a9876543210", string.addFirst('a').toString());
//    }
//
//    @Test
//    public void insertStringAtPos() {
//        assertEquals("abc", string.insert(0, "abc").toString());
//        assertEquals("abc123", string.insert(999, "123").toString());
//        assertEquals("abc000123", string.insert(3, "000").toString());
//        assertEquals("abc000q123", string.insert(6, "q").toString());
//        assertEquals("abc000q12test3", string.insert(string.getSize() - 1, "test").toString());
//    }
//
//    @Test
//    public void insertCharAtPos() {
//        assertEquals("1", string.insert(0, '1').toString());
//        assertEquals("12", string.insert(999, '2').toString());
//        assertEquals("a12", string.insert(0, 'a').toString());
//        assertEquals("aq12", string.insert(1, 'q').toString());
//        assertEquals("aq1z2", string.insert(3, 'z').toString());
//    }
//
//    @Test
//    public void insertDynamicStringAtPos() {
//        assertEquals("abc", string.insert(0, new DynamicLinkedString("abc")).toString());
//        assertEquals("abc123", string.insert(999, new DynamicLinkedString("123")).toString());
//        assertEquals("abc000123", string.insert(3, new DynamicLinkedString("000")).toString());
//        assertEquals("abc000q123", string.insert(6, new DynamicLinkedString("q")).toString());
//        assertEquals("abc000q12test3", string.insert(string.getSize() - 1, new DynamicLinkedString("test")).toString());
//    }
//
//    @Test
//    public void getLast() {
//        String res = "123456789";
//        string.add(res);
//        for (int i = res.length() - 1; i >= 0; i--) {
//            assertEquals(res.charAt(i), string.getLast());
//            string.deleteLast();
//        }
//    }
//
//    @Test
//    public void getFirst() {
//        String res = "123456789";
//        string.add(res);
//        for (int i = 0; i < res.length(); i++) {
//            assertEquals(res.charAt(i), string.getFirst());
//            string.deleteFirst();
//        }
//    }
//
//    @Test
//    public void deleteFirst() {
//        String res = "123456789";
//        string.add(res);
//        for (int i = 0; i < res.length(); i++) {
//            assertEquals(res.substring(i + 1), string.deleteFirst().toString());
//        }
//        for (int i = 0; i < 1000; i++) {
//            assertEquals("", string.deleteFirst().toString());
//        }
//    }
//
//    @Test
//    public void deleteLast() {
//        String res = "123456789";
//        string.add(res);
//        for (int i = res.length() - 1; i >= 0; i--) {
//            assertEquals(res.substring(0, i), string.deleteLast().toString());
//        }
//        for (int i = 0; i < 1000; i++) {
//            assertEquals("", string.deleteLast().toString());
//        }
//    }
//
//    @Test
//    public void count() {
//        string.clear();
//        assertEquals(0, string.count('0'));
//        string.add("01234511111679890ab1c");
//        assertEquals(2, string.count('0'));
//        assertEquals(0, string.count('q'));
//        assertEquals(7, string.count('1'));
//        assertEquals(1, string.count('c'));
//    }
//
//    @Test
//    public void subStringFrom() {
//        string.add("01234").add("56789");
//        assertEquals(string.toString(), string.subString(0));
//        assertEquals("", string.subString(10));
//        assertEquals("56789", string.subString(5));
//        assertEquals("9", string.subString(9));
//    }
//
//    @Test
//    public void subSequenceFrom() {
//        string.add("01234").add("56789");
//        assertEquals(string.toString(), string.subSequence(0).toString());
//        assertEquals("", string.subSequence(10).toString());
//        assertEquals("56789", string.subSequence(5).toString());
//        assertEquals("9", string.subSequence(9).toString());
//    }
//
//    @Test
//    public void subStringFromTo() {
//        string.add("01234").add("56789");
//        assertEquals("012", string.subString(0, 3));
//        assertEquals("0123456789", string.subString(0, string.getSize()));
//        assertEquals("", string.subString(0, 0));
//        assertEquals("234", string.subString(2, 5));
//        assertEquals("789", string.subString(7, 10));
//    }
//
//    @Test
//    public void subSequenceFromTo() {
//        string.add("01234").add("56789");
//        assertEquals("012", string.subSequence(0, 3).toString());
//        assertEquals("0123456789", string.subSequence(0, string.getSize()).toString());
//        assertEquals("", string.subSequence(0, 0).toString());
//        assertEquals("234", string.subSequence(2, 5).toString());
//        assertEquals("789", string.subSequence(7, 10).toString());
//    }
//
//    @Test
//    public void deleteFrom() {
//        string.add("01234").add("56789");
//        assertEquals(10, string.getSize());
//
//        assertEquals("012345678", string.delete(9).toString());
//        assertEquals(9, string.getSize());
//
//        assertEquals("0", string.delete(1).toString());
//        assertEquals(1, string.getSize());
//        assertEquals("", string.delete(0).toString());
//        assertEquals(0, string.getSize());
//
//        string.add("01234").add("56789");
//        assertEquals(10, string.getSize());
//
//        assertEquals("", string.delete(0).toString());
//        assertEquals(0, string.getSize());
//
//    }
//
//    @Test
//    public void deleteFromTo() {
//        string.add("01234").add("56789");
//        assertEquals("01236789", string.delete(4, 6).toString());
//
//        assertEquals(8, string.getSize());
//
//        assertEquals("012369", string.delete(5, string.getSize() - 1).toString());
//        assertEquals(6, string.getSize());
//
//        assertEquals("369", string.delete(0, 3).toString());
//        assertEquals(3, string.getSize());
//
//        assertEquals("", string.delete(0, 3).toString());
//        assertEquals(0, string.getSize());
//        string.add("12");
//        assertEquals("12", string.toString());
//        assertEquals(2, string.getSize());
//
//        assertEquals("1", string.delete(1, 2).toString());
//        assertEquals(1, string.getSize());
//        string.insert(0, "abc");
//        assertEquals(4, string.getSize());
//
//        assertEquals("", string.delete(0, 4).toString());
//    }
//
//    @Test
//    public void delete() {
//        string.add("01234").add("56789");
//        assertEquals("012356789", string.deleteAtPosition(4).toString());
//        String s = "012356789";
//        for (int i = 0; i < 6; i++) {
//            assertEquals(s.substring(0, s.length() - (i + 1)), string.deleteAtPosition(string.getSize() - 1).toString());
//        }
//        assertEquals("12", string.deleteAtPosition(0).toString());
//        assertEquals("2", string.deleteAtPosition(0).toString());
//        assertEquals("", string.deleteAtPosition(0).toString());
//        string.add("01234").add("56789");
//        assertEquals(10, string.getSize());
//        assertEquals("123456789", string.deleteAtPosition(0).toString());
//        assertEquals(9, string.getSize());
//        assertEquals("12345679", string.deleteAtPosition(7).toString());
//        assertEquals(8, string.getSize());
//    }
//
////    @Test
////    public void testToString() {
////        string.add("01234").add("56789");
////        assertEquals("0123456789", string.toString());
////
////        string = new DynamicLinkedString("");
////        assertEquals("", string.toString());
////    }
//
////    @Test
////    public void getSize() {
////        assertEquals(0, string.getSize());
////        string.add("01234").add("56789");
////        assertEquals(10, string.getSize());
////        string.add("abc");
////        assertEquals(13, string.getSize());
////        assertEquals(5, string.subSequence(0, 5).getSize());
////        string = new DynamicLinkedString();
////        assertEquals(0, string.getSize());
////    }
//
//    @Test
//    public void replaceFromTo() {
//        string.add("01234").add("56789");
//        assertEquals("abc3456789", string.replace(0, 3, "abc").toString());
//        assertEquals(10, string.getSize());
//
//        assertEquals("00456789", string.replace(0, 4, "00").toString());
//        assertEquals(8, string.getSize());
//
//        assertEquals("0045675", string.replace(6, 9999, "5").toString());
//        assertEquals(7, string.getSize());
//
//        assertEquals("0", string.replace(1, 7, "").toString());
//        assertEquals(1, string.getSize());
//
//        assertEquals("123", string.replace(0, 1, "123").toString());
//        assertEquals(3, string.getSize());
//
//        assertEquals("1abc3", string.replace(1, 2, "abc").toString());
//        assertEquals(5, string.getSize());
//
//        assertEquals("123", string.replace(1, 5, "23").toString());
//        assertEquals(3, string.getSize());
//
//        assertEquals("00000000000", string.replace(0, 99, "00000000000").toString());
//        assertEquals(11, string.getSize());
//    }
//
//    @Test
//    public void replaceFrom() {
//        string.add("01234").add("56789");
//        assertEquals("012abc", string.replace(3, "abc").toString());
//        assertEquals(6, string.getSize());
//
//        assertEquals("012a|", string.replace(4, "|").toString());
//        assertEquals(5, string.getSize());
//
//        assertEquals("5", string.replace(0, "5").toString());
//        assertEquals(1, string.getSize());
//        assertEquals("4", string.replace(0, "4").toString());
//        assertEquals(1, string.getSize());
//
//    }
//
//    @Test
//    public void get() {
//        string.add("01234").add("56789");
//        for (int i = 0; i < 10; i++) {
//            assertEquals(i + '0', string.get(i));
//        }
//    }
//
//    @Test
//    public void indexOfChar() {
//        string.add("01234").add("56789");
//        for (int i = 0; i < 10; i++) {
//            assertEquals(i, string.indexOf(String.valueOf(i).charAt(0)));
//        }
//    }
//
//    @Test
//    public void indexOfString() {
//        string.add("01234").add("56789");
//        assertEquals(0, string.indexOf("012"));
//        assertEquals(-1, string.indexOf("1111111111111111111111111111111111"));
//        assertEquals(-1, string.indexOf(""));
//        assertEquals(-1, string.indexOf("654"));
//        assertEquals(0, string.indexOf("0123456789"));
//        assertEquals(5, string.indexOf("567"));
//        assertEquals(9, string.indexOf("9"));
//        assertEquals(-1, string.indexOf("9999"));
//        assertEquals(-1, string.indexOf("678910"));
//        assertEquals(6, string.indexOf("6789"));
//    }
//
//    @Test
//    public void indexOfDynamicString() {
//        string.add("01234").add("56789");
//        assertEquals(0, string.indexOf(new DynamicLinkedString("012")));
//        assertEquals(-1, string.indexOf(new DynamicLinkedString("1111111111111111111111111111111111")));
//        assertEquals(-1, string.indexOf(new DynamicLinkedString()));
//        assertEquals(-1, string.indexOf(new DynamicLinkedString("654")));
//        assertEquals(0, string.indexOf(new DynamicLinkedString("0123456789")));
//        assertEquals(5, string.indexOf(new DynamicLinkedString("567")));
//        assertEquals(9, string.indexOf(new DynamicLinkedString("9")));
//        assertEquals(-1, string.indexOf(new DynamicLinkedString("9999")));
//        assertEquals(-1, string.indexOf(new DynamicLinkedString("678910")));
//    }
//
//    @Test
//    public void lastIndexOfChar() {
//        string.add("012434").add("596789");
//        assertEquals(11, string.lastIndexOf('9'));
//        assertEquals(5, string.lastIndexOf('4'));
//        assertEquals(-1, string.lastIndexOf('q'));
//    }
//
//    @Test
//    public void lastIndexOfCharArray() {
//        string.add("012434").add("59667809");
//        assertEquals(0, string.lastIndexOf("012".toCharArray()));
//        assertEquals(-1, string.lastIndexOf("1111111111111111111111111111111111".toCharArray()));
//        assertEquals(-1, string.lastIndexOf("".toCharArray()));
//        assertEquals(7, string.lastIndexOf("966".toCharArray()));
//        assertEquals(0, string.lastIndexOf("01243459667809".toCharArray()));
//        assertEquals(11, string.lastIndexOf("80".toCharArray()));
//        assertEquals(1, string.lastIndexOf("1".toCharArray()));
//        assertEquals(-1, string.lastIndexOf("9999".toCharArray()));
//        assertEquals(3, string.lastIndexOf("434".toCharArray()));
//        assertEquals(string.getSize() - 1, string.lastIndexOf("9".toCharArray()));
//    }
//
//    @Test
//    public void lastIndexOfString() {
//        string.add("012434").add("59667809");
//        assertEquals(0, string.lastIndexOf("012"));
//        assertEquals(-1, string.lastIndexOf("1111111111111111111111111111111111"));
//        assertEquals(-1, string.lastIndexOf(""));
//        assertEquals(7, string.lastIndexOf("966"));
//        assertEquals(0, string.lastIndexOf("01243459667809"));
//        assertEquals(11, string.lastIndexOf("80"));
//        assertEquals(1, string.lastIndexOf("1"));
//        assertEquals(-1, string.lastIndexOf("9999"));
//        assertEquals(3, string.lastIndexOf("434"));
//        assertEquals(string.getSize() - 1, string.lastIndexOf("9"));
//    }
//
//    @Test
//    public void lastIndexOfDynamicString() {
//        string.add("012434").add("59667809");
//        assertEquals(0, string.lastIndexOf("012"));
//        assertEquals(-1, string.lastIndexOf("1111111111111111111111111111111111"));
//        assertEquals(-1, string.lastIndexOf(""));
//        assertEquals(7, string.lastIndexOf("966"));
//        assertEquals(0, string.lastIndexOf("01243459667809"));
//        assertEquals(11, string.lastIndexOf("80"));
//        assertEquals(1, string.lastIndexOf("1"));
//        assertEquals(-1, string.lastIndexOf("9999"));
//        assertEquals(3, string.lastIndexOf("434"));
//        assertEquals(string.getSize() - 1, string.lastIndexOf("9"));
//    }
//
//    @Test
//    public void startsWith() {
//        assertFalse(string.startsWith('0'));
//        assertFalse(string.startsWith(" "));
//        assertFalse(string.startsWith(new DynamicLinkedString(" ")));
//        string.add("98765");
//        assertTrue(string.startsWith('9'));
//        assertFalse(string.startsWith('0'));
//        assertTrue(string.startsWith("98"));
//        assertFalse(string.startsWith("00"));
//        assertTrue(string.startsWith("98765"));
//        assertFalse(string.startsWith("987651"));
//
//        assertTrue(string.startsWith(new DynamicLinkedString("98765")));
//        assertFalse(string.startsWith(new DynamicLinkedString("987650")));
//    }
//
//    @Test
//    public void endsWith() {
//        assertFalse(string.endsWith('1'));
//        assertFalse(string.endsWith(" "));
//        assertFalse(string.endsWith(new DynamicLinkedString(" ")));
//        string.add("98765");
//        assertTrue(string.endsWith('5'));
//        assertFalse(string.endsWith('9'));
//        assertTrue(string.endsWith("65"));
//        assertFalse(string.endsWith("56"));
//        assertTrue(string.endsWith("98765"));
//        assertFalse(string.endsWith("567891"));
//
//        assertTrue(string.endsWith(new DynamicLinkedString("98765")));
//        assertTrue(string.endsWith(new DynamicLinkedString("765")));
//        assertFalse(string.endsWith(new DynamicLinkedString("987650")));
//    }
//
//
//    @Test
//    public void equals() {
//        string.add("01234").add("56789");
//        assertEquals(new DynamicLinkedString("0123456789"), string);
//        assertEquals("0123456789", string.toString());
//        assertNotEquals(new Object(), string.equals(new Object()));
//        assertNotEquals(null, string);
//        assertNotEquals(new DynamicLinkedString("012345678"), string);
//    }
//
////    @Test
////    public void clear() {
////        assertEquals(0, string.getSize());
////        string.clear();
////        assertEquals(0, string.getSize());
////        string.add("01234").add("56789");
////        string.clear();
////        assertEquals(0, string.getSize());
////        string = new DynamicLinkedString("abc");
////        assertEquals(0, string.clear().getSize());
////    }
//
//    @Test
//    public void update() {
//        string.add("01234").add("56789");
//        for (int i = 0; i < 10; i++) {
//            string.update(i, 'z');
//        }
//        assertEquals(10, string.getSize());
//        for (int i = 0; i < 10; i++) {
//            assertEquals('z', string.get(i));
//        }
//    }
//}
