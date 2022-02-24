package TrieTests;

import additional.nodes.Pair;
import lists.impl.ArrayList;
import org.junit.Before;
import org.junit.Test;
import sets.Set;
import tries.TrieMap;

import static org.junit.Assert.*;

public class TrieMapTest {
    TrieMap trieMap;

    public TrieMapTest() {
        this.trieMap = new TrieMap();
    }

    @Before
    public void setUp() {
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
        trieMap.add("1234", "value");
        trieMap.add("4567", "value");
        trieMap.add("qwe", "00000");
        trieMap.add("qwe", "11111111111");
        trieMap.add("test", "00000");
        trieMap.add("test", "00000");
        trieMap.add("abc909", "qwe");
        trieMap.add("12345", "val");
        trieMap.add("12345", "temp");
        trieMap.add("java", "qwerty");
        trieMap.add("java", "random value");
        trieMap.add("c++", "some value");
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
    }

    @Test
    public void putTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
        trieMap.add("string234", "00990099");
        trieMap.add("string234", "00990099");
        trieMap.add("string234", "");
        assertEquals(9, trieMap.getPairsCount());
        assertEquals(90, trieMap.getSize());
        trieMap.add("1value1", "1value1");
        trieMap.add("88888", "11");
        trieMap.add("6", "99999999");
        assertEquals(12, trieMap.getPairsCount());
        assertEquals(110, trieMap.getSize());
        trieMap.add("0987", "0987");
        trieMap.add("wasd", "");
        trieMap.add("qwerty", "");
        assertEquals(15, trieMap.getPairsCount());
    }

    @Test
    public void deleteKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        Set<String> value = new Set<>();
        value.addAll("value");
        assertEquals(new Pair<>("1234", value).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
        value.clear();

        value.addAll("11111111111", "00000");
        assertEquals(new Pair<>("qwe", value).toString(), trieMap.deleteKey("qwe").toString());
        assertEquals(6, trieMap.getPairsCount());
        assertEquals(66, trieMap.getSize());
        value.clear();

        value.addAll("random value", "qwerty");
        assertEquals(new Pair<>("java", value).toString(), trieMap.deleteKey("java").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(47, trieMap.getSize());
        value.clear();

        assertEquals(new Pair<>().toString(), trieMap.deleteKey("java").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(47, trieMap.getSize());
        value.clear();

        value.addAll("value");
        assertEquals(new Pair<>("4567", value).toString(), trieMap.deleteKey("4567").toString());
        assertEquals(4, trieMap.getPairsCount());
        assertEquals(41, trieMap.getSize());
        value.clear();

        value.addAll("some value");
        assertEquals(new Pair<>("c++", value).toString(), trieMap.deleteKey("c++").toString());
        assertEquals(3, trieMap.getPairsCount());
        assertEquals(28, trieMap.getSize());
        value.clear();

        value.addAll("val", "temp");
        assertEquals(new Pair<>("12345", value).toString(), trieMap.deleteKey("12345").toString());
        assertEquals(2, trieMap.getPairsCount());
        assertEquals(18, trieMap.getSize());
        value.clear();

        value.addAll("00000");
        assertEquals(new Pair<>("test", value).toString(), trieMap.deleteKey("test").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(9, trieMap.getSize());
        value.clear();

        value.addAll("qwe");
        assertEquals(new Pair<>("abc909", value).toString(), trieMap.deleteKey("abc909").toString());
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void deleteValueAndKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        Set<String> value = new Set<>();
        assertTrue(trieMap.deleteValue("value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertEquals(new Pair<>("1234", new Set<>()).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertFalse(trieMap.deleteValue("value"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwe"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertFalse(trieMap.deleteValue("1111"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());


        trieMap.add("qwerty", "");
        assertEquals(8, trieMap.getPairsCount());
        trieMap.deleteKey("qwerty");
        assertTrue(trieMap.deleteValue("qwerty"));
        assertEquals(7, trieMap.getPairsCount());

    }

    @Test
    public void deleteValueOrKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        assertTrue(trieMap.deleteValue("value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertTrue(trieMap.deleteValue("val"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(71, trieMap.getSize());

        assertTrue(trieMap.deleteValue("temp"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(69, trieMap.getSize());

        assertTrue(trieMap.deleteValue("some value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(59, trieMap.getSize());

        assertTrue(trieMap.deleteValue("00000"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(54, trieMap.getSize());

        assertTrue(trieMap.deleteValue("11111111111"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(44, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwerty"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(41, trieMap.getSize());

        assertTrue(trieMap.deleteValue("random value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(29, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwe"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(29, trieMap.getSize());

        assertEquals(new Pair<>("12345", new Set<>()).toString(), trieMap.deleteKey("12345").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(28, trieMap.getSize());

        assertEquals(new Pair<>("1234", new Set<>()).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(6, trieMap.getPairsCount());
        assertEquals(24, trieMap.getSize());

        assertEquals(new Pair<>("qwe", new Set<>()).toString(), trieMap.deleteKey("qwe").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(21, trieMap.getSize());

        assertEquals(new Pair<>("c++", new Set<>()).toString(), trieMap.deleteKey("c++").toString());
        assertEquals(4, trieMap.getPairsCount());
        assertEquals(18, trieMap.getSize());

        assertEquals(new Pair<>("abc909", new Set<>()).toString(), trieMap.deleteKey("abc909").toString());
        assertEquals(3, trieMap.getPairsCount());
        assertEquals(12, trieMap.getSize());


        assertEquals(new Pair<>("java", new Set<>()).toString(), trieMap.deleteKey("java").toString());
        assertEquals(2, trieMap.getPairsCount());
        assertEquals(8, trieMap.getSize());


        assertEquals(new Pair<>("test", new Set<>()).toString(), trieMap.deleteKey("test").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(4, trieMap.getSize());

        assertEquals(new Pair<>().toString(), trieMap.deleteKey("java").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(4, trieMap.getSize());

        assertEquals(new Pair<>("4567", new Set<>()).toString(), trieMap.deleteKey("4567").toString());
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void containsKeyTest() {
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll("1234", "4567", "qwe", "test", "abc909", "12345", "java", "c++");
        for (String s : keys) {
            assertTrue(trieMap.containsKey(s));
        }
        assertFalse(trieMap.containsKey("some value"));
        assertFalse(trieMap.containsKey("value"));
        assertFalse(trieMap.containsKey("00000"));
        assertFalse(trieMap.containsKey("temp"));
        assertFalse(trieMap.containsKey("qwerty"));
        assertFalse(trieMap.containsKey(""));
    }

    @Test
    public void containsKeyValue() {
        ArrayList<String> values = new ArrayList<>();
        values.addAll("value", "00000", "11111111111", "qwe", "val", "temp", "qwerty", "random value", "some value");
        for (String s : values) {
            assertTrue(trieMap.containsValue(s));
        }
        assertFalse(trieMap.containsValue("java"));
        assertFalse(trieMap.containsValue("4567"));
        assertFalse(trieMap.containsValue("0000"));
        assertFalse(trieMap.containsValue("test"));
        assertFalse(trieMap.containsValue("abc909"));
        assertFalse(trieMap.containsValue("54321"));
        assertFalse(trieMap.containsValue(""));
    }

    @Test
    public void containsKeyOrValueTest() {
        ArrayList<String> items = new ArrayList<>();
        items.addAll("1234", "4567", "qwe", "test",
                "abc909", "12345", "java", "c++", "value",
                "00000", "11111111111", "qwe", "val", "temp",
                "qwerty", "random value", "some value");
        for (String s : items) {
            assertTrue(trieMap.contains(s));
        }
        assertFalse(trieMap.contains("some val"));
        assertFalse(trieMap.contains("123"));
        assertFalse(trieMap.contains("8"));
        assertFalse(trieMap.contains("6"));
        assertFalse(trieMap.contains(""));
    }
}