package tests.Trie;

import Additional.Trie.Trie;
import Lists.AbstractList;
import Lists.impl.ArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTests {
    Trie trie;
    AbstractList<String> lst;

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
        for (String s : lst) trie.put(s.toCharArray());
    }

    @Test
    public void put() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        String[] list1 = new String[]{"temp", "test", "", "0000", "abc", ""};
        for (String s : list) trie.put(s.toCharArray());

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());
        for (String s : list1) trie.put(s.toCharArray());

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        trie.put(null);
        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

        for (String s : lst) trie.put(s.toCharArray());

        assertEquals(25, trie.getEntriesCount());
        assertEquals(99, trie.getSize());

    }

    @Test
    public void presents() {
        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) {
            trie.put(s.toCharArray());
        }
        for (String s : lst) {
            assertTrue(trie.presents(s.toCharArray()));
        }

        assertTrue(trie.presents("abc".toCharArray()));
        assertTrue(trie.presents("java".toCharArray()));
        assertTrue(trie.presents("123".toCharArray()));

        assertTrue(trie.presents("qwerty".toCharArray()));
        assertTrue(trie.presents("new data".toCharArray()));
        assertTrue(trie.presents("new".toCharArray()));

        assertFalse(trie.presents("javac".toCharArray()));
        assertFalse(trie.presents("keyboard1234".toCharArray()));
        assertFalse(trie.presents("00001".toCharArray()));
    }

    @Test
    public void contains() {
        for (String s : lst) assertTrue(trie.contains(s.toCharArray()));


        assertTrue(trie.contains("abc".toCharArray()));
        assertFalse(trie.contains("java".toCharArray()));
        assertFalse(trie.contains("123".toCharArray()));

        assertTrue(trie.contains("qwerty".toCharArray()));
        assertFalse(trie.contains("qwer".toCharArray()));
        assertTrue(trie.contains("new data".toCharArray()));
        assertFalse(trie.contains("new".toCharArray()));

        assertFalse(trie.contains("javac".toCharArray()));
        assertTrue(trie.contains("1001212454".toCharArray()));
        assertFalse(trie.contains("00001".toCharArray()));
    }

    @Test
    public void getByPrefixString() {
        AbstractList<String> list = new ArrayList<>();
        list.addAll("tes", "132", "1325a", "000", "000qw", "data", "new", "te");
        for (String s : list) assertNotEquals(trie.getByPrefix(s.toCharArray()), "");

        assertEquals(trie.getByPrefix("1001".toCharArray()), "1001212454");
        assertEquals(trie.getByPrefix("00".toCharArray()), "000qwerty");
        assertEquals(trie.getByPrefix("qwe".toCharArray()), "qwerty");
        assertEquals(trie.getByPrefix("abcde".toCharArray()), "abcde");
        assertEquals(trie.getByPrefix("".toCharArray()), "");
    }

    @Test
    public void getByPrefixStringArray() {

        assertArrayEquals(trie.getByPrefix("1001".toCharArray(), 1), new String[]{"1001212454"});
        assertArrayEquals(trie.getByPrefix("00".toCharArray(), 1), new String[]{"000qwerty"});
        assertArrayEquals(trie.getByPrefix("qwe".toCharArray(), 1), new String[]{"qwerty"});
        assertArrayEquals(trie.getByPrefix("abcde".toCharArray(), 1), new String[]{"abcde"});
        assertArrayEquals(trie.getByPrefix("".toCharArray(), 1), new String[0]);

        assertArrayEquals(trie.getByPrefix("ab".toCharArray(), 1), new String[]{"ab"});
        assertArrayEquals(trie.getByPrefix("ab".toCharArray(), 2), new String[]{"ab", "abc"});
        assertArrayEquals(trie.getByPrefix("ab".toCharArray(), 3), new String[]{"ab", "abc", "abcd"});
        assertArrayEquals(trie.getByPrefix("ab".toCharArray(), 4), new String[]{"ab", "abc", "abcd", "abcde"});
        assertArrayEquals(trie.getByPrefix("ab".toCharArray(), 999), new String[]{"ab", "abc", "abcd", "abcde"});

        assertArrayEquals(trie.getByPrefix("q".toCharArray(), 4), new String[]{"q", "qwerty", "qwebcd"});
        assertArrayEquals(trie.getByPrefix("qwe".toCharArray(), 1), new String[]{"qwerty"});
        assertArrayEquals(trie.getByPrefix("0".toCharArray(), 2), new String[]{"000qwerty", "0000"});
    }

    @Test
    public void getSize() {
        assertEquals(68, trie.getSize());
        trie.clear();
        assertEquals(0, trie.getSize());

        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) trie.put(s.toCharArray());

        assertEquals(33, trie.getSize());
    }

    @Test
    public void getEntriesCount() {
        assertEquals(20, trie.getEntriesCount());
        trie.clear();
        assertEquals(0, trie.getEntriesCount());

        String[] list = new String[]{"some data", "keyboard", "computer", "tech", "java"};
        for (String s : list) trie.put(s.toCharArray());
        assertEquals(5, trie.getEntriesCount());
    }

    @Test
    public void removeHard() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.removeHard("abc".toCharArray()));
        assertTrue(trie.contains("ab".toCharArray()));
        assertFalse(trie.contains("abc".toCharArray()));
        assertFalse(trie.contains("abcd".toCharArray()));
        assertFalse(trie.contains("abcde".toCharArray()));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(65, trie.getSize());

        assertTrue(trie.removeHard("q".toCharArray()));
        assertFalse(trie.contains("qwerty".toCharArray()));
        assertFalse(trie.contains("qwebcd".toCharArray()));
        assertEquals(14, trie.getEntriesCount());
        assertEquals(56, trie.getSize());

        assertTrue(trie.removeHard("00".toCharArray()));
        assertFalse(trie.contains("000qwerty".toCharArray()));
        assertFalse(trie.contains("0000".toCharArray()));
        assertEquals(12, trie.getEntriesCount());
        assertEquals(46, trie.getSize());
        assertFalse(trie.removeHard("".toCharArray()));
    }

    @Test
    public void removeWeak() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertFalse(trie.removeWeak("abc".toCharArray()));
        assertTrue(trie.contains("abc".toCharArray()));
        assertTrue(trie.contains("abcd".toCharArray()));
        assertTrue(trie.contains("abcde".toCharArray()));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("q".toCharArray()));
        assertTrue(trie.contains("qwerty".toCharArray()));
        assertTrue(trie.contains("qwebcd".toCharArray()));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.removeWeak("00".toCharArray()));
        assertTrue(trie.contains("000qwerty".toCharArray()));
        assertTrue(trie.contains("0000".toCharArray()));
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.removeWeak("abcde".toCharArray()));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.removeWeak("".toCharArray()));
    }

    @Test
    public void remove() {
        assertEquals(20, trie.getEntriesCount());
        assertEquals(68, trie.getSize());
        assertTrue(trie.remove("abc".toCharArray()));
        assertFalse(trie.contains("abc".toCharArray()));
        assertTrue(trie.contains("abcd".toCharArray()));
        assertTrue(trie.contains("abcde".toCharArray()));
        assertEquals(19, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.remove("q".toCharArray()));
        assertFalse(trie.contains("q".toCharArray()));
        assertTrue(trie.contains("qwerty".toCharArray()));
        assertTrue(trie.contains("qwebcd".toCharArray()));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertFalse(trie.remove("00".toCharArray()));
        assertTrue(trie.contains("000qwerty".toCharArray()));
        assertTrue(trie.contains("0000".toCharArray()));
        assertEquals(18, trie.getEntriesCount());
        assertEquals(68, trie.getSize());

        assertTrue(trie.remove("abcde".toCharArray()));
        assertEquals(17, trie.getEntriesCount());
        assertEquals(67, trie.getSize());
        assertFalse(trie.remove("".toCharArray()));
    }
}
