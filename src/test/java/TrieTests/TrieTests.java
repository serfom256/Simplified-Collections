package TrieTests;

import Sets.AbstractSet;
import Sets.Set;
import Lists.impl.ArrayList;
import Tries.Trie;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTests {
    Trie trie;
    ArrayList<String> lst;

    public TrieTests() {
        trie = new Trie();
        lst = new ArrayList<>();
        lst.addAll("a", "ab", "abc", "abcd", "abcde", "new data", "user",
                "qwerty", "q", "qwebcd", "test", "temp", "data", "1234", "12",
                "1325abc", "test123", "1001212454", "0000", "000qwerty");
    }

    @Before
    public void setUp() {
        trie.clear();
        for (String s : lst) trie.put(s);
    }

    @Test
    public void put() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        String[] list1 = new String[]{"temp", "test", "", "0000", "abc", ""};
        for (String s : list) trie.put(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());
        for (String s : list1) trie.put(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        trie.put(null);
        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        for (String s : lst) trie.put(s);

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

    }

    @Test
    public void presents() {
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) {
            trie.put(s);
        }
        for (String s : lst) {
            assertTrue(trie.presents(s));
        }

        assertTrue(trie.presents("abc"));
        assertTrue(trie.presents("java"));
        assertTrue(trie.presents("123"));

        assertTrue(trie.presents("qwerty"));
        assertTrue(trie.presents("new data"));
        assertTrue(trie.presents("new"));

        assertFalse(trie.presents("javac"));
        assertFalse(trie.presents("keyboard1234"));
        assertFalse(trie.presents("00001"));
    }

    @Test
    public void contains() {
        for (String s : lst) assertTrue(trie.contains(s));


        assertTrue(trie.contains("abc"));
        assertFalse(trie.contains("java"));
        assertFalse(trie.contains("123"));

        assertTrue(trie.contains("qwerty"));
        assertFalse(trie.contains("qwer"));
        assertTrue(trie.contains("new data"));
        assertFalse(trie.contains("new"));

        assertFalse(trie.contains("javac"));
        assertTrue(trie.contains("1001212454"));
        assertFalse(trie.contains("00001"));
    }

    @Test
    public void getByPrefixString() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll("tes", "132", "1325a", "000", "000qw", "data", "new", "te");
        for (String s : list) assertNotEquals(trie.getByPrefix(s), "");

        assertEquals(trie.getByPrefix("1001"), "1001212454");
        assertEquals(trie.getByPrefix("00"), "0000");
        assertEquals(trie.getByPrefix("qwe"), "qwebcd");
        assertEquals(trie.getByPrefix("abcde"), "abcde");
        assertEquals(trie.getByPrefix(""), "");
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
        for (String s : list) trie.put(s);

        assertEquals(33, trie.getSize());
    }

    @Test
    public void getEntriesCount() {
        assertEquals(20, trie.getEntriesCount());
        trie.clear();
        assertEquals(0, trie.getEntriesCount());

        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) trie.put(s);
        assertEquals(5, trie.getEntriesCount());
    }

    @Test
    public void removeHard() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.removeHard("abc"));

        assertTrue(trie.contains("ab"));
        assertFalse(trie.contains("abc"));
        assertFalse(trie.contains("abcd"));
        assertFalse(trie.contains("abcde"));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(65, trie.getSize());

        assertTrue(trie.removeHard("q"));
        assertFalse(trie.contains("qwerty"));
        assertFalse(trie.contains("qwebcd"));
        assertEquals(14, trie.getEntriesCount());
        assertEquals(56, trie.getSize());

        assertTrue(trie.removeHard("00"));
        assertFalse(trie.contains("000qwerty"));
        assertFalse(trie.contains("0000"));
        assertEquals(12, trie.getEntriesCount());
        assertEquals(46, trie.getSize());
        assertFalse(trie.removeHard(""));
    }

    @Test
    public void removeWeak() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertFalse(trie.removeWeak("abc"));
        assertTrue(trie.contains("abc"));
        assertTrue(trie.contains("abcd"));
        assertTrue(trie.contains("abcde"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("q"));
        assertTrue(trie.contains("qwerty"));
        assertTrue(trie.contains("qwebcd"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("00"));
        assertTrue(trie.contains("000qwerty"));
        assertTrue(trie.contains("0000"));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.removeWeak("abcde"));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.removeWeak(""));
    }

    @Test
    public void remove() {

        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.remove("abc"));
        assertFalse(trie.contains("abc"));
        assertTrue(trie.contains("abcd"));
        assertTrue(trie.contains("abcde"));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.remove("q"));
        assertFalse(trie.contains("q"));
        assertTrue(trie.contains("qwerty"));
        assertTrue(trie.contains("qwebcd"));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.remove("00"));
        assertTrue(trie.contains("000qwerty"));
        assertTrue(trie.contains("0000"));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.remove("abcde"));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.remove(""));
        for (String s : lst) {
            if (trie.contains(s)) assertTrue(trie.remove(s));
        }
        assertEquals(trie.getSize(), 0);
        assertEquals(trie.getEntriesCount(), 0);
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
