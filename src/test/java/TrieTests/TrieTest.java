package TrieTests;

import sets.*;
import lists.impl.ArrayList;
import tries.Trie;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTest {
    Trie trie;
    ArrayList<String> lst;

    public TrieTest() {
        trie = new Trie();
        lst = new ArrayList<>();
        lst.addAll("a", "ab", "abc", "abcd", "abcde", "new data", "user",
                "qwerty", "q", "qwebcd", "test", "temp", "data", "1234", "12",
                "1325abc", "test123", "1001212454", "0000", "000qwerty");
    }

    @Before
    public void setUp() {
        trie.clear();
        for (String s : lst) trie.add(s);
    }

    @Test
    public void add() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        String[] list1 = new String[]{"temp", "test", "", "0000", "abc", ""};
        for (String s : list) trie.add(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());
        for (String s : list1) trie.add(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        trie.add(null);
        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        for (String s : lst) trie.add(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

    }

    @Test
    public void containsPrefix() {
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) {
            trie.add(s);
        }
        for (String s : lst) {
            assertTrue(trie.containsPrefix(s));
        }

        assertTrue(trie.containsPrefix("abc"));
        assertTrue(trie.containsPrefix("java"));
        assertTrue(trie.containsPrefix("123"));

        assertTrue(trie.containsPrefix("qwerty"));
        assertTrue(trie.containsPrefix("new data"));
        assertTrue(trie.containsPrefix("new"));

        assertFalse(trie.containsPrefix("javac"));
        assertFalse(trie.containsPrefix("keyboard1234"));
        assertFalse(trie.containsPrefix("00001"));
    }

    @Test
    public void contains() {
        String[] list = new String[]{"abcd", "keyword", "test", "tech", "java", "1234"};
        trie.clear();
        for (String s : list) {
            trie.add(s);
        }

        for (String s : list) {
            assertTrue(trie.contains(s + "additional suffix"));
        }

        assertTrue(trie.contains("abcdefg"));
        assertTrue(trie.contains("java1234"));
        assertTrue(trie.contains("12345678534"));

        assertFalse(trie.contains("jav"));
        assertFalse(trie.contains("keywor"));
        assertFalse(trie.contains("123..."));
        assertFalse(trie.contains("1235678534"));
    }

    @Test
    public void presents() {
        for (String s : lst) assertTrue(trie.presents(s));

        assertTrue(trie.presents("abc"));
        assertFalse(trie.presents("java"));
        assertFalse(trie.presents("123"));

        assertTrue(trie.presents("qwerty"));
        assertFalse(trie.presents("qwer"));
        assertTrue(trie.presents("new data"));
        assertFalse(trie.presents("new"));

        assertFalse(trie.presents("javac"));
        assertTrue(trie.presents("1001212454"));
        assertFalse(trie.presents("00001"));
    }

    @Test
    public void getByPrefixString() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll("tes", "132", "1325a", "000", "000qw", "data", "new", "te");
        for (String s : list) assertNotEquals(trie.getByPrefix(s), "");

        assertEquals("1001212454", trie.getByPrefix("1001"));
        assertEquals("0000", trie.getByPrefix("00"));
        assertEquals("qwebcd", trie.getByPrefix("qwe"));
        assertEquals("abcde", trie.getByPrefix("abcde"));
        assertEquals("", trie.getByPrefix(""));
    }

    @Test
    public void getByPrefixStringArray() {

        assertArrayEquals(trie.getByPrefix("1001", 1), new String[]{"1001212454"});
        assertArrayEquals(trie.getByPrefix("00", 1), new String[]{"0000"});
        assertArrayEquals(trie.getByPrefix("qwe", 1), new String[]{"qwebcd"});
        assertArrayEquals(trie.getByPrefix("abcde", 1), new String[]{"abcde"});
        assertArrayEquals(trie.getByPrefix("", 1), new String[0]);

        assertArrayEquals(trie.getByPrefix("ab", 1), new String[]{"ab"});
        assertArrayEquals(trie.getByPrefix("ab", 2), new String[]{"ab", "abc"});
        assertArrayEquals(trie.getByPrefix("ab", 3), new String[]{"ab", "abc", "abcd"});
        assertArrayEquals(trie.getByPrefix("ab", 4), new String[]{"ab", "abc", "abcd", "abcde"});
        assertArrayEquals(trie.getByPrefix("ab", 999), new String[]{"ab", "abc", "abcd", "abcde"});
        assertArrayEquals(trie.getByPrefix("q", 4), new String[]{"q", "qwebcd", "qwerty"});
        assertArrayEquals(trie.getByPrefix("qwe", 1), new String[]{"qwebcd"});
        assertArrayEquals(trie.getByPrefix("0", 2), new String[]{"0000", "000qwerty"});
    }

    @Test
    public void getSize() {
        assertEquals(68, trie.getSize());
        trie.clear();
        assertEquals(0, trie.getSize());

        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) trie.add(s);

        assertEquals(33, trie.getSize());
    }

    @Test
    public void getEntriesCount() {
        assertEquals(20, trie.getEntriesCount());
        trie.clear();
        assertEquals(0, trie.getEntriesCount());

        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) trie.add(s);
        assertEquals(5, trie.getEntriesCount());
    }

    @Test
    public void removeHard() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.removeHard("abc"));

        assertTrue(trie.presents("ab"));
        assertFalse(trie.presents("abc"));
        assertFalse(trie.presents("abcd"));
        assertFalse(trie.presents("abcde"));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(65, trie.getSize());

        assertTrue(trie.removeHard("q"));
        assertFalse(trie.presents("qwerty"));
        assertFalse(trie.presents("qwebcd"));
        assertEquals(14, trie.getEntriesCount());
        assertEquals(56, trie.getSize());

        assertTrue(trie.removeHard("00"));
        assertFalse(trie.presents("000qwerty"));
        assertFalse(trie.presents("0000"));
        assertEquals(12, trie.getEntriesCount());
        assertEquals(46, trie.getSize());
        assertFalse(trie.removeHard(""));
    }

    @Test
    public void removeWeak() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertFalse(trie.removeWeak("abc"));
        assertTrue(trie.presents("abc"));
        assertTrue(trie.presents("abcd"));
        assertTrue(trie.presents("abcde"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("q"));
        assertTrue(trie.presents("qwerty"));
        assertTrue(trie.presents("qwebcd"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("00"));
        assertTrue(trie.presents("000qwerty"));
        assertTrue(trie.presents("0000"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.removeWeak("abcde"));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.removeWeak(""));
    }

    @Test
    public void delete() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.delete("abc"));
        assertFalse(trie.presents("abc"));
        assertTrue(trie.presents("abcd"));
        assertTrue(trie.presents("abcde"));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.delete("q"));
        assertFalse(trie.presents("q"));
        assertTrue(trie.presents("qwerty"));
        assertTrue(trie.presents("qwebcd"));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.delete("00"));
        assertTrue(trie.presents("000qwerty"));
        assertTrue(trie.presents("0000"));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.delete("abcde"));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.delete(""));
        for (String s : lst) {
            if (trie.presents(s)) assertTrue(trie.delete(s));
        }
        assertEquals(0, trie.getSize());
        assertEquals(0, trie.getEntriesCount());
    }

    @Test
    public void forEach() {
        AbstractSet<String> prev = new Set<>();
        for (String s : trie) {
            assertFalse(prev.contains(s));
            prev.add(s);
            assertNotEquals(-1, lst.indexOf(s));
        }
        assertEquals(lst.getSize(), prev.getSize());
    }

}
